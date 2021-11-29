package top.gytf.family.server.security.email;

import top.gytf.family.server.security.AbstractSecurityCode;

/**
 * Project:     IntelliJ IDEA<br>
 * ClassName:   NumberSecurityCode<br>
 * Description: 数字验证码<br>
 * CreateDate:  2021/11/26 22:42<br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class EmailSecurityCode extends AbstractSecurityCode<String> {
    private final static String TAG = EmailSecurityCode.class.getName();

    private final String email;

    public EmailSecurityCode(long survivalTime, String code, String email) {
        super(survivalTime, code);
        this.email = email;
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
