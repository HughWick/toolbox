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
public class TreeNode extends TreeNodeCustom {

    private String id;//节点位移标识

    private String parentId;//父节点id

    private String value;//节点属性，按需求可定义多个属性

    private List<TreeNode> children;//该节点的子节点对象

    public TreeNode(String id, String parentId, String value, List<TreeNode> children) {
        this.id = id;
        this.parentId = parentId;
        this.value = value;
        this.children = children;
    }
}
