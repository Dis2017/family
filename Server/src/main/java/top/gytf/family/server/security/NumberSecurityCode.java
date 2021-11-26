package top.gytf.family.server.security;

import lombok.Data;

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
public class NumberSecurityCode implements SecurityCode {
    private final static String TAG = NumberSecurityCode.class.getName();

    private final LocalDateTime expiredTime;
    private final String code;

    public NumberSecurityCode(String code, int survivalTime) {
        this.expiredTime = LocalDateTime.now().plusSeconds(survivalTime);
        this.code = code;
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
}
