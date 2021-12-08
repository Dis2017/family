package top.gytf.family.server.security.code;

import top.gytf.family.server.exceptions.SecurityCodeStorageRemoveException;
import top.gytf.family.server.exceptions.SecurityCodeStorageSaveException;
import top.gytf.family.server.exceptions.SecurityCodeStorageTakeException;

import javax.servlet.http.HttpSession;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 会话验证码存储器<br>
 * CreateDate:  2021/11/27 22:35 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public abstract class SessionSecurityCodeStorage<D, C extends SecurityCode<D>> implements SecurityCodeStorage<HttpSession, D, C> {
    private final static String TAG = SessionSecurityCodeStorage.class.getName();

    /**
     * 是否只存储一例</br>
     * 为<code>true</code>只用{@link SessionSecurityCodeStorage#getKeyPrefix()}作为key值<br>
     * 为<code>false</code>则用{@link SessionSecurityCodeStorage#getKeyPrefix()} +
     * {@link SecurityCode#getDesc()}作为key值
     * @return 是否为单例
     */
    public abstract boolean isSingle();

    /**
     * 获取Key前缀
     * @return 前缀
     */
    public abstract String getKeyPrefix();

    /**
     * 获取key值
     * @param desc code的描述
     * @return 键值
     */
    private String getKey(D desc) {
        return getKeyPrefix() + (isSingle() ? "" : desc);
    }

    /**
     * 取出验证码<br>
     * 在{@link SecurityCodeHandler#generate}、{@link SecurityCodeHandler#verify}处调用<br>
     * @param repos 仓库
     * @param desc  验证码描述
     * @return 验证码
     * @throws SecurityCodeStorageTakeException 验证码取出错误
     */
    @Override
    public C take(HttpSession repos, D desc) throws SecurityCodeStorageTakeException {
        Object obj = repos.getAttribute(getKey(desc));
        C code = null;
        if (obj != null) code = convert(obj);
        return code;
    }

    /**
     * 存储验证码<br>
     * 在{@link SecurityCodeHandler#generate}中生成后存储<br>
     * 将多次对同一仓库存储code，保证新存储的顶用旧code
     *
     * @param repos 仓库
     * @param code  验证码
     * @throws SecurityCodeStorageSaveException 验证码存储错误
     */
    @Override
    public void save(HttpSession repos, C code) throws SecurityCodeStorageSaveException {
        repos.setAttribute(getKey(code.getDesc()), code);
    }

    /**
     * 移除验证码<br>
     * 在{@link SecurityCodeHandler#verify}中验证完成后使用
     *
     * @param repos 仓库
     * @param desc  验证码描述
     * @throws SecurityCodeStorageRemoveException 验证取出储错误
     */
    @Override
    public void remove(HttpSession repos, D desc) throws SecurityCodeStorageRemoveException {
        repos.removeAttribute(getKey(desc));
    }
}
