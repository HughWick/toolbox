package com.github.hugh.bean.expand.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
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
@AllArgsConstructor
public class TreeNode {

    private String id;//节点位移标识

    private String parentId;//父节点id

    private String value;//节点属性，按需求可定义多个属性

    private List<TreeNode> children;//该节点的子节点对象
}
