package top.gytf.family.server.security.code.password;

import org.springframework.stereotype.Component;
import top.gytf.family.server.security.code.SecurityCodeHandler;
import top.gytf.family.server.security.code.SecurityCodeStorage;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 密码验证码处理器<br>
 * CreateDate:  2021/12/5 18:59 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class PasswordSecurityCodeHandler extends SecurityCodeHandler<Object, PasswordSecurityCode, Object> {
    private final static String TAG = PasswordSecurityCodeHandler.class.getName();

    public PasswordSecurityCodeHandler(SecurityCodeStorage<Object, Object, PasswordSecurityCode> storage) {
        super(null, null, storage);
    }
}
