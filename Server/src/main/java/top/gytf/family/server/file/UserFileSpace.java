package top.gytf.family.server.file;

import lombok.Getter;
import top.gytf.family.server.constants.FileConstant;
import top.gytf.family.server.utils.FileUtil;

import java.io.File;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 用户文件空间<br>
 * CreateDate:  2021/12/13 17:29 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Getter
public class UserFileSpace {
    private final static String TAG = UserFileSpace.class.getName();

    /**
     * 用户空间根目录
     */
    private final File root;

    /**
     * 头像
     */
    private final File avatar;

    public UserFileSpace(Long userId) {
        root = FileUtil.getUserFileSpace(userId);
        avatar = new File(root, FileConstant.AVATAR);
    }
}
