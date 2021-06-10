package SpeedCodeBKD.Data.Service;

import SpeedCodeBKD.Data.Entites.AccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AccountService extends IService<AccountEntity> {

    AccountEntity getByAccessToken(String accessToken);
    AccountEntity getByUuid(String uuid);
    AccountEntity getByUsername(String username);
    AccountEntity getByEmail(String email);
    AccountEntity getByActivateCode(String activateCode);
    void removeOutdatedAccount();
}
