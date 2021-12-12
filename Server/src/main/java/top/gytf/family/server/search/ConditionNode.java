package top.gytf.family.server.search;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.function.Consumer;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 表示是一个节点<br>
 * CreateDate:  2021/12/12 13:23 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public interface ConditionNode<T> {
    /**
     * 解析出表达式
     * @return 表达式
     */
    Consumer<? extends QueryWrapper<T>> parse();
}
