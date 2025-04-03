package com.github.hugh.support.tree;

import com.github.hugh.bean.dto.EntityCompareResult;
import com.github.hugh.bean.dto.RegionDto;
import com.github.hugh.bean.expand.tree.TreeNode;
import com.github.hugh.bean.expand.tree.TreeNodeExpand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理属性结构数据测试
 *
 * @author AS
 * @date 2023/10/12 10:28
 */
public class ProcessTreeData {

    public static void processExpandStr(List<TreeNodeObject> objects, List<TreeNodeExpand<String>> rootList, List<TreeNodeExpand<String>> childList) {
        Map<String, Object> nodeMap = new HashMap<>();
        Map<String, Object> nodeMap2 = new HashMap<>();
        Map<String, Object> nodeMap3 = new HashMap<>();
        Map<String, Object> nodeMap4 = new HashMap<>();
        // 将每一级都只放一条数据，并用上下级code码区分
        for (TreeNodeObject treeNode : objects) {
            // 存在省份
            if (nodeMap.containsKey(treeNode.getProvinceCode())) {// 省份
                if (nodeMap2.containsKey(treeNode.getCityCode())) { // 城 判断城市key是否存在
//                    System.out.println("====城====");
                    if (nodeMap3.containsKey(treeNode.getAreaCode())) { // 区
//                        System.out.println("====区====");
                        if (nodeMap4.containsKey(treeNode.getStreetCodeEx())) { // 街道
//                            System.out.println("===街道=存在");
                        } else {
//                            System.out.println("-------街道不存在------");
                            // 第四级
                            TreeNodeExpand childNode4 = new TreeNodeExpand(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null, "json");
                            childList.add(childNode4);
                            nodeMap4.put(treeNode.getStreetCodeEx(), true);
                        }
                    } else {
//                        System.out.println("==不存在==区====" + treeNode.getAreaName());
//                        // 保存一次下一级
                        TreeNodeExpand childNode4 = new TreeNodeExpand(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null, "oneJson");
                        childList.add(childNode4);
                        nodeMap4.put(treeNode.getStreetCodeEx(), true);
                        TreeNodeExpand childNode3 = new TreeNodeExpand(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null, "");
                        childList.add(childNode3);
                        nodeMap3.put(treeNode.getAreaCode(), true);
                    }
                } else { // 不存在则
//                    System.out.println("==不存在==城市====");
                    TreeNodeExpand childNode2 = new TreeNodeExpand(treeNode.getCityCode(), treeNode.getProvinceCode(), treeNode.getCityName(), null, "");
                    childList.add(childNode2);
                    // 保存一次下一级
                    TreeNodeExpand childNode3 = new TreeNodeExpand(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null, "");
                    childList.add(childNode3);
                    nodeMap2.put(treeNode.getCityCode(), true);
                }
            } else { // 不存在
                TreeNodeExpand rootNode = new TreeNodeExpand(treeNode.getProvinceCode(), null, treeNode.getProvinceName(), null, "");
                rootList.add(rootNode);
                // 保存一次第二级
                TreeNodeExpand childNode2 = new TreeNodeExpand(treeNode.getCityCode(), treeNode.getProvinceCode(), treeNode.getCityName(), null, "");
                childList.add(childNode2);
                nodeMap2.put(treeNode.getCityCode(), true);
                // 保存一次第三级
                TreeNodeExpand childNode3 = new TreeNodeExpand(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null, "");
                childList.add(childNode3);
                nodeMap3.put(treeNode.getAreaCode(), true);
                // 保存一次第四级
                TreeNodeExpand childNode4 = new TreeNodeExpand(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null, "oneJson");
                childList.add(childNode4);
                nodeMap.put(treeNode.getProvinceCode(), true);
            }
        }
    }


