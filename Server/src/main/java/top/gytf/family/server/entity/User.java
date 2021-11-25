package top.gytf.family.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
     * 用户编号
     */
    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    private Long id;

    /**
     * 用户名称 （8位）
     */
    @TableField("name")
    private String name;

    /**
     * 密码 （32位）
     */
    @TableField("password")
    private String password;

    /**
     * <p>性别</p>
     * <li>true：    男</li>
     * <li>false：   女</li>
     * <li>null：    未知</li>
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

    /**
     * 手机号码 （11位）
     */
    @TableField("phone")
    private String phone;

    /**
     * 权限列表 <br>
     * 由创建实体时选择注入
     */
    @TableField(exist = false)
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
    public String getUsername() {
        return null;
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
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