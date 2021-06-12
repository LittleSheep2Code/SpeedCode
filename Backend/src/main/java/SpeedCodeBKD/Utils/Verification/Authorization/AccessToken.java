package SpeedCodeBKD.Utils.Verification.Authorization;

import SpeedCodeBKD.Data.Entities.AccountEntity;
import SpeedCodeBKD.Data.Service.AccountService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Slf4j
public class AccessToken {

    AccountService accountService;

    public AccessToken(AccountService accountService) {
        this.accountService = accountService;
    }

    @SneakyThrows
    public String updateAccessToken(@NotNull String account_uuid, @Nullable Integer customize_available_time) {
        AccountEntity account = accountService.selectByUuid(account_uuid);

        // 验证
        boolean withoutUpdate = true;

        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(account.getPassword())).build();
        try { verifier.verify(account.getAccess_token()); }
        catch(JWTVerificationException e) { withoutUpdate = false; }

        log.info("(Token Engine): Update accessToken for  " + account_uuid);

        String availableToken = "";

        if(withoutUpdate) {
            availableToken = account.getAccess_token();
        }

        // 如果需要更新
        if(!withoutUpdate) {
            // 标注有效时间
            Date availableTime = new Date();
            Date expirationTime = new Date();

            if(customize_available_time != null)
                expirationTime = new Date(System.currentTimeMillis() + customize_available_time);    // 自定义有效时间

            if(customize_available_time == null)
                expirationTime = new Date(System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000);    // 两周有效时间

            // 生成 JWT Token
            availableToken = JWT.create().withIssuedAt(availableTime).withExpiresAt(expirationTime).sign(Algorithm.HMAC512(account.getPassword()));

            log.info(String.format("(Token Engine): Completed create JWT Token. Issued at: %s; Expires at: %s;", availableTime, expirationTime));

            // 更新至数据库
            account.setAccess_token(availableToken);                                                              // 写入 Database
            accountService.update(account, new UpdateWrapper<AccountEntity>().eq("uuid", account_uuid));  // 推送提交

            log.info("(Token Engine): Completed update accessToken");
        }

        return availableToken;
    }

    public boolean isValid(@NotNull String account_uuid, @NotNull String access_token) {
        AccountEntity account = accountService.selectByUuid(account_uuid);

        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(account.getPassword())).build();
        try { verifier.verify(access_token); }
        catch(JWTVerificationException e) { return false; }

        return true;
    }
}
