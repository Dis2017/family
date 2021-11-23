package top.gytf.family.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   BaseEntity
 * Description: 实体基类
 * CreateDate:  2021/11/22 15:56
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class BaseEntity implements Serializable {
    private final static String TAG = BaseEntity.class.getName();

    public final static String CREATE_TIME_NAME = "create_time";
    public final static String CREATE_TIME_FIELD_NAME = "createTime";
    public final static String MODIFY_TIME_NAME = "modify_time";
    public final static String MODIFY_TIME_FIELD_NAME = "modifyTime";

    /**
     * 创建时间
     */
    @TableField(
            value = CREATE_TIME_NAME,
            fill = FieldFill.INSERT
    )
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(
            value = MODIFY_TIME_NAME,
            fill = FieldFill.INSERT_UPDATE
    )
    private LocalDateTime modifyTime;
}
