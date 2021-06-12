package SpeedCodeBKD.Data.Service;

import SpeedCodeBKD.Data.Entities.ActivateEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ActivateService extends IService<ActivateEntity> {

    List<ActivateEntity> selectByAdder(String adder_uuid);

    ActivateEntity selectBySource(String code);

    void deleteByCount(Integer count);
    void automaticDelete_unused();
}
