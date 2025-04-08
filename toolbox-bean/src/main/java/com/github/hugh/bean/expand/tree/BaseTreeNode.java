package com.github.hugh.bean.expand.tree;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * {@code BaseTreeNode} 类是一个泛型抽象基类，用于表示树形结构中的节点。
 * 它定义了树节点的基本属性，例如唯一标识符、父节点标识符、节点值以及子节点列表。
 * 通过使用泛型，可以确保子节点列表中的元素类型与当前节点类型一致，从而实现类型安全。
 * </p>
 *
 * @param <T> 泛型类型参数，表示继承自 {@code BaseTreeNode} 的具体节点类型。
 *            例如，如果有一个具体的节点类 {@code TreeNode} 继承自 {@code BaseTreeNode}，
 *            那么在使用时可以写成 {@code BaseTreeNode<TreeNode>}。
 * @since 2.8.7
 */
@Data
@NoArgsConstructor
public class BaseTreeNode<T extends BaseTreeNode<T>> { // 使用泛型约束，确保子节点类型一致
    /**
     * 节点的唯一标识符，用于在树形结构中唯一确定一个节点的位置。
     */
    private String id;//节点位移标识

    /**
     * 父节点的标识符，指向当前节点的直接父节点。对于根节点，该值可能为空或具有特定的约定值。
     */
    private String parentId;//父节点id

    /**
     * 节点所代表的属性值或标签。具体含义可以根据不同的业务需求进行定义。
     */
    private String value;//节点属性，按需求可定义多个属性

    /**
     * 当前节点的子节点列表。列表中的每个元素都是类型 {@code T} 的节点对象，表示当前节点的直接下级节点。
     */
    private List<T> children;//该节点的子节点对象
}