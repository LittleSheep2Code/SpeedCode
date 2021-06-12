package SpeedCodeBKD.Controllers;

import SpeedCodeBKD.Data.Entities.ExecutorEntity;
import SpeedCodeBKD.Data.Service.AccountService;
import SpeedCodeBKD.Data.Service.ExecutorService;
import SpeedCodeBKD.Services.CompileUtilsService;
import SpeedCodeBKD.Utils.Processor.ResultProcessor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("code-utils/code-runner/")
public class CodeRunnerController {

    @Autowired
    CompileUtilsService compileUtilsService;

    @Autowired
    ExecutorService executorService;
    AccountService accountService;

    private String universalExecuteCode(String source, String stdin, short languageId) {
        try {
            // 第一种情况 - 没有 stdin
            if (stdin.equals("")) {
                JSONObject response = compileUtilsService.executeProgram(source, languageId);
                if (response.equals(null))
                    return ResultProcessor.warn_response(ResultProcessor.ReasonCode.unknown_error, "Execute");
                else {
                    JSONObject object = new JSONObject();
                    object.put("response", response);
                    return ResultProcessor.completed_response(object, "Execute");
                }
            }

            // 第二种情况 - 有 stdin
            if (!stdin.equals("")) {
                JSONObject response = compileUtilsService.executeProgram(source, languageId, stdin);
                if (response.equals(null))
                    return ResultProcessor.warn_response(ResultProcessor.ReasonCode.unknown_error, "Execute");

                else {
                    JSONObject object = new JSONObject();
                    object.put("response", response);
                    return ResultProcessor.completed_response(object, "Execute");
                }
            }
        } catch (IllegalArgumentException e) {
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.unknown_error, "Execute");
        }

        // 其他情况
        return null;
    }

    @PostMapping(value = "execute", produces = "application/json; charset=UTF-8")
    private String executeCode(@RequestParam("source") String source, @RequestParam("stdin") String stdin, String language, String uuid,
                               HttpServletResponse response) {

        short executorId = compileUtilsService.translateString2Id(language);

        if (executorId == -1) {
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Execute");
        }

        // 校验用户身份
        // State0 | 普通用户
        if (accountService.selectByUuid(uuid) == null) {
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.undefined, "Execute");
        }

        if (accountService.selectByUuid(uuid).getState() == 0) {
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.undefined, "Execute");
        }

        // State-1 | 临时游客登陆


        // 运行记录生成
        ExecutorEntity executeLog = new ExecutorEntity();
        executeLog.setExecute_time(new Date());
        executeLog.setStdin(stdin);
        executeLog.setSource_code(source);
        executeLog.setSender(uuid);

        // 提交运行记录
        executorService.save(executeLog);

        // 运行
        response.setStatus(200);
        return universalExecuteCode(source, stdin, executorId);
    }
}
