package top.gytf.family.server.utils.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 查询工具<br>
 * CreateDate:  2021/12/10 15:16 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
@Slf4j
public class QueryUtil {
    private final static String TAG = QueryUtil.class.getName();

    /**
     * 排序的符号：升序
     */
    public static final char CHAR_SORT_SIGN_ASC = '+';
    /**
     * 排序的符号：降序
     */
    public static final char CHAR_SORT_SIGN_DESC = '-';

    /**
     * like符号
     */
    public static final String SIGN_LIKE = "like ";

    /**
     * 正则表达式：匹配like模式
     */
    public static final String REGEX_LIKE = "(" + SIGN_LIKE.toLowerCase() + ").+|(" +  SIGN_LIKE.toUpperCase() + ").+";

    /**
     * 等于符号
     */
    public static final String SIGN_EQ = "=";

    /**
     * 正则表达式：匹配等于
     */
    public static final String REGEX_EQ = SIGN_EQ + ".+";

    /**
     * 不等于符号
     */
    public static final String SIGN_NE = "!=";

    /**
     * 正则表达式：匹配不等于
     */
    public static final String REGEX_NE = SIGN_NE + ".+";

    /**
     * 大于符号
     */
    public static final String SIGN_GT = ">";

    /**
     * 正则表达式：匹配大于
     */
    public static final String REGEX_GT = SIGN_GT + ".+";

    /**
     * 大于等于符号
     */
    public static final String SIGN_GE = ">=";

    /**
     * 正则表达式：匹配大于等于
     */
    public static final String REGEX_GE = SIGN_GE + ".+";

    /**
     * 小于符号
     */
    public static final String SIGN_LT = "<";

    /**
     * 正则表达式：匹配小于
     */
    public static final String REGEX_LT = SIGN_LT + ".+";

    /**
     * 小于等于符号
     */
    public static final String SIGN_LE = "<=";

    /**
     * 正则表达式：匹配小于等于
     */
    public static final String REGEX_LE = SIGN_LE + ".+";

    public static final String[][] SING_AND_REGEX_PAIRS = {
            {SIGN_EQ, REGEX_EQ},
            {SIGN_NE, REGEX_NE},
            {SIGN_GE, REGEX_GE},
            {SIGN_LE, REGEX_LE},
            {SIGN_GT, REGEX_GT},
            {SIGN_LT, REGEX_LT},
            {SIGN_LIKE, REGEX_LIKE}
    };

    private static final Map<Class, Map<String, Set<Class<? extends Annotation>>>> CACHE = new ConcurrentHashMap<>();

