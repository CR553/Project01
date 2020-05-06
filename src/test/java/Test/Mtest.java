package Test;

import CR553.Application;
import CR553.Pojo.Details;
import CR553.Pojo.History;
import CR553.Pojo.Hot;
import CR553.Service.DetailsService;
import CR553.Service.HistoryService;
import CR553.Service.HotService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class Mtest {

    @Autowired
    DetailsService detailsService;

    @Autowired
    HotService hotService;

    @Autowired
    HistoryService historyService;


    @Test
    public void test33()
    {
        List<Hot> topHot20 = this.hotService.findTopHot20();

        JiebaSegmenter segmenter = new JiebaSegmenter();
        JSONArray list = new JSONArray();

        for (Hot hot : topHot20) {
            String content=hot.getContent();
            //获取数字
            String num = content.replaceAll("[\\u4e00-\\u9fa5]", "");
            //获取中文
            String  msg= content.replaceAll("[0-9]", "");
            //分词
            List<String> strings = segmenter.sentenceProcess(msg);
            for (String string : strings) {
                Pattern pattern = Pattern.compile("[0-9]*");
                if(!pattern.matcher(string).matches())//不为数字
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name",string);
                    jsonObject.put("value",num);
                    list.add(jsonObject);
                }
            }
        }
        System.out.println(list);
    }

    @Test
    public void test22()
    {
        String text = "前任拉甘送苏宁首败落后恒大6分争冠难了123141414";
        String[] str_string = text.split("\\d");
        String[] num_string = text.split("\\D");
        JiebaSegmenter segmenter = new JiebaSegmenter();
        segmenter.sentenceProcess(text);


        System.out.println("数字来源:" + text.replaceAll("[\\u4e00-\\u9fa5]", ""));
        System.out.println("字符知道:" + text.replaceAll("[0-9]", ""));

    }

    @Test
    public void test11()
    {

        Details details = new Details();
        details.setCity("梅海迪");
        details.setProvince("真帅");
        details.setConfirm((long)9999);
        details.setConfirm_add((long)9999);
        details.setDead((long)9999);
        details.setHeal((long)9999);

        detailsService.saveDetails(details);


        History history = new History();
        history.setConfirm_add((long)9999);
        history.setDead((long)9999);
        history.setHeal((long)9999);
        history.setDs("梅海迪");

        this.historyService.saveHistory(history);


        History today = historyService.findToday();
        System.out.println(today);
        Hot hot = new Hot();
        hot.setContent("梅海迪真帅");
        hot.setDt("设计费撒娇哈佛");
        this.hotService.saveHot(hot);

    }

    @Test
    public void test5666()
    {
        List<String> province = this.detailsService.findProvince();
        List<Integer> provinceValue = this.detailsService.findProvinceValue();
        List<Details> list=new ArrayList<>();
        for(int i=0;i<province.size();i++)
        {
            Details details = new Details();
            details.setProvince(province.get(i));
            details.setConfirm((long)provinceValue.get(i));
            list.add(details);
        }
        System.out.println(list);
        System.out.println(list);
    }

    //测试findEachTotal
    @Test
    public void test99087()
    {
        List<History> eachDayTotal = this.historyService.findEachDayTotal();
        for (History history : eachDayTotal) {
            System.out.println(history);
        }
        System.out.println(eachDayTotal);

        List<History> eachDayAdd = this.historyService.findEachDayAdd();
        for (History history : eachDayAdd) {
            System.out.println(history);
        }
        System.out.println(eachDayAdd);
    }

    @Test
    public void test4525()
    {
        History history = new History();
        history.setDs("05.01");
        List<History> list = this.historyService.findHistory(history);
        for (History history1 : list) {
            System.out.println(history1);
        }
        System.out.println(list);

        history.setConfirm_add((long)99999999);
        history.setConfirm_add((long)99999999);
        this.historyService.saveHistory(history);
    }

    @Test
    public void test23452()
    {
        List<String> city = this.detailsService.findCity();
        List<Long> cityValue = this.detailsService.findCityValue();
        for (int i = 0; i <city.size() ; i++) {
            System.out.println(city);
            System.out.println(cityValue);
        }
    }
}
