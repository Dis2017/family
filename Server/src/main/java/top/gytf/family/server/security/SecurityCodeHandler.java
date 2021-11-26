package top.gytf.family.server.security;

import javax.servlet.http.HttpSession;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   SecurityCodeHandler
 * Description: 验证码处理器
 * CreateDate:  2021/11/26 22:36
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface SecurityCodeHandler<T extends SecurityCode> {

    /**
     * 生成验证码
     * @param session 会话
     * @return 验证码
     */
    T generate(HttpSession session);

    /**
     * 验证
     * @param session 会话
     * @param stringCode 待验证码
     * @throws SecurityCodeException 验证码错误
     */
    void verify(HttpSession session, String stringCode) throws SecurityCodeException;

    /**
     * 销毁验证码
     * @param session 会话
     */
    void destroy(HttpSession session);
}
