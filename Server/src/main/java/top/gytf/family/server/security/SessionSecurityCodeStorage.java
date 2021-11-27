package top.gytf.family.server.security;

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
     * Session键值<br>
     * 电子邮箱验证码
     */
    public static final String KEY_EMAIL_SECURITY_CODE = "KEY_EMAIL_SECURITY_CODE";

    /**
     * 取出验证码
     *
     * @param repos 仓库
     * @param desc  验证码描述
     * @return 验证码
     * @throws SecurityCodeStorageTakeException 验证码取出错误
     */
    @Override
    public C take(HttpSession repos, D desc) throws SecurityCodeStorageTakeException {
        Object obj = repos.getAttribute(KEY_EMAIL_SECURITY_CODE + desc);
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
        repos.setAttribute(KEY_EMAIL_SECURITY_CODE + code.getDesc(), code);
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
        repos.removeAttribute(KEY_EMAIL_SECURITY_CODE + desc);
    }
}
