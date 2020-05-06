package CR553.Service.impl;

import CR553.Mapper.HistoryMapper;
import CR553.Pojo.History;
import CR553.Service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public void saveHistory(History history) {
        List<History> list = this.historyMapper.findHistory(history);
        if(list.size()==0)
        {

            this.historyMapper.saveHistory(history);
        }else
        {
            this.historyMapper.updateHistory(history);
        }
    }

    @Override
    public void updateHistory(History history) {
        this.historyMapper.updateHistory(history);
    }

    @Override
    public List<History> findHistory(History history) {
        return this.historyMapper.findHistory(history);
    }

    @Override
    public History findToday() {
        return this.historyMapper.findToday();
    }

    @Override
    public List<History> findEachDayTotal() {
        return this.historyMapper.findEachDayTotal();
    }

    @Override
    public List<History> findEachDayAdd() {
        return this.historyMapper.findEachDayAdd();
    }


}
