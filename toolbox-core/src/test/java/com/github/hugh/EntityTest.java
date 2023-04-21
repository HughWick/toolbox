package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.model.Student1;
import com.github.hugh.util.EntityUtils;
import com.github.hugh.util.MapUtils;
import com.github.hugh.util.ip.IpUtils;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 测试实体工具类
 *
 * @author AS
 * @date 2020/9/18 10:01
 */
class EntityTest {

    @Test
    void testCopy() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "名称");
        map.put("amount", 10.14);
        map.put("password", "密码123");
        map.put("accountName", "真是姓名");
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            Student student = MapUtils.toEntityNotEmpty(Student.class, map);
            Student student2 = new Student();
            EntityUtils.copy(student, student2);
            assertEquals(student, student2);
            assertEquals(student.getAge(), student2.getAge());
            Student1 copy = EntityUtils.copy(student, Student1::new, "name", "accountName");
            assertNull(copy.getName());
            Student1 student3 = EntityUtils.copy(student, Student1::new, (s, t) -> {
                t.setAccount("银行账号");
            });
            assertEquals("银行账号", student3.getAccount());
//            System.out.println("--1->>" + EntityUtils.copy(student, Student1::new));
//            System.out.println("--2-忽略>>" + EntityUtils.copy(student, Student1::new, "name", "accountName"));
//            System.out.println("--3>>" + EntityUtils.copy(student, Student1::new, (s, t) -> {
//                t.setAccount("银行账号");
//            }));
//            System.out.println("--4>>" + EntityUtils.copy(student, Student1::new, (s, t) -> {
//            }, "password"));
//            System.out.println(student + "<----->" + student2);
//            System.out.println("-1-->>" + JSONObject.fromObject(student));
//            System.out.println("-2-->>" + JSONObject.fromObject(student2));
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
        map.put("account", UUID.randomUUID().toString());
        map.put("accountName", UUID.randomUUID().toString());
        map.put("accountType", UUID.randomUUID().toString());
        map.put("password", UUID.randomUUID().toString());
        map.put("phone", UUID.randomUUID().toString());
        map.put("phoneType", "2");
        map.put("role", UUID.randomUUID().toString());
        map.put("authorization", UUID.randomUUID().toString());
        map.put("ip", IpUtils.random());
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            StopWatch stopWatch = new StopWatch("测试转换");
            stopWatch.start("开启map转实体");
            List<Student> list = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                map.put("id", i);
                list.add(MapUtils.toEntityNotEmpty(Student.class, map));
            }
            stopWatch.stop();
//            list.forEach(System.out::println);
            List<Student1> item = new ArrayList<>();
//            stopWatch.start("开启apache实体复制");
//            for (Student student : list) {
//                Student1 student1 = new Student1();
//                EntityUtils.copy(student, student1);
//                item.add(student1);
//            }
//            stopWatch.stop();
            stopWatch.start("开启spring实体复制");
            for (Student student : list) {
                Student1 student1 = new Student1();
                org.springframework.beans.BeanUtils.copyProperties(student, student1);
                item.add(student1);
            }
            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 克隆实体类
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
            assertEquals(1, student.getId());
            Student o1 = EntityUtils.deepClone(student);
            assertEquals(student, o1);
            assertNull(student.getName());
            o1.setName("张三");
            assertEquals("张三" , o1.getName());
            Student student2 = EntityUtils.deepClone(o1);
            assertEquals(o1, student2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testListCopy() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", 2);
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        Student student = MapUtils.toEntityNotEmpty(Student.class, map);
        List<Student> list = Lists.newArrayList(student, EntityUtils.deepClone(student), EntityUtils.deepClone(student), EntityUtils.deepClone(student), EntityUtils.deepClone(student));
        List<Student1> student1s = EntityUtils.copyListProperties(list, Student1::new);
        String strName = "回调设置名称";
        List<Student1> student1ss = EntityUtils.copyListProperties(list, Student1::new, (st, student1) -> {
            student1.setName(strName);
        });
        assertEquals(strName , student1ss.iterator().next().getName());
    }
}
