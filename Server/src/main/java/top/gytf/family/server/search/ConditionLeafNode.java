package top.gytf.family.server.search;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Getter;
import top.gytf.family.server.utils.SearchUtil;

import java.util.function.Consumer;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 条件叶节点<br>
 * CreateDate:  2021/12/12 12:31 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class ConditionLeafNode<T> implements ConditionNode<T> {
    private final static String TAG = ConditionLeafNode.class.getName();

    /**
     * 字段名称
     */
    @Getter
    private final String fieldName;

    /**
     * 操作方式
     */
    private final String sign;

    /**
     * 字段值
     */
    private final String fieldValue;
    
    public ConditionLeafNode(String fieldName, String sign, String fieldValue) {
        this.fieldName = fieldName;
        this.sign = sign;
        this.fieldValue = fieldValue;
    }

    /**
     * 解析出表达式
     *
     * @return 表达式
     */
    @Override
    public Consumer<? extends QueryWrapper<T>> parse() {
        return (wrapper) -> {
            switch (sign) {
                case SearchUtil.SIGN_EQ: wrapper.eq(fieldName, fieldValue); break;
                case SearchUtil.SIGN_NE: wrapper.ne(fieldName, fieldValue); break;
                case SearchUtil.SIGN_GT: wrapper.gt(fieldName, fieldValue); break;
                case SearchUtil.SIGN_GE: wrapper.ge(fieldName, fieldValue); break;
                case SearchUtil.SIGN_LT: wrapper.lt(fieldName, fieldValue); break;
                case SearchUtil.SIGN_LE: wrapper.le(fieldName, fieldValue); break;
                case SearchUtil.SIGN_LIKE: wrapper.like(fieldName, fieldValue); break;
                default: break;
            }
        };
    }
}
