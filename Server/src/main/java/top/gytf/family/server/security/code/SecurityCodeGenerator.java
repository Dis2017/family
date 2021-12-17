package top.gytf.family.server.security.code;

import top.gytf.family.server.exceptions.code.SecurityCodeGenerateException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码生成器<br>
 * CreateDate:  2021/11/27 16:15 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface SecurityCodeGenerator<D, T extends SecurityCode<D>> {
    /**
     * 生成验证码<br>
     * 在{@link AbstractSecurityCodeHandler#generate}中调用
     * @param desc 描述
     * @return 验证码
     * @throws SecurityCodeGenerateException 验证码生成错误
     */
    T generate(D desc) throws SecurityCodeGenerateException;
}
