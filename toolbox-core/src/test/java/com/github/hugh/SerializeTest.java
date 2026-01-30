package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.MapUtils;
import com.github.hugh.util.SerializeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 序列号测试类
 *
 * @author AS
 * @date 2020/8/31 17:59
 */
class SerializeTest {

    @Test
    void test01() {
        String str = "asdasdfdsafaeriuowqeyr4iu13y3iu4gh21juk3739yrfiohdkjcsjdnf`2随身带哦发i是大家都会覅凯撒觉得哈佛i安身的地方哈吉斯的话覅绿卡和肉IP就和外婆而减弱为啊圣诞快乐付款就会考虑就撒娇就发c";
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(i + "--->" + Arrays.toString(SerializeUtils.toBytes(str)));
//        }
        final byte[] bytes = SerializeUtils.toBytes(str);
        assertEquals(str, SerializeUtils.toObject(bytes));
    }

    @Test
    void test02() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("age", "2");
        map.put("name", "null");
        map.put("amount", 10.14);
        map.put("birthday", new Date());
        map.put("create", "2019-04-06 12:11:20");
        try {
            Object o = MapUtils.toEntityNotEmpty(Student.class, map);
            byte[] bytes = SerializeUtils.toBytes(o);
            String str1 = "[1, 0, 99, 111, 109, 46, 103, 105, 116, 104, 117, 98, 46, 104, 117, 103, 104, 46, 109, 111, 100, 101, 108, 46, 83, 116, 117, 100, 101, 110, -12, 1, 0, 0, 0, 4, 64, 36, 71, -82, 20, 122, -31, 72, 0, 1, 1, 106, 97, 118, 97, 46, 117, 116, 105, 108, 46, 68, 97, 116, -27, 1, -64, -12, -33, -122, -97, 45, 2, 0, 0, 0, 0, 0, 0, 0, 0]";
            assertEquals(str1, Arrays.toString(bytes));
//            Student student = (Student) SerializeUtils.toObject(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 测试用的实体类
    public static class User implements Serializable {
        private String name;
        private int age;
        private User bestFriend; // 用于测试循环引用

        public User() {} // Kryo 通常需要无参构造，或者配置 StdInstantiatorStrategy

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Getters, Setters, Equals, HashCode...
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return age == user.age && name.equals(user.name);
        }
    }

    @Test
    @DisplayName("测试基本序列化与反序列化")
    void testBasicSerialization() {
        User original = new User("ZhangSan", 18);
        // 1. 序列化
        byte[] bytes = SerializeUtils.toBytes(original);
        Assertions.assertNotNull(bytes);
        Assertions.assertTrue(bytes.length > 0);
        // 2. 反序列化 (Object)
        Object obj = SerializeUtils.toObject(bytes);
        Assertions.assertTrue(obj instanceof User);
        Assertions.assertEquals(original, obj);
    }

    @Test
    @DisplayName("测试指定Class类型的反序列化")
    void testTypedDeserialization() {
        User original = new User("LiSi", 25);
        byte[] bytes = SerializeUtils.toBytes(original);
        // 3. 反序列化 (指定 Class)
        User deserialized = SerializeUtils.toObject(bytes, User.class);
        Assertions.assertEquals(original.name, deserialized.name);
        Assertions.assertEquals(original.age, deserialized.age);
    }

    @Test
    @DisplayName("测试循环引用 (StackOverflow check)")
    void testCircularReference() {
        User a = new User("A", 1);
        User b = new User("B", 2);
        a.bestFriend = b;
        b.bestFriend = a; // 循环引用 A -> B -> A
        byte[] bytes = SerializeUtils.toBytes(a);
        User resultA = SerializeUtils.toObject(bytes, User.class);
        Assertions.assertEquals("A", resultA.name);
        Assertions.assertEquals("B", resultA.bestFriend.name);
        // 验证反序列化后的引用是否指回了自己
        Assertions.assertSame(resultA, resultA.bestFriend.bestFriend);
    }

    @Test
    @DisplayName("测试类型不匹配抛出异常")
    void testClassCastException() {
        User user = new User("WangWu", 30);
        byte[] bytes = SerializeUtils.toBytes(user);
        // 尝试反序列化成 Map，应该报错
        Assertions.assertThrows(ClassCastException.class, () -> {
            SerializeUtils.toObject(bytes, Map.class);
        });
    }

    @Test
    @DisplayName("测试 Null 值处理")
    void testNullHandling() {
        Assertions.assertNull(SerializeUtils.toBytes(null));
        Assertions.assertNull(SerializeUtils.toObject(null));
        Assertions.assertNull(SerializeUtils.toObject(null,null));
        Assertions.assertNull(SerializeUtils.toObject(new byte[0]));
    }

    @Test
    @DisplayName("测试复杂集合类型")
    void testCollectionSerialization() {
        Map<String, User> map = new HashMap<>();
        map.put("u1", new User("U1", 1));
        map.put("u2", new User("U2", 2));
        byte[] bytes = SerializeUtils.toBytes(map);
        // 必须用 Map.class 或者 HashMap.class，这里泛型擦除，运行时只能检测是 Map
        Map<String, User> result = SerializeUtils.toObject(bytes, HashMap.class);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("U1", result.get("u1").name);
    }

    @Test
    @DisplayName("覆盖 ThreadLocal 的 remove 方法")
    void testRemove() {
        // 先获取一次触发初始化
        SerializeUtils.toBytes("test");
        // 调用清理
        Assertions.assertDoesNotThrow(SerializeUtils::remove);
    }
}
