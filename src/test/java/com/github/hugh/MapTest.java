package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.EntityUtils;
import com.github.hugh.util.MapUtils;
import com.google.common.base.Stopwatch;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            Object o = MapUtils.toEntity(Student.class, map);
            System.out.println(JSONObject.fromObject(o));
            Student student = new Student();
            EntityUtils.copy(o, student);
            student.setName("张三");
            System.out.println(JSONObject.fromObject(student));
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

    @Test
    public void test03() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", null);
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
//            for (int i = 0; i < 100000; i++) {
            Student o = MapUtils.toEntity(Student.class, map);
            System.out.println(JSONObject.fromObject(o));
//            }
            long milli = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            System.out.println(milli);

//            Stopwatch stopwatch2 = Stopwatch.createStarted();
//            for (int i = 0; i < 100000; i++) {
//                Object o = MapUtils.toEntity(Student.class, map);
//            }
//            System.out.println(stopwatch2.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


