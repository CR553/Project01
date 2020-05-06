package CR553.Task;

import CR553.Pojo.Details;
import CR553.Service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

@Component
public class DetailsSpringDataPipeline implements Pipeline {

    @Autowired
    private DetailsService detailsService;

    @Override
    public void process(ResultItems resultItems, Task task) {

        Map<String, Object> all = resultItems.getAll();

        for (Map.Entry<String, Object> entry : all.entrySet()) {
            String key = entry.getKey();
            Details details = resultItems.get(key);
            this.detailsService.saveDetails(details);
        }
    }
}