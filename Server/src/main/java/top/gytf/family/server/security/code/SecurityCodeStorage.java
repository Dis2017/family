package top.gytf.family.server.security.code;

import top.gytf.family.server.exceptions.SecurityCodeStorageRemoveException;
import top.gytf.family.server.exceptions.SecurityCodeStorageSaveException;
import top.gytf.family.server.exceptions.SecurityCodeStorageTakeException;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 验证码存储器<br>
 * CreateDate:  2021/11/27 16:45 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface SecurityCodeStorage<R, D, C extends SecurityCode<D>> {
    /**
     * 将对象转化为验证码<br>
     * obj不是目标验证码类型返回null<br>
     * @param obj 验证码的Object对象
     * @return 验证码
     */
    C convert(Object obj);

    /**
     * 取出验证码<br>
     * 在{@link SecurityCodeHandler#generate}、{@link SecurityCodeHandler#verify}处调用<br>
     * @param repos 仓库
     * @param desc 验证码描述
     * @return 验证码
     * @exception SecurityCodeStorageTakeException 验证码取出错误
     */
    C take(R repos, D desc) throws SecurityCodeStorageTakeException;

    /**
     * 存储验证码<br>
     * 在{@link SecurityCodeHandler#generate}中生成后存储<br>
     * 将多次对同一仓库存储code，保证新存储的顶用旧code
     * @param repos 仓库
     * @param code 验证码
     * @exception SecurityCodeStorageSaveException 验证码存储错误
     */
    void save(R repos, C code) throws SecurityCodeStorageSaveException;

    /**
     * 移除验证码<br>
     * 在{@link SecurityCodeHandler#verify}中验证完成后使用
     * @param repos 仓库
     * @param desc 验证码描述
     * @exception SecurityCodeStorageRemoveException 验证取出储错误
     */
    void remove(R repos, D desc) throws SecurityCodeStorageRemoveException;
}
