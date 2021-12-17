package top.gytf.family.server.security.code;

import lombok.Getter;

import java.util.Collection;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码校验策略描述<br>
 * CreateDate:  2021/12/16 22:16 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Getter
public class SecurityCodeVerifyStrategyInfo {
    private final static String TAG = SecurityCodeVerifyStrategyInfo.class.getName();

    private final boolean only;
    private final Collection<? extends SecurityCodeRequestValidator> validators;

    public SecurityCodeVerifyStrategyInfo(boolean only, Collection<? extends SecurityCodeRequestValidator> validators) {
        this.only = only;
        this.validators = validators;
    }
}
