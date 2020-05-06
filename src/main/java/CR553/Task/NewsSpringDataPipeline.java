package CR553.Task;

import CR553.Pojo.Hot;
import CR553.Service.HotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

@Component
public class NewsSpringDataPipeline implements Pipeline {

    @Autowired
    private HotService hotService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> all = resultItems.getAll();

        for (Map.Entry<String, Object> entry : all.entrySet()) {
            String key = entry.getKey();
            Hot hot = resultItems.get(key);
            this.hotService.saveHot(hot);
        }
    }
}