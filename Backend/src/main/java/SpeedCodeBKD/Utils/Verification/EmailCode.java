package SpeedCodeBKD.Utils.Verification;

import SpeedCodeBKD.Data.Service.AccountService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class EmailCode {

    @Autowired
    AccountService accountService;

    @SneakyThrows
    public String summon() {
        while(true) {
            String code = UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0, 6);
            if(accountService.selectByActivateCode(code) == null) return code;
        }
    }
}
