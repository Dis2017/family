package top.gytf.family.server.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 通用查询实体<br>
 * CreateDate:  2021/12/11 0:00 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralSearchEntity {
    private final static String TAG = GeneralSearchEntity.class.getName();

    /**
     * 请求的参数<br>
     * 使用','分割，不能有空格
     */
    private String requests;

    /**
     * 条件<br>
     * 可以使用运算符：<br>
     * <li> = </li>
     * <li> != </li>
     * <li> > </li>
     * <li> >= </li>
     * <li> < </li>
     * <li> <= </li>
     * <li> like </li>
     */
    private Map<String, String> conditions = new ConcurrentHashMap<>();

    /**
     * 排序依据<br>
     * 使用','分割，不能有空格<br>
     * 格式：[+-]field_name,...<br>
     * 注意，+可以省略
     */
    private String sorts;

    /**
     * 分页依据<br>
     * 格式为：当前页数/一页多少个<br>
     * 该值不存在或者格式错误采用默认<br>
     */
    private String pages;
}
