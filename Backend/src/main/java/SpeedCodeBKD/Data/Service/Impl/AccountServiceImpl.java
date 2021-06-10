package SpeedCodeBKD.Data.Service.Impl;

import SpeedCodeBKD.Utils.Processor.ResourceReader;
import SpeedCodeBKD.Utils.Processor.EmailSender;
import SpeedCodeBKD.Data.Entites.AccountEntity;
import SpeedCodeBKD.Data.Mapper.AccountMapper;
import SpeedCodeBKD.Data.Service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements AccountService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    EmailSender emailProcessor;

    public AccountEntity getByActivateCode(String activateCode) { return accountMapper.selectByActivateCode(activateCode); }
    public AccountEntity getByAccessToken(String accessToken) { return accountMapper.selectByAccessToken(accessToken); }

    public AccountEntity getByUsername(String username) { return accountMapper.selectByUsername(username); }
    public AccountEntity getByEmail(String email) { return accountMapper.selectByEmail(email); }
    public AccountEntity getByUuid(String uuid) { return accountMapper.selectByUuid(uuid); }

    public void removeOutdatedAccount() {
        String basicTemplate = ResourceReader.TemplateReader("static/document/config/delete-user-reason");
        String minute10 = basicTemplate
                .replace("${DELETE_REASON}", "Your SpeedCode account was deleted because it failed email verification for more than 10 minutes");

        String day30 = basicTemplate
                .replace("${DELETE_REASON}", "Your SpeedCode account has been deleted because the activation code has not been used for more than 30 days");

        for(AccountEntity removeAccount : accountMapper.selectList_State0())
            emailProcessor.send(null, null, new String[] {removeAccount.getEmail()},
                    "Your SpeedCode account was deleted", minute10.replace("${USERNAME}", removeAccount.getUsername()));

        accountMapper.removeOutdatedAccount_state0();

        for(AccountEntity removeAccount : accountMapper.selectList_State1())
            emailProcessor.send(null, null, new String[] {removeAccount.getEmail()},
                    "Your SpeedCode account has been deleted", day30.replace("${USERNAME}", removeAccount.getUsername()));
        accountMapper.removeOutdatedAccount_state1();
    }
}
