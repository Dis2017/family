package top.gytf.family.server.security.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import top.gytf.family.server.entity.Role;
import top.gytf.family.server.entity.Url;
import top.gytf.family.server.entity.UrlRole;
import top.gytf.family.server.events.RefreshAuthorityCacheEvent;
import top.gytf.family.server.mapper.RolesMapper;
import top.gytf.family.server.mapper.UrlRoleMapper;
import top.gytf.family.server.mapper.UrlsMapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
@Slf4j
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
    private final static String TAG = FilterInvocationSecurityMetadataSourceImpl.class.getName();

    private final UrlsMapper urlsMapper;
    private final RolesMapper rolesMapper;
    private final UrlRoleMapper urlRoleMapper;

    private final Map<String, Collection<ConfigAttribute>> CACHE = new ConcurrentHashMap<>();

    public FilterInvocationSecurityMetadataSourceImpl(UrlsMapper urlsMapper, RolesMapper rolesMapper, UrlRoleMapper urlRoleMapper) {
        this.urlsMapper = urlsMapper;
        this.rolesMapper = rolesMapper;
        this.urlRoleMapper = urlRoleMapper;
    }

    /**
     * 当服务器发出刷新缓存事件时刷新缓存
     */
    @EventListener(RefreshAuthorityCacheEvent.class)
    public void refreshCache() {
        CACHE.clear();
    }

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
        final FilterInvocation invocation = (FilterInvocation) object;

        String uri = invocation.getRequest().getRequestURI();
        String method = invocation.getRequest().getMethod();
        String key = method + ':' + uri;
        Collection<ConfigAttribute> result = CACHE.get(key);

        if (result == null) {
            //查询url id
            Url url = urlsMapper.selectOne(new LambdaQueryWrapper<Url>()
                    .select(Url::getId)
                    .eq(Url::getUrl, invocation.getRequest().getRequestURI())
                    .eq(Url::getMethod, invocation.getRequest().getMethod()));
            if (url == null) {
                throw new IllegalArgumentException(invocation.getRequest().getRequestURI() + "无对应记录。");
            }
            Long id = url.getId();

            //查询角色id
            final List<Long> roleIdList = urlRoleMapper.selectList(new LambdaQueryWrapper<UrlRole>()
                            .select(UrlRole::getRoleId)
                            .eq(UrlRole::getUrlId, id))
                    .stream()
                    .map(UrlRole::getRoleId)
                    .collect(Collectors.toList());

            //映射到角色名
            final String[] roles = rolesMapper.selectBatchIds(roleIdList).stream()
                    .map(Role::getRole)
                    .toArray(String[]::new);

            result = SecurityConfig.createList(roles);
            CACHE.put(key, result);
        }


        return result;
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
