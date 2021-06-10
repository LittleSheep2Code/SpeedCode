package SpeedCodeBKD.Data.Mapper;

import SpeedCodeBKD.Data.Entites.ExecutorEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExecutorMapper extends BaseMapper<ExecutorEntity> {

    @Select("SELECT * FROM Executor WHERE sender=#{sender}")
    List<ExecutorEntity> selectBySender(String sender);

    @Select("SELECT * FROM Executor WHERE source_code=#{source_code}")
    ExecutorEntity selectBySource(String source_code);

    @Delete("DELETE FROM Executor WHERE amount=#{amount}")
    void deleteByAmount(Integer amount);

    @Delete("DELETE FROM CodeCache WHERE createTime>UNIX_TIMESTAMP()+1728000")
    void automaticDelete_outdated();
}
