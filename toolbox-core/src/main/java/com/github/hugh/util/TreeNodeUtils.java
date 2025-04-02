package com.github.hugh.util;

import com.github.hugh.bean.expand.tree.TreeNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TreeNodeUtils {
    private TreeNodeUtils() {
    }

//    private static final Comparator<TreeNode> comparingById = Comparator.comparing(TreeNode::getId);

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
                    .filter(childNode -> temp(node, childNode)) // 判断是否为当前节点的子节点
                    .sorted(ascending ? Comparator.comparing(TreeNode::getId) : Comparator.comparing(TreeNode::getId).reversed()) // 根据id进行升序或降序排序
                    .forEach(childNode -> loop(childNodesList, childNode, childNodesHashMap, childList, sortEnable, ascending)); // 对符合条件的子节点进行递归处理
        } else { // 如果没有启用排序
            // 直接过滤出当前节点的子节点，不进行排序
            childNodesList.stream()
                    .filter(childNode -> temp(node, childNode)) // 判断是否为当前节点的子节点
                    .forEach(childNode -> loop(childNodesList, childNode, childNodesHashMap, childList, sortEnable, ascending)); // 对符合条件的子节点进行递归处理
        }
        // 将处理过的子节点列表设置到当前节点
        node.setChildren(childList);
    }

    private static boolean temp(TreeNode currentNode, TreeNode childNode) {
        return childNode.getParentId().equals(currentNode.getId());
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
