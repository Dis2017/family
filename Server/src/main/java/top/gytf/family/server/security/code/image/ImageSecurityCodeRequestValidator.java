package top.gytf.family.server.security.code.image;

import org.springframework.stereotype.Component;
import top.gytf.family.server.security.code.SecurityCode;
import top.gytf.family.server.security.code.SecurityCodeHandler;
import top.gytf.family.server.security.code.SecurityCodeRequestValidator;

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

    public ImageSecurityCodeRequestValidator(ImageSecurityCodeHandler imageSecurityCodeHandler) {
        this.imageSecurityCodeHandler = imageSecurityCodeHandler;
    }

    /**
     * 验证器名字<br>
     * 验证错误时调用，用于提示、区别验证器<br>
     * @return 验证器名字
     */
    @Override
    public String name() {
        return "图片验证器";
    }

    /**
     * 验证码处理器<br>
     * 用于调用{@link top.gytf.family.server.security.code.SecurityCodeHandler#verify}
     * @see top.gytf.family.server.security.code.SecurityCodeRequestValidator#verifyRequest
     * @return 验证码处理器
     */
    @Override
    public SecurityCodeHandler<ServletResponse, ? extends SecurityCode<ServletResponse>, HttpSession> getSecurityCodeHandler() {
        return imageSecurityCodeHandler;
    }

    /**
     * 仓库<br>
     * 作为调用{@link SecurityCodeHandler#verify}时的仓库参数
     *
     * @param request 请求
     * @return 仓库
     * @see SecurityCodeRequestValidator#verifyRequest
     */
    @Override
    public HttpSession getRepository(HttpServletRequest request) {
        return request.getSession();
    }

    /**
     * 描述<br>
     * 作为调用{@link SecurityCodeHandler#verify}时的描述参数
     *
     * @param request 请求
     * @return 描述
     * @see SecurityCodeRequestValidator#verifyRequest
     */
    @Override
    public ServletResponse getDesc(HttpServletRequest request) {
        return null;
    }

    /**
     * 验证码<br>
     * 作为调用{@link SecurityCodeHandler#verify}时的验证码参数
     *
     * @param request 请求
     * @return 验证码
     * @see SecurityCodeRequestValidator#verifyRequest
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
