package top.gytf.family.server.aop.search;

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
    /**
     * 和数据库交互的Mapper DAO
     * @return mapper的类型
     */
    Class<? extends BaseMapper> mapper();

    /**
     * 目标对象的类型
     * @return 类型
     */
    Class<? extends BaseEntity> entityClass();
}
