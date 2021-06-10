package SpeedCodeBKD.Data.Mapper;

import SpeedCodeBKD.Data.Entites.ActivateEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ActivateMapper extends BaseMapper<ActivateEntity> {

    @Select("SELECT * FROM Activate WHERE adder=#{adder}")
    List<ActivateEntity> selectByAdder(String adder);

    @Select("SELECT * FROM Activate WHERE source=#{source_code}")
    ActivateEntity selectByCode(String source_code);
}
