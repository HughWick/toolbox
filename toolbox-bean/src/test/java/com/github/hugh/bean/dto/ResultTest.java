package com.github.hugh.bean.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 返回数据实体接口来
 *
 * @author AS
 * @date 2021/2/1 9:52
 */
class ResultTest {

    @Test
    void test01() {
        String code1 = "0000";
        String message1=  "success";
        ResultDTO dto1 = new ResultDTO(code1, message1);
        assertTrue(dto1.equalCode(code1));
        assertFalse(dto1.notEqualCode(code1));
        ResultDTO<Object> build = ResultDTO.builder().code(code1).message(message1).timestamp(System.currentTimeMillis()).build();
        System.out.println(build);

    }

    // 测试无参构造
    @Test
    void test02() {
        ResultDTO dto2 = new ResultDTO<>();
        assertNull(dto2.getMessage());
        assertNull(dto2.getCode());
        assertFalse(dto2.equalCode("0000"));
        assertNull(dto2.getData());
    }

    @Test
    void defaultConstructorTest() {
        ResultDTO<String> resultDTO = new ResultDTO<>();
        // 验证 timestamp 是否已初始化 (接近当前时间)
        long currentTimeMillis = System.currentTimeMillis();
        assertTrue(resultDTO.getTimestamp() <= currentTimeMillis);
        assertTrue(resultDTO.getTimestamp() > currentTimeMillis - 100); // 允许 100ms 的误差，因为测试执行时间和获取时间戳可能存在细微差别
        // 验证 code, message, data 字段是否为默认值 (null)
        assertNull(resultDTO.getCode());
        assertNull(resultDTO.getMessage());
        assertNull(resultDTO.getData());
    }

