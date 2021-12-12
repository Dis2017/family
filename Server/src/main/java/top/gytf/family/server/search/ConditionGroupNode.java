package top.gytf.family.server.search;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 非子节点<br>
 * CreateDate:  2021/12/12 12:27 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class ConditionGroupNode<T> implements ConditionNode<T> {
    private final static String TAG = ConditionGroupNode.class.getName();

    /**
     * 当前节点是不是and连接
     */
    @Setter
    private boolean and;

    /**
     * 子节点
     */
    private final List<ConditionNode> children;

    public ConditionGroupNode() {
        this(true);
    }

    public ConditionGroupNode(boolean and) {
        super();
        this.and = and;
        children = new ArrayList<>();
    }

    public void addChild(ConditionNode child) {
        children.add(child);
    }

    /**
     * 解析出表达式
     *
     * @return 表达式
     */
    @Override
    public Consumer<? extends QueryWrapper<T>> parse() {
        return (wrapper) -> {
            children.forEach((node) -> {
                if (and) {
                    wrapper.and(node.parse());
                } else {
                    wrapper.or(node.parse());
                }
            });
        };
    }
}
