# 页面效果

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507153752808.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70#pic_center)


# 项目准备



## 所用技术

springboot+mybatis+echarts+webmagic

## 实现过程

用webmagic爬取腾讯,百度疫情网站,获取数据

将返回的数据存储在mysql中

编写业务,在controller中调用业务

用ajax获取controller传来的数据

## 环境和软件

JDK1.8 ,IntelliJ IDEA 2020.1 x64, MySQL 5.5.40,node.js v12.16.2 ,Maven

## 依赖

```
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.2.RELEASE</version>
    </parent>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
        </dependency>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>3.9.1</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>com.huaban</groupId>
            <artifactId>jieba-analysis</artifactId>
            <version>1.0.2</version>
        </dependency>
        <!--SpringMVC-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.1.1</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>us.codecraft</groupId>
            <artifactId>webmagic-core</artifactId>
            <version>0.7.3</version>
        </dependency>
        <dependency>
            <groupId>us.codecraft</groupId>
            <artifactId>webmagic-extension</artifactId>
            <version>0.7.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>
    </dependencies>
```

## 数据库搭建

```
#详情信息
CREATE TABLE `details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `update_time` varchar(50) DEFAULT NULL COMMENT '数据最后更新时间',
  `province` varchar(50) DEFAULT NULL COMMENT '省',
  `city` varchar(50) DEFAULT NULL COMMENT '市',
  `confirm` int(11) DEFAULT NULL COMMENT '累计确诊',
  `confirm_add` int(11) DEFAULT NULL COMMENT '新增确诊',
  `heal` int(11) DEFAULT NULL COMMENT '累计治愈',
  `dead` int(11) DEFAULT NULL COMMENT '累计死亡',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1826 DEFAULT CHARSET=utf8mb4

```

```
#历史信息
CREATE TABLE `history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ds` varchar(50) NOT NULL COMMENT '日期',
  `confirm` int(11) DEFAULT NULL COMMENT '累计确诊',
  `confirm_add` int(11) DEFAULT NULL COMMENT '当日新增确诊',
  `suspect` int(11) DEFAULT NULL COMMENT '剩余疑似',
  `suspect_add` int(11) DEFAULT NULL COMMENT '当日新增疑似',
  `heal` int(11) DEFAULT NULL COMMENT '累计治愈',
  `heal_add` int(11) DEFAULT NULL COMMENT '今日新增治愈',
  `dead` int(11) DEFAULT NULL COMMENT '累计死亡',
  `dead_add` int(11) DEFAULT NULL COMMENT '当日新增死亡',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4
```

```sql
#热搜
Create Table

CREATE TABLE `hot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dt` varchar(50) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1085 DEFAULT CHARSET=utf8mb4
```

## 爬虫源网址
在项目中

## Application配置类

```
DB Configuration:
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/pro?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=root


