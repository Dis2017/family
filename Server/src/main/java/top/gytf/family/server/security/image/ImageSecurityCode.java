package top.gytf.family.server.security.image;

import lombok.Data;
import top.gytf.family.server.security.SecurityCode;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 图片验证码<br>
 * CreateDate:  2021/11/28 17:39 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Data
public class ImageSecurityCode implements SecurityCode<ServletResponse> {
    private final static String TAG = ImageSecurityCode.class.getName();

    private final LocalDateTime expiredTime;
    private final String code;
    private final ServletResponse response;

    public ImageSecurityCode(LocalDateTime expiredTime, String code, ServletResponse response) {
        this.expiredTime = expiredTime;
        this.code = code;
        this.response = response;
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
    public ServletResponse getDesc() {
        return response;
    }
}