    public static void processExpandObj(List<TreeNodeObject> objects, List<TreeNodeExpand<EntityCompareResult>> rootList, List<TreeNodeExpand<EntityCompareResult>> childList) {
        Map<String, Object> nodeMap = new HashMap<>();
        Map<String, Object> nodeMap2 = new HashMap<>();
        Map<String, Object> nodeMap3 = new HashMap<>();
        Map<String, Object> nodeMap4 = new HashMap<>();
        // 将每一级都只放一条数据，并用上下级code码区分
        for (TreeNodeObject treeNode : objects) {
            // 存在省份
            if (nodeMap.containsKey(treeNode.getProvinceCode())) {// 省份
                if (nodeMap2.containsKey(treeNode.getCityCode())) { // 城 判断城市key是否存在
//                    System.out.println("====城====");
                    if (nodeMap3.containsKey(treeNode.getAreaCode())) { // 区
//                        System.out.println("====区====");
                        if (nodeMap4.containsKey(treeNode.getStreetCodeEx())) { // 街道
//                            System.out.println("===街道=存在");
                        } else {
//                            System.out.println("-------街道不存在------");
                            // 第四级
                            TreeNodeExpand childNode4 = new TreeNodeExpand<>(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null, new EntityCompareResult("json", "old_json", "new_value"));
                            childList.add(childNode4);
                            nodeMap4.put(treeNode.getStreetCodeEx(), true);
                        }
                    } else {
//                        System.out.println("==不存在==区====" + treeNode.getAreaName());
//                        // 保存一次下一级
                        TreeNodeExpand childNode4 = new TreeNodeExpand<>(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null, new EntityCompareResult());
                        childList.add(childNode4);
                        nodeMap4.put(treeNode.getStreetCodeEx(), true);
                        TreeNodeExpand childNode3 = new TreeNodeExpand<>(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null, null);
                        childList.add(childNode3);
                        nodeMap3.put(treeNode.getAreaCode(), true);
                    }
                } else { // 不存在则
//                    System.out.println("==不存在==城市====");
                    TreeNodeExpand childNode2 = new TreeNodeExpand<>(treeNode.getCityCode(), treeNode.getProvinceCode(), treeNode.getCityName(), null, null);
                    childList.add(childNode2);
                    // 保存一次下一级
                    TreeNodeExpand childNode3 = new TreeNodeExpand<>(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null, null);
                    childList.add(childNode3);
                    nodeMap2.put(treeNode.getCityCode(), true);
                }
            } else { // 不存在
                TreeNodeExpand rootNode = new TreeNodeExpand<>(treeNode.getProvinceCode(), null, treeNode.getProvinceName(), null, new EntityCompareResult("1_json", "1_old_json", "1_new_value"));
                rootList.add(rootNode);
                // 保存一次第二级
                TreeNodeExpand childNode2 = new TreeNodeExpand<>(treeNode.getCityCode(), treeNode.getProvinceCode(), treeNode.getCityName(), null, null);
                childList.add(childNode2);
                nodeMap2.put(treeNode.getCityCode(), true);
                // 保存一次第三级
                TreeNodeExpand childNode3 = new TreeNodeExpand<>(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null, null);
                childList.add(childNode3);
                nodeMap3.put(treeNode.getAreaCode(), true);
                // 保存一次第四级
                TreeNodeExpand childNode4 = new TreeNodeExpand<>(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null, new EntityCompareResult("json", "old_json", "new_value"));
                childList.add(childNode4);
                nodeMap.put(treeNode.getProvinceCode(), true);
            }
        }
    }

