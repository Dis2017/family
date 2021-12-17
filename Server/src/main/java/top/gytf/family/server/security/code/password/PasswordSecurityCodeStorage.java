package top.gytf.family.server.security.code.password;

import org.springframework.stereotype.Component;
import top.gytf.family.server.entity.User;
import top.gytf.family.server.exceptions.code.SecurityCodeStorageRemoveException;
import top.gytf.family.server.exceptions.code.SecurityCodeStorageSaveException;
import top.gytf.family.server.exceptions.code.SecurityCodeStorageTakeException;
import top.gytf.family.server.security.code.AbstractSecurityCodeHandler;
import top.gytf.family.server.security.code.SecurityCodeStorage;
import top.gytf.family.server.services.IUserService;
import top.gytf.family.server.utils.SecurityUtil;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 密码验证码存储器<br>
 * CreateDate:  2021/12/5 19:03 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class PasswordSecurityCodeStorage implements SecurityCodeStorage<Object, Object, PasswordSecurityCode> {
    private final static String TAG = PasswordSecurityCodeStorage.class.getName();

    private final IUserService userService;

    public PasswordSecurityCodeStorage(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 将对象转化为验证码
     *
     * @param obj 验证码的Object对象
     * @return 验证码
     */
    @Override
    public PasswordSecurityCode convert(Object obj) {
        if (obj instanceof PasswordSecurityCode) {
            return (PasswordSecurityCode) obj;
        }
        return null;
    }

    /**
     * 取出验证码
     * 在{@link AbstractSecurityCodeHandler#generate}、{@link AbstractSecurityCodeHandler#verify}处调用<br>
     * @param repos 仓库
     * @param desc  验证码描述
     * @return 验证码
     * @throws SecurityCodeStorageTakeException 验证码取出错误
     */
    @Override
    public PasswordSecurityCode take(Object repos, Object desc) throws SecurityCodeStorageTakeException {
        User user = SecurityUtil.current();
        return new PasswordSecurityCode(Integer.MAX_VALUE, userService.getPassword(user.getId()));
    }

    /**
     * 存储验证码<br>
     * 在{@link AbstractSecurityCodeHandler#generate}中生成后存储<br>
     * 将多次对同一仓库存储code，保证新存储的顶用旧code
     *
     * @param repos 仓库
     * @param code  验证码
     * @throws SecurityCodeStorageSaveException 验证码存储错误
     */
    @Override
    public void save(Object repos, PasswordSecurityCode code) throws SecurityCodeStorageSaveException {

    }

    /**
     * 移除验证码<br>
     * 在{@link AbstractSecurityCodeHandler#verify}中验证完成后使用
     *
     * @param repos 仓库
     * @param desc  验证码描述
     * @throws SecurityCodeStorageRemoveException 验证取出储错误
     */
    @Override
    public void remove(Object repos, Object desc) throws SecurityCodeStorageRemoveException {

    }
}
