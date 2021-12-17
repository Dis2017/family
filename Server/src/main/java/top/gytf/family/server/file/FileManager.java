package top.gytf.family.server.file;

import top.gytf.family.server.entity.User;
import top.gytf.family.server.utils.SecurityUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 文件管理器<br>
 * CreateDate:  2021/12/13 17:18 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class FileManager {
    private final static String TAG = FileManager.class.getName();

    /**
     * 文件管理器的注册表
     */
    private final static Map<Long, FileManager> REGISTRY = new ConcurrentHashMap<>();

    public synchronized static FileManager current() {
        final User current = SecurityUtil.current();
        final Long id = current.getId();
        FileManager manager = REGISTRY.get(id);

        if (manager == null) {
            manager = new FileManager(id);
            REGISTRY.put(id, manager);
        }

        return manager;
    }

    public synchronized static void removeIfExits(Long uid) {
        REGISTRY.remove(uid);
    }

    /**
     * 用户文件空间
     */
    private UserFileSpace userFileSpace;

    /**
     * 用户编号
     */
    private final Long userId;

    public FileManager(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取用户文件空间
     * @return 用户文件空间
     */
    public synchronized UserFileSpace getUserFileSpace() {
        if (userFileSpace == null) {
            userFileSpace = new UserFileSpace(userId);
        }

        return userFileSpace;
    }
}
