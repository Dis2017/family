package top.gytf.family.server.security.code;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import top.gytf.family.server.exceptions.SecurityCodeException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码校验器<br>
 * CreateDate:  2021/12/5 15:49 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface SecurityCodeRequestValidator<D, R> {
    /**
     * 验证器名字
     * @return 验证器名字
     */
    String name();

    /**
     * 验证码处理器
     * @return 验证码处理器
     */
    SecurityCodeHandler<D, ? extends SecurityCode<D>, R> getSecurityCodeHandler();

    /**
     * 仓库
     * @param request 请求
     * @return 仓库
     */
    R getRepository(HttpServletRequest request);

    /**
     * 获取验证码描述
     * @param request 请求
     * @return 描述
     */
    D getDesc(HttpServletRequest request);

    /**
     * 验证码
     * @param request 请求
     * @return 验证码
     */
    String getCode(HttpServletRequest request);

    /**
     * 校验
     * @param request 请求
     */
    default void verifyRequest(HttpServletRequest request) {
        getSecurityCodeHandler().verify(getRepository(request), getDesc(request), getCode(request));
    }
}
