package top.gytf.family.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 角色实体<br>
 * CreateDate:  2021/12/12 0:20 <br>
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
@TableName("roles")
public class Role extends BaseEntity {
    private final static String TAG = Role.class.getName();

    /**
     * 角色编号
     */
    @TableId("id")
    private Long id;

    /**
     * 权限名称<br>
     * 128位
     */
    @TableField("role")
    private String role;

    /**
     * 描述<br>
     * 1024位
     */
    @TableField("description")
    private String description;
}
