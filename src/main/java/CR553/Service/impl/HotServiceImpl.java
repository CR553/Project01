package CR553.Service.impl;

import CR553.Mapper.HotMapper;
import CR553.Pojo.Hot;
import CR553.Service.HotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotServiceImpl implements HotService {

    @Autowired
    private HotMapper hotMapper;

    @Override
    public void saveHot(Hot hot) {
       this.hotMapper.saveHot(hot);
    }

    @Override
    public List<Hot> findTopHot20() {
        return this.hotMapper.findTopHot20();
    }


}
