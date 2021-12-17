package top.gytf.family.server.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.gytf.family.server.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * Project:     IntelliJ IDEA
 * ClassName:   FillBaseFieldHandler
 * Description: 填充基本字段处理器
 * CreateDate:  2021/11/23 15:57
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Component
public class FillBaseFieldHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     * @param metaObject 填充的对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        strictInsertFill(metaObject, BaseEntity.CREATE_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, BaseEntity.MODIFY_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, BaseEntity.DELETE_FLAG_FIELD_NAME, Boolean.class, false);
    }

    /**
     * 更新时自动填充
     * @param metaObject 填充的对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, BaseEntity.MODIFY_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now());
    }
}
