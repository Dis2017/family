package top.gytf.family.server.security.code.email;

import org.springframework.stereotype.Component;
import top.gytf.family.server.constants.SessionConstant;
import top.gytf.family.server.security.code.SecurityCodeHandler;
import top.gytf.family.server.security.code.SecurityCodeSender;
import top.gytf.family.server.security.code.SessionSecurityCodeStorage;

import javax.servlet.http.HttpSession;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 邮箱验证码处理器<br>
 * CreateDate:  2021/11/28 19:01 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class EmailSecurityCodeHandler extends SecurityCodeHandler<String, EmailSecurityCode, HttpSession> {
    private final static String TAG = EmailSecurityCodeHandler.class.getName();

    public EmailSecurityCodeHandler(SecurityCodeSender<EmailSecurityCode> sender) {
        super(
                new EmailSecurityCodeGenerator(),
                sender,
                new SessionSecurityCodeStorage<String, EmailSecurityCode>() {
                    @Override
                    public boolean isSingle() {
                        return false;
                    }

                    @Override
                    public String getKeyPrefix() {
                        return SessionConstant.KEY_EMAIL_SECURITY_CODE;
                    }

                    @Override
                    public EmailSecurityCode convert(Object obj) {
                        if (!(obj instanceof EmailSecurityCode)) return null;
                        return (EmailSecurityCode) obj;
                    }
                }
                );
    }
}