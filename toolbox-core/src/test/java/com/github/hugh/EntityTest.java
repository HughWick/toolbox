package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.model.Student1;
import com.github.hugh.util.EntityUtils;
import com.github.hugh.util.MapUtils;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author AS
 * @date 2020/9/18 10:01
 */
public class EntityTest {

    @Test
    void testCopy() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            Student student = MapUtils.toEntityNotEmpty(Student.class, map);
            Student student2 = new Student();
            EntityUtils.copy(student, student2);
            System.out.println(student + "<----->" + student2);
            System.out.println("-1-->>" + JSONObject.fromObject(student));
            System.out.println("-2-->>" + JSONObject.fromObject(student2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCopyList() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            List<Student> list = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                map.put("id", i);
                list.add(MapUtils.toEntityNotEmpty(Student.class, map));
            }
            List<Student1> copy = EntityUtils.copy(list, Student1.class);
            copy.forEach(System.out::println);
//            System.out.println("===>>" + list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void cloneTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            Student student = MapUtils.toEntityNotEmpty(Student.class, map);
            Student o1 = EntityUtils.deepClone(student);
            System.out.println(student + "====" + o1);
            System.out.println(JSONObject.fromObject(student));
            System.out.println(JSONObject.fromObject(o1));
            o1.setName("张三");
            System.out.println(o1);
            Student student2 = EntityUtils.deepClone(o1);
            System.out.println(student2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cloneTest02() {
        Student student2 = new Student();
        Student o1 = EntityUtils.deepClone(student2);
    }
}
