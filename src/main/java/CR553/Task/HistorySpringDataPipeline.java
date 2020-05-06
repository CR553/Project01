package CR553.Task;

import CR553.Pojo.History;
import CR553.Service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

@Component
public class HistorySpringDataPipeline implements Pipeline {

    @Autowired
    private HistoryService historyService;

    @Override
    public void process(ResultItems resultItems, Task task) {

        Map<String, Object> all = resultItems.getAll();

        for (Map.Entry<String, Object> entry : all.entrySet()) {
                String key = entry.getKey();
                History history= resultItems.get(key);
                this.historyService.saveHistory(history);
        }
    }


}