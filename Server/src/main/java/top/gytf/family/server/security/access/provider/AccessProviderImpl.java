package top.gytf.family.server.security.access.provider;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
 * Description: 服务权限提供器实例<br>
 * CreateDate:  2021/12/12 21:54 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class AccessProviderImpl implements AccessProvider {
    private final static String TAG = AccessProviderImpl.class.getName();

    private final UrlsMapper urlsMapper;
    private final RolesMapper rolesMapper;
    private final UrlRoleMapper urlRoleMapper;

    private final Map<String, Collection<GrantedAuthority>> CACHE = new ConcurrentHashMap<>();

    public AccessProviderImpl(UrlsMapper urlsMapper, RolesMapper rolesMapper, UrlRoleMapper urlRoleMapper) {
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
     * 加载对应uri和method组合所需权限列表
     *
     * @param uri    uri
     * @param method 方法
     * @return 权限列表
     */
    @Override
    public synchronized Collection<GrantedAuthority> loadAuthorities(String uri, String method) {
        String key = method + ':' + uri;
        Collection<GrantedAuthority> result = CACHE.get(key);

        if (result == null) {
            //查询url id
            Url url = urlsMapper.selectOne(new LambdaQueryWrapper<Url>()
                    .select(Url::getId)
                    .eq(Url::getUrl, uri)
                    .eq(Url::getMethod, method));
            if (url == null) {
                throw new IllegalArgumentException(uri + ':' + method + "无对应记录。");
            }
            Long id = url.getId();

            //查询角色id
            final List<Long> roleIdList = urlRoleMapper.selectList(new LambdaQueryWrapper<UrlRole>()
                            .select(UrlRole::getRoleId)
                            .eq(UrlRole::getUrlId, id))
                    .stream()
                    .map(UrlRole::getRoleId)
                    .collect(Collectors.toList());

            //映射到权限
            result = rolesMapper.selectBatchIds(roleIdList).stream()
                    .map(Role::getRole)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            //缓存
            CACHE.put(key, result);
        }


        return result;
    }
}
