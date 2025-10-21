package com.github.hugh.support.tree;

import com.github.hugh.bean.dto.RegionDto;
import com.github.hugh.bean.expand.tree.BaseTreeNode;
import com.github.hugh.bean.expand.tree.TreeNode;
import com.github.hugh.constant.StrPool;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;


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
@Slf4j
public class TreeNodeUtils {
    private TreeNodeUtils() {
    }

    /**
     * 构建树形结构。
     *
     * @param rootNodes            根节点列表
     * @param allNodes             所有节点列表（包括根节点和子节点）
     * @param sortEnable           是否对子节点进行排序
     * @param ascending            排序方向（true: 升序, false: 降序）
     * @param includeEmptyChildren 如果节点没有子级，是否包含一个空的 children 数组 (true: 包含空数组, false: children属性为null)
     * @return 构建并（可选）排序后的根节点列表
     * @since 3.0.11
     */
    public static List<TreeNode> buildTree(List<TreeNode> rootNodes, List<TreeNode> allNodes, boolean sortEnable, boolean ascending, boolean includeEmptyChildren) {
        if (allNodes == null || allNodes.isEmpty()) {
            return rootNodes;
        }
        // 预处理：将所有子节点按 parentId 分组
        Map<String, List<TreeNode>> childrenMap = allNodes.stream()
                .filter(node -> node.getParentId() != null)
                .collect(Collectors.groupingBy(TreeNode::getParentId));
        Set<String> visitedNodeIds = Collections.synchronizedSet(new HashSet<>());
        // 3【新增】根据排序配置，预先创建 Comparator
        final Comparator<TreeNode> nodeComparator;
        if (sortEnable) {
            // 创建一个临时的、非 final 的比较器
            Comparator<TreeNode> tempComparator = Comparator.comparing(TreeNode::getId);
            // 如果是降序，就反转这个临时比较器
            if (!ascending) {
                tempComparator = tempComparator.reversed();
            }
            // 最后，将结果只赋值一次给 final 变量
            nodeComparator = tempComparator;
        } else {
            nodeComparator = null; // 不排序
        }
        rootNodes.forEach(rootNode ->
                assignChildrenRecursive(rootNode, childrenMap, visitedNodeIds, nodeComparator, includeEmptyChildren)
        );
        if (sortEnable) {
            return rootNodes.stream().sorted(nodeComparator).collect(Collectors.toList());
        }
        return rootNodes;
    }

