package top.gytf.family.server.security.code;

import top.gytf.family.server.exceptions.code.SecurityCodeSendException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码发送器<br>
 * CreateDate:  2021/11/27 16:40 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface SecurityCodeSender<C extends SecurityCode<?>> {
    /**
     * 发送验证码<br>
     * 在{@link AbstractSecurityCodeHandler#generate}调用
     * @param code 验证码
     * @throws SecurityCodeSendException 发送错误
     */
    void send(C code) throws SecurityCodeSendException;
}
