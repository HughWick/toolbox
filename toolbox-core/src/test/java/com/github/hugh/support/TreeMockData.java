package com.github.hugh.support;

import com.github.hugh.bean.expand.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 为树结构测试提供模拟数据。
 */
public class TreeMockData {

    /**
     * 创建一套用于测试的 TreeNode 模拟数据。
     * <p>
     * 数据结构：
     * <ul>
     *   <li>(Root) 公司总部 (100)
     *     <ul>
     *       <li>(L2) 人力资源部 (1001)
     *         <ul>
     *           <li>(L3) 招聘组 (100101)</li>
     *         </ul>
     *       </li>
     *       <li>(L2) 研发部 (1002)
     *         <ul>
     *           <li>(L3) 前端团队 (100201)</li>
     *           <li>(L3) 质检团队 (100202)</li>
     *           <li>(L3) 后端团队 (100203)</li>
     *         </ul>
     *       </li>
     *       <li>(L2) 销售部 (1003) - (无子节点)</li>
     *     </ul>
     *   </li>
     *   <li>(Root) 归档项目 (200)
     *     <ul>
     *       <li>(L2) 凤凰项目 (200001)</li>
     *     </ul>
     *   </li>
     * </ul>
     * <b>注意:</b> 节点被故意以乱序添加，以确保排序测试的有效性。
     *
     * @return 一个包含 "roots" 和 "children" 列表的 Map。
     */
    public static Map<String, List<TreeNode>> createMockData() {
        List<TreeNode> rootList = new ArrayList<>();
        List<TreeNode> childList = new ArrayList<>();
        // --- 添加根节点 (故意乱序添加) ---
        rootList.add(new TreeNode("200", null, "归档项目", null));
        rootList.add(new TreeNode("100", null, "公司总部", null));

        // --- 添加 "公司总部" (100) 的子节点 (故意乱序添加) ---
        childList.add(new TreeNode("1002", "100", "研发部", null));
        childList.add(new TreeNode("1001", "100", "人力资源部", null));
        childList.add(new TreeNode("1003", "100", "销售部", null));

        // --- 添加 "研发部" (1002) 的子节点 (故意乱序添加) ---
        childList.add(new TreeNode("100203", "1002", "后端团队", null));
        childList.add(new TreeNode("100201", "1002", "前端团队", null));
        childList.add(new TreeNode("100202", "1002", "质检团队", null));

        // --- 添加 "人力资源部" (1001) 的子节点 ---
        childList.add(new TreeNode("100101", "1001", "招聘组", null));

        // --- 添加 "归档项目" (200) 的子节点 ---
        childList.add(new TreeNode("200001", "200", "凤凰项目", null));

        return Map.of("roots", rootList, "children", childList);
    }

    /**
     * 注意：如果您的测试需要 TreeNodeExpand<String> 类型，
     * 您可以创建另一个类似的方法，或者在使用此方法后进行一次类型转换。
     * 为简化，这里以基础的 TreeNode 为例。
     */
}
