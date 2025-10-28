package com.github.hugh.support.tree;

import com.github.hugh.bean.expand.tree.ElementTreeExpand;
import com.github.hugh.bean.expand.tree.TreeNodeExpand;
import com.github.hugh.util.ListUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 树形结构操作类，TreeNodeOpeExpandImpl 类是 TreeNodeOpe 接口的实现类，用于对 TreeNodeExpand 对象和 ElementTreeExpand 对象进行操作。
 *
 * @param <T> 元素类型，表示 TreeNodeExpand 和 ElementTreeExpand 对象包含的元素类型。
 * @author hugh
 * @since 2.6.3
 */
public class TreeNodeOpeExpand<T> implements TreeNodeOpe<TreeNodeExpand<T>, ElementTreeExpand<T>> {

    private final List<TreeNodeExpand<T>> rootNodesList;//根节点列表

    private final List<TreeNodeExpand<T>> childNodesList;//子节点列表
    /**
     * 表示是否进行升序排序
     */
    private boolean ascending = true;
    /**
     * 表示是否设置了父级ID的标志。
     */
    private boolean isSetParentId = false;
    /**
     * 是否开启排序
     */
    private boolean sortEnable = true;
    /**
     * 表示是否包含空子节点的标志。
     */
    private boolean includeEmptyChildren = true;

    @Override
    public void setMappingType(int mappingType) {

    }

    /**
     * 设置是否设置父节点的 ID。
     *
     * @param setParentId 如果为 true，则在转换过程中设置 elementTree 的 parentId 属性；如果为 false，则不设置 parentId 属性。
     */
    @Override
    public void setParentId(boolean setParentId) {
        this.isSetParentId = setParentId;
    }

    @Override
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public void setSortEnable(boolean sortEnable) {
        this.sortEnable = sortEnable;
    }

    @Override
    public void setIncludeEmptyChildren(boolean includeEmptyChildren) {
        this.includeEmptyChildren = includeEmptyChildren;
    }

    /**
     * 创建 TreeNodeOpe 实例，并传入根节点列表和子节点列表。
     *
     * @param rootNodesList  根节点列表，包含所有的根节点对象
     *                       根节点对象是 TreeNode 类型的对象，表示树结构中的根节点
     *                       每个根节点对象需要提供唯一标识、父节点标识和节点名称
     *                       <p>
     *                       示例：TreeNodeExpand rootNode = new TreeNodeExpand("1",null,"给排水");
     *                       </p>
     * @param childNodesList 子节点列表，包含所有的子节点对象
     *                       子节点对象是 TreeNode 类型的对象，表示树结构中的子节点
     *                       每个子节点对象需要提供唯一标识、父节点标识和节点名称
     *                       <p>
     *                       示例：TreeNodeExpand childNode1 = new TreeNodeExpand("2","1","供配电");
     *                       </p>
     */
    public TreeNodeOpeExpand(List<TreeNodeExpand<T>> rootNodesList, List<TreeNodeExpand<T>> childNodesList) {
        this.rootNodesList = rootNodesList;
        this.childNodesList = childNodesList;
    }

    /**
     * 根据节点的ID属性进行排序的比较器。
     * 通过调用 TreeNodeExpand 对象的 getId 方法来获取 ID 属性。
     */
    private final Comparator<TreeNodeExpand<T>> comparingById = Comparator.comparing(TreeNodeExpand::getId);

    /**
     * 处理树节点列表，将子节点分配给对应的父节点
     *
     * @return 处理后的根节点列表
     */
    @Override
    public List<TreeNodeExpand<T>> process() {
        List<TreeNodeExpand<T>> allNodes = new ArrayList<>(rootNodesList.size() + childNodesList.size());
        allNodes.addAll(rootNodesList);
        allNodes.addAll(childNodesList);
        if (allNodes.isEmpty()) {
            return new ArrayList<>();
        }
        // 预处理：将所有子节点按 parentId 分组
        Map<String, List<TreeNodeExpand<T>>> childrenMap = new HashMap<>();
        for (TreeNodeExpand<T> node : allNodes) {
            if (node.getParentId() != null) {
                childrenMap.computeIfAbsent(node.getParentId(), k -> new ArrayList<>()).add(node);
            }
        }
        // 创建一个线程安全的已访问节点集合
        Set<String> visitedNodeIds = Collections.synchronizedSet(new HashSet<>());
        // 根据是否排序，执行不同的逻辑
        if (sortEnable) {
            final Comparator<TreeNodeExpand<T>> nodeComparator = ascending ? comparingById : comparingById.reversed();
            // 【核心修改点】: 在这里，对 childrenMap 中所有的子列表进行一次性排序
            childrenMap.values().forEach(list -> list.sort(nodeComparator));
            // 使用一个不再需要 comparator 的简化版递归函数
            rootNodesList.forEach(rootNode ->
                    TreeNodeUtils.assignChildrenRecursive(rootNode, childrenMap, visitedNodeIds, includeEmptyChildren)
            );
            // 对根节点进行排序并返回
            return rootNodesList.stream().sorted(nodeComparator).toList();
        }
        // 如果不排序，直接进行递归，然后返回结果
        rootNodesList.forEach(rootNode ->
                TreeNodeUtils.assignChildrenRecursive(rootNode, childrenMap, visitedNodeIds, includeEmptyChildren)
        );
        return rootNodesList;
    }

    /**
     * 将处理过的 TreeNode 转换为 ElementTree 的列表。
     *
     * @return ElementTree 的列表
     */
    @Override
    public List<ElementTreeExpand<T>> processElement() {
        final List<TreeNodeExpand<T>> treeNodes = process();
        return treeNodes.stream()
                .map(this::fromTreeNode)
                .collect(Collectors.toList());
    }

    /**
     * 将 TreeNode 转换为 ElementTree 对象。
     * 如果 TreeNode 为 null，返回一个空的 ElementTree 对象。
     *
     * @param treeNode 要转换的 TreeNode 对象
     * @return 转换后的 ElementTree 对象
     */
    private ElementTreeExpand<T> fromTreeNode(TreeNodeExpand<T> treeNode) {
        ElementTreeExpand<T> elementTree = new ElementTreeExpand<>();
        if (treeNode == null) {
            return elementTree;
        }
        elementTree.setId(treeNode.getId());
        elementTree.setLabel(treeNode.getValue());
        elementTree.setExpand(treeNode.getExpand());
        if (isSetParentId) {
            elementTree.setParentId(treeNode.getParentId());
        }
        List<TreeNodeExpand<T>> childNodes = treeNode.getChildren();
        if (ListUtils.isNotEmpty(childNodes)) {
            List<ElementTreeExpand<T>> children = childNodes.stream()
                    .map(this::fromTreeNode)
                    .collect(Collectors.toList());
            elementTree.setChildren(children);
        }
        return elementTree;
    }
}