    // 处理树形结构数据
    public static void process(List<TreeNodeObject> objects, List<TreeNode> rootList, List<TreeNode> childList) {
        Map<String, Object> nodeMap = new HashMap<>();
        Map<String, Object> nodeMap2 = new HashMap<>();
        Map<String, Object> nodeMap3 = new HashMap<>();
        Map<String, Object> nodeMap4 = new HashMap<>();
        // 将每一级都只放一条数据，并用上下级code码区分
        for (TreeNodeObject treeNode : objects) {
            // 存在省份
            if (nodeMap.containsKey(treeNode.getProvinceCode())) {// 省份
                if (nodeMap2.containsKey(treeNode.getCityCode())) { // 城 判断城市key是否存在
//                    System.out.println("====城====");
                    if (nodeMap3.containsKey(treeNode.getAreaCode())) { // 区
//                        System.out.println("====区====");
                        if (nodeMap4.containsKey(treeNode.getStreetCodeEx())) { // 街道
//                            System.out.println("===街道=存在");
                        } else {
//                            System.out.println("-------街道不存在------");
                            // 第四级
                            TreeNode childNode4 = new TreeNode(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null);
                            childList.add(childNode4);
                            nodeMap4.put(treeNode.getStreetCodeEx(), true);
                        }
                    } else {
//                        System.out.println("==不存在==区====" + treeNode.getAreaName());
//                        // 保存一次下一级
                        TreeNode childNode4 = new TreeNode(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null);
                        childList.add(childNode4);
                        nodeMap4.put(treeNode.getStreetCodeEx(), true);
                        TreeNode childNode3 = new TreeNode(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null);
                        childList.add(childNode3);
                        nodeMap3.put(treeNode.getAreaCode(), true);
                    }
                } else { // 不存在则
//                    System.out.println("==不存在==城市====");
                    TreeNode childNode2 = new TreeNode(treeNode.getCityCode(), treeNode.getProvinceCode(), treeNode.getCityName(), null);
                    childList.add(childNode2);
                    // 保存一次下一级
                    TreeNode childNode3 = new TreeNode(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null);
                    childList.add(childNode3);
                    nodeMap2.put(treeNode.getCityCode(), true);
                }
            } else { // 不存在
                TreeNode rootNode = new TreeNode(treeNode.getProvinceCode(), null, treeNode.getProvinceName(), null);
                rootList.add(rootNode);
                // 保存一次第二级
                TreeNode childNode2 = new TreeNode(treeNode.getCityCode(), treeNode.getProvinceCode(), treeNode.getCityName(), null);
                childList.add(childNode2);
                nodeMap2.put(treeNode.getCityCode(), true);
                // 保存一次第三级
                TreeNode childNode3 = new TreeNode(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null);
                childList.add(childNode3);
                nodeMap3.put(treeNode.getAreaCode(), true);
                // 保存一次第四级
                TreeNode childNode4 = new TreeNode(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null);
                childList.add(childNode4);
                nodeMap.put(treeNode.getProvinceCode(), true);
            }
        }
    }
    /**
     * 优化的方法，用于将地域信息列表转换为树形结构的节点列表（省 -> 市 -> 区 -> 街道）。
     * 该方法遍历地域信息，为每个省份创建一个根节点，并为每个省份下的城市、区域和街道创建子节点。
     * 使用一个 Map 来跟踪已创建的节点，避免重复添加。
     *
     * @param objects   包含地域信息的 RegionDto 对象列表。每个对象应包含省份、城市、区域和街道的代码及名称。
     * @param rootList  用于存储生成的根节点（省份）的列表。
     * @param childList 用于存储生成的子节点（城市、区域和街道）的列表。
     */
    public static void processCustomOptimizedFour(List<RegionDto> objects, List<TreeNode> rootList, List<TreeNode> childList) {
        // 用于存储已创建的 TreeNode 对象，Key 为节点的唯一标识（Code）
        Map<String, TreeNode> existingNodes = new HashMap<>();
        for (RegionDto region : objects) {
            // 处理省份
            String provinceCode = region.getProvinceCode();
            // 如果该省份节点尚未创建
            if (!existingNodes.containsKey(provinceCode)) {
                TreeNode provinceNode = createProvinceNode(region);
                rootList.add(provinceNode);
                existingNodes.put(provinceCode, provinceNode);
            }
            // 处理城市
            String cityCode = region.getCityCode();
            // 如果该城市节点尚未创建
            if (!existingNodes.containsKey(cityCode)) {
                TreeNode cityNode = createCityNode(region);
                cityNode.setParentId(provinceCode);
                childList.add(cityNode);
                existingNodes.put(cityCode, cityNode);
            }

            // 处理区域
            String areaCode = region.getAreaCode();
            // 如果该区域节点尚未创建
            if (!existingNodes.containsKey(areaCode)) {
                TreeNode areaNode = createAreaNode(region);
                areaNode.setParentId(cityCode);
                childList.add(areaNode);
                existingNodes.put(areaCode, areaNode);
            }
            // 处理街道
            String streetCodeEx = region.getStreetCodeEx();
            // 如果街道代码不为空且该街道节点尚未创建
            if (streetCodeEx != null && !streetCodeEx.isEmpty() && !existingNodes.containsKey(streetCodeEx)) {
                TreeNode streetNode = createStreetNode(region);
                streetNode.setParentId(areaCode);
                childList.add(streetNode);
                existingNodes.put(streetCodeEx, streetNode);
            }
        }
    }

