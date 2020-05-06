package CR553.Mapper;

import CR553.Pojo.Details;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DetailsMapper {

    //存储
    void saveDetails(Details details);

    //更新
    void updateDetails(Details details);

    //查找(省份和市名相同的)
    List<Details> findDetails(Details details);

    //查找省
    List<String> findProvince();

    //查找每个省的确诊人数
    List<Integer> findProvinceValue();

    //查找城市
    List<String> findCity();

    //查找每个城市的确诊人数
    List<Long> findCityValue();

}
