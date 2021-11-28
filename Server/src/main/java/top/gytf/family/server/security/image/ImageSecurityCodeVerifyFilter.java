package top.gytf.family.server.security.image;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import top.gytf.family.server.constants.PathConstant;
import top.gytf.family.server.exceptions.SecurityCodeException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 图片验证码认证过滤器<br>
 * CreateDate:  2021/11/28 18:33 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class ImageSecurityCodeVerifyFilter extends OncePerRequestFilter {
    private final static String TAG = ImageSecurityCodeVerifyFilter.class.getName();

    public static final String KEY_IMAGE_SECURITY_CODE = "image_code";

    private final AntPathMatcher matcher;
    private final ImageSecurityCodeHandler imageSecurityCodeHandler;

    public ImageSecurityCodeVerifyFilter(ImageSecurityCodeHandler imageSecurityCodeHandler) {
        matcher = new AntPathMatcher();
        this.imageSecurityCodeHandler = imageSecurityCodeHandler;
    }


    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request request
     * @param response response
     * @param filterChain filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean action = false;
        String uri = request.getRequestURI();
        for (String path : PathConstant.PATHS_IMAGE_VERIFY) {
            if (matcher.match(path, uri)) {
                action = true;
                break;
            }
        }

        if (action) {
            String code = getCode(request);
            try {
                imageSecurityCodeHandler.verify(request.getSession(), null, code);
            } catch (SecurityCodeException e) {
                throw new IOException(e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getCode(HttpServletRequest request) {
        return request.getParameter(KEY_IMAGE_SECURITY_CODE);
    }
}
