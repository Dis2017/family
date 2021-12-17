package top.gytf.family.server.security.code.email;

import top.gytf.family.server.constants.SessionConstant;
import top.gytf.family.server.security.code.AbstractSessionSecurityCodeStorage;
import top.gytf.family.server.security.code.SecurityCode;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 邮箱验证码存储器<br>
 * CreateDate:  2021/12/17 21:45 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class EmailSecurityCodeStorage extends AbstractSessionSecurityCodeStorage<String, EmailSecurityCode> {

    private final String keyPrefix;

    public EmailSecurityCodeStorage() {
        keyPrefix = SessionConstant.KEY_EMAIL_SECURITY_CODE.name();
    }

    /**
     * 是否只存储一例</br>
     * 为<code>true</code>只用{@link AbstractSessionSecurityCodeStorage#getKeyPrefix()}作为key值<br>
     * 为<code>false</code>则用{@link AbstractSessionSecurityCodeStorage#getKeyPrefix()} +
     * {@link SecurityCode#getDesc()}作为key值
     *
     * @return 是否为单例
     */
    @Override
    public boolean isSingle() {
        return false;
    }

    /**
     * 获取Key前缀
     *
     * @return 前缀
     */
    @Override
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * 将对象转化为验证码<br>
     * obj不是目标验证码类型返回null<br>
     *
     * @param obj 验证码的Object对象
     * @return 验证码
     */
    @Override
    public EmailSecurityCode convert(Object obj) {
        if (!(obj instanceof EmailSecurityCode)) {
            return null;
        }
        return (EmailSecurityCode) obj;
    }
}
