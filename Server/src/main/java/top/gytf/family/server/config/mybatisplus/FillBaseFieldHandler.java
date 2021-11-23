package top.gytf.family.server.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
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
public class FillBaseFieldHandler implements MetaObjectHandler {
    private final static String TAG = FillBaseFieldHandler.class.getName();

    @Override
    public void insertFill(MetaObject metaObject) {
        strictInsertFill(metaObject, BaseEntity.CREATE_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, BaseEntity.MODIFY_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, BaseEntity.CREATE_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now());
    }
}
