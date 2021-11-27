package top.gytf.family.server.security.email;

import org.springframework.stereotype.Component;
import top.gytf.family.server.security.*;

import javax.servlet.http.HttpSession;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   EmailSecurityCodeHandler
 * Description: 邮箱验证码处理器
 * CreateDate:  2021/11/26 22:52
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class EmailSecurityCodeHandler implements SecurityCodeHandler<String, EmailSecurityCode, HttpSession, String> {
    private final EmailSecurityCodeGenerator generator;
    private final EmailSecurityCodeSender sender;
    private final SessionSecurityCodeStorage<String, EmailSecurityCode> storage;

    public EmailSecurityCodeHandler(EmailSecurityCodeGenerator generator, EmailSecurityCodeSender sender) {
        this.generator = generator;
        this.sender = sender;
        this.storage = new SessionSecurityCodeStorage<String, EmailSecurityCode>() {
            @Override
            public EmailSecurityCode convert(Object obj) {
                if (!(obj instanceof EmailSecurityCode)) return null;
                return (EmailSecurityCode) obj;
            }
        };
    }

    /**
     * 获取验证码生成器<br>
     * 不能返回null
     *
     * @return 验证码生成器
     */
    @Override
    public SecurityCodeGenerator<String, EmailSecurityCode> getGenerator() {
        return generator;
    }

    /**
     * 获取验证码发送器<br>
     * 不能返回null
     *
     * @return 验证码发送器
     */
    @Override
    public SecurityCodeSender<String, EmailSecurityCode> getSender() {
        return sender;
    }

    /**
     * 获取验证码发送器<br>
     * 不能返回null
     *
     * @return 验证码发送器
     */
    @Override
    public SecurityCodeStorage<HttpSession, String, EmailSecurityCode> getStorage() {
        return storage;
    }
}


