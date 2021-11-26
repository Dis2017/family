package top.gytf.family.server.controller;

import org.springframework.web.bind.annotation.*;
import top.gytf.family.server.security.NumberSecurityCode;
import top.gytf.family.server.security.email.EmailSecurityCodeHandler;

import javax.servlet.http.HttpSession;

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
@RequestMapping("/auth")
public class AuthenticationController {
    private final static String TAG = AuthenticationController.class.getName();

    private final EmailSecurityCodeHandler emailSecurityCodeHandler;

    public AuthenticationController(EmailSecurityCodeHandler emailSecurityCodeHandler) {
        this.emailSecurityCodeHandler = emailSecurityCodeHandler;
    }

    @GetMapping("/security_code/email")
    public String generateEmailSecurityCode(HttpSession session, @RequestParam("email") String email) {
        NumberSecurityCode code = emailSecurityCodeHandler.generate(session);
        return code.getCode();
    }
}
