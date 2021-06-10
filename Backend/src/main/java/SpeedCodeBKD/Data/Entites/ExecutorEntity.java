package SpeedCodeBKD.Data.Entites;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName(value = "Executor")
public class ExecutorEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long execute_time;

    private String source_code;
    private String sender;
    private String stdin;
    private String stdout;
}
