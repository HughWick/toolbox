package com.github.hugh.support.tree;

import com.github.hugh.bean.expand.tree.ElementTree;
import com.github.hugh.bean.expand.tree.TreeNode;
import com.github.hugh.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树形结构操作类
 *
 * @author hugh
 * @since 2.6.3
 */
public class TreeNodeOpes implements TreeNodeOpe<TreeNode, ElementTree> {

    private final List<TreeNode> rootNodesList;//根节点列表

    private final List<TreeNode> childNodesList;//子节点列表
    /**
     * 表示是否进行升序排序
     */
    private boolean ascending = true;

    /**
     * 是否开启排序
     */
    private boolean sortEnable = true;

    /**
     * 表示是否设置了父级ID的标志。
     */
    private boolean isSetParentId = false;
    /**
     * 表示是否包含空子节点的标志。
     */
    private boolean includeEmptyChildren = true;

    @Override
    public void setMappingType(int mappingType) {

    }

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
     *                       示例：TreeObject rootNode = new TreeObject("1",null,"给排水");
     *                       </p>
     * @param childNodesList 子节点列表，包含所有的子节点对象
     *                       子节点对象是 TreeNode 类型的对象，表示树结构中的子节点
     *                       每个子节点对象需要提供唯一标识、父节点标识和节点名称
     *                       <p>
     *                       示例：TreeObject childNode1 = new TreeObject("2","1","供配电");
     *                       </p>
     */
    public TreeNodeOpes(List<TreeNode> rootNodesList, List<TreeNode> childNodesList) {
        this.rootNodesList = rootNodesList;
        this.childNodesList = childNodesList;
    }

    /**
     * 处理树节点列表，将子节点分配给对应的父节点
     *
     * @return 处理后的根节点列表
     */
    @Override
    public List<TreeNode> process() {
        List<TreeNode> allNodes = new ArrayList<>(rootNodesList.size() + childNodesList.size());
        allNodes.addAll(rootNodesList);
        allNodes.addAll(childNodesList);
        return TreeNodeUtils.buildTree(rootNodesList, allNodes, sortEnable, ascending, includeEmptyChildren);
    }

    /**
     * 将处理过的 TreeNode 转换为 ElementTree 的列表。
     *
     * @return ElementTree 的列表
     */
    @Override
    public List<ElementTree> processElement() {
        final List<TreeNode> treeNodes = process();
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
    private ElementTree fromTreeNode(TreeNode treeNode) {
        ElementTree elementTree = new ElementTree();
        if (treeNode == null) {
            return elementTree;
        }
        elementTree.setId(treeNode.getId());
        elementTree.setLabel(treeNode.getValue());
        if (isSetParentId) {
            elementTree.setParentId(treeNode.getParentId());
        }
        List<TreeNode> childNodes = treeNode.getChildren();
        if (ListUtils.isNotEmpty(childNodes)) {
            List<ElementTree> children = childNodes.stream()
                    .map(this::fromTreeNode)
                    .collect(Collectors.toList());
            elementTree.setChildren(children);
        }
        return elementTree;
    }
}
