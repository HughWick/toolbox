package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.EntityUtils;
import com.github.hugh.util.MapUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
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
        map.clear();
        assertTrue(MapUtils.isEmpty(map));
        assertFalse(MapUtils.isNotEmpty(map));
        assertFalse(MapUtils.isSuccess(map, "code", null));
        assertFalse(MapUtils.isSuccess(null, "code", null));
        map.put("code", "0000");
        assertTrue(MapUtils.isSuccess(map, "code", "0000"));
        assertTrue(MapUtils.isFailure(map, "code", "00100"));
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
//        System.out.println("--1->" + data);
//        String str = MapUtils.getString(map, "create");
        assertEquals(strCreateDate, MapUtils.getString(map, "create"));
//        System.out.println("-2-->" + str);
        assertEquals(1, MapUtils.getInt(map, "id"));
        assertEquals(2, MapUtils.getLong(map, "age"));
        assertEquals(10.14, MapUtils.getDouble(map, "amount"));
//        System.out.println("-3-->" + MapUtils.getInt(map, "id"));
//        System.out.println("-4-->" + MapUtils.getLong(map, "age"));
//        System.out.println("-5-->" + MapUtils.getDouble(map, "amount"));
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
    void testRemoveKey() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("size", 2);
        for (int i = 0; i < 6000000; i++) {
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

    @Test
    void testSort() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("z", 1);
        map1.put("a", 1);
        map1.put("w", 1);
        map1.put("i", 1);
        map1.put("n", 1);
        map1.put("m", 1);
        map1.put("e", 1);
        System.out.println("--->原来>!>>>>" + map1);
//        System.out.println("--2->>!>>>>" + MapUtils.sortMap(map));
        System.out.println("--6->>!>>>>" + MapUtils.sortByKeyAsc(map1));
//        Assertions.assertEquals(MapUtils.sortMap(map) ,MapUtils.sortByKeyAsc(map));
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("2", "a");
        stringMap.put("5", "i");
        stringMap.put("1", "z");
        stringMap.put("3", "c");
        stringMap.put("6", "w");
        stringMap.put("4", "n");
        System.out.println("--3->排序前>!>>>>" + stringMap);
        System.out.println("--4->>!>>>>" + MapUtils.sortByValueDesc(stringMap));
        System.out.println("--5->>!>>>>" + MapUtils.sortByValueAsc(stringMap));
        System.out.println("--6->>!>>>>" + MapUtils.sortByKeyDesc(stringMap));
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

}


