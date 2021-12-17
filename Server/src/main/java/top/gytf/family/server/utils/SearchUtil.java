package top.gytf.family.server.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import top.gytf.family.server.exceptions.IllegalArgumentException;
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
    public static final String SIGN_LIKE = "like";

    /**
     * 等于符号
     */
    public static final String SIGN_EQ = "=";

    /**
     * 不等于符号
     */
    public static final String SIGN_NE = "!=";

    /**
     * 大于符号
     */
    public static final String SIGN_GT = ">";

    /**
     * 大于等于符号
     */
    public static final String SIGN_GE = ">=";

    /**
     * 小于符号
     */
    public static final String SIGN_LT = "<";

    /**
     * 小于等于符号
     */
    public static final String SIGN_LE = "<=";

    public static final String AND = "AND";
    public static final String OR = "OR";
    public static final char LEFT_BRACKET = '(';
    public static final char RIGHT_BRACKET = ')';
    public static final char LINK_CHAR = ',';
    public static final char REFERENCE = '\"';

    public static final String[] SIGNS = {
            SIGN_EQ,
            SIGN_NE,
            SIGN_GE,
            SIGN_LE,
            SIGN_GT,
            SIGN_LT,
            SIGN_LIKE
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
        if (conditions != null && !conditions.isBlank()) {
            final ConditionNode conditionNode = parseCondition(clazz, new Reader(conditions));
            queryWrapper.and(conditionNode.parse());
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

        //跳过空格
        reader.skipBlank();

        // 取出前缀，确定连接方式
        if (reader.startsWith(AND)) {
            reader.skips(3);
            node = new ConditionGroupNode();
        } else if (reader.startsWith(OR)) {
            reader.skips(2);
            node = new ConditionGroupNode(false);
        } else {
            // 是叶子表达式
            ConditionLeafNode<T> conditionLeafNode = parseLeftCondition(reader);
            final String fieldName = conditionLeafNode.getFieldName();
            final Set<Class<? extends Annotation>> classes = getClassInfo(clazz).get(fieldName);
            // 使用了不安全的字段作为条件
            if (classes == null || !classes.contains(ConditionField.class)) {
                throw new IllegalArgumentException("使用不受允许的字段：" + fieldName);
            }
            return conditionLeafNode;
        }

        reader.skipBlank();
        if (reader.readable() <= 0 || reader.read() != LEFT_BRACKET) {
            throw new IllegalArgumentException("逻辑连接符后没有添加括号。");
        }

        // 取出里面所有表达式（包括嵌套）
        while (reader.readable() > 0) {
            ConditionNode children = parseCondition(clazz, reader);
            //跳过空格
            reader.skipBlank();

            //添加子条件
            node.addChild(children);

            char ch = reader.read();
            if (ch == RIGHT_BRACKET) {
                break;
            } else if (ch != LINK_CHAR) {
                throw new IllegalArgumentException("未添加连接符");
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
        String name;
        String value;
        int signIdx;

        // 取出名称
        name = readReference(reader);

        // 跳过空格
        reader.skipBlank();
        // 匹配运算符
        signIdx = 0;
        for (String sign : SIGNS) {
            if (reader.startsWith(sign)) {
                break;
            }
            signIdx++;
        }
        if (signIdx == SIGNS.length) {
            throw new IllegalArgumentException("运算符不匹配");
        }
        reader.skips(SIGNS[signIdx].length());

        // 跳过空格
        reader.skipBlank();
        // 读取值
        value = readReference(reader);

        return new ConditionLeafNode<>(name, SIGNS[signIdx], value);
    }

    /**
     * 读取一个引用（"括起来的）
     * @param reader 阅读器
     * @return 内容
     */
    private static String readReference(Reader reader) {
        StringBuilder builder = new StringBuilder();

        // 读取完成后可以读取内容为空 或者 下一个字符不为" 代表并不是一个正确格式的表达式
        if (reader.readable() <= 0 || reader.read() != REFERENCE) {
            throw new IllegalArgumentException("字段名称或值未使用" + REFERENCE + "引用");
        }

        // 读取值
        boolean flag = true;
        while (reader.readable() > 0) {
            char ch = reader.read();
            if (ch == '\\') {
                // 是\则跳过到下个字符，并且不处理
                ch = reader.read();
            } else if (ch == REFERENCE) {
                // 读取到了"代表值结束
                flag = false;
                break;
            }
            builder.append(ch);
        }

        // 从未读到过"代表格式错误
        if (flag) {
            throw new IllegalArgumentException("字段名称或值未使用" + REFERENCE + "结尾");
        }

        return builder.toString();
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
            // 未指定请求内容 返回全部允许请求的内容
            final String[] strings = classInfo.entrySet().stream()
                    .filter((entry) -> entry.getValue().contains(RequestField.class))
                    .map(Map.Entry::getKey)
                    .toArray(String[]::new);

            queryWrapper.select(strings);
        } else {
            // 过滤请求内容
            StringBuilder e = new StringBuilder();

            final String[] requestArray = Arrays.stream(requests.split(","))
                    .filter((request) -> {
                        final Set<Class<? extends Annotation>> classes = classInfo.get(request);

                        if (classes == null || !classes.contains(RequestField.class)) {
                            e.append("请求不允许的字段：").append(request).append(' ');
                            return false;
                        }

                        return true;
                    })
                    .toArray(String[]::new);

            if (!e.isEmpty()) {
                throw new IllegalArgumentException(e.toString());
            }

            queryWrapper.select(requestArray);
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
        StringBuilder e = new StringBuilder();

        // 过滤排序
        String[] sortArray = Arrays.stream(sorts.split(","))
                .filter((sort) -> {
                    if (sort.isBlank()) {
                        return false;
                    }
                    char sign = sort.charAt(0);
                    String name = sort;
                    Set<Class<? extends Annotation>> classes;
                    if (sign == CHAR_SORT_SIGN_DESC || sign == CHAR_SORT_SIGN_ASC) {
                        name = name.substring(1);
                    }
                    classes = classInfo.get(name);

                    if (classes == null || !classes.contains(SortField.class)) {
                        e.append("使用不允许的排序字段：").append(name).append(' ');
                        return false;
                    }

                    return true;
                })
                .toArray(String[]::new);

        if (!e.isEmpty()) {
            throw new IllegalArgumentException(e.toString());
        }

        Arrays.stream(sortArray)
                .forEach((sort) -> {
                    char sign = sort.charAt(0);
                    boolean desc = sign == CHAR_SORT_SIGN_DESC;
                    String field = (sign == CHAR_SORT_SIGN_DESC || sign == CHAR_SORT_SIGN_ASC) ?
                            sort.substring(1) : sort;
                    queryWrapper.orderBy(true, desc, field);
                });
    }
}
