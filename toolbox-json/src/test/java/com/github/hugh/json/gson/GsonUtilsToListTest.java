package com.github.hugh.json.gson;

import com.github.hugh.json.model.Student;
import com.github.hugh.util.DateUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GsonUtilsToListTest {
    // 测试字符串转list
    @Test
    void testToList_SensorData_FromJsonString() {
        String str = "[{\"action\":\"84\",\"commandKey\":\"ELECTRICAL_ENERGY_DATA_VOLTAGE\",\"commandId\":\"01010001\",\"commandName\":\"电能数据_电压\",\"value\":\"235.45\",\"type\":2,\"unit\":\"V\",\"remake\":\"00005bf9\"}," +
                "{\"action\":\"84\",\"commandKey\":\"ELECTRICAL_ENERGY_DATA_POWER\",\"commandId\":\"01010002\",\"commandName\":\"电能数据_功率\",\"value\":\"1.189\",\"type\":2,\"unit\":\"kW\",\"remake\":\"000004a5\"}," +
                "{\"action\":\"84\",\"commandKey\":\"ELECTRICAL_ENERGY_DATA_CURRENT\",\"commandId\":\"01010003\",\"commandName\":\"电能数据_电流\",\"value\":\"5.083\",\"type\":2,\"unit\":\"A\",\"remake\":\"000013db\"},{\"action\":\"84\",\"commandKey\":\"ELECTRICAL_ENERGY_DATA_TOTAL_ACTIVE_ENERGY\",\"commandId\":\"01010010\",\"commandName\":\"电能数据_有功总电能\",\"value\":\"9.99\",\"type\":2,\"unit\":\"kwh\",\"remake\":\"000003e7\"},{\"action\":\"84\",\"commandKey\":\"INTERNAL_TEMPERATURE\",\"commandId\":\"01020001\",\"commandName\":\"ADC数据-内部温度\",\"value\":\"60.4\",\"type\":2,\"unit\":\"C\",\"remake\":\"0000025c\"}," +
                "{\"action\":\"84\",\"commandKey\":\"BATTERY_VOLTAGE\",\"commandId\":\"01020002\",\"commandName\":\"ADC数据-电池电压\",\"value\":\"4.15\",\"type\":2,\"unit\":\"V\",\"remake\":\"019f\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_1_VOLTAGE\",\"commandId\":\"01020003\",\"commandName\":\"ADC数据-直流通道1电压\",\"value\":\"12.08\",\"type\":2,\"unit\":\"V\",\"remake\":\"04b8\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_1_CURRENT\",\"commandId\":\"01020004\",\"commandName\":\"ADC数据-直流通道1电流\",\"value\":\"0\",\"type\":2,\"unit\":\"mA\",\"remake\":\"0000\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_2_VOLTAGE\",\"commandId\":\"01020005\",\"commandName\":\"ADC数据-直流通道2电压\",\"value\":\"12.08\",\"type\":2,\"unit\":\"V\",\"remake\":\"04b8\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_2_CURRENT\",\"commandId\":\"01020006\",\"commandName\":\"ADC数据-直流通道2电流\",\"value\":\"0\",\"type\":2,\"unit\":\"mA\",\"remake\":\"0000\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_3_VOLTAGE\",\"commandId\":\"01020007\",\"commandName\":\"ADC数据-直流通道3电压\",\"value\":\"12.09\",\"type\":2,\"unit\":\"V\",\"remake\":\"04b9\"}," +
                "{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_3_CURRENT\",\"commandId\":\"01020008\",\"commandName\":\"ADC数据-直流通道3电流\",\"value\":\"0\",\"type\":2,\"unit\":\"mA\",\"remake\":\"0000\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_4_VOLTAGE\",\"commandId\":\"01020009\",\"commandName\":\"ADC数据-直流通道4电压\",\"value\":\"0.22\",\"type\":2,\"unit\":\"V\",\"remake\":\"0016\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_4_CURRENT\",\"commandId\":\"0102000a\",\"commandName\":\"ADC数据-直流通道4电流\",\"value\":\"0\",\"type\":2,\"unit\":\"mA\",\"remake\":\"0000\"},{\"action\":\"84\",\"commandKey\":\"NET_NODE_STATUS\",\"commandId\":\"01020019\",\"commandName\":\"ADC数据-Net节点状态\",\"value\":\"0000000000111111\",\"type\":2,\"unit\":\"\",\"remake\":\"003f\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_1_RELAY_ACTUAL_STATE\",\"commandId\":\"010f0005\",\"commandName\":\"交流通道1继电器实际状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_2_RELAY_ACTUAL_STATE\",\"commandId\":\"010f0006\",\"commandName\":\"交流通道2继电器实际状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_3_RELAY_ACTUAL_STATE\",\"commandId\":\"010f0007\",\"commandName\":\"交流通道3继电器实际状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_4_RELAY_ACTUAL_STATE\",\"commandId\":\"010f0008\",\"commandName\":\"交流通道4继电器实际状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_1_STATUS\",\"commandId\":\"0102000f\",\"commandName\":\"ADC数据-直流通道1状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_2_STATUS\",\"commandId\":\"01020010\",\"commandName\":\"ADC数据-直流通道2状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_3_STATUS\",\"commandId\":\"01020011\",\"commandName\":\"ADC数据-直流通道3状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_4_STATUS\",\"commandId\":\"01020012\",\"commandName\":\"ADC数据-直流通道4状态\",\"value\":\"2\",\"type\":2,\"unit\":\"\",\"remake\":\"02\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_5_STATUS\",\"commandId\":\"01020013\",\"commandName\":\"ADC数据-直流通道5状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_6_STATUS\",\"commandId\":\"01020014\",\"commandName\":\"ADC数据-直流通道6状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"INPUT_SIGNAL_1_STATUS\",\"commandId\":\"010f0001\",\"commandName\":\"输入信号1状态（开关门状态）\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"GPRS_SIGNAL_STRENGTH\",\"commandId\":\"01050001\",\"commandName\":\"GPRS数据-信号强度\",\"value\":\"24\",\"type\":2,\"unit\":\"\",\"remake\":\"24\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_1_RMS_CURRENT\",\"commandId\":\"10000001\",\"commandName\":\"交流通道1电流有效值\",\"value\":\"4.672\",\"type\":2,\"unit\":\"A\",\"remake\":\"00001240\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_2_RMS_CURRENT\",\"commandId\":\"10000002\",\"commandName\":\"交流通道2电流有效值\",\"value\":\"0.16\",\"type\":2,\"unit\":\"A\",\"remake\":\"000000a0\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_3_RMS_CURRENT\",\"commandId\":\"10000003\",\"commandName\":\"交流通道3电流有效值\",\"value\":\"0.075\",\"type\":2,\"unit\":\"A\",\"remake\":\"0000004b\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_4_RMS_CURRENT\",\"commandId\":\"10000004\",\"commandName\":\"交流通道4电流有效值\",\"value\":\"0.153\",\"type\":2,\"unit\":\"A\",\"remake\":\"00000099\"}]";
        JsonArray jsonArray = GsonUtils.parseArray(str);
        assertNotNull(jsonArray);
        assertEquals(31, jsonArray.size());

    }

    @Test
    @DisplayName("测试将JsonArray转换为List<String>")
    void testToArrayListOfString() {
        String jsonString = "[\"apple\", \"banana\", \"cherry\"]";
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        List<String> expectedList = Arrays.asList("apple", "banana", "cherry");
        List<String> actualList = GsonUtils.toArrayList(jsonArray, String.class);
        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);

        String str2 = "[{\"serialNo\":\"1339497989051277312\",\"createBy\":1,\"createDate\":1608196182000,\"updateBy\":\"xxxx\",\"updateDate\":1615444156000,\"name\":\"张三\"}]";
        List<Map<String, Object>> objectList = GsonUtils.toArrayList(str2);
//        Object firstElement = actualList.get(0);
        Map<String, Object> actualMap = objectList.get(0);
        assertNotNull(actualMap);
        // 验证 Map 中的内容
        assertEquals("1339497989051277312", actualMap.get("serialNo"));
        assertEquals(1, actualMap.get("createBy")); // Gson 默认将整数解析为 double
        assertEquals(1608196182000L, actualMap.get("createDate")); // Gson 默认将 long 解析为 double
        assertEquals("xxxx", actualMap.get("updateBy"));
        assertEquals(1615444156000L, actualMap.get("updateDate"));
        assertEquals("张三", actualMap.get("name"));
    }

    @Test
    @DisplayName("测试将JsonArray转换为List<Integer>")
    void testToArrayListOfInteger() {
        String jsonString = "[10, 20, 30, 40]";
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();

        List<Integer> expectedList = Arrays.asList(10, 20, 30, 40);
        List<Integer> actualList = GsonUtils.toArrayList(jsonArray, Integer.class);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("测试将JsonArray转换为List<User> (自定义对象)")
    void testToArrayListOfCustomObject() {
        String jsonString = "[{\"name\":\"Alice\",\"age\":25,\"city\":\"New York\"}, {\"name\":\"Bob\",\"age\":30,\"city\":\"London\"}]";
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();

        Student user1 = new Student();
        user1.setName("Alice");
        user1.setAge(25);
        user1.setCity("New York");

        Student user2 = new Student();
        user2.setName("Bob");
        user2.setAge(30);
        user2.setCity("London");

        List<Student> expectedList = Arrays.asList(user1, user2);
        List<Student> actualList = GsonUtils.toArrayList(jsonArray, Student.class);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        // 因为User类重写了equals和hashCode方法，可以直接比较列表
        assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("测试将JsonArray转换为List<Map<String, Object>> (带MapTypeAdapter)")
    void testToArrayListOfMap() {
        // 注意：这里我们使用 MapTypeAdapter，因此Gson会尝试将JSON对象反序列化为Map<String, Object>
        String jsonString = "[{\"id\":\"A001\",\"data\":{\"prop1\":1,\"prop2\":\"value1\"}}, {\"id\":\"B002\",\"data\":{\"propA\":true,\"propB\":123.45}}]";
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();

        // 预期结果的构建方式需要和 MapTypeAdapter 的read逻辑一致
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", "A001");
        Map<String, Object> innerMap1 = new HashMap<>();
        innerMap1.put("prop1", 1.0); // Gson会将整数默认为double
        innerMap1.put("prop2", "value1");
        map1.put("data", innerMap1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", "B002");
        Map<String, Object> innerMap2 = new HashMap<>();
        innerMap2.put("propA", true);
        innerMap2.put("propB", 123.45);
        map2.put("data", innerMap2);


        List<Map<String, Object>> expectedList = new ArrayList<>();
        expectedList.add(map1);
        expectedList.add(map2);

        // 调用方法，传入 Map.class
        List<Map<String, Object>> actualList = GsonUtils.toArrayList(jsonArray, (Class<Map<String, Object>>) (Class<?>) Map.class);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        // 由于Map可能包含复杂类型，直接比较Map可能需要deepEquals
        // 或者遍历比较每个Map的键值对
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i).get("id"), actualList.get(i).get("id"));
            // 对于嵌套的Map，需要再次进行断言
            assertTrue(areMapsEqual((Map<String, Object>) expectedList.get(i).get("data"), (Map<String, Object>) actualList.get(i).get("data")));
        }
    }

    // 辅助方法，用于比较两个Map是否相等（考虑到浮点数等）
    private boolean areMapsEqual(Map<String, Object> map1, Map<String, Object> map2) {
        if (map1.size() != map2.size()) return false;
        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            String key = entry.getKey();
            Object val1 = entry.getValue();
            Object val2 = map2.get(key);

            if (val1 instanceof Number && val2 instanceof Number) {
                if (((Number) val1).doubleValue() != ((Number) val2).doubleValue()) return false;
            } else if (!java.util.Objects.equals(val1, val2)) {
                return false;
            }
        }
        return true;
    }


    @Test
    @DisplayName("测试空JsonArray")
    void testToArrayListOfEmptyArray() {
        JsonArray jsonArray = new JsonArray(); // 空数组
        List<String> actualList = GsonUtils.toArrayList(jsonArray, String.class);
        assertNotNull(actualList);
        assertTrue(actualList.isEmpty());
    }

