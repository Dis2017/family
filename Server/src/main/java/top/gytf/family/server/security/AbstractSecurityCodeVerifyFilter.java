package top.gytf.family.server.security;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import top.gytf.family.server.exceptions.SecurityCodeException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 抽象验证码验证过滤器<br>
 * CreateDate:  2021/11/29 23:24 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public abstract class AbstractSecurityCodeVerifyFilter<D> extends OncePerRequestFilter {
    private final static String TAG = AbstractSecurityCodeVerifyFilter.class.getName();

    private final AntPathMatcher matcher = new AntPathMatcher();

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        String uri = request.getRequestURI();
        String[] targetPaths = getTargetPaths();
        for (String path : targetPaths) {
            if (matcher.match(path, uri)) {
                action = true;
                break;
            }
        }

        if (action) {
            try {
                getSecurityCodeHandler().verify(request.getSession(), getDesc(request), getCode(request));
            } catch (SecurityCodeException e) {
                e.printStackTrace();
                getFailureHandler().onAuthenticationFailure(request, response,e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 失败处理器
     * @return 失败处理器
     */
    protected abstract AuthenticationFailureHandler getFailureHandler();

    /**
     * 验证码处理器
     * @return 验证码处理器
     */
    protected abstract SecurityCodeHandler<D, ? extends SecurityCode<D>, HttpSession> getSecurityCodeHandler();

    /**
     * 获取验证码描述
     * @param request 请求
     * @return 描述
     */
    protected abstract D getDesc(HttpServletRequest request);

    /**
     * 验证码
     * @param request 请求
     * @return 验证码
     */
    protected abstract String getCode(HttpServletRequest request);

    /**
     * 目标路径<br>
     * 拦截这些路径进行验证码验证
     * @return 目标路径
     */
    protected abstract String[] getTargetPaths();
}
