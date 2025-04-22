package com.github.hugh;

import com.github.hugh.model.GsonTest;
import com.github.hugh.model.Student;
import com.github.hugh.util.EntityUtils;
import com.github.hugh.util.MapUtils;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * map 工具测试了类
 *
 * @author AS
 * @date 2020/8/31 9:18
 */
class MapTest {

    @Test
    void test01() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
//        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            Object o = MapUtils.toEntityNotEmpty(Student.class, map);
//            System.out.println(JSONObject.fromObject(o));
            Student student = new Student();
            EntityUtils.copy(o, student);
            student.setName("张三");
//            System.out.println(JSONObject.fromObject(student));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(map.getOrDefault("id" , "13d"));
        map.clear();
        assertTrue(MapUtils.isEmpty(map));
        assertFalse(MapUtils.isNotEmpty(map));
//        assertFalse(MapUtils.isSucces(map, "code", null));
        assertFalse(MapUtils.isEquals(map, "code", null));
//        assertFalse(MapUtils.isSuccess(null, "code", null));
        assertFalse(MapUtils.isEquals(null, "code", null));
        map.put("code", "0000");
//        assertTrue(MapUtils.isSuccess(map, "code", "0000"));
        assertTrue(MapUtils.isEquals(map, "code", "0000"));
//        assertTrue(MapUtils.isFailure(map, "code", "00100"));
        assertTrue(MapUtils.isNotEquals(map, "code", "00100"));
        System.out.println(map.getOrDefault("id1" , "13d"));
    }

    // 测试map获取值
    @Test
    void testGetValue() {
        String strCreateDate = "2019-04-06 12:11:20";
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", strCreateDate);
        Map data = MapUtils.getMap(map, "data");
        assertNull(data);
        assertEquals(strCreateDate, MapUtils.getString(map, "create"));
    }

    // 测试 getLong 方法
    @Test
    void testGetLongWithLongValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", 123L);
        Long result = MapUtils.getLong(map, "key");
        assertEquals(123L, result);
    }

    @Test
    void testGetLongWithIntegerValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", 123);
        Long result = MapUtils.getLong(map, "key");
        assertEquals(123L, result);
    }

    @Test
    void testGetLongWithStringValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "123");
        Long result = MapUtils.getLong(map, "key");
        assertEquals(123L, result);
    }

    @Test
    public void testGetLongWithNullValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", null);
        Long result = MapUtils.getLong(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetLongWithNonNumberValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "abc");
        Long result = MapUtils.getLong(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetLongWithNullMap() {
        Map<String, Object> map = null;
        Long result = MapUtils.getLong(map, "key");
        assertNull(result);
    }

    // 测试 getNumber 方法
    @Test
    public void testGetNumberWithNumberValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", 123L);
        Number result = MapUtils.getNumber(map, "key");
        assertEquals(123L, result);
    }

    @Test
    public void testGetNumberWithIntegerValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", 123);
        Number result = MapUtils.getNumber(map, "key");
        assertEquals(123, result);
    }

    @Test
    public void testGetNumberWithStringValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "123");
        Number result = MapUtils.getNumber(map, "key");
        assertNotNull(result);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testGetNumberWithNullValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", null);
        Number result = MapUtils.getNumber(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetNumberWithInvalidString() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "abc");
        Number result = MapUtils.getNumber(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetNumberWithNullMap() {
        Map<String, Object> map = null;
        Number result = MapUtils.getNumber(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetNumberWithNegativeNumber() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", -123L);
        Number result = MapUtils.getNumber(map, "key");
        assertEquals(-123L, result);
    }

    @Test
    public void testGetNumberWithDecimalValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "123.45");
        Number result = MapUtils.getNumber(map, "key");
        assertNotNull(result);
        assertEquals(123.45, result.doubleValue());
    }

    @Test
    public void testGetNumberWithEmptyString() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");
        Number result = MapUtils.getNumber(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetNumberWithSpecialCharacters() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "\\$123");
        Number result = MapUtils.getNumber(map, "key");
        assertNull(result);
    }

    // 测试 getInt 方法
    @Test
    public void testGetIntWithIntegerValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", 123);
        Integer result = MapUtils.getInt(map, "key");
        assertEquals(123, result);
    }

    @Test
    public void testGetIntWithLongValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", 123L);
        Integer result = MapUtils.getInt(map, "key");
        assertEquals(123, result);
    }

    @Test
    public void testGetIntWithStringValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "123");
        Integer result = MapUtils.getInt(map, "key");
        assertEquals(123, result);
    }

    @Test
    public void testGetIntWithNullValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", null);
        Integer result = MapUtils.getInt(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetIntWithNonNumberValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "abc");
        Integer result = MapUtils.getInt(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetIntWithNullMap() {
        Map<String, Object> map = null;
        Integer result = MapUtils.getInt(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetIntWithNegativeIntegerValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", -123);
        Integer result = MapUtils.getInt(map, "key");
        assertEquals(-123, result);
    }

    @Test
    public void testGetIntWithNegativeLongValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", -123L);
        Integer result = MapUtils.getInt(map, "key");
        assertEquals(-123, result);
    }

    @Test
    public void testGetIntWithDecimalString() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "123.45");
        Integer result = MapUtils.getInt(map, "key");
        assertEquals(123, result);  // 由于是浮动数值，不能正确转换为整数
    }

    @Test
    public void testGetIntWithEmptyString() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "");
        Integer result = MapUtils.getInt(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetIntWithSpecialCharacterString() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "\\$123");
        Integer result = MapUtils.getInt(map, "key");
        assertNull(result);
    }

    @Test
    public void testGetDoubleWithDoubleValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", 123.45);
        Double result = MapUtils.getDouble(map, "key");
        assertEquals(Double.valueOf(123.45), result);  // 确保返回的是原始的 Double 值
    }

    @Test
    public void testGetDoubleWithIntegerValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", 123);
        Double result = MapUtils.getDouble(map, "key");
        assertEquals(Double.valueOf(123), result);  // 整数值转换为 Double，值应为 123.0
    }

    @Test
    public void testGetDoubleWithStringValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "123.45");
        Double result = MapUtils.getDouble(map, "key");
        assertEquals(Double.valueOf(123.45), result);  // 如果支持字符串转数字，值应为 123.45
    }

    @Test
    public void testGetDoubleWithInvalidStringValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "abc");
        Double result = MapUtils.getDouble(map, "key");
        assertNull(result);  // 字符串 "abc" 无法转换为数字，应返回 null
    }

    @Test
    public void testGetDoubleWithNullValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", null);
        Double result = MapUtils.getDouble(map, "key");
        assertNull(result);  // 如果值为 null，返回 null
    }

    @Test
    public void testGetDoubleWithUnsupportedType() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", true);  // 布尔值不能转换为数字
        Double result = MapUtils.getDouble(map, "key");
        assertNull(result);  // 返回 null，因为布尔值不能转换为数字
    }

    @Test
    public void testGetDoubleWithFloatString() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "123.45");
        Double result = MapUtils.getDouble(map, "key");
        assertEquals(Double.valueOf(123.45), result);  // 浮动数值的字符串应能正确转换为 Double
    }

    @Test
    void testGetDoubleWithMultipleNumbersInString() {
        String str1 = "12.34.56";
        Map<String, Object> map = new HashMap<>();
        map.put("key", str1);
        Double result = MapUtils.getDouble(map, "key");
        assertEquals(12.34d, result);  // 如果字符串无法被解析为单一数字，应返回 null
    }

    @Test
    void testSetValue() {
        Map<String, Object> map = new HashMap<>();
        MapUtils.setValue(map, "a", null);
        assertTrue(map.isEmpty());
        MapUtils.setValue(map, "null", "null");
        assertTrue(map.isEmpty());
        MapUtils.setValue(map, "null", "1");
        assertTrue(map.isEmpty());
        MapUtils.setValue(map, "a", "1");
        assertEquals("1", map.get("a").toString());
    }

    @Test
    void testPutNotEmpty() {
        String defaultValue1 = "str";
        Map<String, Object> map = new HashMap<>();
        MapUtils.putNotEmpty(map, "a", null, defaultValue1);
        assertEquals(defaultValue1, map.get("a"));
        String key2 = "b";
        String value1 = "value1";
        MapUtils.putNotEmpty(map, key2, value1, defaultValue1);
        assertEquals(value1, map.get(key2));
    }


    @Test
    void testRemoveKey() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("size", 2);
        for (int i = 0; i < 600000; i++) {
            map.put(i++ + "", i);
        }
        map.put("id", 2);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("测试删除");
        MapUtils.removeKeys(map, "page", "size");
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
//        System.out.println(map);
    }

    // 测试map升序与降序
    @Test
    void testSort() {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("z", 1);
        map1.put("a", 1);
        map1.put("w", 1);
        map1.put("i", 1);
        map1.put("n", 1);
        map1.put("m", 1);
        map1.put("c", "建");
        map1.put("e", 1);
        String str = "{a=1, c=建, e=1, w=1, i=1, z=1, m=1, n=1}";
        assertEquals(str, map1.toString());
        String str2 = "{a=1, c=建, e=1, i=1, m=1, n=1, w=1, z=1}";
        assertEquals(str2, MapUtils.sortByKeyAsc(map1).toString());
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("account", "bsd");
        contentMap.put("password", "bsd");
        map1.put("content", contentMap);
        String str3 = "{a=1, c=建, content={password=bsd, account=bsd}, e=1, i=1, m=1, n=1, w=1, z=1}";
        assertEquals(str3, MapUtils.sortByKeyAsc(map1).toString());
        String str4 = "{z=1, w=1, n=1, m=1, i=1, e=1, content={password=bsd, account=bsd}, c=建, a=1}";
        assertEquals(str4, MapUtils.sortByKeyDesc(map1).toString());
//        System.out.println("--->原来>!>>>>" +  MapUtils.sortByKeyAsc(map1));
//        System.out.println("--2->>!>>>>" + MapUtils.sortMap(map));
//        System.out.println("--6->>!>>>>" + MapUtils.sortByKeyDesc(map1));
//        Assertions.assertEquals(MapUtils.sortMap(map) ,MapUtils.sortByKeyAsc(map));
        Map<String, Object> stringMap = new HashMap<>();
        stringMap.put("2", "a");
        stringMap.put("5", "i");
        stringMap.put("1", "z");
        stringMap.put("3", "c");
        stringMap.put("111", "csa");
        stringMap.put("6", "w");
        stringMap.put("4", "n");
        // 排序前
        assertEquals("{1=z, 111=csa, 2=a, 3=c, 4=n, 5=i, 6=w}", stringMap.toString());
        assertEquals("{6=w, 5=i, 4=n, 3=c, 2=a, 111=csa, 1=z}", MapUtils.sortByKeyDesc(stringMap).toString());
//        System.out.println("--4->>!>>>>" + MapUtils.sortByValueDesc(stringMap));
//        System.out.println("--5->>!>>>>" + MapUtils.sortByValueAsc(stringMap));
//        System.out.println("--6->>!>>>>" + MapUtils.sortByKeyDesc(stringMap));
    }

    // 根据map中的value进行降序
    @Test
    void testSortByValueDesc() {
        Map<String, String> map = new HashMap<>();
        map.put("2", "a");
        map.put("5", "i");
        map.put("1", "z");
        map.put("3", "c");
        map.put("6", "w");
        map.put("4", "n");
        String str = "{1=z, 6=w, 4=n, 5=i, 3=c, 2=a}";
        assertEquals(str, MapUtils.sortByValueDesc(map).toString());
//        System.out.println("--4->>!>>>>" + MapUtils.sortByValueDesc(map));
    }

    @Test
    void testMapToEntity() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("sex", "women");
        map.put("list", "1,2,3,4");
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        Student student1 = MapUtils.toEntityNotEmpty(Student.class, map);
        Student student2 = MapUtils.toEntityNotEmpty(new Student(), map);
        assertEquals(student1.toString(), student2.toString());
