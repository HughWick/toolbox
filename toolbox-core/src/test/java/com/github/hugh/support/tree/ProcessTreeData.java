package com.github.hugh.support.tree;

import com.github.hugh.bean.dto.EntityCompareResult;
import com.github.hugh.bean.expand.tree.TreeNode;
import com.github.hugh.bean.expand.tree.TreeNodeExpand;

import java.util.*;

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


    public static void processCustom(List<TreeNodeObject> objects, List<TreeNode> rootList, List<TreeNode> childList) {
        Map<String, Object> nodeMap = new HashMap<>();
        Map<String, Object> nodeMap2 = new HashMap<>();
        Map<String, Object> nodeMap3 = new HashMap<>();
        Map<String, Object> nodeMap4 = new HashMap<>();
        // 将每一级都只放一条数据，并用上下级code码区分
        for (TreeNodeObject treeNode : objects) {
            // 存在省份
            if (nodeMap.containsKey(treeNode.getProvinceCode())) {// 省份
                if (nodeMap2.containsKey(treeNode.getCityCode())) { // 城 判断城市key是否存在
                    if (nodeMap3.containsKey(treeNode.getAreaCode())) { // 区
                        if (nodeMap4.containsKey(treeNode.getStreetCodeEx())) { // 街道
                        } else {
                            // 第四级
                            TreeNode childNode4 = new TreeNode();
                            childNode4.setId(treeNode.getStreetCodeEx());
                            childNode4.setParentId(treeNode.getAreaCode());
                            childNode4.setCustomLabel(treeNode.getStreetName());
                            childNode4.setCustomValue(treeNode.getStreetCodeEx());
                            childList.add(childNode4);
                            nodeMap4.put(treeNode.getStreetCodeEx(), true);
                        }
                    } else {
//                        // 保存一次下一级
//                        TreeNode childNode4 = new TreeNode(treeNode.getStreetCodeEx(), treeNode.getAreaCode(), treeNode.getStreetName(), null);
                        TreeNode childNode4 = new TreeNode();
                        childNode4.setId(treeNode.getStreetCodeEx());
                        childNode4.setParentId(treeNode.getAreaCode());
                        childNode4.setCustomLabel(treeNode.getStreetName());
                        childNode4.setCustomValue(treeNode.getStreetCodeEx());
                        childList.add(childNode4);
                        nodeMap4.put(treeNode.getStreetCodeEx(), true);
//                        TreeNode childNode3 = new TreeNode(treeNode.getAreaCode(), treeNode.getCityCode(), treeNode.getAreaName(), null);
                        TreeNode childNode3 = new TreeNode();
                        childNode3.setId(treeNode.getAreaCode());
                        childNode3.setParentId(treeNode.getCityCode());
                        childNode3.setCustomLabel(treeNode.getAreaName());
                        childNode3.setCustomValue(treeNode.getAreaCode());
                        childList.add(childNode3);
                        nodeMap3.put(treeNode.getAreaCode(), true);
                    }
                } else { // 不存在则
                    TreeNode childNode2 = new TreeNode();
                    childNode2.setId(treeNode.getCityCode());
                    childNode2.setParentId(treeNode.getProvinceCode());
                    childNode2.setCustomLabel(treeNode.getCityName());
                    childNode2.setCustomValue(treeNode.getCityCode());
                    childList.add(childNode2);
                    // 保存一次下一级
                    TreeNode childNode3 = new TreeNode();
                    childNode3.setId(treeNode.getAreaCode());
                    childNode3.setParentId(treeNode.getAreaCode());
                    childNode3.setCustomLabel(treeNode.getAreaName());
                    childNode3.setCustomValue(treeNode.getAreaCode());
                    childList.add(childNode3);
                    nodeMap2.put(treeNode.getCityCode(), true);
                }
            } else { // 不存在
                TreeNode rootNode = new TreeNode();
                rootNode.setId(treeNode.getProvinceCode());
                rootNode.setCustomLabel(treeNode.getProvinceName());
                rootNode.setCustomValue(treeNode.getProvinceCode());
                rootList.add(rootNode);
                // 保存一次第二级
                TreeNode childNode2 = new TreeNode();
                childNode2.setId(treeNode.getCityCode());
                childNode2.setParentId(treeNode.getProvinceCode());
                childNode2.setCustomLabel(treeNode.getCityName());
                childNode2.setCustomValue(treeNode.getCityCode());
                childList.add(childNode2);
                nodeMap2.put(treeNode.getCityCode(), true);
                // 保存一次第三级
                TreeNode childNode3 = new TreeNode();
                childNode3.setId(treeNode.getAreaCode());
                childNode3.setParentId(treeNode.getCityCode());
                childNode3.setCustomLabel(treeNode.getAreaName());
                childNode3.setCustomValue(treeNode.getAreaCode());
                childList.add(childNode3);
                nodeMap3.put(treeNode.getAreaCode(), true);
                // 保存一次第四级
                TreeNode childNode4 = new TreeNode();
                childNode4.setId(treeNode.getStreetCodeEx());
                childNode4.setParentId(treeNode.getAreaCode());
                childNode4.setCustomLabel(treeNode.getStreetName());
                childNode4.setCustomValue(treeNode.getStreetCodeEx());
                childList.add(childNode4);
                nodeMap.put(treeNode.getProvinceCode(), true);
            }
        }
    }


    /**
     * 将 TreeNodeObject 列表处理成三级级联选择器所需的 TreeNode 列表。
     * <p>
     * 该方法通过一次遍历输入的 TreeNodeObject 列表，提取省、市、区/县信息，
     * 并使用 Set 集合来去重，确保每个层级的节点只被创建一次。
     * 创建的 TreeNode 对象会添加到 rootList（省级节点）和 childList（市级和区/县级节点）中，
     * 并设置相应的父子关系。
     *
     * @param objects   包含省、市、区/县信息的 TreeNodeObject 列表。
     * @param rootList  用于存储处理后的省级 TreeNode 对象的列表。
     * @param childList 用于存储处理后的市级和区/县级 TreeNode 对象的列表。
     */
    public static void processCustomThree(List<TreeNodeObject> objects, List<TreeNode> rootList, List<TreeNode> childList) {
        Set<String> processedProvinces = new LinkedHashSet<>();
        Set<String> processedCities = new LinkedHashSet<>();
        Set<String> processedAreas = new LinkedHashSet<>();

        for (TreeNodeObject treeNode : objects) {
            String provinceCode = treeNode.getProvinceCode();
            String provinceName = treeNode.getProvinceName();
            String cityCode = treeNode.getCityCode();
            String cityName = treeNode.getCityName();
            String areaCode = treeNode.getAreaCode();
            String areaName = treeNode.getAreaName();

            // 处理省级节点
            if (!processedProvinces.contains(provinceCode)) {
                TreeNode rootNode = new TreeNode();
                rootNode.setId(provinceCode);
                rootNode.setCustomLabel(provinceName);
                rootNode.setCustomValue(provinceCode);
                rootList.add(rootNode);
                processedProvinces.add(provinceCode);
            }

            // 处理市级节点
            if (!processedCities.contains(cityCode)) {
                TreeNode cityNode = new TreeNode();
                cityNode.setId(cityCode);
                cityNode.setParentId(provinceCode);
                cityNode.setCustomLabel(cityName);
                cityNode.setCustomValue(cityCode);
                childList.add(cityNode);
                processedCities.add(cityCode);
            }

            // 处理区/县级节点
            if (!processedAreas.contains(areaCode)) {
                TreeNode areaNode = new TreeNode();
                areaNode.setId(areaCode);
                areaNode.setParentId(cityCode);
                areaNode.setCustomLabel(areaName);
                areaNode.setCustomValue(areaCode);
                childList.add(areaNode);
                processedAreas.add(areaCode);
            }
        }
    }
}
