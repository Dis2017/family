package top.gytf.family.server.security.code;

import java.time.LocalDateTime;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   SecurityCode
 * Description: 验证码
 * CreateDate:  2021/11/26 22:31
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface SecurityCode<D> {
    /**
     * 是否过期
     * @return 是否过期
     */
    default boolean isExpired() {
        return LocalDateTime.now().isAfter(getExpiredTime());
    }

    /**
     * 获取签发日期
     * @return 签发日期
     */
    LocalDateTime getIssueDate();

    /**
     * 获取到期时间
     * @return 到期时间
     */
    LocalDateTime getExpiredTime();

    /**
     * 获取验证码
     * @return 验证码
     */
    String getCode();

    /**
     * 获取描述
     * @return 描述
     */
    D getDesc();
}
