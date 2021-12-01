package top.gytf.family.server.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.services.IUserService;

import javax.annotation.security.PermitAll;

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
     * @param id 用户编号
     * @param email 邮箱地址
     */
    @PostMapping("/email")
    public void bindEmail(@RequestParam("id") Long id, @RequestParam("email") String email) {
        userService.bindEmail(id, email);
    }
}
