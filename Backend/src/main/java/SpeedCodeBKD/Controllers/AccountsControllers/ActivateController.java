package SpeedCodeBKD.Controllers.AccountsControllers;

import SpeedCodeBKD.Common.Annotation.AccessLevel;
import SpeedCodeBKD.Data.Entities.AccountEntity;
import SpeedCodeBKD.Data.Entities.ActivateEntity;
import SpeedCodeBKD.Data.Service.AccountService;
import SpeedCodeBKD.Data.Service.ActivateService;
import SpeedCodeBKD.Utils.Processor.ResultProcessor;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("accounts-utils/activate")
public class ActivateController {

    @Autowired
    ActivateService activateService;
    AccountService accountService;

    @SneakyThrows
    @AccessLevel(0)
    @PostMapping(value = "create/{adder}/{source}", produces = "application/json; charset=UTF-8")
    private String activateCodeBuilder(@PathVariable String source, @PathVariable String adder, HttpServletResponse response) {

        // 验证提交数据
        // $(0) 验证 目标数据
        if (source.split("-").length != 5) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Create-ActivateEntity");
        }

        // $(1) 验证 Owner
        if (accountService.selectByUuid(adder) == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.undefined, "Create-ActivateEntity");
        }

        // $(2) 验证 Owner 邮箱与等级
        if (accountService.selectByUuid(adder).getState() == 0) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.permission_insufficient, "Create-ActivateEntity");
        }

        if (accountService.selectByUuid(adder).getPermission() < 100) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.permission_insufficient, "Create-ActivateEntity");
        }

        // 创建对象
        // $(0) new 对象
        ActivateEntity commitData = new ActivateEntity();

        // $(1) 填入数据
        commitData.setSource(source);
        commitData.setAdder(adder);
        commitData.setAmount(1);

        // $(2) 提交至数据库
        activateService.save(commitData);

        // 返回完成
        JSONObject object = new JSONObject();
        object.put("status", "Completed register code");

        response.setStatus(200);
        return ResultProcessor.completed_response("Completed add a new code", "Create-ActivateEntity");
    }

    @SneakyThrows
    @AccessLevel(value = 0, max = 1, ignore_admins = true)
    @GetMapping(value = "activate/{source}:{account_uuid}", produces = "application/json; charset=UTF-8")
    private String activateAccount(@PathVariable String source, @PathVariable String account_uuid, HttpServletResponse response) {

        // 验证提交数据
        // $(0) 验证 目标数据
        if (activateService.selectBySource(source) == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.wrong_data, "Use-ActivateEntity");
        }

        // $(1) 验证 使用目标
        if (accountService.selectByUuid(account_uuid) == null) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.undefined, "Use-ActivateEntity");
        }

        // $(3) 验证 使用权限
        if (activateService.selectBySource(source).getState() < 1 && !account_uuid.equals(activateService.selectBySource(source).getAdder())) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.permission_insufficient, "Use-ActivateEntity");
        }

        if (accountService.selectByUuid(account_uuid).getState() == 0) {
            response.setStatus(201);
            return ResultProcessor.warn_response(ResultProcessor.ReasonCode.permission_insufficient, "Use-ActivateEntity");
        }

        // 提交数据
        AccountEntity updateAccount = accountService.selectByUuid(account_uuid);
        updateAccount.setState(2); accountService.saveOrUpdate(updateAccount);

        ActivateEntity updateActivate = activateService.selectBySource(source);
        updateActivate.setAmount(updateActivate.getAmount() - 1);
        activateService.update(updateActivate, new UpdateWrapper<ActivateEntity>().eq("code", source));

        // 清理门户
        automaticClean_unusedData();

        // 返回完成
        response.setStatus(200);
        return ResultProcessor.completed_response("Completed activated account", "Use-ActivateEntity");
    }

    @SneakyThrows
    private void automaticClean_unusedData() {
        activateService.automaticDelete_unused();
    }
}
