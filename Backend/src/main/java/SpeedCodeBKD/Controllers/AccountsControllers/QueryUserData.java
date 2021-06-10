package SpeedCodeBKD.Controllers.AccountsControllers;

import SpeedCodeBKD.Utils.Processor.ResultProcessor;
import SpeedCodeBKD.Data.Entites.AccountEntity;
import SpeedCodeBKD.Data.Service.AccountService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("user-tools/query")
public class QueryUserData {

    @Autowired
    AccountService accountService;

    @SneakyThrows
    @PostMapping(value = "/basic/{targetUUid}:{queryMode}", produces = "application/json; charset=UTF-8")
    private String BasicQuery(@PathVariable String targetUUid, @PathVariable String queryMode, HttpServletResponse response) {
        log.info("POST Methods(`/user-tools/query/basic`): Handle a request! target: " + targetUUid + "; mode: " + queryMode);

        if(queryMode.equals("all")) {
            log.info("POST Methods(`/user-tools/query/basic`): Processing(All)... ");
            AccountEntity result = accountService.getByUuid(targetUUid);
            log.info("POST Methods(`/user-tools/query/basic`): Done!");

            if(result == null) {
                response.setStatus(201);
                return ResultProcessor.warn_response("Target user not found", "BasicQuery-All");
            }

            result.setAccessToken(null);
            result.setPassword(null);
            JSONObject resultObject = new JSONObject();
            resultObject.put("account", result);

            response.setStatus(200);
            return ResultProcessor.completed_response(resultObject, "BasicQuery-All");
        }

        if(queryMode.equals("accessToken") || queryMode.equals("password")) {
            response.setStatus(200);
            return ResultProcessor.warn_response("Unable to read, permission is locked, please use AdvanceQuery", "BasicQuery-Unknown");
        }

        String authorQueryResult = authorQuery(queryMode, targetUUid);
        if(authorQueryResult.equals("Unknown")) {
            response.setStatus(500);
            return ResultProcessor.error_response("Unknown query mode(The code executes to the expected position)", "BasicQuery-Unknown");
        }

        response.setStatus(new JSONObject(authorQueryResult).getInt("statusCode"));
        return authorQueryResult;
    }

    @SneakyThrows
    @PostMapping(value = "/advance/{accessToken}:{queryMode}", produces = "application/json; charset=UTF-8")
    private String AdvanceQuery(@PathVariable String accessToken, @PathVariable String queryMode, HttpServletResponse response) {
        log.info("POST Methods(`/user-tools/query/advance`): Handle a request! accessToken: " + accessToken + "; mode: " + queryMode);

        if(accountService.getByAccessToken(accessToken) == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response("Wrong accessToken", "AdvanceQuery-Unknown");
        }

        AccountEntity result = accountService.getByAccessToken(accessToken);
        if(queryMode.equals("all")) {
            log.info("POST Methods(`/user-tools/query/advance`): Processing(All)... ");

            JSONObject resultObject = new JSONObject();
            resultObject.put("account", result);

            log.info("POST Methods(`/user-tools/query/advance`): Done!");
            response.setStatus(200);
            return ResultProcessor.completed_response(resultObject, "AdvanceQuery-All");
        }

        String authorQueryResult = authorQuery(queryMode, result.getUuid());
        if(authorQueryResult.equals("Unknown")) {
            response.setStatus(500);
            return ResultProcessor.error_response("Unknown query mode(The code executes to the expected position)", "AdvanceQuery-Unknown");
        }

        response.setStatus(new JSONObject(authorQueryResult).getInt("statusCode"));
        return authorQueryResult;
    }

    @SneakyThrows
    @GetMapping(value = "/username2uuid/{username}", produces = "application/json; charset=UTF-8")
    private String username2uuid(@PathVariable String username, HttpServletResponse response) {
        AccountEntity result = accountService.getByUsername(username);

        if(result == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response("Cannot found target user", "Query-Username2Uuid");
        }

        response.setStatus(200);
        JSONObject resultObject = new JSONObject(); resultObject.put("uuid", result.getUuid());
        return ResultProcessor.completed_response(resultObject, "Query-Username2Uuid");
    }

    private String authorQuery(String mode, String uuid) {
        if(mode.equals("email")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(Email)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-Email");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("email", result.getEmail());
            return ResultProcessor.completed_response(resultObject, "Query-Email");
        }

        if(mode.equals("phoneId")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(phoneId)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-PhoneId");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("phoneId", result.getPhoneId());
            return ResultProcessor.completed_response(resultObject, "Query-PhoneId");
        }

        if(mode.equals("username")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(username)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-Username");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("username", result.getUsername());
            return ResultProcessor.completed_response(resultObject, "Query-Username");
        }

        if(mode.equals("password")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(password)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-Password");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("password", result.getPassword());
            return ResultProcessor.completed_response(resultObject, "Query-Password");
        }

        if(mode.equals("uuid")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(uuid)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-Uuid");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("uuid", result.getUuid());
            return ResultProcessor.completed_response(resultObject, "Query-Uuid");
        }

        if(mode.equals("accessToken")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(accessToken)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-AccessToken");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("accessToken", result.getAccessToken());
            return ResultProcessor.completed_response(resultObject, "Query-AccessToken");
        }

        if(mode.equals("permission")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(permission)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-Permission");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("permission", result.getPermission());
            return ResultProcessor.completed_response(resultObject, "Query-Permission");
        }

        if(mode.equals("level")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(level)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-Level");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("level", result.getPhoneId());
            return ResultProcessor.completed_response(resultObject, "Query-Level");
        }

        if(mode.equals("certificated")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(certificated)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-Certificated");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("certificated", result.getCertificated());
            return ResultProcessor.completed_response(resultObject, "Query-Certificated");
        }

        if(mode.equals("exp")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(exp)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-Exp");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("certificated", result.getExp());
            return ResultProcessor.completed_response(resultObject, "Query-Exp");
        }

        if(mode.equals("registerTime")) {
            log.info("POST Methods(`/user-tools/query/*`): Processing(registerTime)... ");
            AccountEntity result = accountService.getByUuid(uuid);
            if(result == null) {
                return ResultProcessor.warn_response("Target user not found", "Query-RegisterTime");
            }

            JSONObject resultObject = new JSONObject(); resultObject.put("registerTime", result.getRegisterTime());
            return ResultProcessor.completed_response(resultObject, "Query-RegisterTime");
        }

        return "Unknown";
    }
}