package com.github.hugh.support.tree;

import com.github.hugh.bean.expand.tree.ElementTree;
import com.github.hugh.bean.expand.tree.TreeNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 针对 TreeNodeOpes 核心构建算法的性能压力测试 (非侵入式)。
 * 此测试不会对任何生产代码（如 TreeNode）进行修改。
 */
@DisplayName("树构建算法性能压测")
class TreePerformanceTest {

    /**
     * 生成一个大规模的、具有层级结构的树形数据集。
     *
     * @param totalNodes         目标节点总数
     * @param maxLevels          树的最大深度
     * @param maxChildrenPerNode 每个父节点最多拥有的子节点数量
     * @return 包含 "roots" 和 "children" 列表的 Map
     */
    public static Map<String, List<TreeNode>> createLargeMockData(int totalNodes, int maxLevels, int maxChildrenPerNode) {
        List<TreeNode> rootList = new ArrayList<>();
        List<TreeNode> childList = new ArrayList<>();
        // 使用一个外部的 Map 来追踪节点的层级，而不是修改 TreeNode 类
        Map<TreeNode, Integer> nodeLevels = new HashMap<>();
        Queue<TreeNode> parentQueue = new LinkedList<>();
        Random random = new Random();
        int nodeCounter = 0;
        // 1. 创建初始的根节点
        int rootCount = 5;
        for (int i = 0; i < rootCount && nodeCounter < totalNodes; i++) {
            // 构造函数不传入 level
            TreeNode root = new TreeNode(String.valueOf(nodeCounter++), null, "Root-" + i, null);
            rootList.add(root);
            parentQueue.add(root);
            nodeLevels.put(root, 1); // 在外部 Map 中记录层级
        }
        // 2. 通过广度优先的方式逐层生成子节点
        while (!parentQueue.isEmpty() && nodeCounter < totalNodes) {
            TreeNode parent = parentQueue.poll();
            int parentLevel = nodeLevels.get(parent); // 从外部 Map 中获取父节点层级
            if (parentLevel >= maxLevels) {
                continue;
            }
            int numChildren = random.nextInt(maxChildrenPerNode) + 1;
            for (int i = 0; i < numChildren && nodeCounter < totalNodes; i++) {
                TreeNode child = new TreeNode(
                        String.valueOf(nodeCounter++),
                        parent.getId(),
                        "Node-" + nodeCounter,
                        null // 构造函数不传入 level
                );
                childList.add(child);
                parentQueue.add(child);
                nodeLevels.put(child, parentLevel + 1); // 在外部 Map 中记录新子节点的层级
            }
        }
        return Map.of("roots", rootList, "children", childList);
    }

    @Test
    @DisplayName("构建10万节点并排序的树，应在指定时间内完成")
    @Tag("performance")
    @Timeout(value = 5)
    void buildTree_with100kNodes_shouldCompleteWithinTimeout() {
        // --- 准备 (Arrange) ---
        final int TOTAL_NODES = 100_000;
        final int MAX_LEVELS = 7;
        final int MAX_CHILDREN_PER_NODE = 10;

        System.out.printf("开始生成 %d 条模拟树形数据 (非侵入式)...%n", TOTAL_NODES);
        Map<String, List<TreeNode>> mockData = createLargeMockData(TOTAL_NODES, MAX_LEVELS, MAX_CHILDREN_PER_NODE);
        List<TreeNode> rootList = mockData.get("roots");
        List<TreeNode> childList = mockData.get("children");
        int totalGenerated = rootList.size() + childList.size();
        System.out.printf("数据生成完毕。根节点: %d, 子节点: %d, 总计: %d%n",
                rootList.size(), childList.size(), totalGenerated);
        TreeNodeOpe<TreeNode, ElementTree> treeNodeOpe = new TreeNodeOpes(rootList, childList);
        treeNodeOpe.setSortEnable(true);
        treeNodeOpe.setAscending(true);
        // --- 执行 (Act) & 计时 ---
        System.out.println("开始构建树并计时...");
        long startTime = System.nanoTime();
        List<TreeNode> resultTree = treeNodeOpe.process();
        long endTime = System.nanoTime();
        long durationInMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.printf("✅ 构建 %d 个节点的树耗时: %d ms%n", totalGenerated, durationInMillis);
        // --- 验证 (Assert) ---
        assertNotNull(resultTree, "构建结果不应为 null");
        assertEquals(rootList.size(), resultTree.size(), "返回的根节点数量应与输入一致");
    }
}