//    @Test
//    @DisplayName("测试null JsonArray")
//    void testToArrayListOfNullArray() {
//        JsonArray jsonArray = null;
//        List<String> actualList = GsonUtils.toArrayList(jsonArray, String.class);
//        assertNotNull(actualList);
//        assertTrue(actualList.isEmpty());
//    }

    @Test
    @DisplayName("测试包含null元素的JsonArray")
    void testToArrayListWithNullElements() {
        String jsonString = "[\"item1\", null, \"item3\"]";
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        List<String> expectedList = Arrays.asList("item1", null, "item3");
        List<String> actualList = GsonUtils.toArrayList(jsonArray, String.class);
        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("测试将JsonArray转换为List<Student> - 完整数据")
    void testToArrayListOfStudent_fullData() {
        // 创建 JSON 字符串
        String jsonString = "[{\"id\":1,\"age\":20,\"name\":\"Alice\",\"amount\":100.5,\"birthday\":\"2022-05-12 21:32:00\",\"create\":\"2024-05-27 09:22:11\",\"city\":\"New York\",\"sex\":\"female\",\"system\":100,\"account\":\"alice_acc\",\"accountName\":\"Alice Account\",\"accountType\":\"Savings\",\"password\":\"pass123\",\"phone\":\"1234567890\",\"phoneType\":\"Mobile\",\"ip\":\"192.168.1.1\",\"role\":\"student\",\"authorization\":\"read,write\"}," +
                "{\"id\":2,\"age\":22,\"name\":\"Bob\",\"amount\":200.75,\"birthday\":\"1999-01-23 00:32:11\",\"create\":\"2024-05-27 09:11:22\",\"city\":\"London\",\"sex\":\"male\",\"system\":101,\"account\":\"bob_acc\",\"accountName\":\"Bob Account\",\"accountType\":\"Checking\",\"password\":\"pass456\",\"phone\":\"0987654321\",\"phoneType\":\"Home\",\"ip\":\"192.168.1.2\",\"role\":\"student\",\"authorization\":\"read\"}]";
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        // 构造预期 Student 对象
        Student student1 = new Student();
        student1.setId(1);
        student1.setAge(20);
        student1.setName("Alice");
        student1.setAmount(100.5);
        student1.setBirthday(DateUtils.parse("2022-05-12 21:32:00"));
        student1.setCreate(DateUtils.parse("2024-05-27 09:22:11"));
        student1.setCity("New York");
        student1.setSex("female");
        student1.setSystem(100);
        student1.setAccount("alice_acc");
        student1.setAccountName("Alice Account");
        student1.setAccountType("Savings");
        student1.setPassword("pass123");
        student1.setPhone("1234567890");
        student1.setPhoneType("Mobile");
        student1.setIp("192.168.1.1");
        student1.setRole("student");
        student1.setAuthorization("read,write");

        Student student2 = new Student();
        student2.setId(2);
        student2.setAge(22);
        student2.setName("Bob");
        student2.setAmount(200.75);
        student2.setBirthday(DateUtils.parse("1999-01-23 00:32:11"));
        student2.setCreate(DateUtils.parse("2024-05-27 09:11:22"));
        student2.setCity("London");
        student2.setSex("male");
        student2.setSystem(101);
        student2.setAccount("bob_acc");
        student2.setAccountName("Bob Account");
        student2.setAccountType("Checking");
        student2.setPassword("pass456");
        student2.setPhone("0987654321");
        student2.setPhoneType("Home");
        student2.setIp("192.168.1.2");
        student2.setRole("student");
        student2.setAuthorization("read");
        List<Student> expectedList = Arrays.asList(student1, student2);
        List<Student> actualList = GsonUtils.toArrayList(jsonArray, Student.class);
        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList); // 依赖 Student 的 equals 和 hashCode
    }

    @Test
    @DisplayName("测试将JsonArray转换为List<Student> - 部分字段缺失")
    void testToArrayListOfStudent_partialData() {
        String jsonString = "[{\"id\":3,\"name\":\"Charlie\",\"age\":21,\"city\":\"Paris\"}]"; // 缺少部分字段
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();

        Student student = new Student();
        student.setId(3);
        student.setName("Charlie");
        student.setAge(21);
        student.setCity("Paris");
        // 其他字段应为默认值（0 或 null）
        List<Student> expectedList = List.of(student);
        List<Student> actualList = GsonUtils.toArrayList(jsonArray, Student.class);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);

        // 进一步断言缺失字段是否为默认值
        Student actualStudent = actualList.get(0);
        assertEquals(0, actualStudent.getAmount());
        assertNull(actualStudent.getBirthday());
        assertNull(actualStudent.getCreate());
        assertNull(actualStudent.getSex());
        // ... 对所有缺失字段进行断言
    }

    @Test
    @DisplayName("测试空JsonArray")
    void testToArrayListOfStudent_emptyArray() {
        JsonArray jsonArray = new JsonArray(); // 空数组
        List<Student> actualList = GsonUtils.toArrayList(jsonArray, Student.class);
        assertNotNull(actualList);
        assertTrue(actualList.isEmpty());
    }

