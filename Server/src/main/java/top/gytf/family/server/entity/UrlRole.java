package top.gytf.family.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: URL与角色的映射实体<br>
 * CreateDate:  2021/12/12 0:28 <br>
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
@TableName("url_role")
public class UrlRole extends BaseEntity {
    private final static String TAG = UrlRole.class.getName();

    /**
     * 映射编号
     */
    @TableId("id")
    private Long id;

    /**
     * url编号
     */
    @TableField("url_id")
    private Long urlId;

    /**
     * 角色编号
     */
    @TableField("role_id")
    private Long roleId;
}
