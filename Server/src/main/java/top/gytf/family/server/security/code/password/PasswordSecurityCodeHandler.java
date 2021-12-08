package top.gytf.family.server.security.code.password;

import org.springframework.stereotype.Component;
import top.gytf.family.server.security.code.AbstractSecurityCodeHandler;

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
public class PasswordSecurityCodeHandler extends AbstractSecurityCodeHandler<Object, PasswordSecurityCode, Object> {
    private final static String TAG = PasswordSecurityCodeHandler.class.getName();


    /**
     * 构造器
     *
     * @param storage   存储器（不可为null）
     */
    public PasswordSecurityCodeHandler(PasswordSecurityCodeStorage storage) {
        super(null, null, storage);
    }
}
