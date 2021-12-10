package top.gytf.family.server.security.auth;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 数据源<br>
 * CreateDate:  2021/12/11 1:36 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
    private final static String TAG = FilterInvocationSecurityMetadataSourceImpl.class.getName();

    /**
     * Accesses the {@code ConfigAttribute}s that apply to a given secure object.
     *
     * @param object the object being secured
     * @return the attributes that apply to the passed in secured object. Should return an
     * empty collection if there are no applicable attributes.
     * @throws IllegalArgumentException if the passed object is not of a type supported by
     *                                  the <code>SecurityMetadataSource</code> implementation
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        return null;
    }

    /**
     * If available, returns all of the {@code ConfigAttribute}s defined by the
     * implementing class.
     * <p>
     * This is used by the AbstractSecurityInterceptor to perform startup time
     * validation of each {@code ConfigAttribute} configured against it.
     *
     * @return the {@code ConfigAttribute}s or {@code null} if unsupported
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * Indicates whether the {@code SecurityMetadataSource} implementation is able to
     * provide {@code ConfigAttribute}s for the indicated secure object type.
     *
     * @param clazz the class that is being queried
     * @return true if the implementation can process the indicated class
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
