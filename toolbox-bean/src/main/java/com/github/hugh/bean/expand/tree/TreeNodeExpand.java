package com.github.hugh.bean.expand.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 属性节点结构额外拓展实体类
 *
 * @author hugh
 * @since 2.6.3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNodeExpand<T>  extends BaseTreeNode<TreeNodeExpand<T>>{

    private T expand;// 拓展字段

    /**
     * <p>
     * {@code TreeNodeExpand} 类的带参构造方法。
     * 用于创建一个包含基本树节点属性和扩展字段的 {@code TreeNodeExpand} 对象实例。
     * </p>
     *
     * @param id       节点的唯一标识符。对应父类 {@code BaseTreeNode} 的 {@code id} 字段。
     * @param parentId 父节点的标识符。对应父类 {@code BaseTreeNode} 的 {@code parentId} 字段。
     * @param value    节点的值或标签。对应父类 {@code BaseTreeNode} 的 {@code value} 字段。
     * @param children 该节点的子节点列表。列表中的元素类型为 {@code TreeNodeExpand<T>}，对应父类 {@code BaseTreeNode} 的 {@code children} 字段。
     * @param expand   该节点的扩展字段，类型为泛型 {@code T}，用于存储额外的、特定于业务的数据。
     */
    public TreeNodeExpand(String id, String parentId, String value, List<TreeNodeExpand<T>> children, T expand) {
        super(); // 调用父类的无参构造器
        this.setId(id);
        this.setParentId(parentId);
        this.setValue(value);
        this.setChildren(children);
        this.expand = expand;
    }
}
