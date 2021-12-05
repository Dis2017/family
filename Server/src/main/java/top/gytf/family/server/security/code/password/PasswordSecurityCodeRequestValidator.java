package top.gytf.family.server.security.code.password;

import org.springframework.stereotype.Component;
import top.gytf.family.server.security.code.SecurityCode;
import top.gytf.family.server.security.code.SecurityCodeHandler;
import top.gytf.family.server.security.code.SecurityCodeRequestValidator;
import top.gytf.family.server.security.code.SecurityCodeVerifyFailureHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 密码验证码请求验证器<br>
 * CreateDate:  2021/12/5 18:59 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class PasswordSecurityCodeRequestValidator implements SecurityCodeRequestValidator<Object, Object> {
    private final static String TAG = PasswordSecurityCodeRequestValidator.class.getName();

    private final PasswordSecurityCodeHandler securityCodeHandler;

    public PasswordSecurityCodeRequestValidator(PasswordSecurityCodeHandler securityCodeHandler) {
        this.securityCodeHandler = securityCodeHandler;
    }

    /**
     * 验证器名字
     *
     * @return 验证器名字
     */
    @Override
    public String name() {
        return "密码验证器";
    }

    /**
     * 验证码处理器
     *
     * @return 验证码处理器
     */
    @Override
    public SecurityCodeHandler<Object, ? extends SecurityCode<Object>, Object> getSecurityCodeHandler() {
        return securityCodeHandler;
    }

    /**
     * 仓库
     *
     * @param request 请求
     * @return 仓库
     */
    @Override
    public Object getRepository(HttpServletRequest request) {
        return null;
    }

    /**
     * 获取验证码描述
     *
     * @param request 请求
     * @return 描述
     */
    @Override
    public Object getDesc(HttpServletRequest request) {
        return null;
    }

    /**
     * 验证码
     *
     * @param request 请求
     * @return 验证码
     */
    @Override
    public String getCode(HttpServletRequest request) {
        return null;
    }
}
