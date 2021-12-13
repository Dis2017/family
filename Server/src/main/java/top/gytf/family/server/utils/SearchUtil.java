package top.gytf.family.server.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import top.gytf.family.server.exceptions.GeneralSearchConditionFormatException;
import top.gytf.family.server.search.*;

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
public class SearchUtil {
    private final static String TAG = SearchUtil.class.getName();

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
    public static final String SIGN_LIKE = " like ";

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
    private synchronized static Map<String, Set<Class<? extends Annotation>>> getClassInfo(Class clazz) {
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
    public static <T> QueryWrapper<T> parse(Class<T> clazz, String requests, String conditions, String sorts) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        setRequests(queryWrapper, clazz, requests);
//        setConditions(queryWrapper, clazz, conditions);
        if (conditions != null) {
            final ConditionNode conditionNode = parseCondition(clazz, new Reader(conditions));
            if (conditionNode != null) {
                queryWrapper.and(conditionNode.parse());
            }
        }
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
     * @param reader 存储了条件表达式的阅读器
     * @return 条件节点（调用{@link ConditionNode#parse()}可以解析出Wrapper用的表达式
     */
    private static <T> ConditionNode<T> parseCondition(Class<T> clazz, Reader reader) {
        ConditionGroupNode<T> node;

        // 取出前缀，确定连接方式
        if (reader.startsWith("AND(")) {
            reader.skips(4);
            node = new ConditionGroupNode();
        } else if (reader.startsWith("OR(")) {
            reader.skips(3);
            node = new ConditionGroupNode(false);
        } else {
            // 是叶子表达式
            ConditionLeafNode<T> conditionLeafNode = parseLeftCondition(reader);
            final String fieldName = conditionLeafNode.getFieldName();
            final Set<Class<? extends Annotation>> classes = getClassInfo(clazz).get(fieldName);
            if (classes == null || !classes.contains(ConditionField.class)) {
                return null;
            }
            return conditionLeafNode;
        }

        // 取出里面所有表达式（包括嵌套）
        while (reader.readable() > 0) {
            ConditionNode children = parseCondition(clazz, reader);
            if (children != null) {
                node.addChild(children);
            }
            char ch = reader.read();
            if (ch == ')') {
                break;
            } else if (ch != ',') {
                throw new RuntimeException("格式错误");
            }
        }

        return node;
    }

    /**
     * 解析叶子上的条件表达式
     * @param reader 存储了条件表达式的阅读器
     * @return 叶子条件节点
     */
    private static <T> ConditionLeafNode<T> parseLeftCondition(Reader reader) {
        StringBuilder name = new StringBuilder();
        StringBuilder value = new StringBuilder();
        String sign = null;
        boolean flag = true;

        // 取出名称
        do {
            name.append(reader.read());
            if (reader.readable() > 0) {
                for (String[] pair : SearchUtil.SING_AND_REGEX_PAIRS) {
                    if (reader.startsWith(pair[0])) {
                        flag = false;
                        sign = pair[0];
                        reader.skips(sign.length());
                        break;
                    }
                }
            }
        } while (reader.readable() > 0 && flag);

        // 读取完成后可以读取内容为空 或者 下一个字符不为" 代表并不是一个正确格式的表达式
        if (reader.readable() <= 0 || reader.read() != '\"') {
            throw new GeneralSearchConditionFormatException();
        }

        // 读取值
        flag = true;
        while (reader.readable() > 0) {
            char ch = reader.read();
            if (ch == '\\') {
                // 是\则跳过到下个字符，并且不处理
                ch = reader.read();
            } else if (ch == '\"') {
                // 读取到了"代表值结束
                flag = false;
                break;
            }
            value.append(ch);
        }

        // 从未读到过" 代表格式错误
        if (flag) {
            throw new GeneralSearchConditionFormatException();
        }

        return new ConditionLeafNode<>(name.toString(), sign, value.toString());
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
