package top.gytf.family.server.security.email;

import lombok.Data;
import top.gytf.family.server.security.SecurityCode;

import java.time.LocalDateTime;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   NumberSecurityCode
 * Description: 数字验证码
 * CreateDate:  2021/11/26 22:42
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Data
public class EmailSecurityCode implements SecurityCode<String> {
    private final static String TAG = EmailSecurityCode.class.getName();

    private final LocalDateTime expiredTime;
    private final String code;
    private final String email;

    public EmailSecurityCode(LocalDateTime expiredTime, String code, String email) {
        this.expiredTime = expiredTime;
        this.code = code;
        this.email = email;
    }

    /**
     * 是否过期
     *
     * @return 是否过期
     */
    @Override
    public boolean isExpired() {
        return expiredTime.isBefore(LocalDateTime.now());
    }

    /**
     * 获取验证码
     *
     * @return 验证码
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public String getDesc() {
        return email;
    }
}
