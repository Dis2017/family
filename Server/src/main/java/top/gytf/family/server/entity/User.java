package top.gytf.family.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   User
 * Description: 用户
 * CreateDate:  2021/11/22 15:49
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("User")
public class User extends BaseEntity {
    private final static String TAG = User.class.getName();

    /**
     * 用户编号
     */
    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    private Long id;

    /**
     * 用户名称
     */
    @TableField("name")
    private String name;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 性别
     */
    @TableField("sex")
    private Short sex;

    /**
     * 出生日期
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 地区
     */
    @TableField("area")
    private Integer area;

    /**
     * 归属家庭编号
     */
    @TableField("family_id")
    private Long familyId;

    /**
     * 电子邮箱
     */
    @TableField("email")
    private String email;
}
