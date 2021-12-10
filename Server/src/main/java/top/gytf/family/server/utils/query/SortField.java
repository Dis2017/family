package top.gytf.family.server.utils.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 查询时可以作为排序依据的字段<br>
 * CreateDate:  2021/12/10 15:21 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SortField {
}
