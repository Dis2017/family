package top.gytf.family.server.security.image;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.constants.RequestParamConstant;
import top.gytf.family.server.security.AbstractSecurityCodeVerifyFilter;
import top.gytf.family.server.security.LoginHandler;
import top.gytf.family.server.security.SecurityCode;
import top.gytf.family.server.security.SecurityCodeHandler;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 图片验证码认证过滤器<br>
 * CreateDate:  2021/11/28 18:33 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class ImageSecurityCodeVerifyFilter extends AbstractSecurityCodeVerifyFilter<ServletResponse> {
    private final static String TAG = ImageSecurityCodeVerifyFilter.class.getName();

    private final ImageSecurityCodeHandler imageSecurityCodeHandler;
    private final LoginHandler loginHandler;

    public ImageSecurityCodeVerifyFilter(ImageSecurityCodeHandler imageSecurityCodeHandler, LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
        this.imageSecurityCodeHandler = imageSecurityCodeHandler;
    }

    /**
     * 失败处理器
     *
     * @return 失败处理器
     */
    @Override
    protected AuthenticationFailureHandler getFailureHandler() {
        return loginHandler;
    }

    /**
     * 验证码处理器
     *
     * @return 验证码处理器
     */
    @Override
    protected SecurityCodeHandler<ServletResponse, ? extends SecurityCode<ServletResponse>, HttpSession> getSecurityCodeHandler() {
        return imageSecurityCodeHandler;
    }

    /**
     * 获取验证码描述
     *
     * @param request 请求
     * @return 描述
     */
    @Override
    protected ServletResponse getDesc(HttpServletRequest request) {
        return null;
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
        Object obj = request.getAttribute(RequestParamConstant.KEY_IMAGE_SECURITY_CODE);
        if (obj instanceof String) code = (String) obj;
        if (code == null) code = request.getParameter(RequestParamConstant.KEY_IMAGE_SECURITY_CODE);
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
        return PathConstant.Auth.PATHS_IMAGE_VERIFY;
    }
}