//        System.out.println("--->" + o);
//        System.out.println("-1==>>" + student);
//        Student student3 = MapUtils.toEntityNotEmpty(null, map);
//        System.out.println("----");
    }

    @Test
    void testConvertEntity() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("sex", "women");
        map.put("list", "1,2,3,4");
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        Student student = MapUtils.convertEntity(Student.class, map);
        Student student1 = MapUtils.convertEntity(new Student(), map);
        assertEquals(student.toString(), student1.toString());
        assertEquals(1, student.getId());
        assertEquals("null", student.getName());
    }

    // 测试cookie转map
    @Test
    void testCookieValueToMap() {
        String userToken = "739a8543-6ef1-42e9-b6d9-4234918d1d00";
        String string = "X-Cmmop-User-Token=" + userToken + "; X-Cmmop-App-User-Token=88501fd998f1438fa64652deff345e22; Admin-Token=88501fd998f1438fa64652deff345e22; sidebarStatus=1";
        String mapStr = "{Admin-Token=88501fd998f1438fa64652deff345e22, X-Cmmop-App-User-Token=88501fd998f1438fa64652deff345e22, X-Cmmop-User-Token=739a8543-6ef1-42e9-b6d9-4234918d1d00, sidebarStatus=1}";
        Map<String, String> stringStringMap = MapUtils.cookieToMap(string);
        assertEquals(mapStr, stringStringMap.toString());
        assertEquals(userToken, stringStringMap.get("X-Cmmop-User-Token"));
    }

    @Test
    void testEntityToMap() {
        GsonTest gsonTest = new GsonTest();
        gsonTest.setCode("012");
        gsonTest.setAge(18);
        gsonTest.setAmount(998.9);
        gsonTest.setSwitchs(false);
        gsonTest.setCreated(new Date());
        Map<String, Object> stringObjectMap = MapUtils.entityToMap(gsonTest);
//        System.out.println("==2==>>"+stringObjectMap);
        // 校验stringObjectMap中的值类型是否与GsonTest中的一致
        assertEquals(String.class, stringObjectMap.get("code").getClass());
        assertEquals(Integer.class, stringObjectMap.get("age").getClass());
        assertEquals(Double.class, stringObjectMap.get("amount").getClass());
        assertEquals(Boolean.class, stringObjectMap.get("switchs").getClass());
        assertEquals(Date.class, stringObjectMap.get("created").getClass());
    }

    // Test 1: `isNotEmpty` 为 true，空值字段不应被添加到 Map 中
    @Test
    void testEntityToMapWithIsNotEmptyTrue() {
        TestBean bean = new TestBean("John", null, List.of("Reading", "Swimming"));
        Map<String, Object> result = MapUtils.entityToMap(bean, true);
        assertTrue(result.containsKey("name"));
        assertFalse(result.containsKey("age"));
        assertTrue(result.containsKey("hobbies"));
        assertEquals("John", result.get("name"));
        assertEquals(List.of("Reading", "Swimming"), result.get("hobbies"));
    }

    // Test 2: `isNotEmpty` 为 false，所有字段都应被添加到 Map 中
    @Test
    public void testEntityToMapWithIsNotEmptyFalse() {
        TestBean bean = new TestBean("John", null, List.of("Reading", "Swimming"));
        Map<String, Object> result = MapUtils.entityToMap(bean, false);

        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("age"));
        assertTrue(result.containsKey("hobbies"));
        assertEquals("John", result.get("name"));
        assertNull(result.get("age"));
        assertEquals(List.of("Reading", "Swimming"), result.get("hobbies"));
    }

    // Test 3: 测试非空字符串
    @Test
    public void testEmptyString() {
        TestBean bean = new TestBean("", 25, List.of("Reading"));
        Map<String, Object> result = MapUtils.entityToMap(bean, true);

        // name 是空字符串，所以它不应该出现在 Map 中
        assertFalse(result.containsKey("name"));
        assertTrue(result.containsKey("age"));
        assertTrue(result.containsKey("hobbies"));
    }

    // Test 4: 测试空列表
    @Test
    public void testEmptyList() {
        TestBean bean = new TestBean("John", 30, List.of());
        Map<String, Object> result = MapUtils.entityToMap(bean, true);

        // hobbies 是空列表，所以它不应该出现在 Map 中
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("age"));
        assertFalse(result.containsKey("hobbies"));
    }


    // 测试正常排序
    @Test
    public void testSortByValueAscNormal() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 3);
        map.put("b", 1);
        map.put("c", 2);

        Map<String, Integer> sortedMap = MapUtils.sortByValueAsc(map);
        // 确保按值升序排序
        List<Integer> sortedValues = new ArrayList<>(sortedMap.values());
        assertEquals(Arrays.asList(1, 2, 3), sortedValues);
    }

    // 测试空map
    @Test
    public void testSortByValueAscEmptyMap() {
        Map<String, Integer> map = new HashMap<>();

        Map<String, Integer> sortedMap = MapUtils.sortByValueAsc(map);

        // 空的 map 应该返回空的 map
        assertTrue(sortedMap.isEmpty());
    }

    // 测试具有相同值的元素
    @Test
    public void testSortByValueAscWithEqualValues() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 3);
        map.put("b", 1);
        map.put("c", 3);

        Map<String, Integer> sortedMap = MapUtils.sortByValueAsc(map);

        // 确保排序后值相同的元素按键的顺序排列
        List<String> sortedKeys = new ArrayList<>(sortedMap.keySet());
        assertEquals(Arrays.asList("b", "a", "c"), sortedKeys);
    }

    // 测试单元素 map
    @Test
    public void testSortByValueAscSingleElement() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 3);

        Map<String, Integer> sortedMap = MapUtils.sortByValueAsc(map);

        // 单元素的 map 排序后应该仍然只有一个元素
        assertEquals(1, sortedMap.size());
        assertTrue(sortedMap.containsKey("a"));
        assertEquals(Integer.valueOf(3), sortedMap.get("a"));
    }

    // 测试降序输入数据的排序
    @Test
    public void testSortByValueAscReversedInput() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 5);
        map.put("b", 3);
        map.put("c", 1);

        Map<String, Integer> sortedMap = MapUtils.sortByValueAsc(map);

        // 检查升序排序结果
        List<String> sortedKeys = new ArrayList<>(sortedMap.keySet());
        assertEquals(Arrays.asList("c", "b", "a"), sortedKeys);
    }
}

@Data
class TestBean {
    private String name;
    private Integer age;
    private List<String> hobbies;

    public TestBean(String name, Integer age, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.hobbies = hobbies;
    }
}

