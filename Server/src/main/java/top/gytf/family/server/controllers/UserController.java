package top.gytf.family.server.controllers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.gytf.family.server.aop.response.IgnoreResultAdvice;
import top.gytf.family.server.aop.search.GeneralSearch;
import top.gytf.family.server.aop.user.UpdateCurrentAuthentication;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.mapper.UserMapper;
import top.gytf.family.server.security.code.SecurityCodeVerifyStrategy;
import top.gytf.family.server.security.code.email.EmailSecurityCodeRequestValidator;
import top.gytf.family.server.security.code.password.PasswordSecurityCodeRequestValidator;
import top.gytf.family.server.services.IUserService;
import top.gytf.family.server.utils.ResponseUtil;
import top.gytf.family.server.utils.SecurityUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletResponse;
import javax.validation.constraints.Email;
import java.io.IOException;

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
@AllArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;

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
    public Long register(@Validated(User.GroupRegister.class) User userInfo) {
        userService.add(userInfo);
        return userInfo.getId();
    }

    /**
     * 更新用户信息（非保护数据）
     * @param updateInfo 用户信息
     */
    @PatchMapping(PathConstant.User.PATH_MODIFY)
    @UpdateCurrentAuthentication
    public void modifyUser(@Validated(User.GroupModify.class) User updateInfo) {
        userService.update(SecurityUtil.current().getId(), updateInfo);
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
        Long id = SecurityUtil.current().getId();
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
    @UpdateCurrentAuthentication
    public void bindEmail(@Email(message = "邮箱格式不正确")
                          @RequestParam("email") String email) {
        Long id = SecurityUtil.current().getId();
        userService.bindEmail(id, email);
    }

    /**
     * 解绑邮箱<br>
     * 需要通过邮箱验证
     */
    @DeleteMapping(PathConstant.User.PATH_UNBIND_EMAIL)
    @SecurityCodeVerifyStrategy(EmailSecurityCodeRequestValidator.class)
    @UpdateCurrentAuthentication
    public void unbindEmail() {
        User user = SecurityUtil.current();
        userService.unbindEmail(user.getId());
    }

    /**
     * 下载头像
     * @param response 响应
     */
    @GetMapping(PathConstant.User.PATH_DOWNLOAD_AVATAR)
    @IgnoreResultAdvice
    public void downloadAvatar(ServletResponse response) {
        User user = SecurityUtil.current();
        try {
            ResponseUtil.setToImage(response, userService.getAvatar(user.getId()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("下载失败");
        }
    }

    /**
     * 上传头像
     */
    @PostMapping(PathConstant.User.PATH_DOWNLOAD_AVATAR)
    public void uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        User user = SecurityUtil.current();
        try {
            userService.setAvatar(user.getId(), ImageIO.read(file.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传失败");
        }
    }
}
