package top.gytf.family.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: URL实体<br>
 * CreateDate:  2021/12/12 0:24 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("urls")
public class Url extends BaseEntity {
    private final static String TAG = Url.class.getName();

    /**
     * url编号
     */
    @TableId("id")
    private Long id;

    /**
     * url<br>
     * 512位
     */
    @TableField("url")
    private String url;

    /**
     * 请求方式<br>
     * 16位
     */
    @TableField("method")
    private String method;

    /**
     * 描述<br>
     * 1024位
     */
    @TableField("description")
    private String description;
}
