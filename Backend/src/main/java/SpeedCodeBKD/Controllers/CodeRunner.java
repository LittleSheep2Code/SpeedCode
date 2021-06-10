package SpeedCodeBKD.Controllers;

import SpeedCodeBKD.Services.CompileUtilsService;
import SpeedCodeBKD.Utils.Processor.ResultProcessor;
import SpeedCodeBKD.Data.Entites.CodeCacheEntity;
import SpeedCodeBKD.Data.Service.AccountService;
import SpeedCodeBKD.Data.Service.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("code-utils/code-runner/")
public class CodeRunner {

    @Autowired
    CompileUtilsService compileUtilsService;

    @Autowired
    ExecutorService codeCacheService;

    @Autowired
    AccountService accountService;

    private String universalExecuteCode(String source, String stdin, short languageId) {
        try {
            // 第一种情况 - 没有 stdin
            if(stdin.equals("")) {
                JSONObject response = compileUtilsService.executeProgram(source, languageId);
                if(response.equals(null))
                    return ResultProcessor.warn_response("Cannot execute, please check your code and language or contact the administrator", "ExecCode");
                else {
                    JSONObject object = new JSONObject(); object.put("response", response);
                    return ResultProcessor.completed_response(object, "ExecCode");
                }
            }

            // 第二种情况 - 有 stdin
            if(!stdin.equals("")) {
                JSONObject response = compileUtilsService.executeProgram(source, languageId, stdin);
                if(response.equals(null))
                    return ResultProcessor.warn_response("Cannot execute, please check your code and language or contact the administrator", "ExecCode");
                else {
                    JSONObject object = new JSONObject(); object.put("response", response);
                    return ResultProcessor.completed_response(object, "ExecCode");
                }
            }
        }

        catch(IllegalArgumentException e) {
            return ResultProcessor.warn_response("Cannot execute, check your code and choose language or contact the administrator", "ExecCode");
        }

        // 其他情况
        return null;
    }

    @PostMapping(value = "execute", produces = "application/json; charset=UTF-8")
    private String executeCode(@RequestParam("source") String source, @RequestParam("stdin") String stdin, String language, String uuid,
                               HttpServletResponse response) {

        short languageId = compileUtilsService.translateString2Id(language);

        if(languageId == -1) return ResultProcessor.warn_response("Cannot found language", "ExecCode");

        // 校验用户身份
        if(accountService.getByUuid(uuid) == null) return ResultProcessor.warn_response("Cannot found execute user", "ExecCode");
        if(accountService.getByUuid(uuid).getCertificated() == 0) return ResultProcessor.warn_response("You need pass email check to using this function", "ExecCode");

        // 运行记录生成
        CodeCacheEntity executeLog = new CodeCacheEntity();
        executeLog.setSessionCode(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        executeLog.setCreateTime(Time.sec(new Date().getTime()));
        executeLog.setStdin(stdin);
        executeLog.setSource(source);
        executeLog.setUuid(uuid);

        // 提交运行记录
        codeCacheService.save(executeLog);

        // 运行
        response.setStatus(200);
        return universalExecuteCode(source, stdin, languageId);
    }
}
