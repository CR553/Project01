package CR553.Service.impl;


import CR553.Mapper.DetailsMapper;
import CR553.Pojo.Details;
import CR553.Service.DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DetailsServiceImpl implements DetailsService {

    @Autowired
    private DetailsMapper detailsMapper;

    //查找并更新
    @Override
    public void saveDetails(Details details) {
        List<Details> list = this.findDetails(details);

        if (list.size() == 0) {
            //没查到,新增
            this.detailsMapper.saveDetails(details);
        }else
        {
            //查到了,修改
            this.detailsMapper.updateDetails(details);
        }
    }

    @Override
    public void updateDetails(Details details) {
        this.updateDetails(details);
    }

    @Override
    public List<Details> findDetails(Details details) {
        List<Details> list = this.detailsMapper.findDetails(details);
        return list;
    }

    @Override
    public List<String> findProvince() {
        List<String> list = this.detailsMapper.findProvince();
        return list;
    }

    @Override
    public List<Integer> findProvinceValue() {
        List<Integer> list = this.detailsMapper.findProvinceValue();
        return list;
    }

    @Override
    public List<String> findCity() {
        List<String> city = this.detailsMapper.findCity();
        return city;
    }

    @Override
    public List<Long> findCityValue() {
        List<Long> cityValue = this.detailsMapper.findCityValue();
        return cityValue;
    }

}
