package SpeedCodeBKD.Common.Aspect;

import SpeedCodeBKD.Common.Annotation.AccessLevel;
import SpeedCodeBKD.Data.Entities.AccountEntity;
import SpeedCodeBKD.Data.Service.AccountService;
import SpeedCodeBKD.Utils.Processor.ResultProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.AccessException;

@Aspect
@Component
public class AccessInterceptorAOP {

    @Autowired
    AccountService accountService;

    @Before(value = "@annotation(access)")
    public void authorizationInterceptor(JoinPoint point, AccessLevel access) throws Throwable {

        try {
            // 直接放行该请求，因为 -256 是所有人等级，不必浪费资源
            if (access.value() <= -256) {
                return;
            }

            // 通过 Header 里的 AccessToken 获取到 AccountEntity
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            AccountEntity entity = accountService.selectByAccessToken(request.getHeader("access_token"));

            // 验证 AccountEntity

            // 无该用户
            if (entity == null) {
                throw new AccessException(ResultProcessor.ReasonCode.undefined.getCode());
            }

            // 权限不足
            else if (entity.getPermission() <= access.value()) {
                throw new UnavailableException(ResultProcessor.ReasonCode.permission_insufficient.getCode());
            }

            // 权限太大，但不忽略管理员
            else if(access.max() != access.value() && !access.ignore_admins() && entity.getPermission() > access.max() && entity.getPermission() < 155) {
                throw new UnavailableException(ResultProcessor.ReasonCode.permissions_superfluous.getCode());
            }

            // 权限太大，并忽略管理员
            else if (access.max() != access.value() && access.ignore_admins() && entity.getPermission() > access.max()) {
                throw new UnavailableException(ResultProcessor.ReasonCode.permissions_superfluous.getCode());
            }

        } catch (Exception e) {
            throw e;
        }

        // 通过验证，放行
    }
}
