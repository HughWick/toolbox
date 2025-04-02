package com.github.hugh.util;

import com.github.hugh.bean.expand.tree.TreeNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * {@code TreeNodeUtils} 是一个用于处理树形结构数据的实用工具类。
 * 它提供了一系列静态方法，用于构建和操作树形节点（{@link TreeNode}）。
 * 该类包含了递归分配子节点、判断节点是否为子节点以及循环处理节点等功能，
 * 并支持在分配子节点时进行可选的排序。
 * <p>
 * 该类的构造方法是私有的，因此不能被实例化，只能通过其提供的静态方法来使用。
 *
 * @since 2.8.5
 */
public class TreeNodeUtils {
    private TreeNodeUtils() {
    }

    /**
     * 递归分配子节点并设置排序（可选）。该方法将遍历所有子节点，并将符合条件的子节点分配给当前节点。
     * 如果设置了排序标志，子节点将按照指定的顺序（升序或降序）进行排序。
     *
     * @param childNodesList    子节点列表，包含所有节点数据
     * @param node              当前节点，该节点将被赋予对应的子节点
     * @param childNodesHashMap 存储节点ID和父节点ID的映射，用于去重和节点关联
     * @param sortEnable        是否启用排序，true表示启用，false表示不排序
     * @param ascending         排序顺序，true表示升序，false表示降序
     */
    public static void assignChildNodes(List<TreeNode> childNodesList, TreeNode node, Map<String, String> childNodesHashMap, boolean sortEnable, boolean ascending) {
        List<TreeNode> childList = new ArrayList<>();
        // 如果启用了排序
        if (sortEnable) {
            // 使用Stream流进行过滤和排序，筛选出当前节点的子节点，并根据升序或降序排序
            childNodesList.stream()
                    .filter(childNode -> isChildNode(node, childNode)) // 判断是否为当前节点的子节点
                    .sorted(ascending ? Comparator.comparing(TreeNode::getId) : Comparator.comparing(TreeNode::getId).reversed()) // 根据id进行升序或降序排序
                    .forEach(childNode -> loop(childNodesList, childNode, childNodesHashMap, childList, sortEnable, ascending)); // 对符合条件的子节点进行递归处理
        } else { // 如果没有启用排序
            // 直接过滤出当前节点的子节点，不进行排序
            childNodesList.stream()
                    .filter(childNode -> isChildNode(node, childNode)) // 判断是否为当前节点的子节点
                    .forEach(childNode -> loop(childNodesList, childNode, childNodesHashMap, childList, sortEnable, ascending)); // 对符合条件的子节点进行递归处理
        }
        // 将处理过的子节点列表设置到当前节点
        node.setChildren(childList);
    }

    /**
     * 判断一个节点是否为当前节点的子节点。
     * <p>
     * 该方法通过比较当前节点的 ID 和子节点的父节点 ID 来判断子节点是否属于当前节点。
     * 如果当前节点的 ID 等于子节点的父节点 ID，则返回 true，表示该子节点是当前节点的子节点；
     * 否则返回 false。
     *
     * @param currentNode 当前节点，作为父节点进行判断。
     * @param childNode   子节点，用于判断是否是当前节点的子节点。
     * @return 如果 childNode 是 currentNode 的子节点，返回 true；否则返回 false。
     */
    private static boolean isChildNode(TreeNode currentNode, TreeNode childNode) {
        return currentNode.getId().equals(childNode.getParentId());
    }

    /**
     * 递归处理每个子节点并将其添加到子节点列表中。该方法确保不会重复处理节点，并且将每个节点的子节点递归地赋予当前节点。
     *
     * @param childNodesList    所有节点的列表，用于获取每个节点的子节点。
     * @param childNode         当前正在处理的子节点。该节点将被递归赋予子节点。
     * @param childNodesHashMap 用于存储节点 ID 和父节点 ID 的映射，避免重复处理。
     * @param childList         当前根节点的子节点列表，处理完的子节点将被添加到该列表中。
     * @param sortEnable        是否启用排序，true 表示启用，false 表示不排序。
     * @param ascending         排序顺序，true 表示升序，false 表示降序。当启用排序时，决定子节点的排序顺序。
     */
    private static void loop(List<TreeNode> childNodesList, TreeNode childNode, Map<String, String> childNodesHashMap, List<TreeNode> childList, boolean sortEnable, boolean ascending) {
        if (childNodesHashMap.containsKey(childNode.getId())) { // 排除重复的
            return;
        }
        childNodesHashMap.put(childNode.getId(), childNode.getParentId()); // 添加处理子节点信息
        assignChildNodes(childNodesList, childNode, childNodesHashMap, sortEnable, ascending); // 递归设置该子节点的子节点列表
        childList.add(childNode); // 添加该子节点到对应的根节点列表
    }
}
