package CR553.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
