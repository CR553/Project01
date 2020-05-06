package CR553.Mapper;

import CR553.Pojo.Hot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;



@Mapper
public interface HotMapper {

    //保存
    void saveHot(Hot hot);

    List<Hot> findTopHot20();

}
