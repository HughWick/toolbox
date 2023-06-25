package com.github.hugh.support;

import com.github.hugh.bean.expand.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 树形结构操作类
 *
 * @author hugh
 * @since 2.5.11
 */
public class TreeNodeOpe {

    private final List<TreeNode> rootNodesList;//根节点列表

    private final List<TreeNode> childNodesList;//子节点列表

    /**
     * 构造方法，初始化根节点列表和子节点列表
     *
     * @param rootNodesList   根节点列表
     * @param childNodesList  子节点列表
     */
    public TreeNodeOpe(List<TreeNode> rootNodesList, List<TreeNode> childNodesList) {
        this.rootNodesList = rootNodesList;
        this.childNodesList = childNodesList;
    }

    /**
     * 创建 TreeNodeOpe 实例，传入根节点列表和子节点列表
     *
     * @param rootNodesList   根节点列表
     * @param childNodesList  子节点列表
     * @return TreeNodeOpe 实例
     */
    public static TreeNodeOpe on(List<TreeNode> rootNodesList, List<TreeNode> childNodesList) {
        return new TreeNodeOpe(rootNodesList, childNodesList);
    }
    /**
     * 处理树节点列表，将子节点分配给对应的父节点
     *
     * @return 处理后的根节点列表
     */
    public List<TreeNode> process() {
        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //创建一个map用于保存已经处理过的childNodesList中的TreeObject的id（去重）
        Map<String, String> childNodesHashMap = new ConcurrentHashMap<>(childNodesList.size());
        rootNodesList.forEach(rootNode -> {
            // 提交任务给线程池
            executorService.submit(() -> {
                // 循环根节点列表，将子节点列表封装到对应的根节点TreeObject对象中
                assignChildNodes(childNodesList, rootNode, childNodesHashMap);
            });
        });
        // 关闭线程池
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        return rootNodesList;
    }

    /**
     * 将子节点分配给对应的父节点
     *
     * @param childNodesList    子节点列表
     * @param node              当前节点
     * @param childNodesHashMap 存储已处理的子节点ID的HashMap
     */
    private static void assignChildNodes(List<TreeNode> childNodesList, TreeNode node, Map<String, String> childNodesHashMap) {
        //创建一个list来保存每个根节点中对应的子节点
        List<TreeNode> childList = new ArrayList<>();
        childNodesList.stream()
                .filter(childNode -> !childNodesHashMap.containsKey(childNode.getId()))//过滤处理过的childNode
                .filter(childNode -> childNode.getParentId().equals(node.getId()))//判断是否根节点的子节点
                .forEach(childNode -> {
                    childNodesHashMap.put(childNode.getId(), childNode.getParentId());//添加处理子节点信息
                    assignChildNodes(childNodesList, childNode, childNodesHashMap);//递归设置该子节点的子节点列表
                    childList.add(childNode);//添加该子节点到对应的根节点列表
                });
        node.setChildren(childList);
    }
}
