package com.github.hugh.support.tree;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.github.hugh.bean.dto.RegionDto;
import com.github.hugh.bean.expand.tree.ElementTree;
import com.github.hugh.bean.expand.tree.TreeNode;
import com.github.hugh.util.file.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("全国省市区数据树结构构建测试")
class NationTreeBuildingTest {
    // --- 静态成员，用于存储昂贵的、一次性加载的数据 ---
    // 第一版本省市区街道四级数据路径、没有处理东莞、中山这种特殊没有第四级的数据
    public static final String DATA_FILE_PATH = "/file/json/data.text";
    private static List<RegionDto> regionV1;
    private static List<TreeNode> originalRootList;
    private static List<TreeNode> originalChildList;

    // --- 实例成员，确保每个测试方法都有一个干净的副本 ---
    private List<TreeNode> rootListForTest;
    private List<TreeNode> childListForTest;

    @BeforeAll
    static void loadAndProcessInitialData() {
        System.out.println("--- @BeforeAll: 正在加载并预处理全国数据... ---");

        // 【修改】将文件读取和JSON解析移到这里！这是昂贵的一次性操作。
        StopWatch stopWatch = new StopWatch("One-time data setup");
        stopWatch.start("读取文件");
        String fullPath = NationTreeBuildingTest.class.getResource(DATA_FILE_PATH).getPath(); // 假设文件在资源目录
        stopWatch.stop();

        stopWatch.start("解析JSON");
        String fileData = FileUtils.readContent(fullPath);
        regionV1 = JSONArray.parseArray(fileData, RegionDto.class); // 初始化静态变量
        stopWatch.stop();

        // 【保留】预处理，生成原始的节点列表模板
        originalRootList = new ArrayList<>();
        originalChildList = new ArrayList<>();
        ProcessTreeData.processRegionDto(regionV1, originalRootList, originalChildList); // 现在 regionV1 不是 null 了

        System.out.println("--- @BeforeAll: 数据加载完毕 ---");
        System.out.println(stopWatch.prettyPrint());
    }

    @BeforeEach
    void setUp() {
        // @BeforeEach 的职责简化为：为每个测试提供一份干净的数据副本
        // 这样可以防止一个测试修改了数据，影响到另一个测试
        rootListForTest = new ArrayList<>();
        childListForTest = new ArrayList<>();

        // 【重要】再次调用 processRegionDto 来填充每个测试独有的列表
        // 这样可以确保每个测试都从最原始的状态开始
        ProcessTreeData.processRegionDto(regionV1, rootListForTest, childListForTest);
    }

    @Test
    @DisplayName("默认行为：应为叶子节点包含空子列表")
    void testProcess_Default_ShouldIncludeEmptyChildren() {
        // 1. 准备
        TreeNodeOpe<TreeNode, ElementTree> treeNodeOpe = new TreeNodeOpes(rootListForTest, childListForTest);
        // 2. 执行
        List<TreeNode> resultTree = treeNodeOpe.process();
        // 3. 验证
        assertNotNull(resultTree);
        assertEquals(originalRootList.size(), resultTree.size(), "根节点数量应与原始数据一致");
        // 验证结构 - 找到 "北京市"
        Optional<TreeNode> beijingOpt = findNodeById(resultTree, "110000"); // 北京市ID
        assertTrue(beijingOpt.isPresent(), "树中应包含北京市");
        TreeNode beijing = beijingOpt.get();
        assertFalse(beijing.getChildren().isEmpty(), "北京市应有子节点（区）");

        // 关键验证：找到一个叶子节点，例如 "东华门街道" (ID: 110101001)
        Optional<TreeNode> leafNodeOpt = findNodeById(resultTree, "44010603");
        assertTrue(leafNodeOpt.isPresent(), "应能找到叶子节点'员村街道'");

        TreeNode leafNode = leafNodeOpt.get();
        assertNotNull(leafNode.getChildren(), "默认行为下，叶子节点的 children 属性不应为 null");
        assertTrue(leafNode.getChildren().isEmpty(), "默认行为下，叶子节点的 children 列表应为空");
    }

    @Test
    @DisplayName("指定选项：应为叶子节点排除子列表 (children=null)")
    void testProcess_Option_ShouldExcludeEmptyChildren() {
        // 1. 准备
        TreeNodeOpe<TreeNode, ElementTree> treeNodeOpe = new TreeNodeOpes(rootListForTest, childListForTest);
        treeNodeOpe.setIncludeEmptyChildren(false); // 设置关键选项
        // 2. 执行
        List<TreeNode> resultTree = treeNodeOpe.process();
        // 3. 验证
        assertNotNull(resultTree);
        // 关键验证：再次找到那个叶子节点
        Optional<TreeNode> leafNodeOpt = findNodeById(resultTree, "44010603");
        assertTrue(leafNodeOpt.isPresent());
        TreeNode leafNode = leafNodeOpt.get();
        assertNull(leafNode.getChildren(), "关闭空子列表时，叶子节点的 children 属性应为 null");
    }

    @Test
//    @Tag("performance")
    @DisplayName("性能基准测试")
    void testProcess_Performance() {
        StopWatch stopWatch = new StopWatch("全国数据构建性能测试");
        stopWatch.start("包含空子列表");
        TreeNodeOpe<TreeNode, ElementTree> ope1 = new TreeNodeOpes(rootListForTest, childListForTest);
        ope1.setIncludeEmptyChildren(true);
        List<TreeNode> process = ope1.process();
        stopWatch.stop();
        String string1 = JSON.toJSONString(process);
        assertEquals(3014109, string1.length());
        // 需要重新创建副本，因为上一步可能修改了对象状态
        setUp();
        stopWatch.start("排除空子列表");
        TreeNodeOpe<TreeNode, ElementTree> ope2 = new TreeNodeOpes(rootListForTest, childListForTest);
        ope2.setIncludeEmptyChildren(false);
        List<TreeNode> process2 = ope2.process();
        stopWatch.stop();
        String string2 = JSON.toJSONString(process2);
        assertEquals(2423239, string2.length());
        System.out.println(stopWatch.prettyPrint());
        // 对于性能测试，通常关注的是执行时间是否在某个阈值内
        assertTrue(stopWatch.getTotalTimeMillis() < 2000, "树构建应在2秒内完成");
    }

    /**
     * 辅助方法：在树形结构中递归查找指定ID的节点
     */
    private Optional<TreeNode> findNodeById(List<TreeNode> nodes, String id) {
        if (nodes == null || nodes.isEmpty()) {
            return Optional.empty();
        }
        for (TreeNode node : nodes) {
            if (id.equals(node.getId())) {
                return Optional.of(node);
            }
            Optional<TreeNode> foundInChildren = findNodeById(node.getChildren(), id);
            if (foundInChildren.isPresent()) {
                return foundInChildren;
            }
        }
        return Optional.empty();
    }
}