    /**
     * 一个通用的、递归分配子节点的静态辅助方法。
     *
     * @param parentNode           当前父节点
     * @param childrenMap          按 parentId 分组的所有节点的映射
     * @param visitedNodeIds       用于防止循环引用的已访问ID集合
     * @param comparator           用于子节点排序的比较器。如果为 null，则不排序。
     * @param includeEmptyChildren 是否为叶子节点设置空的 children 列表
     * @param <N>                  节点的类型，必须是 BaseTreeNode 的子类
     * @since 3.0.11
     */
    public static <N extends BaseTreeNode<N>> void assignChildrenRecursive(N parentNode, Map<String, List<N>> childrenMap, Set<String> visitedNodeIds, Comparator<N> comparator,
                                                                           boolean includeEmptyChildren) {
        // 防止循环引用
        if (!visitedNodeIds.add(parentNode.getId())) {
            return;
        }
        // 获取潜在的子节点列表
        List<N> potentialChildren = childrenMap.get(parentNode.getId());
        List<N> actualChildren = new ArrayList<>();
        if (potentialChildren != null && !potentialChildren.isEmpty()) {
            // 3过滤掉已访问的节点并去重
            actualChildren = potentialChildren.stream()
                    .filter(child -> !visitedNodeIds.contains(child.getId()))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(BaseTreeNode::getId, child -> child, (existing, replacement) -> existing),
                            map -> new ArrayList<>(map.values())
                    ));
        }
        // 在递归【之前】进行排序
        if (comparator != null && !actualChildren.isEmpty()) {
            actualChildren.sort(comparator);
        }
        // 根据配置设置 children 属性
        if (includeEmptyChildren || !actualChildren.isEmpty()) {
            parentNode.setChildren(actualChildren);
        } else {
            parentNode.setChildren(null);
        }
        // 对子节点进行递归
        for (N child : actualChildren) {
            assignChildrenRecursive(child, childrenMap, visitedNodeIds, comparator, includeEmptyChildren);
        }
    }

    /**
     * 将地域信息列表转换为树形结构的节点列表（省 {@code ->} 市 {@code ->} 区）。
     * 该方法遍历地域信息，为每个省份创建一个根节点，并为每个省份下的城市和区域创建子节点。
     * 使用 Set 集合来跟踪已处理过的省份、城市和区域，以避免重复添加。
     *
     * @param objects   包含地域信息的 RegionsDo 对象列表。每个对象应包含省份、城市和区域的代码及名称。
     * @param rootList  用于存储生成的根节点（省份）的列表。
     * @param childList 用于存储生成的子节点（城市和区域）的列表。
     * @since 2.8.6
     */
    public static void processCustomThree(List<RegionDto> objects, List<TreeNode> rootList, List<TreeNode> childList) {
        // 用于存储已处理过的省份代码，避免重复添加
        Set<String> processedProvinces = new HashSet<>();
        // 用于存储已处理过的城市代码，避免重复添加
        Set<String> processedCities = new HashSet<>();
        // 用于存储已处理过的区域 ID，避免重复添加
        Set<String> processedAreas = new HashSet<>();
        for (RegionDto region : objects) {
            // 生成区域节点的唯一 ID
            String areaId = generateAreaId(region.getAreaCode(), region.getAreaName());
            // 如果当前省份已经被处理过
            if (processedProvinces.contains(region.getProvinceCode())) {
                // 如果当前城市尚未被处理过
                if (!processedCities.contains(region.getCityCode())) {
                    // 添加城市节点
                    addCityNode(region, childList, processedCities);
                    // 添加区域节点
                    addAreaNode(region, areaId, childList, processedAreas);
                }
                // 如果当前城市已经被处理过，但当前区域尚未被处理过
                else if (!processedAreas.contains(areaId)) {
                    // 添加区域节点
                    addAreaNode(region, areaId, childList, processedAreas);
                }
            } else { // 如果当前省份尚未被处理过
                // 添加省份节点
                addProvinceNode(region, rootList, processedProvinces);
                // 添加城市节点
                addCityNode(region, childList, processedCities);
                // 添加区域节点
                addAreaNode(region, areaId, childList, processedAreas);
            }
        }
    }

    /**
     * 生成区域节点的唯一 ID。
     *
     * @param areaCode 区域代码
     * @param areaName 区域名称
     * @return 区域节点的 ID
     * @since 2.8.6
     */
    private static String generateAreaId(String areaCode, String areaName) {
        return areaCode + StrPool.UNDERLINE + areaName;
    }

    /**
     * 根据 RegionDto 对象创建一个表示省份的 TreeNode 对象。
     * 该方法仅负责创建 TreeNode 对象并设置其基本属性。
     *
     * @param region 包含省份信息的 RegionDto 对象。
     * @return 创建好的表示省份的 TreeNode 对象。
     * @since 2.8.6
     */
    private static TreeNode createProvinceNode(RegionDto region) {
        TreeNode node = new TreeNode();
        node.setId(region.getProvinceCode());
        node.setCustomLabel(region.getProvinceName());
        node.setCustomValue(region.getProvinceCode());
        return node;
    }

    /**
     * 创建省份 TreeNode 对象并将其添加到根节点列表，同时标记该省份为已处理。
     * 该方法调用 {@link #createProvinceNode(RegionDto)} 创建节点，然后将其添加到提供的 rootList 和 processedProvinces 集合中。
     *
     * @param region             包含省份信息的 RegionDto 对象。
     * @param rootList           用于存储根节点（省份）的列表。
     * @param processedProvinces 用于存储已处理过的省份代码的 Set 集合。
     * @since 2.8.6
     */
    private static void addProvinceNode(RegionDto region, List<TreeNode> rootList, Set<String> processedProvinces) {
        TreeNode provinceNode = createProvinceNode(region);
        rootList.add(provinceNode);
        processedProvinces.add(region.getProvinceCode());
    }

    /**
     * 创建城市 TreeNode 对象并将其添加到子节点列表，同时标记该城市为已处理。
     * 该方法创建一个表示城市的 TreeNode 对象，设置其父节点 ID 为省份代码，然后将其添加到提供的 childList 和 processedCities 集合中。
     *
     * @param region          包含城市信息的 RegionDto 对象。
     * @param childList       用于存储子节点（城市、区域、街道）的列表。
     * @param processedCities 用于存储已处理过的城市代码的 Set 集合。
     * @since 2.8.6
     */
    private static void addCityNode(RegionDto region, List<TreeNode> childList, Set<String> processedCities) {
        TreeNode cityNode = new TreeNode();
        cityNode.setId(region.getCityCode());
        cityNode.setParentId(region.getProvinceCode());
        cityNode.setCustomLabel(region.getCityName());
        cityNode.setCustomValue(region.getCityCode());
        childList.add(cityNode);
        processedCities.add(region.getCityCode());
    }

    /**
     * 创建区域 TreeNode 对象并将其添加到子节点列表，同时标记该区域为已处理。
     * 该方法创建一个表示区域的 TreeNode 对象，设置其父节点 ID 为城市代码，然后将其添加到提供的 childList 和 processedAreas 集合中。
     *
     * @param region         包含区域信息的 RegionDto 对象。
     * @param areaId         区域节点的 ID。
     * @param childList      用于存储子节点（城市、区域、街道）的列表。
     * @param processedAreas 用于存储已处理过的区域 ID 的 Set 集合。
     * @since 2.8.6
     */
    private static void addAreaNode(RegionDto region, String areaId, List<TreeNode> childList, Set<String> processedAreas) {
        TreeNode areaNode = new TreeNode();
        areaNode.setId(areaId);
        areaNode.setParentId(region.getCityCode());
        areaNode.setCustomLabel(region.getAreaName());
        areaNode.setCustomValue(region.getAreaCode());
        childList.add(areaNode);
        processedAreas.add(areaId);
    }
}
