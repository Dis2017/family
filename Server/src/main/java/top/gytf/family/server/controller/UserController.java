package top.gytf.family.server.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.gytf.family.server.Utils;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.services.IUserService;

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
public class UserController {
    private final static String TAG = UserController.class.getName();

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 注册用户
     * @param user 用户
     */
    @PostMapping(PathConstant.User.PATH_REGISTER)
    @PermitAll
    public Long register(@Validated User user) {
        userService.add(user);
        return user.getId();
    }

    /**
     * 绑定邮箱<br>
     * 建议调用前判断用户是否已经绑定邮箱
     * @param email 邮箱地址
     */
    @PostMapping(PathConstant.User.PATH_BIND_EMAIL)
    public void bindEmail(
            @Validated
            @Email(message = "邮箱格式不正确")
            @RequestParam("email") String email) {
        Long id = Objects.requireNonNull(Utils.Security.current()).getId();
        userService.bindEmail(id, email);
        Utils.Security.update(userService.get(id, null, null));
    }

    /**
     * 解绑邮箱
     */
    @DeleteMapping(PathConstant.User.PATH_UNBIND_EMAIL)
    public void unbindEmail(
            @Validated
            @Email(message = "邮箱格式不正确")
            @RequestParam("email") String email) {
        User user = Utils.Security.current();
        assert user != null;
        if (user.getEmail() == null || !user.getEmail().equals(email)) throw new IllegalArgumentException("邮箱地址不正确。");
        userService.unbindEmail(user.getId());
        Utils.Security.update(userService.get(user.getId(), null, null));
    }
}