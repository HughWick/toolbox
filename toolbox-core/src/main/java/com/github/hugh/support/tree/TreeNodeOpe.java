package com.github.hugh.support.tree;

import java.util.List;

/**
 * 树形结构操作
 *
 * @author hugh
 * @since 2.6.3
 */
public interface TreeNodeOpe<T , E> {
    /**
     * 设置是否设置父节点ID
     *
     * @param setParentId true表示设置父节点ID，false表示不设置
     */
    void setParentId(boolean setParentId);

    /**
     * 设置排序方式
     *
     * @param ascending true表示升序排序，false表示降序排序
     */
    void setAscending(boolean ascending);

    /**
     * 处理树节点操作，并返回排序结果列表
     *
     * @return 排序后的树节点列表
     */
    List<T> process();

    /**
     * 处理每个元素的操作，并返回排序结果列表
     *
     * @return 排序后的元素列表
     */
    List<E> processElement();
}
