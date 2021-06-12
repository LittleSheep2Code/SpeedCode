package SpeedCodeBKD.Controllers.AccountsControllers;

import SpeedCodeBKD.Utils.Processor.ResultProcessor;
import SpeedCodeBKD.Data.Entities.AccountEntity;
import SpeedCodeBKD.Data.Service.AccountService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("accounts-utils/search")
public class SearchAccountController {

    @Autowired
    AccountService accountService;

    public static class AccountsFinder {

        public static String rendererSearchContent(String data) {
            JSONObject source = new JSONObject(String.format("{result: %s}", data));

            return ResultProcessor.completed_response(source, "Search-" + data);
        }

        public static String search(String mode, AccountEntity account) {

            BeanMap search_data = BeanMap.create(account);

            Object data = search_data.get(mode);

            if(data == null) {
                return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Search-" + data);
            }

            return rendererSearchContent(data.toString());
        }
    }

    @SneakyThrows
    @PostMapping(value = "/{uuid}:{mode}", produces = "application/json; charset=UTF-8")
    private String BasicQuery(@PathVariable String uuid, @PathVariable String mode, HttpServletResponse response) {

        AccountEntity result = accountService.selectByUuid(uuid);

        if(mode.equals("list_profile")) {
            if(result == null) {
                response.setStatus(201);
                return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Search");
            }

            result.setAccess_token(null);
            result.setPassword(null);

            JSONObject resObject = new JSONObject();
            resObject.put("result", result);

            response.setStatus(200);
            return ResultProcessor.completed_response(resObject, "Search");
        }

        if(mode.equals("accessToken") || mode.equals("password")) {
            response.setStatus(200);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.permission_insufficient, "Search");
        }

        String data = AccountsFinder.search(mode, result);

        response.setStatus(new JSONObject(data).getInt("status_code"));
        return data;
    }

    @SneakyThrows
    @PostMapping(value = "/advance/{access_token}:{mode}", produces = "application/json; charset=UTF-8")
    private String AdvanceQuery(@PathVariable String access_token, @PathVariable String mode, HttpServletResponse response) {
        if(accountService.selectByAccessToken(access_token) == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Search");
        }

        AccountEntity result = accountService.selectByAccessToken(access_token);
        if(mode.equals("list_profile")) {

            JSONObject resultObject = new JSONObject();
            resultObject.put("result", result);

            response.setStatus(200);
            return ResultProcessor.completed_response(resultObject, "Search");
        }

        String data = AccountsFinder.search(mode, result);

        response.setStatus(new JSONObject(data).getInt("status_code"));
        return data;
    }

    @SneakyThrows
    @GetMapping(value = "/username2uuid/{username}", produces = "application/json; charset=UTF-8")
    private String username2uuid(@PathVariable String username, HttpServletResponse response) {
        AccountEntity result = accountService.selectByUsername(username);

        if(result == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Username2Uuid");
        }

        response.setStatus(200);
        JSONObject resObject = new JSONObject(); resObject.put("result", result.getUuid());
        return ResultProcessor.completed_response(resObject, "Username2Uuid");
    }
}