package top.gytf.family.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Collection;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("User")
public class User extends BaseEntity implements UserDetails {
    private final static String TAG = User.class.getName();

    /**
     * 注册场景
     */
    public interface GroupRegister {}

    /**
     * 更新场景
     */
    public interface GroupModify {}

    public static final Class<?>[] GROUP_ALL = {
            GroupRegister.class,
            GroupModify.class
    };

    /**
     * 用户编号
     */
    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    @Null(
            message = "应该为空",
            groups = {
                    GroupRegister.class,
                    GroupModify.class
            }
    )
    private Long id;

    /**
     * 用户名称 （8位）
     */
    @TableField("name")
    @NotNull(
            message = "名称不能为空",
            groups = {
                    GroupRegister.class
            }
    )
    @Length(
            min = 2,
            max = 8,
            message = "名称应该在2-8位",
            groups = {
                    GroupRegister.class,
                    GroupModify.class
            }
    )
    private String name;

    /**
     * 密码 （32位）
     */
    @TableField("password")
    @Null(
            message = "密码应该为空",
            groups = {
                    GroupModify.class
            }
    )
    @NotNull(
            message = "密码不能为空",
            groups = {
                    GroupRegister.class
            }
    )
    @Length(min = 8,
            max = 32,
            message = "密码应该在8-32位",
            groups = {
                    GroupRegister.class,
                    GroupModify.class
            }
    )
    private String password;

    /**
     * <p>性别</p>
     * <li>true：    男</li>
     * <li>false：   女</li>
     * <li>null：    未知</li>
     */
    @TableField("sex")
    private Boolean sex;

    /**
     * 出生日期
     */
    @TableField("birthday")
    @Past(
            message = "出生日期应该是一个已经过去的时间"
    )
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
    @Null(
            message = "家庭应该为空",
            groups = {
                    GroupRegister.class,
                    GroupModify.class
            }
    )
    private Long familyId;

    /**
     * 电子邮箱
     */
    @TableField("email")
    @Email(
            message = "电子邮箱格式不正确",
            groups = {
                    GroupRegister.class,
                    GroupModify.class
            }
    )
    @Null(
            message = "电子邮箱应该为空",
            groups = {
                    GroupRegister.class,
                    GroupModify.class
            }
    )
    private String email;

    /**
     * 手机号码 （11位）
     */
    @TableField("phone")
    @Length(
            min = 11,
            max = 11,
            message = "手机号应该为11位",
            groups = {
                    GroupRegister.class,
                    GroupModify.class
            }
    )
    @Null(
            message = "手机号应该为空",
            groups = {
                    GroupRegister.class,
                    GroupModify.class
            }
    )
    private String phone;

    /**
     * 权限列表 <br>
     * 由创建实体时选择注入
     */
    @TableField(exist = false)
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns the username used to authenticate the user. Cannot return <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    @JsonIgnore
    public String getUsername() {
        return String.valueOf(id);
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return !getDeleteFlag();
    }
}
