package top.gytf.family.server.search;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.gytf.family.server.entity.BaseEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 统一查询<br>
 * CreateDate:  2021/12/11 12:10 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GeneralSearch {
    Class<? extends BaseMapper> mapper();
    Class<? extends BaseEntity> entityClass();
}
