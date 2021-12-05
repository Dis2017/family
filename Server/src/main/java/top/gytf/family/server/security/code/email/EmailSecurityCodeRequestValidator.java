package top.gytf.family.server.security.code.email;

import org.springframework.stereotype.Component;
import top.gytf.family.server.Utils;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.security.code.SecurityCode;
import top.gytf.family.server.security.code.SecurityCodeHandler;
import top.gytf.family.server.security.code.SecurityCodeRequestValidator;
import top.gytf.family.server.security.code.SecurityCodeVerifyFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 邮箱验证码验证过滤器<br>
 * CreateDate:  2021/11/29 23:20 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class EmailSecurityCodeRequestValidator implements SecurityCodeRequestValidator<String, HttpSession> {
    private final static String TAG = EmailSecurityCodeRequestValidator.class.getName();

    public static final String SECURITY_CODE_KEY = "email_code";

    private final EmailSecurityCodeHandler securityCodeHandler;

    public EmailSecurityCodeRequestValidator(EmailSecurityCodeHandler securityCodeHandler) {
        this.securityCodeHandler = securityCodeHandler;
    }

    /**
     * 验证器名字
     *
     * @return 验证器名字
     */
    @Override
    public String name() {
        return "邮箱验证器";
    }

    /**
     * 验证码处理器
     *
     * @return 验证码处理器
     */
    @Override
    public SecurityCodeHandler<String, ? extends SecurityCode<String>, HttpSession> getSecurityCodeHandler() {
        return securityCodeHandler;
    }

    /**
     * 仓库
     *
     * @param request 请求
     * @return 仓库
     */
    @Override
    public HttpSession getRepository(HttpServletRequest request) {
        return request.getSession();
    }

    /**
     * 获取验证码描述
     *
     * @param request 请求
     * @return 描述
     */
    @Override
    public String getDesc(HttpServletRequest request) {
        String desc = null;

        //从当前登录用户中获取邮箱描述
        User user = Utils.Security.current();
        if (user != null) desc = user.getEmail();

        //从请求中获取
        if (desc == null) {
            Object obj = request.getAttribute("email");
            if (obj instanceof String) desc = (String) obj;
            if (desc == null) desc = request.getParameter("email");
        }

        return desc;
    }

    /**
     * 验证码
     *
     * @param request 请求
     * @return 验证码
     */
    @Override
    public String getCode(HttpServletRequest request) {
        String code = null;
        Object obj = request.getAttribute(SECURITY_CODE_KEY);
        if (obj instanceof String) code = (String) obj;
        if (code == null) code = request.getParameter(SECURITY_CODE_KEY);
        return code;
    }
}
