package top.gytf.family.server.security.code.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 构造器
     *
     * @param storage   存储器（不可为null）
     * @param passwordEncoder 密码编码器
     */
    public PasswordSecurityCodeHandler(PasswordSecurityCodeStorage storage, PasswordEncoder passwordEncoder) {
        super(null, null, storage);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 是否匹配
     *
     * @param code       验证码
     * @param stringCode 被校验的字符串验证码
     * @return 是否匹配
     */
    @Override
    public boolean isMatch(PasswordSecurityCode code, String stringCode) {
        return passwordEncoder.matches(stringCode, code.getCode());
    }
}
