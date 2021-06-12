package SpeedCodeBKD.Data.Service;

import SpeedCodeBKD.Data.Entities.ExecutorEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ExecutorService extends IService<ExecutorEntity> {
    List<ExecutorEntity> selectBySender(String sender);
    ExecutorEntity selectBySource(String source_code);

    void automaticDelete_outdated();
}
