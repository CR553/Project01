package CR553.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
