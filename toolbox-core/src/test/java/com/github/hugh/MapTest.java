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
    public void testGetValue() {
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

    @Test
    public void testSetValue() {
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

    @Test
    void testSortByDesc() {
        Map<String, String> map = new HashMap<>();
        map.put("2", "a");
        map.put("5", "i");
        map.put("1", "z");
        map.put("3", "c");
        map.put("6", "w");
        map.put("4", "n");
        System.out.println("--4->>!>>>>" + MapUtils.sortByValueDesc(map));
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
        Student o = MapUtils.toEntityNotEmpty(Student.class, map);
        Student student = MapUtils.toEntityNotEmpty(new Student(), map);
        System.out.println("--->" + o);
        System.out.println("-1==>>" + student);
//        Student student3 = MapUtils.toEntityNotEmpty(null, map);
        System.out.println("----");
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
        Student o = MapUtils.convertEntity(Student.class, map);
        System.out.println("--->>" + o);
        Student student1 = MapUtils.convertEntity(new Student(), map);
        System.out.println("--===>>"+ student1);
    }

    @Test
    void testCookieValueToMap(){
        String string = "X-Cmmop-User-Token=739a8543-6ef1-42e9-b6d9-4234918d1d00; X-Cmmop-App-User-Token=88501fd998f1438fa64652deff345e22; Admin-Token=88501fd998f1438fa64652deff345e22; sidebarStatus=1";
        String mapStr = "{Admin-Token=88501fd998f1438fa64652deff345e22, X-Cmmop-App-User-Token=88501fd998f1438fa64652deff345e22, X-Cmmop-User-Token=739a8543-6ef1-42e9-b6d9-4234918d1d00, sidebarStatus=1}";
        Assertions.assertEquals(mapStr,MapUtils.cookieToMap(string).toString());

    }

}


