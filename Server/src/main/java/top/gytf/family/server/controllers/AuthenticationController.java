package top.gytf.family.server.controllers;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.gytf.family.server.aop.response.IgnoreResultAdvice;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.exceptions.IllegalArgumentException;
import top.gytf.family.server.exceptions.NotLoginException;
import top.gytf.family.server.exceptions.code.SecurityCodeException;
import top.gytf.family.server.security.code.SecurityCodeVerifyStrategy;
import top.gytf.family.server.security.code.email.EmailSecurityCode;
import top.gytf.family.server.security.code.email.EmailSecurityCodeHandler;
import top.gytf.family.server.security.code.image.ImageSecurityCodeHandler;
import top.gytf.family.server.security.code.image.ImageSecurityCodeRequestValidator;
import top.gytf.family.server.utils.SecurityUtil;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   AuthenticationController
 * Description: 授权控制器
 * CreateDate:  2021/11/26 23:28
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@RestController
@RequestMapping(PathConstant.Auth.AUTH_PREFIX)
@Validated
public class AuthenticationController {
    public static final Integer EMAIL_RESEND_TIME = 60;

    private final EmailSecurityCodeHandler emailSecurityCodeHandler;
    private final ImageSecurityCodeHandler imageSecurityCodeHandler;
    private final SecurityContextLogoutHandler securityContextLogoutHandler;

    public AuthenticationController(EmailSecurityCodeHandler emailSecurityCodeHandler, ImageSecurityCodeHandler imageSecurityCodeHandler) {
        this.emailSecurityCodeHandler = emailSecurityCodeHandler;
        this.imageSecurityCodeHandler = imageSecurityCodeHandler;
        this.securityContextLogoutHandler = new SecurityContextLogoutHandler();
    }

    /**
     * 生成邮箱验证码<br>
     * 不设置email参数则向已登录账户的邮箱发送<br>
     * 需要图片验证码<br>
     * 由{@link top.gytf.family.server.security.code.email.EmailSecurityCodeSender}发送验证码
     * @param session 会话
     * @param email 邮箱地址
     * @throws SecurityCodeException 验证码错误
     */
    @GetMapping( PathConstant.Auth.PATH_SECURITY_CODE_EMAIL)
    @SecurityCodeVerifyStrategy(ImageSecurityCodeRequestValidator.class)
    public void generateEmailSecurityCode(HttpSession session,
                                          @Email(message = "邮箱地址格式不正确")
                                          @RequestParam(value = "email", required = false)
                                          String email)
            throws SecurityCodeException {
        // 确定邮箱地址
        if (email == null) {
            try {
                User user = SecurityUtil.current();
                email = user.getEmail();
            } catch (NotLoginException e) {
                throw new IllegalArgumentException("请设置邮箱地址");
            }
        }

        // 尝试取出验证码
        EmailSecurityCode code = emailSecurityCodeHandler.getStorage().take(session, email);
        // 没有存在验证码 或者 达到刷新时间
        if (code != null && code.getIssueDate().plusSeconds(EMAIL_RESEND_TIME).isBefore(LocalDateTime.now())) {
            // 移除
            emailSecurityCodeHandler.getStorage().remove(session, email);
            code = null;
        }

        // 重新生成
        if (code == null) {
            emailSecurityCodeHandler.generate(session, email);
        }
    }

    /**
     * 生成图片验证码<br>
     * 由{@link top.gytf.family.server.security.code.image.ImageSecurityCodeSender}发送验证码
     * @param session 会话
     * @param response 响应
     * @throws SecurityCodeException 验证码错误
     */
    @GetMapping(PathConstant.Auth.PATH_SECURITY_CODE_IMAGE)
    @IgnoreResultAdvice
    public void generateImageSecurityCode(HttpSession session, ServletResponse response)
            throws SecurityCodeException  {
        // 直接移除验证码
        imageSecurityCodeHandler.getStorage().remove(session, response);
        // 重新生成
        imageSecurityCodeHandler.generate(session, response);
    }

    /**
     * 获取当前登录用户
     * @return 当前登录用户
     */
    @GetMapping(PathConstant.Auth.PATH_CURRENT)
    public Object getCurrentUser() {
        return SecurityUtil.current();
    }

    /**
     * 登出
     * @param request 请求
     */
    @PostMapping(PathConstant.Auth.PATH_LOGOUT)
    public void logout(HttpServletRequest request) {
        securityContextLogoutHandler.logout(request, null, null);
    }
}