    private static TreeNode createProvinceNode(RegionDto region) {
        TreeNode node = new TreeNode();
        node.setId(region.getProvinceCode());
        node.setCustomLabel(region.getProvinceName());
        node.setCustomValue(region.getProvinceCode());
        return node;
    }

    private static TreeNode createCityNode(RegionDto region) {
        TreeNode node = new TreeNode();
        node.setId(region.getCityCode());
        node.setCustomLabel(region.getCityName());
        node.setCustomValue(region.getCityCode());
        return node;
    }

    private static TreeNode createAreaNode(RegionDto region) {
        TreeNode node = new TreeNode();
        node.setId(region.getAreaCode());
        node.setCustomLabel(region.getAreaName());
        node.setCustomValue(region.getAreaCode());
        return node;
    }

    private static TreeNode createStreetNode(RegionDto region) {
        TreeNode node = new TreeNode();
        node.setId(region.getStreetCodeEx());
        node.setCustomLabel(region.getStreetName());
        node.setCustomValue(region.getStreetCodeEx());
        return node;
    }

//    public static void processCustom(List<RegionDto> objects, List<TreeNode> rootList, List<TreeNode> childList) {
//        Map<String, Object> nodeMap = new HashMap<>();
//        Map<String, Object> nodeMap2 = new HashMap<>();
//        Map<String, Object> nodeMap3 = new HashMap<>();
//        Map<String, Object> nodeMap4 = new HashMap<>();
//        // 将每一级都只放一条数据，并用上下级code码区分
//        for (RegionDto treeNode : objects) {
//            // 存在省份
//            if (nodeMap.containsKey(treeNode.getProvinceCode())) {// 省份
//                if (nodeMap2.containsKey(treeNode.getCityCode())) { // 城 判断城市key是否存在
//                    if (nodeMap3.containsKey(treeNode.getAreaCode())) { // 区
//                        if (nodeMap4.containsKey(treeNode.getStreetCodeEx())) { // 街道
//                        } else {
//                            // 第四级
//                            TreeNode childNode4 = new TreeNode();
//                            childNode4.setId(treeNode.getStreetCodeEx());
//                            childNode4.setParentId(treeNode.getAreaCode());
//                            childNode4.setCustomLabel(treeNode.getStreetName());
//                            childNode4.setCustomValue(treeNode.getStreetCodeEx());
//                            childList.add(childNode4);
//                            nodeMap4.put(treeNode.getStreetCodeEx(), true);
//                        }
//                    } else {
////                        // 保存一次下一级
////                        TreeNode childNode4 = new TreeNode(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null);
//                        TreeNode childNode4 = new TreeNode();
//                        childNode4.setId(treeNode.getStreetCodeEx());
//                        childNode4.setParentId(treeNode.getAreaCode());
//                        childNode4.setCustomLabel(treeNode.getStreetName());
//                        childNode4.setCustomValue(treeNode.getStreetCodeEx());
//                        childList.add(childNode4);
//                        nodeMap4.put(treeNode.getStreetCodeEx(), true);
////                        TreeNode childNode3 = new TreeNode(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null);
//                        TreeNode childNode3 = new TreeNode();
//                        childNode3.setId(treeNode.getAreaCode());
//                        childNode3.setParentId(treeNode.getCityCode());
//                        childNode3.setCustomLabel(treeNode.getAreaName());
//                        childNode3.setCustomValue(treeNode.getAreaCode());
//                        childList.add(childNode3);
//                        nodeMap3.put(treeNode.getAreaCode(), true);
//                    }
//                } else { // 不存在则
//                    TreeNode childNode2 = new TreeNode();
//                    childNode2.setId(treeNode.getCityCode());
//                    childNode2.setParentId(treeNode.getProvinceCode());
//                    childNode2.setCustomLabel(treeNode.getCityName());
//                    childNode2.setCustomValue(treeNode.getCityCode());
//                    childList.add(childNode2);
//                    // 保存一次下一级
//                    TreeNode childNode3 = new TreeNode();
//                    childNode3.setId(treeNode.getAreaCode());
//                    childNode3.setParentId(treeNode.getAreaCode());
//                    childNode3.setCustomLabel(treeNode.getAreaName());
//                    childNode3.setCustomValue(treeNode.getAreaCode());
//                    childList.add(childNode3);
//                    nodeMap2.put(treeNode.getCityCode(), true);
//                }
//            } else { // 不存在
//                TreeNode rootNode = new TreeNode();
//                rootNode.setId(treeNode.getProvinceCode());
//                rootNode.setCustomLabel(treeNode.getProvinceName());
//                rootNode.setCustomValue(treeNode.getProvinceCode());
//                rootList.add(rootNode);
//                // 保存一次第二级
//                TreeNode childNode2 = new TreeNode();
//                childNode2.setId(treeNode.getCityCode());
//                childNode2.setParentId(treeNode.getProvinceCode());
//                childNode2.setCustomLabel(treeNode.getCityName());
//                childNode2.setCustomValue(treeNode.getCityCode());
//                childList.add(childNode2);
//                nodeMap2.put(treeNode.getCityCode(), true);
//                // 保存一次第三级
//                TreeNode childNode3 = new TreeNode();
//                childNode3.setId(treeNode.getAreaCode());
//                childNode3.setParentId(treeNode.getCityCode());
//                childNode3.setCustomLabel(treeNode.getAreaName());
//                childNode3.setCustomValue(treeNode.getAreaCode());
//                childList.add(childNode3);
//                nodeMap3.put(treeNode.getAreaCode(), true);
//                // 保存一次第四级
//                TreeNode childNode4 = new TreeNode();
//                childNode4.setId(treeNode.getStreetCodeEx());
//                childNode4.setParentId(treeNode.getAreaCode());
//                childNode4.setCustomLabel(treeNode.getStreetName());
//                childNode4.setCustomValue(treeNode.getStreetCodeEx());
//                childList.add(childNode4);
//                nodeMap.put(treeNode.getProvinceCode(), true);
//            }
//        }
//    }

//    /**
//     * 将地域信息列表转换为树形结构的节点列表（省 -> 市 -> 区）。
//     * 该方法遍历地域信息，为每个省份创建一个根节点，并为每个省份下的城市和区域创建子节点。
//     * 使用 Set 集合来跟踪已处理过的省份、城市和区域，以避免重复添加。
//     *
//     * @param objects   包含地域信息的 RegionsDo 对象列表。每个对象应包含省份、城市和区域的代码及名称。
//     * @param rootList  用于存储生成的根节点（省份）的列表。
//     * @param childList 用于存储生成的子节点（城市和区域）的列表。
//     */
//    public static void processCustomThree(List<RegionDto> objects, List<TreeNode> rootList, List<TreeNode> childList) {
//        // 用于存储已处理过的省份代码，避免重复添加
//        Set<String> processedProvinces = new HashSet<>();
//        // 用于存储已处理过的城市代码，避免重复添加
//        Set<String> processedCities = new HashSet<>();
//        // 用于存储已处理过的区域 ID，避免重复添加
//        Set<String> processedAreas = new HashSet<>();
//        for (RegionDto region : objects) {
//            // 生成区域节点的唯一 ID
//            String areaId = generateAreaId(region.getAreaCode(), region.getAreaName());
//            // 如果当前省份已经被处理过
//            if (processedProvinces.contains(region.getProvinceCode())) {
//                // 如果当前城市尚未被处理过
//                if (!processedCities.contains(region.getCityCode())) {
//                    // 添加城市节点
//                    addCityNode(region, childList, processedCities);
//                    // 添加区域节点
//                    addAreaNode(region, areaId, childList, processedAreas);
//                }
//                // 如果当前城市已经被处理过，但当前区域尚未被处理过
//                else if (!processedAreas.contains(areaId)) {
//                    // 添加区域节点
//                    addAreaNode(region, areaId, childList, processedAreas);
//                }
//            } else { // 如果当前省份尚未被处理过
//                // 添加省份节点
//                addProvinceNode(region, rootList, processedProvinces);
//                // 添加城市节点
//                addCityNode(region, childList, processedCities);
//                // 添加区域节点
//                addAreaNode(region, areaId, childList, processedAreas);
//            }
//        }
//    }
//
//    /**
//     * 生成区域节点的唯一 ID。
//     *
//     * @param areaCode 区域代码
//     * @param areaName 区域名称
//     * @return 区域节点的 ID
//     */
//    private static String generateAreaId(String areaCode, String areaName) {
//        return areaCode + StrPool.UNDERLINE + areaName;
//    }
//
//    private static void addProvinceNode(RegionDto region, List<TreeNode> rootList, Set<String> processedProvinces) {
//        TreeNode provinceNode = createProvinceNode(region);
//        rootList.add(provinceNode);
//        processedProvinces.add(region.getProvinceCode());
//    }
//
//    private static void addCityNode(RegionDto region, List<TreeNode> childList, Set<String> processedCities) {
//        TreeNode cityNode = new TreeNode();
//        cityNode.setId(region.getCityCode());
//        cityNode.setParentId(region.getProvinceCode());
//        cityNode.setCustomLabel(region.getCityName());
//        cityNode.setCustomValue(region.getCityCode());
//        childList.add(cityNode);
//        processedCities.add(region.getCityCode());
//    }
//
//    private static void addAreaNode(RegionDto region, String areaId, List<TreeNode> childList, Set<String> processedAreas) {
//        TreeNode areaNode = new TreeNode();
//        areaNode.setId(areaId);
//        areaNode.setParentId(region.getCityCode());
//        areaNode.setCustomLabel(region.getAreaName());
//        areaNode.setCustomValue(region.getAreaCode());
//        childList.add(areaNode);
//        processedAreas.add(areaId);
//    }

//    /**
//     * 将 TreeNodeObject 列表处理成三级级联选择器所需的 TreeNode 列表。
//     * <p>
//     * 该方法通过一次遍历输入的 TreeNodeObject 列表，提取省、市、区/县信息，
//     * 并使用 Set 集合来去重，确保每个层级的节点只被创建一次。
//     * 创建的 TreeNode 对象会添加到 rootList（省级节点）和 childList（市级和区/县级节点）中，
//     * 并设置相应的父子关系。
//     *
//     * @param objects   包含省、市、区/县信息的 TreeNodeObject 列表。
//     * @param rootList  用于存储处理后的省级 TreeNode 对象的列表。
//     * @param childList 用于存储处理后的市级和区/县级 TreeNode 对象的列表。
//     */
//    public static void processCustomThree(List<TreeNodeObject> objects, List<TreeNode> rootList, List<TreeNode> childList) {
//        Set<String> processedProvinces = new LinkedHashSet<>();
//        Set<String> processedCities = new LinkedHashSet<>();
//        Set<String> processedAreas = new LinkedHashSet<>();
//
//        for (TreeNodeObject treeNode : objects) {
//            String provinceCode = treeNode.getProvinceCode();
//            String provinceName = treeNode.getProvinceName();
//            String cityCode = treeNode.getCityCode();
//            String cityName = treeNode.getCityName();
//            String areaCode = treeNode.getAreaCode();
//            String areaName = treeNode.getAreaName();
//
//            // 处理省级节点
//            if (!processedProvinces.contains(provinceCode)) {
//                TreeNode rootNode = new TreeNode();
//                rootNode.setId(provinceCode);
//                rootNode.setCustomLabel(provinceName);
//                rootNode.setCustomValue(provinceCode);
//                rootList.add(rootNode);
//                processedProvinces.add(provinceCode);
//            }
//
//            // 处理市级节点
//            if (!processedCities.contains(cityCode)) {
//                TreeNode cityNode = new TreeNode();
//                cityNode.setId(cityCode);
//                cityNode.setParentId(provinceCode);
//                cityNode.setCustomLabel(cityName);
//                cityNode.setCustomValue(cityCode);
//                childList.add(cityNode);
//                processedCities.add(cityCode);
//            }
//
//            // 处理区/县级节点
//            if (!processedAreas.contains(areaCode)) {
//                TreeNode areaNode = new TreeNode();
//                areaNode.setId(areaCode);
//                areaNode.setParentId(cityCode);
//                areaNode.setCustomLabel(areaName);
//                areaNode.setCustomValue(areaCode);
//                childList.add(areaNode);
//                processedAreas.add(areaCode);
//            }
//        }
//    }
}
