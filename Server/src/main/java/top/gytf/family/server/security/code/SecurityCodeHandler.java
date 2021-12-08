package top.gytf.family.server.security.code;

import top.gytf.family.server.exceptions.NullSecurityCodeException;
import top.gytf.family.server.exceptions.SecurityCodeException;
import top.gytf.family.server.exceptions.SecurityCodeExpiredException;
import top.gytf.family.server.exceptions.SecurityCodeNotMatchException;

/**
 * Project:     IntelliJ IDEA<br>
 * ClassName:   SecurityCodeHandler<br>
 * Description: 验证码处理器<br>
 * 统一管理验证码处理，完成验证码的生成（包括验证码产生、发送及存储）和
 * 验证（取出、校验及销毁）环节
 * CreateDate:  2021/11/26 22:36<br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public abstract class SecurityCodeHandler<D, C extends SecurityCode<D>, R> {

    /**
     * 验证码生成器
     */
    private final SecurityCodeGenerator<D, C> generator;
    /**
     * 验证码发送器
     */
    private final SecurityCodeSender<C> sender;
    /**
     * 验证码存储器
     */
    private final SecurityCodeStorage<R, D, C> storage;

    /**
     * 构造器
     * @param generator 生成器（如果为空则应该保证存储器永远可以取出非空数据）
     * @param sender 发送器（如果为空则应该保证存储器永远可以取出非空数据）
     * @param storage 存储器（不可为null）
     */
    public SecurityCodeHandler(SecurityCodeGenerator<D, C> generator, SecurityCodeSender<C> sender, SecurityCodeStorage<R, D, C> storage) {
        this.generator = generator;
        this.sender = sender;
        this.storage = storage;
    }

    /**
     * 获取验证码生成器<br>
     * 不能返回（除非该验证码无需生成，即存储器永远不返回null）
     * @return 验证码生成器
     */
    public SecurityCodeGenerator<D, C> getGenerator() {
        return generator;
    }

    /**
     * 获取验证码发送器<br>
     * 不能返回null（除非该验证码无需生成，即存储器永远不返回null）
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
        //不存在或过期
        if (code == null || code.isExpired()) {
            // 生成新的
            code = getGenerator().generate(desc);
            try {
                //尝试发送
                getSender().send(code);
                //存储
                getStorage().save(repos, code);
            } catch (Exception e) {
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
        // 取出验证码
        C code = getStorage().take(repos, desc);

        if (code == null) {
            throw new NullSecurityCodeException("验证码不存在");
        }
        if (code.isExpired()) {
            throw new SecurityCodeExpiredException("验证码已过期");
        }
        if (code.getCode() == null || !code.getCode().equals(stringCode)) {
            throw new SecurityCodeNotMatchException("验证码不匹配");
        }

        //验证完成删除 防止重复验证
        getStorage().remove(repos, desc);
    }
}