mybatis.type-aliases-package=CR553.Pojo
mybatis.mapper-locations=classpath:Mapper/*.xml

server.port=8088

//关闭缓存
spring.thymeleaf.cache=false
```



## POJO,Mapper,Service

没啥特别的,基本的crud,直接上代码

POJO

```java
@Data//依赖于lombok
@AllArgsConstructor
@NoArgsConstructor
public class Details {
    private Long id;
    private String update_time; //更新时间
    private String province; //省
    private String city; //市
    private Long confirm; //累计确诊
    private Long confirm_add; //今日新增确诊
    private Long heal; //治愈
    private Long dead; //死亡
}
```

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
public class History {
    private Long id;
    private String ds; //日期
    private Long confirm;//累计确诊
    private Long confirm_add;//今日累计确诊
    private Long suspect;//疑似
    private Long suspect_add;//今日新增疑似
    private Long heal;//治愈
    private Long heal_add;//当日新增治愈
    private Long dead;//死亡
    private Long dead_add;//今日新增死亡
}
```

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hot {
    private Long id;
    private String content;
    private String dt;
}

```

mapper

```
@Mapper
public interface DetailsMapper {

    //存储
    void saveDetails(Details details);

    //更新
    void updateDetails(Details details);

    //查找(省份和市名相同的)
    List<Details> findDetails(Details details);

    //查找省
    List<String> findProvince();

    //查找每个省的确诊人数
    List<Integer> findProvinceValue();

    //查找城市
    List<String> findCity();

    //查找每个城市的确诊人数
    List<Long> findCityValue();

}
```

```
@Mapper
public interface HistoryMapper {

    //保存
    void saveHistory(History history);

    //更新
    void updateHistory(History history);

    //查找 日期相同的
    List<History> findHistory(History history);

    //查找今日数据
    History findToday();

    //返回每天历史累计数据
    List<History> findEachDayTotal();

    //返回每天历史增加数据
    List<History> findEachDayAdd();

}

```

```
@Mapper
public interface HotMapper {

    //保存
    void saveHot(Hot hot);

    List<Hot> findTopHot20();

}
```

serviceImp

```
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

```

```
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

```

```
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

```



# Webmagic爬虫

可能就是在解析数据的时候要花一点点时间

这里是通过打个断点,debug慢慢分析的

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020050711585661.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)

差不多就这样测试



先看details数据的解析

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855787.png)

数据在这两个中获取


![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507124248393.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507124248374.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507124248415.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
本质上就是遍历一个双重for循环,外层获取省,内层获取市

history
注意的也是两个json数据

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855905.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507124248416.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507124248412.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
这里要注意一下,chinaDayList中的日期是从1月20号开始的,而chinaDayAddList中的日期是从1月13号开始的,还有一点就是防止数组越界,这一点在代码里面会提到.

一个for循环就可以搞定了.

热搜hot表数据很少,debug一下很容易发现,这里不做说明.
# 数据的保存

以details为例

把数据先保存在ResultItems中,我这里用了随机id,防止key相同

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855857.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
使用pipeline
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020050711585612.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
遍历ResultItems,调用detailsService方法保存数据到数据库
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855986.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
另外两个操作基本相同


# Controller+SQL+ajax


# Controller+SQL+ajax

数据保存到数据库后,剩下就是发送数据了

整个数据的展示大体分为了六个部分,依次是l1,l2,c1,c2,r1,r2

#### c1  

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855998.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855886.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855970.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
#### c2  地图

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020050711585620.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/202005071158568.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855952.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
这里的两个sql其实是可以放一起,我这里多走了一步路

#### l1  折线图



![在这里插入图片描述](https://img-blog.csdnimg.cn/2020050711585611.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855917.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855900.png)

#### l2  折线图

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020050711585660.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855921.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855893.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)

#### r1  柱形图


![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855972.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020050711585614.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855989.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
这两个sql其实是可以放在一起的,但是我的放一起只查出了一列数据,于是就多走了一步路

#### r2  词云

这里用到了jieba分词器,将分词后的关键字和数值返回给前端

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020050711585677.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855974.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855849.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200507115855944.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDI3Mzk0OA==,size_16,color_FFFFFF,t_70)


# 其他技术
关于echarts,该项目用的图表都是echarts中的基础图表,在官网都可以找到,你也可以用你自己找的图表,插值方式大同小异,这里注意的是
地图和词云需要先引入相关js文件,相关文件都可以在echarts官网下载
地图和词云需要先引入相关js文件,相关文件都可以在echarts官网下载

<https://echarts.apache.org/zh/download-extension.html>

五分钟上手echarts

[<https://echarts.apache.org/zh/tutorial.html#5%20%E5%88%86%E9%92%9F%E4%B8%8A%E6%89%8B%20ECharts>](%3Chttps://echarts.apache.org/zh/tutorial.html#5%20%E5%88%86%E9%92%9F%E4%B8%8A%E6%89%8B%20ECharts%3E)

简单实用jieba

[<https://blog.csdn.net/wbcg111/article/details/53191721>](%3Chttps://blog.csdn.net/wbcg111/article/details/53191721%3E)

selenium基本实用

[<https://blog.csdn.net/qq_22003641/article/details/79137327>](%3Chttps://blog.csdn.net/qq_22003641/article/details/79137327%3E)

webmagic官方文档
[http://webmagic.io/docs/zh/](

# 总结

这个项目简单,基础,适合新手.这是我学完springboot 之后第一个小项目,做之前感觉无从下手,做完后又觉得没有什么,项目是参考b站上一个基于python实现的视频做的. 数据库的搭建,sql语句,都是模仿的.自己的部分主要是爬虫和业务的编写.尽管如此,我还是花了好几天时间,从一开始的懵逼到慢慢拨云见日,做完还是有一丢丢的成就感的.

获取资源的路有点曲折,那个视频拿资料要加vx,我一加,说要去他们的培训机构官网注册账号,当时嫌麻烦,没要

在视频的评论区有人先做出来了,基本和视频没差,他把链接发了到了评论(事实上他在好几个疫情可视化的视频下面都留了链接,我真的是服了),我顺着网站找到了他的博客,下面有人留言说可不可以要源码,他说先加vx.我兴冲冲的去加了,但是向他要的时候他说**"有偿!!!" **   

我理解但不认同这种做法,也幸亏他拒绝了我,浇灭了我想偷懒的心

在网上也很少搜到基于java实现的疫情可视化项目(其实主要是爬虫数据解析部分不明朗,大家好像都去用python了)

基于以上原因,还有我一路对白嫖过来的视频和资源提供者感激,我当时就决定自己把这种项目写出来,发到网上,供像我一样的萌新参考,学习.

第一次写,可能整理的不太详细,具体的就参照源码吧,. 基本的框架差不多就是这样,网站我会慢慢修改,逐渐脱离之前的布局(u1s1,真的有点丑),与此同时这也是一个融合和学习的好途径.

**开源是一种精神**

基础,适合新手.这是我学完springboot 之后第一个小项目,做之前感觉无从下手,做完后又觉得没有什么,项目是参考b站上一个基于python实现的视频做的. 数据库的搭建,sql语句,都是模仿的.自己的部分主要是爬虫和业务的编写.尽管如此,我还是花了好几天时间,从一开始的懵逼到慢慢拨云见日,做完还是有一丢丢的成就感的.

获取资源的路有点曲折,那个视频拿资料要加vx,我一加,说要去他们的培训机构官网注册账号,当时嫌麻烦,没要

在视频的评论区有人先做出来了,基本和视频没差,他把链接发了到了评论(事实上他在好几个疫情可视化的视频下面都留了链接,我真的是服了),我顺着网站找到了他的博客,下面有人留言说可不可以要源码,他说先加vx.我兴冲冲的去加了,但是向他要的时候他说**"有偿!!!" **   

我理解但不认同这种做法,也幸亏他拒绝了我,浇灭了我想偷懒的心

在网上也很少搜到基于java实现的疫情可视化项目(其实主要是爬虫数据解析部分不明朗,大家好像都去用python了)

基于以上原因,还有我一路对白嫖过来的视频和资源提供者感激,我当时就决定自己把这种项目写出来,发到网上,供像我一样的萌新参考,学习.

第一次写,可能整理的不太详细,具体的就参照源码吧,. 基本的框架差不多就是这样,网站我会慢慢修改,逐渐脱离之前的布局(u1s1,真的有点丑),与此同时这也是一个融合和学习的好途径.

**开源是一种精神**

具体代码见 GitHub:[https://github.com/CR553/Project01.git](https://github.com/CR553/Project01.git)
