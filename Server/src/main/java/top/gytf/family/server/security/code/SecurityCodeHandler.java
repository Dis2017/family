package top.gytf.family.server.security.code;

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
public class SecurityCodeHandler<D, C extends SecurityCode<D>, R> {

    private final SecurityCodeGenerator<D, C> generator;
    private final SecurityCodeSender<C> sender;
    private final SecurityCodeStorage<R, D, C> storage;

    public SecurityCodeHandler(SecurityCodeGenerator<D, C> generator, SecurityCodeSender<C> sender, SecurityCodeStorage<R, D, C> storage) {
        this.generator = generator;
        this.sender = sender;
        this.storage = storage;
    }

    /**
     * 获取验证码生成器<br>
     * 不能返回null
     * @return 验证码生成器
     */
    public SecurityCodeGenerator<D, C> getGenerator() {
        return generator;
    }

    /**
     * 获取验证码发送器<br>
     * 不能返回null
     * @return 验证码发送器
     */
    public SecurityCodeSender<C> getSender() {
        return sender;
    }

    /**
     * 获取验证码发送器<br>
     * 不能返回null
     * @return 验证码发送器
     */
    public SecurityCodeStorage<R, D, C> getStorage() {
        return storage;
    }

    /**
     * 生成验证码<br>
     * 可能错误内容：<br>
     * <li>{@link top.gytf.family.server.exceptions.SecurityCodeSendException}: 验证码发送错误</li>
     *
     * @param desc 验证码描述
     * @param repos 仓库
     * @return 验证码
     * @exception SecurityCodeException 验证码错误
     */
    public C generate(R repos, D desc) throws SecurityCodeException {
        C code = getStorage().take(repos, desc);
        if (code == null || code.isExpired()) {
            code = getGenerator().generate(desc);
            getStorage().save(repos, code);
            try {
                getSender().send(code);
            } catch (Exception e) {
                getStorage().remove(repos, desc);
                throw new SecurityCodeException(e.getMessage());
            }
        }
        return code;
    }

    /**
     * 验证
     * @param repos 仓库
     * @param stringCode 待验证码
     * @param desc 验证码描述
     * @throws SecurityCodeException 验证码错误
     */
    public void verify(R repos, D desc, String stringCode) throws SecurityCodeException {
        assert stringCode != null;

        C code = getStorage().take(repos, desc);

        if (code == null) {
            throw new NullSecurityCodeException(repos + "的验证码不存在");
        }
        if (code.isExpired()) {
            throw new SecurityCodeExpiredException(code + "验证码过期");
        }
        if (code.getCode() == null || !code.getCode().equals(stringCode)) {
            throw new SecurityCodeNotMatchException(code + "和" + stringCode + "不匹配");
        }

        getStorage().remove(repos, desc);
    }
}
