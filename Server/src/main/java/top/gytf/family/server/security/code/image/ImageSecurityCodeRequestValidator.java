package top.gytf.family.server.security.code.image;

import org.springframework.stereotype.Component;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.security.code.SecurityCodeRequestValidator;
import top.gytf.family.server.security.code.SecurityCode;
import top.gytf.family.server.security.code.SecurityCodeHandler;
import top.gytf.family.server.security.code.SecurityCodeVerifyFailureHandler;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 图片验证码请求校验器<br>
 * CreateDate:  2021/11/28 18:33 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class ImageSecurityCodeRequestValidator implements SecurityCodeRequestValidator<ServletResponse, HttpSession> {
    private final static String TAG = ImageSecurityCodeRequestValidator.class.getName();

    public static final String SECURITY_CODE_KEY = "image_code";

    private final ImageSecurityCodeHandler imageSecurityCodeHandler;
    private final SecurityCodeVerifyFailureHandler failureHandler;

    public ImageSecurityCodeRequestValidator(ImageSecurityCodeHandler imageSecurityCodeHandler, SecurityCodeVerifyFailureHandler failureHandler) {
        this.imageSecurityCodeHandler = imageSecurityCodeHandler;
        this.failureHandler = failureHandler;
    }

    /**
     * 失败处理器
     *
     * @return 失败处理器
     */
    @Override
    public SecurityCodeVerifyFailureHandler getFailureHandler() {
        return failureHandler;
    }

    /**
     * 验证码处理器
     *
     * @return 验证码处理器
     */
    @Override
    public SecurityCodeHandler<ServletResponse, ? extends SecurityCode<ServletResponse>, HttpSession> getSecurityCodeHandler() {
        return imageSecurityCodeHandler;
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
    public ServletResponse getDesc(HttpServletRequest request) {
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
        String code = null;
        Object obj = request.getAttribute(SECURITY_CODE_KEY);
        if (obj instanceof String) code = (String) obj;
        if (code == null) code = request.getParameter(SECURITY_CODE_KEY);
        return code;
    }
}
