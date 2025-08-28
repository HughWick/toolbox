package com.github.hugh.bean.expand.tree;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 属性节点结构实体类
 *
 * @author hugh
 * @since 2.5.11
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TreeNode extends BaseTreeNode<TreeNode> {

    private String customLabel;// 自定义标签（element、）
    private String customValue;// 自定义的值

    public TreeNode(String id, String parentId, String value, List<TreeNode> children) {
        super(); // 调用父类的无参构造器
        this.setId(id);
        this.setParentId(parentId);
        this.setValue(value);
        this.setChildren(children);
    }
}
