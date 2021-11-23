package top.gytf.family.server.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
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
@Data
public class BaseEntity implements Serializable {
    private final static String TAG = BaseEntity.class.getName();

    public final static String CREATE_TIME_NAME = "create_time";
    public final static String CREATE_TIME_FIELD_NAME = "createTime";
    public final static String MODIFY_TIME_NAME = "modify_time";
    public final static String MODIFY_TIME_FIELD_NAME = "modifyTime";
    public final static String DELETE_FLAG_NAME = "delete_flag";
    public final static String DELETE_FLAG_FIELD_NAME = "deleteFlag";

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

    /**
     * 删除标记
     */
    @TableField(
            value = DELETE_FLAG_NAME,
            fill = FieldFill.INSERT
    )
    @TableLogic
    private Boolean deleteFlag;
}
