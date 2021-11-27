package top.gytf.family.server.security;

import top.gytf.family.server.exceptions.NullSecurityCodeException;
import top.gytf.family.server.exceptions.SecurityCodeException;
import top.gytf.family.server.exceptions.SecurityCodeExpiredException;
import top.gytf.family.server.exceptions.SecurityCodeNotMatchException;

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
public interface SecurityCodeHandler<C_D, C extends SecurityCode<C_D>, R, S_D> {

    /**
     * 获取验证码生成器<br>
     * 不能返回null
     * @return 验证码生成器
     */
    SecurityCodeGenerator<C_D, C> getGenerator();

    /**
     * 获取验证码发送器<br>
     * 不能返回null
     * @return 验证码发送器
     */
    SecurityCodeSender<S_D, C> getSender();

    /**
     * 获取验证码发送器<br>
     * 不能返回null
     * @return 验证码发送器
     */
    SecurityCodeStorage<R, C_D, C> getStorage();

    /**
     * 生成验证码<br>
     * 可能错误内容：<br>
     * <li>{@link top.gytf.family.server.exceptions.SecurityCodeSendException}: 验证码发送错误</li>
     *
     * @param sendDesc 发送描述
     * @param codeDesc 验证码描述
     * @param repos 仓库
     * @return 验证码
     * @exception SecurityCodeException 验证码错误
     */
    default C generate(R repos, C_D codeDesc, S_D sendDesc) throws SecurityCodeException {
        C code = getStorage().take(repos, codeDesc);
        if (code == null || code.isExpired()) {
            code = getGenerator().generate(codeDesc);
            getStorage().save(repos, code);
            getSender().send(sendDesc, code);
        }
        return code;
    }

    /**
     * 验证
     * @param repos 仓库
     * @param stringCode 待验证码
     * @param codeDesc 验证码描述
     * @throws SecurityCodeException 验证码错误
     */
    default void verify(R repos, C_D codeDesc, String stringCode) throws SecurityCodeException {
        assert stringCode != null;

        C code = getStorage().take(repos, codeDesc);

        if (code == null) {
            throw new NullSecurityCodeException(repos + "的验证码不存在");
        }
        if (code.isExpired()) {
            throw new SecurityCodeExpiredException(code + "验证码过期");
        }
        if (code.getCode() == null || !code.getCode().equals(stringCode)) {
            throw new SecurityCodeNotMatchException(code + "和" + stringCode + "不匹配");
        }

        getStorage().remove(repos, codeDesc);
    }
}
