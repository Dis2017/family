package top.gytf.family.server.security;

import top.gytf.family.server.exceptions.SecurityCodeSendException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码发送器<br>
 * CreateDate:  2021/11/27 16:40 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface SecurityCodeSender<D, C extends SecurityCode> {
    /**
     * 发送验证码
     * @param desc 接收方描述
     * @param code 验证码
     * @exception SecurityCodeSendException 发送错误
     */
    void send(D desc, C code) throws SecurityCodeSendException;
}
