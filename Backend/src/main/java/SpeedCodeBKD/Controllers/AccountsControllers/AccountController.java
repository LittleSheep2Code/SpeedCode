package SpeedCodeBKD.Controllers.AccountsControllers;

import SpeedCodeBKD.Data.Entites.AccountEntity;
import SpeedCodeBKD.Data.Service.AccountService;
import SpeedCodeBKD.Utils.Processor.EmailSender;
import SpeedCodeBKD.Utils.Processor.ResourceReader;
import SpeedCodeBKD.Utils.Processor.ResultProcessor;
import SpeedCodeBKD.Utils.Verification.Authorization.AccessToken;
import SpeedCodeBKD.Utils.Verification.EmailCode;
import SpeedCodeBKD.Utils.Verification.EncryptionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("accounts-utils/authorization")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    EmailSender EmailProcessor;
    EmailCode EmailCodeUtils;

    @SneakyThrows
    @PostMapping(value = "login", produces = "application/json; charset=UTF-8")
    private String loginRequest(String username, String password, HttpServletResponse response) {
        accountService.removeOutdatedAccount();
        AccountEntity target = accountService.getByUsername(username);

        // 检查用户名
        if (target == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Login");
        }

        // 检查是否验证
        if (accountService.getByUsername(username).getState() == 0) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.permission_insufficient, "Login");
        }

        // 验证用户密码
        password = EncryptionUtils.passwordEncryption(password);
        if (!password.equals(target.getPassword())) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Login");
        }

        // 更新 AccessToken
        String token = new AccessToken(accountService).updateAccessToken(target.getUuid(), 0);

        // 返回 Response
        response.setStatus(200);
        JSONObject result = new JSONObject();
        result.put("access_token", token);
        return ResultProcessor.completed_response(result, "Login");
    }

    @SneakyThrows
    @PostMapping(value = "register", produces = "application/json; charset=UTF-8")
    private String registerRequest(String username, String email, String password,
                                   @RequestParam(required = false, defaultValue = "null") String email_code, HttpServletResponse response) {

        accountService.removeOutdatedAccount();

        // 检查 EmailCode
        if (email_code.equals("null")) {

            // 检查参数是否提交齐全
            if (email.equals("") || password.equals("") || username.equals("")) {
                response.setStatus(201);
                return ResultProcessor.warn_response(ResultProcessor.ReasonCode.parameters_error, "Register");
            }

            // 检查是否重复
            if (accountService.getByUsername(username) != null) {
                response.setStatus(201);
                return ResultProcessor.warn_response(ResultProcessor.ReasonCode.duplicate_data, "Register");
            }

            if (accountService.getByEmail(email) != null) {
                response.setStatus(201);
                return ResultProcessor.warn_response("Duplicate email", "Register");
            }

            String activateCode = EmailCodeUtils.summon();
            String commitUuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();

            // 注册 EmailCode & User
            AccountEntity commitTarget = new AccountEntity();
            commitTarget.setPassword(EncryptionUtils.passwordEncryption(password));
            commitTarget.setActivate_code(activateCode);
            commitTarget.setCreate_date(new Date());
            commitTarget.setUsername(username);
            commitTarget.setUuid(commitUuid);
            accountService.save(commitTarget);
            commitTarget.setPermission(0);
            commitTarget.setEmail(email);
            commitTarget.setState(0);

            // 读取 Template 文件
            String registerTemplate = ResourceReader.TemplateReader("static/document/config/email-code.json")
                    .replace("${EMAIL_CODE}", email_code).replace("${USERNAME}", username);

            try {
                EmailProcessor.send(null, true, new String[]{email}, "SpeedCode account register request", registerTemplate);
            } catch (Exception e) {
                e.printStackTrace();

                response.setStatus(400);
                accountService.remove(new QueryWrapper<AccountEntity>().eq("uuid", commitUuid));
                return ResultProcessor.warn_response(ResultProcessor.ReasonCode.email_error, "Register");
            }

            response.setStatus(201);
            return ResultProcessor.completed_response("EmailCode sent", "Register");
        }

        // 检查 Username 是否有效
        if (username.equals("")) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.undefined, "Register");
        }

        if (accountService.getByUsername(username) == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.undefined, "Register");
        }

        // 检查 EmailCode 是否有效
        if (accountService.getByActivateCode(email_code) == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Register");
        }

        if (!accountService.getByActivateCode(email_code).getUsername().equals(username)) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.permission_insufficient, "Register");
        }


        // 提交数据
        AccountEntity commitTarget = new AccountEntity();
        commitTarget.setState(1);
        commitTarget.setActivate_code("");

        accountService.update(commitTarget, new UpdateWrapper<AccountEntity>().eq("username", username));

        JSONObject object = new JSONObject();
        object.put("status", "Completed register a new user");

        response.setStatus(200);
        return ResultProcessor.completed_response(object, "Register");
    }
}