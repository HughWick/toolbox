package com.github.hugh.support.tree;

import com.github.hugh.bean.dto.RegionDto;
import com.github.hugh.bean.expand.tree.TreeNode;
import com.github.hugh.constant.StrPool;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


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

    /**
     * 将地域信息列表转换为树形结构的节点列表（省 -> 市 -> 区）。
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