    /**
     * 获取类信息<br>
     * 返回对应字段代表的数据库字段名称与字段注解的映射
     * @param clazz 类
     * @return 信息
     */
    private static Map<String, Set<Class<? extends Annotation>>> getClassInfo(Class clazz) {
        Map<String, Set<Class<? extends Annotation>>> info = CACHE.get(clazz);

        if (info == null) {
            final TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
            final List<TableFieldInfo> fieldList = tableInfo.getFieldList();
            Map<String, Set<Class<? extends Annotation>>> finalInfo = new ConcurrentHashMap<>(8);
            fieldList.forEach((filedInfo) -> {
                Set<Class<? extends Annotation>> annotations = Arrays.stream(filedInfo.getField().getAnnotations())
                        .filter((annotation) -> annotation instanceof ConditionField ||
                                annotation instanceof RequestField ||
                                annotation instanceof SortField)
                        .map((Annotation::annotationType))
                        .collect(Collectors.toSet());
                if (!annotations.isEmpty()) {
                    finalInfo.put(filedInfo.getColumn(), annotations);
                }
            });

            //主键
            try {
                Field keyField = clazz.getDeclaredField(tableInfo.getKeyProperty());
                keyField.setAccessible(true);
                Set<Class<? extends Annotation>> annotations = Arrays.stream(keyField.getAnnotations())
                        .filter((annotation) -> annotation instanceof ConditionField ||
                                annotation instanceof RequestField ||
                                annotation instanceof SortField)
                        .map((Annotation::annotationType))
                        .collect(Collectors.toSet());
                if (!annotations.isEmpty()) {
                    finalInfo.put(tableInfo.getKeyColumn(), annotations);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            info = finalInfo;
            CACHE.put(clazz, info);
        }

        return info;
    }

    /**
     * 解析出QueryWrapper
     * @param clazz 对象类型
     * @param generalSearchEntity 统一查询
     * @param <T> 对象泛型
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> parse(Class<T> clazz, GeneralSearchEntity generalSearchEntity) {
        return parse(clazz, generalSearchEntity.getRequests(), generalSearchEntity.getConditions(), generalSearchEntity.getSorts());
    }

    /**
     * 解析出QueryWrapper
     * @param clazz 对象类型
     * @param requests 请求的内容
     * @param conditions 条件
     * @param sorts 排序依据
     */
    public static <T> QueryWrapper<T> parse(Class<T> clazz, String requests, Map<String, String> conditions, String sorts) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        setRequests(queryWrapper, clazz, requests);
        setConditions(queryWrapper, clazz, conditions);
        setSort(queryWrapper, clazz, sorts);
        return queryWrapper;
    }

    /**
     * 设置分页
     * @param page 分页信息
     * @param <T> 泛型
     */
    public static <T> IPage<T> generatePage(String page) {
        if (page != null) {
            try {
                String[] params = page.split("/");
                long current = Long.parseLong(params[0]);
                long size = Long.parseLong(params[1]);
                return new Page<>(current, size);
            } catch (Exception ignored) {
            }
        }
        return new Page<>();
    }

    /**
     * 解析条件
     * @param queryWrapper Wrapper
     * @param clazz 请求对象类型
     * @param conditions 条件
     * @param <T> 对象泛型
     */
    private static <T> void setConditions(QueryWrapper<T> queryWrapper, Class<T> clazz, Map<String, String> conditions) {
        if (conditions == null) {
            return;
        }

        final Map<String, Set<Class<? extends Annotation>>> classInfo = getClassInfo(clazz);

        conditions.entrySet().stream()
                .filter((entry) -> {
                    final Set<Class<? extends Annotation>> classes = classInfo.get(entry.getKey());
                    return classes != null && classes.contains(ConditionField.class);
                })
                .forEach((entry) -> {
                    //去除左侧空格
                    String value = entry.getValue().replaceFirst("^[ ]+", "");
                    //匹配运算符
                    for (String[] pair : SING_AND_REGEX_PAIRS) {
                        if (!value.matches(pair[1])) {
                            continue;
                        }

                        //设置到条件
                        value = value.substring(pair[0].length());
                        switch (pair[0]) {
                            case SIGN_EQ: queryWrapper.eq(entry.getKey(), value); break;
                            case SIGN_NE: queryWrapper.ne(entry.getKey(), value); break;
                            case SIGN_GT: queryWrapper.gt(entry.getKey(), value); break;
                            case SIGN_GE: queryWrapper.ge(entry.getKey(), value); break;
                            case SIGN_LT: queryWrapper.lt(entry.getKey(), value); break;
                            case SIGN_LE: queryWrapper.le(entry.getKey(), value); break;
                            case SIGN_LIKE: queryWrapper.like(entry.getKey(), value); break;
                            default: break;
                        }
                        break;
                    }
                });
    }

    /**
     * 请求的内容
     * @param queryWrapper Wrapper
     * @param clazz 请求对象类型
     * @param requests 请求内容
     * @param <T> 对象泛型
     */
    private static <T> void setRequests(QueryWrapper<T> queryWrapper, Class<T> clazz, String requests) {
        final Map<String, Set<Class<? extends Annotation>>> classInfo = getClassInfo(clazz);

        if (requests == null || requests.isBlank()) {
            final String[] strings = classInfo.entrySet().stream()
                    .filter((entry) -> entry.getValue().contains(RequestField.class))
                    .map(Map.Entry::getKey)
                    .toArray(String[]::new);
            log.debug(Arrays.toString(strings));
            queryWrapper.select(strings);
        } else {
            queryWrapper.select(
                    Arrays.stream(requests.split(","))
                            .filter((request) -> {
                                final Set<Class<? extends Annotation>> classes = classInfo.get(request);
                                return classes != null && classes.contains(RequestField.class);
                            })
                            .toArray(String[]::new)
            );
        }
    }

    /**
     * 设置排序
     * @param queryWrapper Wrapper
     * @param clazz 请求对象类型
     * @param sorts 排序
     * @param <T> 对象泛型
     */
    private static <T> void setSort(QueryWrapper<T> queryWrapper, Class<T> clazz, String sorts) {
        if (sorts == null) {
            return;
        }

        final Map<String, Set<Class<? extends Annotation>>> classInfo = getClassInfo(clazz);
        Arrays.stream(sorts.split(","))
                .filter((sort) -> {
                    if (sort.isBlank()) {
                        return false;
                    }
                    char sign = sort.charAt(0);
                    final Set<Class<? extends Annotation>> classes = classInfo.get(sort);
                    if (sign == CHAR_SORT_SIGN_DESC || sign == CHAR_SORT_SIGN_ASC) {
                        return classes != null && classes.contains(SortField.class);
                    }
                    return classes != null && classes.contains(SortField.class);
                })
                .forEach((sort) -> {
                    char sign = sort.charAt(0);
                    boolean desc = sign == CHAR_SORT_SIGN_DESC;
                    String field = (sign == CHAR_SORT_SIGN_DESC || sign == CHAR_SORT_SIGN_ASC) ?
                            sort.substring(1) : sort;
                    queryWrapper.orderBy(true, desc, field);
                });
    }
}
