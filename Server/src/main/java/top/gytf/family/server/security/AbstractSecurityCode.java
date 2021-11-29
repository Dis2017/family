package top.gytf.family.server.security;

import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 简单的验证码<br>
 * CreateDate:  2021/11/29 15:29 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@ToString
public abstract class AbstractSecurityCode<D> implements SecurityCode<D> {
    private final static String TAG = AbstractSecurityCode.class.getName();

    private final LocalDateTime expiredTime;
    private final LocalDateTime issueTime;
    private final String code;

    /**
     * 构造器
     * @param survivalTime 存活时长
     * @param code 验证码
     */
    public AbstractSecurityCode(long survivalTime, String code) {
        issueTime = LocalDateTime.now();
        expiredTime = issueTime.plusSeconds(survivalTime);
        this.code = code;
    }

    /**
     * 获取签发日期
     *
     * @return 签发日期
     */
    @Override
    public LocalDateTime getIssueDate() {
        return issueTime;
    }

    /**
     * 获取到期时间
     *
     * @return 到期时间
     */
    @Override
    public LocalDateTime getExpiredTime() {
        return expiredTime;
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
