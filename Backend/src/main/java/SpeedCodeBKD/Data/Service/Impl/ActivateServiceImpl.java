package SpeedCodeBKD.Data.Service.Impl;

import SpeedCodeBKD.Data.Entites.ActivateEntity;
import SpeedCodeBKD.Data.Mapper.ActivateMapper;
import SpeedCodeBKD.Data.Service.ActivateService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivateServiceImpl extends ServiceImpl<ActivateMapper, ActivateEntity> implements ActivateService {

    @Autowired
    ActivateMapper activateMapper;

    public List<ActivateEntity> selectByAdder(String adder_uuid) { return activateMapper.selectByAdder(adder_uuid); }
    public ActivateEntity selectBySource(String source_code) { return activateMapper.selectByCode(source_code); }

    public void deleteByCount(Integer amount) { activateMapper.delete(new UpdateWrapper<ActivateEntity>().eq("amount", amount)); }

    public void automaticDelete_unused() { activateMapper.delete(new UpdateWrapper<ActivateEntity>().le("amount", 0)); }
}
