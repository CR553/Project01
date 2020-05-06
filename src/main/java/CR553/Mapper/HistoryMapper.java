package CR553.Mapper;

import CR553.Pojo.History;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface HistoryMapper {

    //保存
    void saveHistory(History history);

    //更新
    void updateHistory(History history);

    //查找 日期相同的
    List<History> findHistory(History history);

    //查找今日数据
    History findToday();

    //返回每天历史累计数据
    List<History> findEachDayTotal();

    //返回每天历史增加数据
    List<History> findEachDayAdd();

}
