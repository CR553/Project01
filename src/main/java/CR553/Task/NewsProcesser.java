package CR553.Task;

import CR553.Pojo.Hot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class NewsProcesser implements PageProcessor {
    @Override
    public void process(Page page) {
        //百度疫情热搜是动态渲染的,直接获取html格式获取不到
        //这里使用selenium模拟谷歌浏览器
        System.setProperty("webdriver.chrome.driver", "H:\\chromedriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();  //Chrome浏览器
        driver.get("https://voice.baidu.com/act/virussearch/virussearch");
        driver.findElement(By.xpath("//*[@id=\"ptab-0\"]/div/div[1]/section/div")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"ptab-0\"]/div/div[1]/section/a/div/span[2]"));
        for(int i=0;i<elements.size();i++)
        {
            Hot hot = new Hot();
            WebElement webElement = elements.get(i);
            String text = webElement.getText();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(new Date());

            hot.setDt(format);
            hot.setContent(text);

            UUID uuid = UUID.randomUUID();
            page.putField("hot"+uuid,hot);
        }
        driver.close();

    }

    private Site site = Site.me()
            .setCharset("utf-8")//设置编码
            .setTimeOut(10 * 1000)//超时时间
            .setRetrySleepTime(3000)//重试的间隔时间
            .setRetryTimes(3);//重试的次数

    @Override
    public Site getSite() {
        return site;
    }

    @Autowired
    private  NewsSpringDataPipeline newsSpringDataPipeline;

    @Scheduled(initialDelay = 60*60*1000, fixedDelay = 12*60*60 * 1000)
    public void process() {
        Spider.create(new NewsProcesser())
                .addUrl("https://voice.baidu.com/act/virussearch/virussearch")
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(1000000)))
                .thread(10)
                .addPipeline(newsSpringDataPipeline)
                .run();
    }
}