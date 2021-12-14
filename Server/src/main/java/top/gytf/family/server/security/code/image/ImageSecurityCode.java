package top.gytf.family.server.security.code.image;

import top.gytf.family.server.security.code.AbstractSecurityCode;

import javax.servlet.ServletResponse;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 图片验证码<br>
 * CreateDate:  2021/11/28 17:39 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class ImageSecurityCode extends AbstractSecurityCode<ServletResponse> {
    private final static String TAG = ImageSecurityCode.class.getName();

    private final ServletResponse response;

    public ImageSecurityCode(long survivalTime, String code, ServletResponse response) {
        super(survivalTime, code);
        this.response = response;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public ServletResponse getDesc() {
        return response;
    }
}
