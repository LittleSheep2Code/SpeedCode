package SpeedCodeBKD.Data.Mapper;

import SpeedCodeBKD.Data.Entities.AccountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountMapper extends BaseMapper<AccountEntity> {

    @Select("SELECT * FROM Accounts WHERE uuid=#{uuid}")
    AccountEntity selectByUuid(String uuid);

    @Select("SELECT * FROM Accounts WHERE accessToken=#{accessToken}")
    AccountEntity selectByAccessToken(String accessToken);

    @Select("SELECT * FROM Accounts WHERE username=#{username}")
    AccountEntity selectByUsername(String username);

    @Select("SELECT * FROM Accounts WHERE email=#{email}")
    AccountEntity selectByEmail(String email);

    @Select("SELECT * FROM Accounts WHERE activateCode=#{activateCode}")
    AccountEntity selectByActivateCode(String activateCode);

    @Select("SELECT * FROM Accounts WHERE registerTime>UNIX_TIMESTAMP()+600 AND state=0")
    List<AccountEntity> selectList_State0();

    @Select("SELECT * FROM Accounts WHERE registerTime>UNIX_TIMESTAMP()+2592000 AND state=1")
    List<AccountEntity> selectList_State1();

    @Delete("DELETE FROM Accounts WHERE registerTime>UNIX_TIMESTAMP()+600 AND certificated=0")
    void removeOutdatedAccount_state0();

    @Delete("DELETE FROM Accounts WHERE registerTime>UNIX_TIMESTAMP()+2592000 AND certificated=1")
    void removeOutdatedAccount_state1();
}
