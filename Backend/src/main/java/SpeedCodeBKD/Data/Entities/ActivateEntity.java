package SpeedCodeBKD.Data.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName(value = "Activate")
public class ActivateEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer amount;
    private Integer state;

    private String source;
    private String adder;
    private String settings;

    private Date destroy_time;
    private Date create_time;

    public JSONObject getSettings() { return new JSONObject(this.settings); }

    public void setSettings(JSONObject settings) { this.settings = settings.toString(); }
}
