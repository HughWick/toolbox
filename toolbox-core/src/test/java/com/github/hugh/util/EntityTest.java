package com.github.hugh.util;

import com.github.hugh.model.Student;
import com.github.hugh.model.StudentTarget;
import com.github.hugh.util.ip.IpUtils;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
        Student student;
        try {
            student = MapUtils.toEntityNotEmpty(Student.class, map);
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
        Student copyStudent = new Student();
        EntityUtils.copy(student, copyStudent);
        assertEquals(student, copyStudent);
        assertEquals(student.getAge(), copyStudent.getAge());
        assertEquals(student.getCreate().getTime(), copyStudent.getCreate().getTime());
        StudentTarget copy = EntityUtils.copy(student, StudentTarget::new, "name", "accountName");
        assertNull(copy.getName());
        StudentTarget student3 = EntityUtils.copy(student, StudentTarget::new, (s, t) -> {
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
            List<StudentTarget> item = new ArrayList<>();
//            stopWatch.start("开启apache实体复制");
//            for (Student student : list) {
//                Student1 student1 = new Student1();
//                EntityUtils.copy(student, student1);
//                item.add(student1);
//            }
//            stopWatch.stop();
            stopWatch.start("开启spring实体复制");
            for (Student student : list) {
                StudentTarget studentTarget = new StudentTarget();
                org.springframework.beans.BeanUtils.copyProperties(student, studentTarget);
                item.add(studentTarget);
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
            assertEquals("张三", o1.getName());
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
        List<StudentTarget> student1s = EntityUtils.copyListProperties(list, StudentTarget::new);
        String strName = "回调设置名称";
        List<StudentTarget> student1ss = EntityUtils.copyListProperties(list, StudentTarget::new, (st, student1) -> {
            student1.setName(strName);
        });
        assertEquals(strName, student1ss.iterator().next().getName());
    }

    @Test
    @DisplayName("测试 copy(source, targetSupplier) 方法：基本复制")
    void testCopy_Basic() {
        Student student = new Student();
        student.setId(2);
        student.setName("Alice");
        student.setAge(30);
        student.setAccount("123 main st");
        // 使用 Lambda 表达式作为 Supplier 创建目标对象
        StudentTarget target = EntityUtils.copy(student, StudentTarget::new);

        assertNotNull(target, "目标对象不应为null");
        assertEquals("Alice", target.getName(), "name属性应被复制");
        assertEquals(30, target.getAge(), "age属性应被复制");
        // address 属性在 TargetBean 中没有同名属性 (只有 city)，所以不会被复制
//        assertNull(target.getAccount(), "city属性不应被复制（源对象无此属性）");
    }

    @Test
    @DisplayName("测试 copy(source, targetSupplier) 方法：源对象为null")
    void testCopy_SourceNull() {
        // 当 source 为 null 时，BeanUtils.copyProperties 通常不会抛出异常，
        // 而是直接不执行任何复制操作。Supplier 依然会创建对象。
        Student student = null;
        assertThrows(IllegalArgumentException.class, () -> {
            EntityUtils.copy(student, StudentTarget::new);
        }, "");
    }

    @Test
    @DisplayName("测试 copy(source, targetSupplier) 方法：源对象和目标对象类型不兼容")
    void testCopy_IncompatibleTypes() {
        // 尽管方法签名是泛型的，但 BeanUtils.copyProperties 会基于属性名匹配进行复制。
        // 如果属性名完全不匹配，则不会有任何复制。
        // 这里只是为了演示，假设有一个完全不兼容的源对象类型
        class AnotherSource {
            public String someProperty = "value";
        }
        AnotherSource source = new AnotherSource();
        StudentTarget target = EntityUtils.copy(source, StudentTarget::new);
        assertNotNull(target);
        assertNull(target.getName(), "类型不兼容时，name属性应为null或默认值");
        assertEquals(0, target.getAge(), "类型不兼容时，age属性应为0或默认值");
    }

}
