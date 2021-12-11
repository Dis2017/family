package top.gytf.family.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户和角色映射实体<br>
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
@TableName("user_role")
public class UserRole extends BaseEntity {
    private final static String TAG = UserRole.class.getName();

    /**
     * 映射编号
     */
    @TableId("id")
    private Long id;

    /**
     * user编号
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色编号
     */
    @TableField("role_id")
    private Long roleId;
}