//    @Test
//    @DisplayName("测试null JsonArray")
//    void testToArrayListOfStudent_nullArray() {
//        List<Student> actualList = GsonUtils.toArrayList(null, Student.class);
//        assertNotNull(actualList);
//        assertTrue(actualList.isEmpty());
//    }

    @Test
    @DisplayName("测试包含null元素的JsonArray")
    void testToArrayListOfStudent_withNullElements() {
        // 包含一个 null 对象
        String jsonString = "[{\"id\":4,\"name\":\"David\"}, null, {\"id\":5,\"name\":\"Eve\"}]";
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();

        Student student1 = new Student();
        student1.setId(4);
        student1.setName("David");

        Student student3 = new Student();
        student3.setId(5);
        student3.setName("Eve");

        List<Student> expectedList = new ArrayList<>();
        expectedList.add(student1);
        expectedList.add(null); // Gson 会将 JSON null 反序列化为 Java null
        expectedList.add(student3);

        List<Student> actualList = GsonUtils.toArrayList(jsonArray, Student.class);

        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("测试JsonArray包含不匹配的类型元素")
    void testToArrayListOfStudent_mismatchedType() {
        // JsonArray 中包含一个非对象元素，Gson 默认会尝试将其设置为 null
        String jsonString = "[{\"id\":6,\"name\":\"Frank\"}, null, {\"id\":7,\"name\":\"Grace\"}]";
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();

        Student student1 = new Student();
        student1.setId(6);
        student1.setName("Frank");

        Student student3 = new Student();
        student3.setId(7);
        student3.setName("Grace");

        List<Student> expectedList = new ArrayList<>();
        expectedList.add(student1);
        expectedList.add(null); // "not_an_object" 无法反序列化为 Student，默认为 null
        expectedList.add(student3);
        List<Student> actualList = GsonUtils.toArrayList(jsonArray, Student.class);
        assertNotNull(actualList);
        assertEquals(expectedList.size(), actualList.size());
        assertEquals(expectedList, actualList);
    }

}
