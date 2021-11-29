package top.gytf.family.server.security.email;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.constants.RequestParamConstant;
import top.gytf.family.server.security.AbstractSecurityCodeVerifyFilter;
import top.gytf.family.server.security.LoginHandler;
import top.gytf.family.server.security.SecurityCode;
import top.gytf.family.server.security.SecurityCodeHandler;

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
public class EmailSecurityCodeVerifyFilter extends AbstractSecurityCodeVerifyFilter<String> {
    private final static String TAG = EmailSecurityCodeVerifyFilter.class.getName();

    private final EmailSecurityCodeHandler securityCodeHandler;
    private final LoginHandler failureHandler;

    public EmailSecurityCodeVerifyFilter(EmailSecurityCodeHandler securityCodeHandler, LoginHandler failureHandler) {
        this.securityCodeHandler = securityCodeHandler;
        this.failureHandler = failureHandler;
    }

    /**
     * 失败处理器
     *
     * @return 失败处理器
     */
    @Override
    protected AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }

    /**
     * 验证码处理器
     *
     * @return 验证码处理器
     */
    @Override
    protected SecurityCodeHandler<String, ? extends SecurityCode<String>, HttpSession> getSecurityCodeHandler() {
        return securityCodeHandler;
    }

    /**
     * 获取验证码描述
     *
     * @param request 请求
     * @return 描述
     */
    @Override
    protected String getDesc(HttpServletRequest request) {
        String code = null;
        Object obj = request.getAttribute(RequestParamConstant.KEY_EMAIL);
        if (obj instanceof String) code = (String) obj;
        if (code == null) code = request.getParameter(RequestParamConstant.KEY_EMAIL);
        return code;
    }

    /**
     * 验证码
     *
     * @param request 请求
     * @return 验证码
     */
    @Override
    protected String getCode(HttpServletRequest request) {
        String code = null;
        Object obj = request.getAttribute(RequestParamConstant.KEY_EMAIL_SECURITY_CODE);
        if (obj instanceof String) code = (String) obj;
        if (code == null) code = request.getParameter(RequestParamConstant.KEY_EMAIL_SECURITY_CODE);
        return code;
    }

    /**
     * 目标路径<br>
     * 拦截这些路径进行验证码验证
     *
     * @return 目标路径
     */
    @Override
    protected String[] getTargetPaths() {
        return PathConstant.Auth.PATHS_EMAIL_VERIFY;
    }
}
