package top.gytf.family.server.security.login;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 任一一个匹配器匹配则匹配成功的匹配器<br>
 * CreateDate:  2021/12/15 23:35 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class AnyRequestMatcher implements RequestMatcher {
    private final static String TAG = AnyRequestMatcher.class.getName();

    private final Set<RequestMatcher> matcherSet = new CopyOnWriteArraySet<>();

    public AnyRequestMatcher(Collection<RequestMatcher> matchers) {
        matcherSet.addAll(matchers);
    }

    /**
     * Decides whether the rule implemented by the strategy matches the supplied request.
     *
     * @param request the request to check for a match
     * @return true if the request matches, false otherwise
     */
    @Override
    public boolean matches(HttpServletRequest request) {
        for (RequestMatcher requestMatcher : matcherSet) {
            if (requestMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }
}
