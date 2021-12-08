package top.gytf.family.server.services;

import top.gytf.family.server.entity.User;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户服务接口<br>
 * CreateDate:  2021/11/29 23:07 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface IUserService {
    /**
     * 用户是否存在<br>
     * 先查询是否存在id的用户，再查询手机号，最后查询邮箱<br>
     * 如果某项为null则跳过<br>
     * @param id 用户的id
     * @param phone 用户的手机号
     * @param email 用户的邮箱
     * @return 是否存在
     */
    boolean exist(Long id, String phone, String email);

    /**
     * 获取用户<br>
     * 先查询是否存在id的用户，再查询手机号，最后查询邮箱<br>
     * 如果某项为null则跳过<br>
     * @param id 用户的id
     * @param phone 用户的手机号
     * @param email 用户的邮箱
     * @return 用户
     */
    User get(Long id, String phone, String email);

    /**
     * 更新用户
     * @param id 更新的用户id
     * @param user 新的用户内容
     */
    void update(Long id, User user);

    /**
     * 添加用户
     * @param user 用户
     */
    void add(User user);

    /**
     * 绑定邮箱
     * @param id 用户id
     * @param email 待绑邮箱地址
     */
    void bindEmail(Long id, String email);

    /**
     * 解除邮箱绑定
     * @param id 用户id
     */
    void unbindEmail(Long id);

    /**
     * 绑定手机
     * @param id 用户id
     * @param phone 待绑手机号
     */
    void bindPhone(Long id, String phone);

    /**
     * 解除手机绑定
     * @param id 用户id
     */
    void unbindPhone(Long id);

    /**
     * 修改密码
     * @param id 用户id
     * @param password 新密码
     */
    void modifyPassword(Long id, String password);

    /**
     * 获取密码
     * @param id 用户编号
     * @return 密码
     */
    String getPassword(Long id);
}
