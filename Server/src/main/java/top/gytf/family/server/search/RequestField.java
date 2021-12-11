package top.gytf.family.server.search;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 可以被查询请求拉取的字段<br>
 * CreateDate:  2021/12/10 15:18 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequestField {
}
