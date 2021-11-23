package top.gytf.family.server.dao;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   BaseDao
 * Description: Dao基类
 * CreateDate:  2021/11/22 15:56
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public abstract class BaseDao {
    private final static String TAG = BaseDao.class.getName();

    /**
     * 创建时间
     */
    @TableField(
            value = "create_time",
            fill = FieldFill.INSERT
    )
    @Getter
    @Setter
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(
            value = "modify_time",
            fill = FieldFill.UPDATE
    )
    @Getter
    @Setter
    private LocalDateTime modifyTime;
}
