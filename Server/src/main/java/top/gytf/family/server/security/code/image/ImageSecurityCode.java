package top.gytf.family.server.security.code.image;

import top.gytf.family.server.security.code.AbstractSecurityCode;

import javax.servlet.http.HttpServletResponse;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 图片验证码<br>
 * CreateDate:  2021/11/28 17:39 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class ImageSecurityCode extends AbstractSecurityCode<HttpServletResponse> {
    private final static String TAG = ImageSecurityCode.class.getName();

    private final HttpServletResponse response;

    public ImageSecurityCode(long survivalTime, String code, HttpServletResponse response) {
        super(survivalTime, code);
        this.response = response;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    @Override
    public HttpServletResponse getDesc() {
        return response;
    }
}
