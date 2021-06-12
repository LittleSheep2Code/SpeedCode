package SpeedCodeBKD.Data.Entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName(value = "Executor")
public class ExecutorEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Date execute_time;

    private String source_code;
    private String sender;
    private String stdin;
    private String stdout;
}
