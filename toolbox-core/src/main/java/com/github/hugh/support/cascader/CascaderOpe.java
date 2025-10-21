package com.github.hugh.support.cascader;

import com.github.hugh.bean.expand.cascader.ElementCascader;
import com.github.hugh.bean.expand.tree.TreeNode;
import com.github.hugh.support.tree.TreeNodeOpe;
import com.github.hugh.support.tree.TreeNodeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * element cascader 组件
 *
 * @since 2.8.5
 */
public class CascaderOpe implements TreeNodeOpe<TreeNode, ElementCascader> {

    // 定义常量来控制选择的映射规则
    public static final int DEFAULT_MAPPING = 0;
    public static final int CUSTOM_MAPPING = 1;

    private int mappingType = DEFAULT_MAPPING;  // 默认选择普通映射规则

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
     * 表示是否包含空子节点的标志。
     */
    private boolean includeEmptyChildren = true;
    /**
     * 表示是否设置了父级ID的标志。
     */
//    private boolean isSetParentId = false;
    @Override
    // 设置映射类型
    public void setMappingType(int mappingType) {
        this.mappingType = mappingType;
    }

    /**
     * 设置是否设置父节点的 ID。
     *
     * @param setParentId 如果为 true，则在转换过程中设置 elementTree 的 parentId 属性；如果为 false，则不设置 parentId 属性。
     */
    @Override
    public void setParentId(boolean setParentId) {
//        this.isSetParentId = setParentId;
    }

    /**
     * 设置排序顺序是否升序。
     *
     * @param ascending 如果为 true，则按升序排序；如果为 false，则按降序排序。
     */
    @Override
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * 设置是否启用排序功能。
     *
     * @param sortEnable true表示启用排序，false表示禁用排序
     * @since 2.6.7
     */
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
    public CascaderOpe(List<TreeNode> rootNodesList, List<TreeNode> childNodesList) {
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
    public List<ElementCascader> processElement() {
        final List<TreeNode> treeNodes = process();
        return treeNodes.stream()
                .map(this::fromTreeNode)
                .collect(Collectors.toList());
    }

    /**
     * 将 TreeNode 转换为 ElementCascader 对象。
     * 通过映射类型来选择使用默认映射或自定义映射。
     *
     * @param treeNode 要转换的 TreeNode 对象
     * @return 转换后的 ElementCascader 对象
     */
    private ElementCascader fromTreeNode(TreeNode treeNode) {
        switch (mappingType) {
            case CUSTOM_MAPPING:
                return fromTreeNode(treeNode, TreeNode::getCustomLabel, TreeNode::getCustomValue);
            case DEFAULT_MAPPING:
            default:
                return fromTreeNode(treeNode, TreeNode::getValue, TreeNode::getId);
        }
    }

    /**
     * 将树节点转换为 ElementCascader 对象。
     * <p>
     * 该方法会将一个 TreeNode 对象递归转换为 ElementCascader 对象，并使用提供的映射函数
     * 将 TreeNode 的属性映射为 ElementCascader 的 label 和 value。
     * 如果树节点有子节点，则递归处理每个子节点并将其转换为 ElementCascader 对象。
     *
     * @param treeNode    当前处理的树节点，类型为 TreeNode。
     * @param labelMapper 用于将 TreeNode 转换为 label 的映射函数。
     * @param valueMapper 用于将 TreeNode 转换为 value 的映射函数。
     * @return 转换后的 ElementCascader 对象。
     * 如果传入的 treeNode 为 null，则返回一个空的 ElementCascader 对象。
     */
    private ElementCascader fromTreeNode(TreeNode treeNode, Function<TreeNode, String> labelMapper, Function<TreeNode, String> valueMapper) {
        ElementCascader elementCascader = new ElementCascader();
        // 如果传入的 treeNode 为 null，则返回一个空的 ElementCascader 对象
        if (treeNode == null) {
            return elementCascader;
        }
        // 使用提供的 labelMapper 和 valueMapper 将 treeNode 转换为 label 和 value
        elementCascader.setLabel(labelMapper.apply(treeNode));
        elementCascader.setValue(valueMapper.apply(treeNode));
        // 获取当前节点的子节点
        List<TreeNode> childNodes = treeNode.getChildren();
        // 如果当前节点有子节点，则递归转换每个子节点
        if (childNodes != null && !childNodes.isEmpty()) {
            List<ElementCascader> children = childNodes.stream()
                    .map(child -> fromTreeNode(child, labelMapper, valueMapper)) // 递归调用 fromTreeNode 方法
                    .collect(Collectors.toList());
            // 设置子节点
            elementCascader.setChildren(children);
        }
        return elementCascader;
    }
}
