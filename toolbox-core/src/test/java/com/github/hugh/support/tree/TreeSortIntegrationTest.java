package com.github.hugh.support.tree;

import com.github.hugh.bean.expand.tree.ElementTree;
import com.github.hugh.bean.expand.tree.TreeNode;
import com.github.hugh.support.TreeMockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TreeSortIntegrationTest {
    private List<TreeNode> rootList;
    private List<TreeNode> childList;

    /**
     * 在每个测试方法运行前，都获取一套全新的模拟数据。
     * 这保证了测试之间的完全隔离，杜绝状态污染。
     */
    @BeforeEach
    void setUp() {
        Map<String, List<TreeNode>> mockData = TreeMockData.createMockData();
        // 注意：我们创建了新的 ArrayList，以防原始数据在测试中被意外修改
        this.rootList = new ArrayList<>(mockData.get("roots"));
        this.childList = new ArrayList<>(mockData.get("children"));
    }

    @Test
    @DisplayName("当排序开启时，树的所有层级都应按ID升序排列")
    void process_whenSortIsEnabled_shouldReturnFullySortedTree() {
        // --- 准备 (Arrange) ---
        TreeNodeOpe<TreeNode, ElementTree> treeNodeOpe = new TreeNodeOpes(rootList, childList);
        // 默认设置为 sortEnable=true, ascending=true

        // --- 执行 (Act) ---
        List<TreeNode> sortedTree = treeNodeOpe.process();

        // --- 验证 (Assert) ---
        // 1. 验证根节点顺序
        List<String> rootIds = sortedTree.stream().map(TreeNode::getId).collect(Collectors.toList());
        assertEquals(Arrays.asList("100", "200"), rootIds, "根节点应按ID升序排列");

        // 2. 验证 "公司总部" (100) 的子节点顺序
        TreeNode companyHq = findNodeById(sortedTree, "100");
        assertNotNull(companyHq);
        List<String> hqChildrenIds = companyHq.getChildren().stream().map(TreeNode::getId).collect(Collectors.toList());
        assertEquals(Arrays.asList("1001", "1002", "1003"), hqChildrenIds, "公司总部的子部门应按ID升序排列");

        // 3. 验证 "研发部" (1002) 的子节点顺序（验证深层排序）
        TreeNode engineeringDept = findNodeById(companyHq.getChildren(), "1002");
        assertNotNull(engineeringDept);
        List<String> engChildrenIds = engineeringDept.getChildren().stream().map(TreeNode::getId).collect(Collectors.toList());
        assertEquals(Arrays.asList("100201", "100202", "100203"), engChildrenIds, "研发部的子团队应按ID升序排列");
    }

    @Test
    @DisplayName("当排序关闭时，树的所有层级应保持原始的添加顺序")
    void process_whenSortIsDisabled_shouldPreserveOriginalOrder() {
        // --- 准备 (Arrange) ---
        TreeNodeOpe<TreeNode, ElementTree> treeNodeOpe = new TreeNodeOpes(rootList, childList);
        treeNodeOpe.setSortEnable(false); // 明确关闭排序

        // --- 执行 (Act) ---
        List<TreeNode> unsortedTree = treeNodeOpe.process();

        // --- 验证 (Assert) ---
        // 1. 验证根节点顺序 (应与 createMockData 中的添加顺序一致)
        List<String> rootIds = unsortedTree.stream().map(TreeNode::getId).collect(Collectors.toList());
        assertEquals(Arrays.asList("200", "100"), rootIds, "根节点应保持原始添加顺序");

        // 2. 验证 "公司总部" (100) 的子节点顺序 (应与 createMockData 中的添加顺序一致)
        TreeNode companyHq = findNodeById(unsortedTree, "100");
        assertNotNull(companyHq);
        List<String> hqChildrenIds = companyHq.getChildren().stream().map(TreeNode::getId).collect(Collectors.toList());
        assertEquals(Arrays.asList("1002", "1001", "1003"), hqChildrenIds, "公司总部的子部门应保持原始添加顺序");

        // 3. 验证 "研发部" (1002) 的子节点顺序 (应与 createMockData 中的添加顺序一致)
        TreeNode engineeringDept = findNodeById(companyHq.getChildren(), "1002");
        assertNotNull(engineeringDept);
        List<String> engChildrenIds = engineeringDept.getChildren().stream().map(TreeNode::getId).collect(Collectors.toList());
        assertEquals(Arrays.asList("100203", "100201", "100202"), engChildrenIds, "研发部的子团队应保持原始添加顺序");
    }

    /** 辅助方法，用于在节点列表中根据ID查找节点 */
    private TreeNode findNodeById(List<TreeNode> nodes, String id) {
        if (nodes == null || id == null) return null;
        return nodes.stream().filter(node -> id.equals(node.getId())).findFirst().orElse(null);
    }
}
