package SpeedCodeBKD.Data.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName(value = "Accounts")
public class AccountEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String access_token;
    private String activate_code;

    private Date create_date;
    private Date destroy_date;

    private String email;
    private String username;
    private String password;
    private String uuid;

    private Integer state;
    private Integer permission;

    @Override
    @SneakyThrows
    public String toString() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(this);
    }
}