    @Test
    void parameterizedConstructorTest() {
        String testCode = "200";
        String testMessage = "请求成功";
        ResultDTO<Integer> resultDTO = new ResultDTO<>(testCode, testMessage);
        // 验证 timestamp 是否已初始化 (接近当前时间)
        long currentTimeMillis = System.currentTimeMillis();
        assertTrue(resultDTO.getTimestamp() <= currentTimeMillis);
        assertTrue(resultDTO.getTimestamp() > currentTimeMillis - 100); // 允许 100ms 的误差
        // 验证 code 和 message 字段是否已正确赋值
        assertEquals(testCode, resultDTO.getCode());
        assertEquals(testMessage, resultDTO.getMessage());
        // 验证 data 字段是否为默认值 (null)
        assertNull(resultDTO.getData());
    }
//    @Test
//    @DisplayName("测试基础数据类型序列化 - String Data")
//    void testToByteArray_BasicString() {
//        // 1. 准备数据
//        ResultDTO<String> original = new ResultDTO<>();
//        original.setCode("200");
//        original.setMessage("Success");
//        original.setTimestamp(System.currentTimeMillis());
//        original.setData("Hello World");
//
//        // 2. 执行序列化 (调用你新增的方法)
//        byte[] bytes = original.toByteArray();
//
//        // 3. 验证结果不为空
//        Assertions.assertNotNull(bytes);
//        Assertions.assertTrue(bytes.length > 0);
//
//        // 4. 执行反序列化并验证内容
//        ResultDTO<String> restored = deserialize(bytes);
//
//        Assertions.assertEquals(original.getCode(), restored.getCode());
//        Assertions.assertEquals(original.getMessage(), restored.getMessage());
//        Assertions.assertEquals(original.getTimestamp(), restored.getTimestamp());
//        Assertions.assertEquals(original.getData(), restored.getData());
//    }
//
//    @Test
//    @DisplayName("测试复杂泛型序列化 - List<Map> Data")
//    void testToByteArray_ComplexList() {
//        // 1. 准备复杂数据 (模拟数据库查询结果)
//        ResultDTO<List<Map<String, Object>>> original = new ResultDTO<>();
//        original.setCode("0");
//        original.setMessage("Query OK");
//        original.setTimestamp(System.currentTimeMillis());
//
//        List<Map<String, Object>> list = new ArrayList<>();
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("id", 101);
//        map1.put("name", "Toolbox");
//        list.add(map1);
//
//        original.setData(list);
//
//        // 2. 序列化与反序列化
//        byte[] bytes = original.toByteArray();
//        ResultDTO<List<Map<String, Object>>> restored = deserialize(bytes);
//
//        // 3. 验证深层内容
//        Assertions.assertEquals(1, restored.getData().size());
//        Assertions.assertEquals(101, restored.getData().get(0).get("id"));
//        Assertions.assertEquals("Toolbox", restored.getData().get(0).get("name"));
//    }
//
//    @Test
//    @DisplayName("测试 Null 值安全性 (防止 NullPointerException)")
//    void testToByteArray_WithNullFields() {
//        // 1. 准备包含 Null 的对象
//        ResultDTO<String> original = new ResultDTO<>();
//        original.setCode(null);    // 测试 code 为 null
//        original.setMessage(null); // 测试 message 为 null
//        original.setData(null);    // 测试 data 为 null
//        original.setTimestamp(123456789L);
//
//        // 2. 执行序列化
//        // 如果你的 writeExternal 中使用了 out.writeUTF(code)，这里可能会抛出异常
//        // 除非你加了判空逻辑：out.writeUTF(code == null ? "" : code)
//        byte[] bytes = original.toByteArray();
//
//        Assertions.assertNotNull(bytes);
//
//        // 3. 反序列化验证
//        ResultDTO<String> restored = deserialize(bytes);
//
//        // 4. 验证逻辑 (根据你的实现调整预期)
//        // 假设实现里做了空转空字符串处理:
//        Assertions.assertTrue(restored.getCode() == null || restored.getCode().isEmpty(),
//                "Code 应该是 null 或者空字符串");
//
//        Assertions.assertNull(restored.getData());
//        Assertions.assertEquals(123456789L, restored.getTimestamp());
//    }
//
//    @Test
//    @DisplayName("测试大数据量效率")
//    void testPerformance_LargeData() {
//        ResultDTO<List<String>> original = new ResultDTO<>();
//        original.setCode("200");
//        original.setMessage("Large Payload");
//        original.setTimestamp(System.currentTimeMillis());
//
//        // 生成 10000 条数据
//        List<String> bigList = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            bigList.add("Item-" + i);
//        }
//        original.setData(bigList);
//
//        long start = System.currentTimeMillis();
//        byte[] bytes = original.toByteArray();
//        long end = System.currentTimeMillis();
//
//        System.out.println("10000条String数据序列化耗时: " + (end - start) + "ms, 大小: " + bytes.length + " bytes");
//
//        Assertions.assertNotNull(bytes);
//
//        ResultDTO<List<String>> restored = deserialize(bytes);
//        Assertions.assertEquals(10000, restored.getData().size());
//        Assertions.assertEquals("Item-9999", restored.getData().get(9999));
//    }
//
//    /**
//     * 测试辅助工具：将字节数组反序列化为 ResultDTO
//     * (模拟接收端)
//     */
//    @SuppressWarnings("unchecked")
//    private <T> ResultDTO<T> deserialize(byte[] bytes) {
//        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//             ObjectInputStream ois = new ObjectInputStream(bais)) {
//            return (ResultDTO<T>) ois.readObject();
//        } catch (Exception e) {
//            throw new RuntimeException("反序列化失败", e);
//        }
//    }
//    @Test
//    @DisplayName("覆盖 fromByteArray - 基础String类型")
//    void testSerialization_StringData() {
//        // 1. 准备数据
//        ResultDTO<String> original = new ResultDTO<>();
//        original.setCode("200");
//        original.setMessage("Success");
//        original.setTimestamp(System.currentTimeMillis());
//        original.setData("Hello World");
//        // 2. 序列化
//        byte[] bytes = original.toByteArray();
//        // 3. 反序列化 - 【关键点】直接调用静态方法 ResultDTO.fromByteArray
//        ResultDTO<String> restored = ResultDTO.fromByteArray(bytes);
//        // 4. 断言验证
//        Assertions.assertNotNull(restored);
//        Assertions.assertEquals(original.getCode(), restored.getCode());
//        Assertions.assertEquals(original.getMessage(), restored.getMessage());
//        Assertions.assertEquals(original.getTimestamp(), restored.getTimestamp());
//        Assertions.assertEquals(original.getData(), restored.getData());
//    }
//
//    @Test
//    @DisplayName("覆盖 fromByteArray - 复杂集合类型 List<Map>")
//    void testSerialization_ComplexData() {
//        // 1. 准备数据
//        ResultDTO<List<Map<String, Object>>> original = new ResultDTO<>();
//        original.setCode("0");
//        original.setMessage("OK");
//        original.setTimestamp(System.currentTimeMillis());
//        List<Map<String, Object>> list = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("key", "value");
//        map.put("num", 123);
//        list.add(map);
//        original.setData(list);
//        // 2. 序列化
//        byte[] bytes = original.toByteArray();
//        // 3. 反序列化 - 【关键点】直接调用静态方法
//        ResultDTO<List<Map<String, Object>>> restored = ResultDTO.fromByteArray(bytes);
//        // 4. 验证
//        Assertions.assertEquals(1, restored.getData().size());
//        Assertions.assertEquals("value", restored.getData().get(0).get("key"));
//    }
//
//    @Test
//    @DisplayName("覆盖 fromByteArray - 异常处理分支")
//    void testFromByteArray_Exception() {
//        // 构造一个损坏的/无效的字节数组
//        byte[] invalidBytes = new byte[]{1, 2, 3, 4, 5};
//        // 验证调用 fromByteArray 时是否会抛出 RuntimeException
//        // 对应你代码中的 catch(Exception e) { throw new RuntimeException(..., e); }
//        Assertions.assertThrows(RuntimeException.class, () -> {
//            ResultDTO.fromByteArray(invalidBytes);
//        }, "当传入无效字节流时，应该抛出运行时异常");
//    }
//
//    @Test
//    @DisplayName("覆盖 Null 字段场景")
//    void testSerialization_NullFields() {
//        ResultDTO<Integer> original = new ResultDTO<>();
//        // 故意不设置 code, message, data，保持为 null
//        original.setTimestamp(123456L);
//
//        byte[] bytes = original.toByteArray();
//
//        // 反序列化
//        ResultDTO<Integer> restored = ResultDTO.fromByteArray(bytes);
//
//        Assertions.assertNotNull(restored);
//        // 如果你的 writeExternal 处理了 null 转空字符串，这里就是 isEmpty
//        // 如果直接 writeObject 写 null，这里就是 assertNull
//        // 根据你的实现，这里断言不报错即可
//        Assertions.assertEquals(original.getTimestamp(), restored.getTimestamp());
//    }
}
