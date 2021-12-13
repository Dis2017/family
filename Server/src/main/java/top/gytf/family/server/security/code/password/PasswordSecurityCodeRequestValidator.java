package top.gytf.family.server.security.code.password;

import org.springframework.stereotype.Component;
import top.gytf.family.server.security.code.AbstractSecurityCodeHandler;
import top.gytf.family.server.security.code.SecurityCode;
import top.gytf.family.server.security.code.SecurityCodeRequestValidator;

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
    private static final String SECURITY_CODE_KEY = "password_code";

    private final PasswordSecurityCodeHandler securityCodeHandler;

    public PasswordSecurityCodeRequestValidator(PasswordSecurityCodeHandler securityCodeHandler) {
        this.securityCodeHandler = securityCodeHandler;
    }

    /**
     * 验证器名字<br>
     * 验证错误时调用，用于提示、区别验证器<br>
     * @return 验证器名字
     */
    @Override
    public String name() {
        return "密码验证器";
    }

    /**
     * 验证码处理器<br>
     * 用于调用{@link AbstractSecurityCodeHandler#verify}
     * @see top.gytf.family.server.security.code.SecurityCodeRequestValidator#verifyRequest
     * @return 验证码处理器
     */
    @Override
    public AbstractSecurityCodeHandler<Object, ? extends SecurityCode<Object>, Object> getSecurityCodeHandler() {
        return securityCodeHandler;
    }

    /**
     * 仓库<br>
     * 作为调用{@link AbstractSecurityCodeHandler#verify}时的仓库参数<br>
     * 因为用不到仓库，所以返回null<br>
     * @param request 请求
     * @return 仓库
     * @see SecurityCodeRequestValidator#verifyRequest
     * @see PasswordSecurityCodeStorage
     */
    @Override
    public Object getRepository(HttpServletRequest request) {
        return null;
    }

    /**
     * 描述<br>
     * 作为调用{@link AbstractSecurityCodeHandler#verify}时的描述参数<br>
     * 用不到描述，返回null
     * @param request 请求
     * @return 描述
     * @see SecurityCodeRequestValidator#verifyRequest
     * @see PasswordSecurityCodeStorage
     */
    @Override
    public Object getDesc(HttpServletRequest request) {
        return null;
    }

    /**
     * 验证码<br>
     * 作为调用{@link AbstractSecurityCodeHandler#verify}时的验证码参数
     *
     * @param request 请求
     * @return 验证码
     * @see SecurityCodeRequestValidator#verifyRequest
     */
    @Override
    public String getCode(HttpServletRequest request) {
        String code = null;
        Object obj = request.getAttribute(SECURITY_CODE_KEY);
        if (obj instanceof String) {
            code = (String) obj;
        }
        if (code == null) {
            code = request.getParameter(SECURITY_CODE_KEY);
        }
        return code;
    }
}
