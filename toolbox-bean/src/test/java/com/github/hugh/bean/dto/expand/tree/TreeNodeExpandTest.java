package com.github.hugh.bean.dto.expand.tree;

import com.github.hugh.bean.expand.tree.TreeNodeExpand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TreeNodeExpand 单元测试
 */
class TreeNodeExpandTest {

    @Test
    @DisplayName("测试树节点扩展结构 - 模拟设备拓扑树")
    void testTreeNodeExpandStructure() {
        // 1. 定义扩展业务数据 (以 String 或自定义对象为例)
        String gatewayInfo = "{\"ip\":\"192.168.1.1\", \"model\":\"GW-01\"}";
        String sensorInfo = "{\"type\":\"Temperature\", \"sn\":\"SN123\"}";

        // 2. 使用全参构造函数创建子节点
        TreeNodeExpand<String> childNode = new TreeNodeExpand<>(
                "sensor_01", "gw_01", "温度传感器", new ArrayList<>(), sensorInfo);

        List<TreeNodeExpand<String>> children = new ArrayList<>();
        children.add(childNode);

        // 3. 创建父节点
        TreeNodeExpand<String> rootNode = new TreeNodeExpand<>(
                "gw_01", "0", "核心网关", children, gatewayInfo);

        // 4. 断言校验
        assertAll("节点数据校验",
                () -> assertEquals("gw_01", rootNode.getId()),
                () -> assertEquals(gatewayInfo, rootNode.getExpand(), "父节点扩展数据应匹配"),
                () -> assertEquals(1, rootNode.getChildren().size(), "子节点数量应为1"),
                () -> assertEquals("sensor_01", rootNode.getChildren().get(0).getId()),
                () -> assertEquals(sensorInfo, rootNode.getChildren().get(0).getExpand(), "子节点扩展数据应匹配")
        );
    }

    @Test
    @DisplayName("测试 Equals 包含父类属性校验")
    void testEqualsWithSuper() {
        // 创建两个 ID 相同但 expand 不同的节点
        TreeNodeExpand<String> node1 = new TreeNodeExpand<>();
        node1.setId("100");
        node1.setExpand("DataA");

        TreeNodeExpand<String> node2 = new TreeNodeExpand<>();
        node2.setId("100");
        node2.setExpand("DataA");

        TreeNodeExpand<String> node3 = new TreeNodeExpand<>();
        node3.setId("200"); // ID 不同
        node3.setExpand("DataA");

        assertAll("相等性校验",
                () -> assertEquals(node1, node2, "ID 和 Expand 都相同时应相等"),
                () -> assertNotEquals(node1, node3, "父类 ID 不同时应不相等（依赖 callSuper=true）")
        );
    }
}
