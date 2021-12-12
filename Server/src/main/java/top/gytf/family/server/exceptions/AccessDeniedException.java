package top.gytf.family.server.exceptions;

import top.gytf.family.server.response.StateCode;
import top.gytf.family.server.response.StatusCarrier;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 权限不足<br>
 * CreateDate:  2021/12/12 22:26 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@StatusCarrier(code = StateCode.SECURITY_NO_PERMISSION)
public class AccessDeniedException extends org.springframework.security.access.AccessDeniedException {
    private final static String TAG = AccessDeniedException.class.getName();

    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     */
    public AccessDeniedException() {
        super("权限不足");
    }
}
