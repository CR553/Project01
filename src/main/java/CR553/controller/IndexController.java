package CR553.controller;

import CR553.Pojo.History;
import CR553.Pojo.Hot;
import CR553.Service.DetailsService;
import CR553.Service.HistoryService;
import CR553.Service.HotService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class IndexController {

    @Autowired
    HistoryService historyService;

    @Autowired
    HotService hotService;

    @Autowired
    DetailsService detailsService;

    @RequestMapping("/")
    public String index()
    {
        return "index";
    }

    @RequestMapping("/time")
    @ResponseBody
    public String getTime()
    {
        String date = DateFormat.getDateTimeInstance().format(new Date());
        return date;
    }


    @RequestMapping("/c1")
    @ResponseBody
    public JSONObject getDateC1()
    {
        History today = this.historyService.findToday();
        JSONObject json = new JSONObject();
        json.put("confirm",today.getConfirm());
        json.put("suspect",today.getSuspect());
        json.put("heal",today.getHeal());
        json.put("dead",today.getDead());
        return json;
    }

    @RequestMapping("/r2")
    @ResponseBody
    public JSONArray r2()
    {
        //获取hot前20
        List<Hot> topHot20 = this.hotService.findTopHot20();
        //jieba分词
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
        return list;
    }


    @RequestMapping("/c2")
    @ResponseBody
    public JSONArray c2()
    {
        JSONArray list = new JSONArray();
        List<String> province = this.detailsService.findProvince();
        List<Integer> provinceValue = this.detailsService.findProvinceValue();
        for (int i = 0; i <province.size() ; i++) {
            JSONObject js = new JSONObject();
            js.put("name",province.get(i));
            js.put("value",provinceValue.get(i));
            list.add(js);
        }
        return list;

    }
    
    @RequestMapping("/l1")
    @ResponseBody
    public JSONObject l1()
    {
        List<History> eachDayTotal = this.historyService.findEachDayTotal();
        JSONObject json = new JSONObject();
        List<String> daylist=new ArrayList<>();
        List<Long> confirmlist=new ArrayList<>();
        List<Long> heallist=new ArrayList<>();
        List<Long> deadlist=new ArrayList<>();
        List<Long> suspectlist=new ArrayList<>();

        for (History history : eachDayTotal) {
            daylist.add(history.getDs());
            confirmlist.add(history.getConfirm());
            heallist.add(history.getHeal());
            deadlist.add(history.getDead());
            suspectlist.add(history.getSuspect());
        }
        json.put("day",daylist);
        json.put("confirm",confirmlist);
        json.put("heal",heallist);
        json.put("dead",deadlist);
        return json;
    }


    @RequestMapping("/l2")
    @ResponseBody
    public JSONObject l2()
    {
        List<History> eachDayTotal = this.historyService.findEachDayAdd();
        JSONObject json = new JSONObject();
        List<String> daylist=new ArrayList<>();
        List<Long> confirmlist=new ArrayList<>();
        List<Long> heallist=new ArrayList<>();
        List<Long> deadlist=new ArrayList<>();
        List<Long> suspectlist=new ArrayList<>();

        for (History history : eachDayTotal) {
            daylist.add(history.getDs());
            confirmlist.add(history.getConfirm_add());
            heallist.add(history.getHeal_add());
            deadlist.add(history.getDead_add());
            suspectlist.add(history.getSuspect_add());
        }

        json.put("day",daylist);
        json.put("confirm",confirmlist);
        json.put("heal",heallist);
        json.put("dead",deadlist);
        return json;
    }

    @RequestMapping("/r1")
    @ResponseBody
    public JSONObject r1()
    {
        JSONObject json = new JSONObject();
        List<String> city = this.detailsService.findCity();
        List<Long> cityValue = this.detailsService.findCityValue();
        json.put("city",city);
        json.put("cityValue",cityValue);
        return json;
    }

}
