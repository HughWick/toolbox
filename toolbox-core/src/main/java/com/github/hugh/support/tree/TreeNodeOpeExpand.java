package com.github.hugh.support.tree;

import com.github.hugh.bean.expand.tree.ElementTreeExpand;
import com.github.hugh.bean.expand.tree.TreeNodeExpand;
import com.github.hugh.util.ListUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 树形结构操作类，TreeNodeOpeExpandImpl 类是 TreeNodeOpe 接口的实现类，用于对 TreeNodeExpand 对象和 ElementTreeExpand 对象进行操作。
 *
 * @param <T> 元素类型，表示 TreeNodeExpand 和 ElementTreeExpand 对象包含的元素类型。
 * @author hugh
 * @since 2.6.3
 */
public class TreeNodeOpeExpand<T> implements TreeNodeOpe<TreeNodeExpand<T>, ElementTreeExpand<T>> {

    private final List<TreeNodeExpand<T>> rootNodesList;//根节点列表

    private final List<TreeNodeExpand<T>> childNodesList;//子节点列表
    /**
     * 表示是否进行升序排序
     */
    private boolean ascending = true;
    /**
     * 表示是否设置了父级ID的标志。
     */
    private boolean isSetParentId = false;

    /**
     * 设置是否设置父节点的 ID。
     *
     * @param setParentId 如果为 true，则在转换过程中设置 elementTree 的 parentId 属性；如果为 false，则不设置 parentId 属性。
     */
    @Override
    public void setParentId(boolean setParentId) {
        this.isSetParentId = setParentId;
    }

    /**
     * 设置排序顺序是否升序。
     * <p>
     * 默认为升序排序
     * </p>
     *
     * @param ascending 如果为 true，则按升序排序；如果为 false，则按降序排序。
     */
    @Override
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * 创建 TreeNodeOpe 实例，并传入根节点列表和子节点列表。
     *
     * @param rootNodesList  根节点列表，包含所有的根节点对象
     *                       根节点对象是 TreeNode 类型的对象，表示树结构中的根节点
     *                       每个根节点对象需要提供唯一标识、父节点标识和节点名称
     *                       <p>
     *                       示例：TreeNodeExpand rootNode = new TreeNodeExpand("1",null,"给排水");
     *                       </p>
     * @param childNodesList 子节点列表，包含所有的子节点对象
     *                       子节点对象是 TreeNode 类型的对象，表示树结构中的子节点
     *                       每个子节点对象需要提供唯一标识、父节点标识和节点名称
     *                       <p>
     *                       示例：TreeNodeExpand childNode1 = new TreeNodeExpand("2","1","供配电");
     *                       </p>
     */
    public TreeNodeOpeExpand(List<TreeNodeExpand<T>> rootNodesList, List<TreeNodeExpand<T>> childNodesList) {
        this.rootNodesList = rootNodesList;
        this.childNodesList = childNodesList;
    }

    /**
     * 根据节点的ID属性进行排序的比较器。
     * 通过调用 TreeNodeExpand 对象的 getId 方法来获取 ID 属性。
     */
    final Comparator<TreeNodeExpand<T>> comparingById = Comparator.comparing(TreeNodeExpand::getId);

    /**
     * 处理树节点列表，将子节点分配给对应的父节点
     *
     * @return 处理后的根节点列表
     */
    @Override
    public List<TreeNodeExpand<T>> process() {
        /**
         * 使用固定数量的线程池创建 ExecutorService 实例。
         * 线程池的大小由可用处理器数量决定。
         * ExecutorService 用于管理和调度线程池中的任务执行。
         */
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
        return rootNodesList.stream()
//                .sorted(ascending ? Comparator.comparing(TreeNodeExpand::getId) : Comparator.comparing(TreeNodeExpand::getId).reversed())
                .sorted(ascending ? comparingById : comparingById.reversed())
                .collect(Collectors.toList());
    }

    /**
     * 将处理过的 TreeNode 转换为 ElementTree 的列表。
     *
     * @return ElementTree 的列表
     */
    @Override
    public List<ElementTreeExpand<T>> processElement() {
        final List<TreeNodeExpand<T>> treeNodes = process();
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
    private ElementTreeExpand<T> fromTreeNode(TreeNodeExpand<T> treeNode) {
        ElementTreeExpand<T> elementTree = new ElementTreeExpand<>();
        if (treeNode == null) {
            return elementTree;
        }
        elementTree.setId(treeNode.getId());
        elementTree.setLabel(treeNode.getValue());
        elementTree.setExpand(treeNode.getExpand());
        if (isSetParentId) {
            elementTree.setParentId(treeNode.getParentId());
        }
        List<TreeNodeExpand<T>> childNodes = treeNode.getChildren();
        if (ListUtils.isNotEmpty(childNodes)) {
            List<ElementTreeExpand<T>> children = childNodes.stream()
                    .map(this::fromTreeNode)
                    .collect(Collectors.toList());
            elementTree.setChildren(children);
        }
        return elementTree;
    }


    /**
     * 将子节点分配给对应的父节点
     *
     * @param childNodesList    子节点列表
     * @param node              当前节点
     * @param childNodesHashMap 存储已处理的子节点ID的HashMap
     */
    private void assignChildNodes(List<TreeNodeExpand<T>> childNodesList, TreeNodeExpand<T> node, Map<String, String> childNodesHashMap) {
        //创建一个list来保存每个根节点中对应的子节点
        List<TreeNodeExpand<T>> childList = new ArrayList<>();
        childNodesList.stream()
                .filter(childNode -> childNode.getParentId().equals(node.getId()))//判断是否根节点的子节点
                .sorted(ascending ? comparingById : comparingById.reversed()) // 根据id进行升序或降序排序
                .forEach(childNode -> {
                    if (childNodesHashMap.containsKey(childNode.getId())) { // 排除重复的
                        return;
                    }
                    childNodesHashMap.put(childNode.getId(), childNode.getParentId());//添加处理子节点信息
                    assignChildNodes(childNodesList, childNode, childNodesHashMap);//递归设置该子节点的子节点列表
                    childList.add(childNode);//添加该子节点到对应的根节点列表
                });
        node.setChildren(childList);
    }
}
