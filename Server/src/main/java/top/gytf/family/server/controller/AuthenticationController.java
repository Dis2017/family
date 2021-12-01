package top.gytf.family.server.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.exceptions.SecurityCodeException;
import top.gytf.family.server.security.code.email.EmailSecurityCode;
import top.gytf.family.server.security.code.email.EmailSecurityCodeHandler;
import top.gytf.family.server.security.code.image.ImageSecurityCode;
import top.gytf.family.server.security.code.image.ImageSecurityCodeHandler;

import javax.annotation.security.PermitAll;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
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
public class AuthenticationController {
    private final static String TAG = AuthenticationController.class.getName();

    private final EmailSecurityCodeHandler emailSecurityCodeHandler;
    private final ImageSecurityCodeHandler imageSecurityCodeHandler;

    public AuthenticationController(EmailSecurityCodeHandler emailSecurityCodeHandler, ImageSecurityCodeHandler imageSecurityCodeHandler) {
        this.emailSecurityCodeHandler = emailSecurityCodeHandler;
        this.imageSecurityCodeHandler = imageSecurityCodeHandler;
    }

    /**
     * 生成邮箱验证码
     * @param session 会话
     * @param email 邮箱地址
     * @throws SecurityCodeException 验证码错误
     */
    @GetMapping( PathConstant.Auth.PATH_SECURITY_CODE_EMAIL)
    @PermitAll
    public String generateEmailSecurityCode(HttpSession session, @RequestParam("email") String email)
            throws SecurityCodeException {
        EmailSecurityCode code = emailSecurityCodeHandler.getStorage().take(session, email);
        if (code != null && code.getIssueDate().plusSeconds(60).isBefore(LocalDateTime.now())) {
            emailSecurityCodeHandler.getStorage().remove(session, email);
            code = null;
        }

        if (code == null) code = emailSecurityCodeHandler.generate(session, email);

        return code.getCode();
    }

    /**
     * 生成图片验证码
     * @param session 会话
     * @param response 响应
     * @throws SecurityCodeException 验证码错误
     */
    @GetMapping(PathConstant.Auth.PATH_SECURITY_CODE_IMAGE)
    @PermitAll
    public void generateImageSecurityCode(HttpSession session, ServletResponse response)
            throws SecurityCodeException  {
        imageSecurityCodeHandler.getStorage().remove(session, response);
        ImageSecurityCode code = imageSecurityCodeHandler.generate(session, response);
    }

    @GetMapping(PathConstant.Auth.PATH_CURRENT)
    @PermitAll
    public Object getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
