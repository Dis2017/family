package top.gytf.family.server.controllers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.mapper.UserMapper;
import top.gytf.family.server.search.GeneralSearch;
import top.gytf.family.server.security.code.SecurityCodeVerifyStrategy;
import top.gytf.family.server.security.code.email.EmailSecurityCodeRequestValidator;
import top.gytf.family.server.security.code.password.PasswordSecurityCodeRequestValidator;
import top.gytf.family.server.services.IUserService;
import top.gytf.family.server.utils.SecurityUtil;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.Email;
import java.util.Objects;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户控制器<br>
 * CreateDate:  2021/11/29 23:01 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@RestController
@RequestMapping(PathConstant.User.USER_PREFIX)
@Validated
@Slf4j
public class UserController {
    private final static String TAG = UserController.class.getName();

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 查询用户
     * @return 结果
     */
    @GetMapping(PathConstant.User.PATH_FIND_USER)
    @GeneralSearch(mapper = UserMapper.class, entityClass = User.class)
    public Object[] findUser() {
        return null;
    }

    /**
     * 分页查询用户
     * @return 分页结果
     */
    @GetMapping(PathConstant.User.PATH_FIND_USER_PAGE)
    @GeneralSearch(mapper = UserMapper.class, entityClass = User.class)
    public IPage<User> findUserPage() {
        return null;
    }

    /**
     * 注册用户
     * @param userInfo 用户
     */
    @PostMapping(PathConstant.User.PATH_REGISTER)
    @PermitAll
    public Long register(@Validated(User.GroupRegister.class) User userInfo) {
        userService.add(userInfo);
        return userInfo.getId();
    }

    /**
     * 更新用户信息（非保护数据）
     * @param updateInfo 用户信息
     * @return 新用户信息
     */
    @PatchMapping(PathConstant.User.PATH_MODIFY)
    public User modifyUser(@Validated(User.GroupModify.class) User updateInfo) {
        User user = SecurityUtil.current();
        assert user != null : "当前用户不存在";
        userService.update(user.getId(), updateInfo);
        SecurityUtil.update(userService.get(user.getId(), null, null));
        return SecurityUtil.current();
    }

    /**
     * 更新密码<br>
     * 需要通过密码、邮箱其中之一
     * @param password 新密码
     */
    @PatchMapping(PathConstant.User.PATH_MODIFY_PASSWORD)
    @SecurityCodeVerifyStrategy(
            value = {
                    PasswordSecurityCodeRequestValidator.class,
                    EmailSecurityCodeRequestValidator.class
            },
            only = true
    )
    public void modifyPassword(@Length(min = 8, max = 32, message = "密码应该在8-32位")
                               @RequestParam("password") String password) {
        Long id = Objects.requireNonNull(SecurityUtil.current()).getId();
        userService.modifyPassword(id, password);
    }

    /**
     * 绑定邮箱<br>
     * 建议调用前判断用户是否已经绑定邮箱<br>
     * 需要通过邮箱验证
     * @param email 邮箱地址
     */
    @PatchMapping(PathConstant.User.PATH_BIND_EMAIL)
    @SecurityCodeVerifyStrategy(EmailSecurityCodeRequestValidator.class)
    public void bindEmail(@Email(message = "邮箱格式不正确")
                          @RequestParam("email") String email) {
        Long id = Objects.requireNonNull(SecurityUtil.current()).getId();
        userService.bindEmail(id, email);
        SecurityUtil.update(userService.get(id, null, null));
    }

    /**
     * 解绑邮箱<br>
     * 需要通过邮箱验证
     */
    @DeleteMapping(PathConstant.User.PATH_UNBIND_EMAIL)
    @SecurityCodeVerifyStrategy(EmailSecurityCodeRequestValidator.class)
    public void unbindEmail() {
        User user = SecurityUtil.current();
        assert user != null : "当前用户不存在";
        userService.unbindEmail(user.getId());
        SecurityUtil.update(userService.get(user.getId(), null, null));
    }
}
