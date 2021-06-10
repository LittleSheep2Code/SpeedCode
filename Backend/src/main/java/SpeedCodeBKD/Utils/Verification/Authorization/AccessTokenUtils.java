package SpeedCodeBKD.Utils.Verification.Authorization;

import SpeedCodeBKD.Data.Entites.AccountEntity;
import SpeedCodeBKD.Data.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenUtils {

    @Autowired
    AccountService accountService;

    public AccountEntity Access2AccountEntity(String access_token) {
        return accountService.getByAccessToken(access_token);
    }
}
