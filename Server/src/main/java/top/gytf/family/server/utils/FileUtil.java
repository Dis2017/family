package top.gytf.family.server.utils;

import top.gytf.family.server.constants.FileConstant;
import top.gytf.family.server.exceptions.FolderCreateException;

import java.io.File;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 文件工具<br>
 * CreateDate:  2021/12/13 18:02 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class FileUtil {
    private final static String TAG = FileUtil.class.getName();

    public static File getFolder(String path) {
        return getFolder(new File(path));
    }

    /**
     * 获取文件夹（不存在则建立）
     * @param folder 文件夹
     * @return 文件夹对象
     */
    public static File getFolder(File folder) {
        // 存在同名文件
        if (folder.isFile()) {
            throw new FolderCreateException("存在同名文件：" + folder.getName());
        }
        // 不存在目录 建立
        if (!folder.exists() && !folder.mkdirs()) {
            throw new FolderCreateException("文件夹建立失败。");
        }
        return folder;
    }

    /**
     * 获取应用程序根目录
     * @return 文件
     */
    public synchronized static File getRoot() {
        return getFolder(FileConstant.APPLICATION_ROOT);
    }

    /**
     * 获取用户文件空间
     * @param id 用户id
     * @return 文件
     */
    public synchronized static File getUserFileSpace(Long id) {
        File fileSpace = getFolder(new File(getRoot(), FileConstant.USER_SPACE_ROOT));
        return getFolder(new File(fileSpace, String.valueOf(id)));
    }
}
