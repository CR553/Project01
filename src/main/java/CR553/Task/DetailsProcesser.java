package CR553.Task;

import CR553.Pojo.Details;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Json;

import java.util.UUID;

@Component
public class DetailsProcesser implements PageProcessor {

    @Override
    public void process(Page page) {
        if(page!=null)
        {
            saveDetails(page);
        }
    }

    private void saveDetails(Page page) {
        Json json = page.getJson();
        JSONObject jsonObject = JSON.parseObject(String.valueOf(json));
        //获取key为data的json对象
        JSONObject data = jsonObject.getJSONObject("data");
        //获取key为areaTree的json数组
        JSONArray areaTree = data.getJSONArray("areaTree");
        //获取key为0的json对象,0表示中国
        JSONObject china = areaTree.getJSONObject(0);
        //获取省份列表
        JSONArray provincelist =china.getJSONArray("children");
        for (int ProvinceNum = 0; ProvinceNum <provincelist.size() ; ProvinceNum++) {
            //获取省份
            JSONObject province = provincelist.getJSONObject(ProvinceNum);
            Object provinceName = province.get("name");//获取省名

            //获取城市列表
            JSONArray citylist =province.getJSONArray("children");
            for(int CityNum=0;CityNum <citylist.size();CityNum++)
            {
                Details details = new Details();
                //获取城市
                JSONObject city = citylist.getJSONObject(CityNum);

                //获取key为total的json对象
                JSONObject city_total = city.getJSONObject("total");
                Object city_confirm = city_total.get("confirm");
                Object city_heal = city_total.get("heal");
                Object city_dead = city_total.get("dead");

                long city_confirm1 = Long.valueOf(String.valueOf(city_confirm));
                long city_heal1 = Long.valueOf(String.valueOf(city_heal));
                long city_dead1 = Long.valueOf(String.valueOf(city_dead));
                details.setConfirm(city_confirm1);//设置该市累计确诊
                details.setDead(city_heal1);//设置该市累计治愈
                details.setHeal(city_dead1);//设置该市累计死亡

                //获取key为tody的json对象
                JSONObject today = city.getJSONObject("today");
                Object today_confirm = today.get("confirm");
                Long today_confirm1 = Long.valueOf(String.valueOf(today_confirm));
                details.setConfirm_add(today_confirm1);//设置该市当日新增确诊

                //获取最后更新时间
                Object lastUpdateTimeObj = data.get("lastUpdateTime");
                details.setProvince(provinceName.toString());//设置省
                details.setUpdate_time(lastUpdateTimeObj.toString());//设置更新时间
                Object cityName = city.get("name");
                details.setCity(cityName.toString());//设置市名

                UUID uuid = UUID.randomUUID();
                //保存结果
                page.putField("details"+uuid,details);

            }

        }

    }

    private  Site site=Site.me()
            .setCharset("utf-8")//设置编码
            .setTimeOut(10*1000)//超时时间
            .setRetrySleepTime(3000)//重试的间隔时间
            .setRetryTimes(3);//重试的次数
    @Override
    public Site getSite() {
        return site;
    }

    @Autowired
    private DetailsSpringDataPipeline detailsSpringDataPipeline;

    @Scheduled(initialDelay = 1000,fixedDelay = 12*60 *60* 1000)
    public void process(){
        Spider.create(new DetailsProcesser())
                .addUrl("https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5")
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000000)))
                .addPipeline(detailsSpringDataPipeline)
                .thread(5)
                .run();

    }


}
