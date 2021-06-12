package SpeedCodeBKD.Data.Service.Impl;

import SpeedCodeBKD.Data.Entities.ExecutorEntity;
import SpeedCodeBKD.Data.Mapper.ExecutorMapper;
import SpeedCodeBKD.Data.Service.ExecutorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecutorServiceImpl extends ServiceImpl<ExecutorMapper, ExecutorEntity> implements ExecutorService {

    @Autowired
    ExecutorMapper executorMapper;

    public List<ExecutorEntity> selectBySender(String sender) { return executorMapper.selectBySender(sender); }
    public ExecutorEntity selectBySource(String source_code) { return executorMapper.selectBySource(source_code); }
    public void automaticDelete_outdated() { executorMapper.automaticDelete_outdated(); }
}
