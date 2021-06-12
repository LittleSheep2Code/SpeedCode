package SpeedCodeBKD.Data.Service;

import SpeedCodeBKD.Data.Entities.AccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AccountService extends IService<AccountEntity> {

    AccountEntity selectByAccessToken(String accessToken);
    AccountEntity selectByUuid(String uuid);
    AccountEntity selectByUsername(String username);
    AccountEntity selectByEmail(String email);
    AccountEntity selectByActivateCode(String activateCode);
    void removeOutdatedAccount();
}
