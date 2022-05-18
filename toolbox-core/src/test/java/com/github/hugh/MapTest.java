package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.EntityUtils;
import com.github.hugh.util.MapUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AS
 * @date 2020/8/31 9:18
 */
public class MapTest {

    @Test
    public void test01() {
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
        System.out.println("--1->>" + MapUtils.isEmpty(map));
        map.clear();
        System.out.println("--2->>" + MapUtils.isEmpty(map));
        System.out.println("--2->>" + MapUtils.isNotEmpty(map));
        System.out.println("-0--isSuccess-=>" + MapUtils.isSuccess(map, "code", null));
        System.out.println("-1--isSuccess-=>" + MapUtils.isSuccess(null, "code", null));
        map.put("code", "0000");
        System.out.println("2-isSuccess---=>" + MapUtils.isSuccess(map, "code", "0000"));
        System.out.println("-isFailure---=>" + MapUtils.isFailure(map, "code", "00100"));
    }

    @Test
    public void test02() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        Map data = MapUtils.getMap(map, "data");
        System.out.println("--1->" + data);
        String str = MapUtils.getString(map, "create");
        System.out.println("-2-->" + str);
        System.out.println("-3-->" + MapUtils.getInt(map, "id"));
        System.out.println("-4-->" + MapUtils.getLong(map, "age"));
        System.out.println("-5-->" + MapUtils.getDouble(map, "amount"));
    }

//    @Test
//    public void test03() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", 1);
//        map.put("age", 2);
//        map.put("name", "null");
//        map.put("amount", 10.14);
//        map.put("birthday", new Date());
//        map.put("create", "2019-04-06 12:11:20");
//        JSONArray arr = new JSONArray();
//        arr.add("a");
//        arr.add("b");
//        JSONArray arr2 = new JSONArray();
//        arr2.add("c");
//        arr.add(arr2.toString());
//        map.put("list", arr.toString());
//        System.out.println("--->>" + map);
//        try {
//            Student Student1 = MapUtils.toEntityNotEmpty(Student.class, map);
//            System.out.println(JSONObject.fromObject(Student1));
//            map.put("age", 22233);
//            Student Student12 = MapUtils.convertEntity(Student1, map);
//            System.out.println(JSONObject.fromObject(Student12));
////            Object o = Student12.getList().get(2);
////            System.out.println("--->>>>>" + JSONArray.fromObject(o));
////            String s = JsonObjectUtils.toJson(Student1);
////            Student Student3 = MapUtils.toEntity(Student1, JsonObjectUtils.parse(s));
////            System.out.println(JSONObject.fromObject(Student3));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void test04() {
        Map<String, Object> map = new HashMap<>();
        MapUtils.setValue(map, "a", null);
        System.out.println("--1>>" + map);
        MapUtils.setValue(map, "null", "null");
        System.out.println("--2>>" + map);
        MapUtils.setValue(map, "a", "1");
        System.out.println("--4>>" + map);
        MapUtils.setValue(map, "null", "1");
        System.out.println("--3>>" + map);
    }

    @Test
    void test05() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("size", 2);
        map.put("id", 2);
        MapUtils.removeKeys(map, "page", "size");
        System.out.println(map);
    }

    @Test
    void testSort() {
        Map map = new HashMap<>();
        map.put("z", 1);
        map.put("a", 1);
        map.put("w", 1);
        map.put("i", 1);
        map.put("n", 1);
        map.put("m", 1);
        map.put("e", 1);
        System.out.println("--->原来>!>>>>" + map);
//        System.out.println("--2->>!>>>>" + MapUtils.sortMap(map));
        System.out.println("--6->>!>>>>" + MapUtils.sortByKeyAsc(map));
//        Assertions.assertEquals(MapUtils.sortMap(map) ,MapUtils.sortByKeyAsc(map));
        map.clear();
        map.put("2", "a");
        map.put("5", "i");
        map.put("1", "z");
        map.put("3", "c");
        map.put("6", "w");
        map.put("4", "n");
        System.out.println("--3->排序前>!>>>>" + map);
        System.out.println("--4->>!>>>>" + MapUtils.sortByValueDesc(map));
        System.out.println("--5->>!>>>>" + MapUtils.sortByValueAsc(map));
        System.out.println("--5->>!>>>>" + MapUtils.sortByKeyDesc(map));
    }
}


