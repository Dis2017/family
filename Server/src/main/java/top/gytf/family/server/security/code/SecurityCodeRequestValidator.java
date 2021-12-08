package top.gytf.family.server.security.code;

import top.gytf.family.server.exceptions.SecurityCodeException;

import javax.servlet.http.HttpServletRequest;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码校验器<br>
 * 用于{@link top.gytf.family.server.security.code.SecurityCodeVerifyFilter}中进行验证码校验<br>
 * 子类通过注册成为bean加入到{@link top.gytf.family.server.security.code.SecurityCodeVerifyFilter}的validators字段
 * 最终在{@link top.gytf.family.server.security.code.SecurityCodeVerifyFilter#doFilterInternal}中对被校验请求进行校验<br>
 * CreateDate:  2021/12/5 15:49 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface SecurityCodeRequestValidator<D, R> {
    /**
     * 验证器名字<br>
     * 验证错误时调用，用于提示、区别验证器<br>
     * @return 验证器名字
     */
    String name();

    /**
     * 验证码处理器<br>
     * 调用其{@link AbstractSecurityCodeHandler#verify}
     * @see top.gytf.family.server.security.code.SecurityCodeRequestValidator#verifyRequest
     * @return 验证码处理器
     */
    AbstractSecurityCodeHandler<D, ? extends SecurityCode<D>, R> getSecurityCodeHandler();

    /**
     * 仓库<br>
     * 作为调用{@link AbstractSecurityCodeHandler#verify}时的仓库参数
     * @see top.gytf.family.server.security.code.SecurityCodeRequestValidator#verifyRequest
     * @param request 请求
     * @return 仓库
     */
    R getRepository(HttpServletRequest request);

    /**
     * 描述<br>
     * 作为调用{@link AbstractSecurityCodeHandler#verify}时的描述参数
     * @see top.gytf.family.server.security.code.SecurityCodeRequestValidator#verifyRequest
     * @param request 请求
     * @return 描述
     */
    D getDesc(HttpServletRequest request);

    /**
     * 验证码<br>
     * 作为调用{@link AbstractSecurityCodeHandler#verify}时的验证码参数
     * @see top.gytf.family.server.security.code.SecurityCodeRequestValidator#verifyRequest
     * @param request 请求
     * @return 验证码
     */
    String getCode(HttpServletRequest request);

    /**
     * 校验请求<br>
     * 调用于{@link top.gytf.family.server.security.code.SecurityCodeVerifyFilter}
     * @param request 请求
     * @throws SecurityCodeException 验证码错误
     */
    default void verifyRequest(HttpServletRequest request) throws SecurityCodeException {
        getSecurityCodeHandler().verify(getRepository(request), getDesc(request), getCode(request));
    }
}
