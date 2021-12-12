package top.gytf.family.server.security.access.provider;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 访问权限提供器<br>
 * CreateDate:  2021/12/12 21:52 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface AccessProvider {
    /**
     * 加载对应uri和method组合所需权限列表
     * @param uri uri
     * @param method 方法
     * @return 权限列表
     */
    Collection<GrantedAuthority> loadAuthorities(String uri, String method);
}
