package com.github.hugh.components;

import com.github.hugh.exception.ConstantsLoadException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReflectionConstantsLoader 测试")
public class ReflectionConstantsLoaderTest {
    // --- 用于测试的模拟常量类 ---

    /**
     * 模拟一个包含String常量的类。
     */
    static class MockStringConstants {
        public static final String STATUS_ACTIVE = "ACTIVE";
        public static final String STATUS_INACTIVE = "INACTIVE";
        public static final String TYPE_A = "TYPE_A";
        // 非String类型的常量，应被忽略
        public static final int MAX_ATTEMPTS = 5;
        // 非public的常量，应被忽略
        private static final String PRIVATE_FIELD = "private";
        // 非final的常量，应被忽略
        public static String NON_FINAL_FIELD = "non_final";
        // 非static的常量，应被忽略
        public final String INSTANCE_FIELD = "instance";
    }

    /**
     * 模拟一个包含Integer常量的类。
     */
    static class MockIntegerConstants {
        public static final Integer CODE_SUCCESS = 200;
        public static final Integer CODE_NOT_FOUND = 404;
        // 非Integer类型的常量，应被忽略
        public static final String NAME = "test";
    }

    /**
     * 模拟一个空的常量类。
     */
    static class MockEmptyConstants {
        // 什么也没有
    }

    /**
     * 模拟一个包含非public static final String 的常量类。
     */
    static class MockInvalidConstants {
        public String field1 = "value1"; // 非 static final
        private static final String field2 = "value2"; // private
        public static String field3 = "value3"; // 非 final
        public static final int field4 = 123; // 非 String
    }

    /**
     * 包含各种 static 字段的类，用于测试 loadStaticStringConstants。
     */
    public static class MixedStaticConstants {
        public static final String PUBLIC_FINAL_STR = "public_final_str";
        private static final String PRIVATE_FINAL_STR = "private_final_str"; // loadStaticStringConstants 会尝试访问
        public static String PUBLIC_NON_FINAL_STR = "public_non_final_str";
        private static String PRIVATE_NON_FINAL_STR = "private_non_final_str"; // loadStaticStringConstants 会尝试访问
        public static final Integer PUBLIC_FINAL_INT = 100; // 非 String
        public String INSTANCE_STR = "instance_str"; // 非 static
    }

    /**
     * 包含带前缀字段的类，用于测试 loadConstantsWithFilter。
     */
    public static class PrefixedConstants {
        public static final String ERROR_CODE_1 = "E001";
        public static final String ERROR_CODE_2 = "E002";
        public static final String INFO_MESSAGE = "Info";
        private static final String SECRET_KEY = "XYZ123";
        public static final int MAX_RETRIES = 3;
    }

    // --- loadStringConstants 方法的测试 ---

    @Test
    @DisplayName("测试 loadStringConstants 方法：加载有效String常量")
    void testLoadStringConstants_Valid() {
        String name = MockStringConstants.class.getName();
        System.out.println(name);
        Set<String> constants = ConstantsLoader.loadPublicStaticFinalStringConstants(name);

        assertNotNull(constants, "返回的Set不应为null");
        assertEquals(3, constants.size(), "应正确加载3个String常量");
        assertTrue(constants.contains("ACTIVE"), "应包含ACTIVE");
        assertTrue(constants.contains("INACTIVE"), "应包含INACTIVE");
        assertTrue(constants.contains("TYPE_A"), "应包含TYPE_A");

        assertFalse(constants.contains("private"), "不应包含private字段");
        assertFalse(constants.contains("non_final"), "不应包含非final字段");
        assertFalse(constants.contains("instance"), "不应包含实例字段");
    }

    @Test
    @DisplayName("测试 loadStringConstants 方法：加载空常量类")
    void testLoadStringConstants_EmptyClass() {
        Set<String> constants = ConstantsLoader.loadPublicStaticFinalStringConstants(MockEmptyConstants.class.getName());
        assertNotNull(constants, "返回的Set不应为null");
        assertTrue(constants.isEmpty(), "空常量类应返回空Set");
    }

    @Test
    @DisplayName("测试 loadStringConstants 方法：加载不存在的类")
    void testLoadStringConstants_ClassNotFound() {
        String nonExistentClass = "com.nonexistent.NonExistentClass";
        ConstantsLoadException exception = assertThrows(ConstantsLoadException.class, () -> {
            ConstantsLoader.loadPublicStaticFinalStringConstants(nonExistentClass);
        }, "加载不存在的类应抛出 ConstantsLoadException");

        assertTrue(exception.getMessage().contains("Class not found"), "异常消息应包含 'Class not found'");
        assertTrue(exception.getCause() instanceof ClassNotFoundException, "底层原因应为 ClassNotFoundException");
    }

    @Test
    @DisplayName("测试 loadStringConstants 方法：加载不符合规范的常量类")
    void testLoadStringConstants_InvalidConstants() {
        Set<String> constants = ConstantsLoader.loadPublicStaticFinalStringConstants(MockInvalidConstants.class.getName());
        assertNotNull(constants, "返回的Set不应为null");
        assertTrue(constants.isEmpty(), "不符合规范的常量类应返回空Set");
    }


    // --- loadConstants 方法的测试 ---

    @Test
    @DisplayName("测试 loadConstants 方法：加载有效Integer常量")
    void testLoadConstants_ValidInteger() {
        Set<Integer> constants = ConstantsLoader.loadPublicStaticFinalConstants(MockIntegerConstants.class.getName(), Integer.class);

        assertNotNull(constants, "返回的Set不应为null");
        assertEquals(2, constants.size(), "应正确加载2个Integer常量");
        assertTrue(constants.contains(200), "应包含CODE_SUCCESS");
        assertTrue(constants.contains(404), "应包含CODE_NOT_FOUND");

        assertFalse(constants.contains("test"), "不应包含非Integer类型的常量");
    }

    @Test
    @DisplayName("测试 loadConstants 方法：加载String常量，使用指定类型")
    void testLoadConstants_ValidStringWithType() {
        Set<String> constants = ConstantsLoader.loadPublicStaticFinalConstants(MockStringConstants.class.getName(), String.class);
        assertNotNull(constants, "返回的Set不应为null");
        assertEquals(3, constants.size(), "应正确加载3个String常量");
        assertTrue(constants.contains("ACTIVE"), "应包含ACTIVE");
        assertTrue(constants.contains("INACTIVE"), "应包含INACTIVE");
        assertTrue(constants.contains("TYPE_A"), "应包含TYPE_A");
    }

    @Test
    @DisplayName("测试 loadConstants 方法：加载空常量类")
    void testLoadConstants_EmptyClass() {
        Set<Object> constants = ConstantsLoader.loadPublicStaticFinalConstants(MockEmptyConstants.class.getName(), Object.class);
        assertNotNull(constants, "返回的Set不应为null");
        assertTrue(constants.isEmpty(), "空常量类应返回空Set");
    }

    @Test
    @DisplayName("测试 loadConstants 方法：加载不存在的类")
    void testLoadConstants_ClassNotFound() {
        String nonExistentClass = "com.nonexistent.NonExistentClass";
        ConstantsLoadException exception = assertThrows(ConstantsLoadException.class, () -> {
            ConstantsLoader.loadPublicStaticFinalConstants(nonExistentClass, String.class);
        }, "加载不存在的类应抛出 ConstantsLoadException");

        assertTrue(exception.getMessage().contains("Class not found"), "异常消息应包含 'Class not found'");
        assertTrue(exception.getCause() instanceof ClassNotFoundException, "底层原因应为 ClassNotFoundException");
    }

    @Test
    @DisplayName("测试 loadConstants 方法：加载不符合规范的常量类")
    void testLoadConstants_InvalidConstants() {
        Set<String> constants = ConstantsLoader.loadPublicStaticFinalConstants(MockInvalidConstants.class.getName(), String.class);
        assertNotNull(constants, "返回的Set不应为null");
        assertTrue(constants.isEmpty(), "不符合规范的常量类应返回空Set");
    }

    // --- 为 loadPublicStaticFinalConstants(Class<?>, Class<T>) 方法编写的测试 ---

    @Test
    @DisplayName("测试 loadPublicStaticFinalConstants(Class, Class) 方法：加载有效String常量")
    void testLoadPublicStaticFinalConstantsByClass_ValidString() {
        Set<String> constants = ConstantsLoader.loadPublicStaticFinalConstants(MockStringConstants.class, String.class);

        assertNotNull(constants, "返回的Set不应为null");
        assertEquals(3, constants.size(), "应正确加载3个String常量");
        assertTrue(constants.contains("ACTIVE"), "应包含ACTIVE");
        assertTrue(constants.contains("INACTIVE"), "应包含INACTIVE");
        assertTrue(constants.contains("TYPE_A"), "应包含TYPE_A");

        assertFalse(constants.contains("private"), "不应包含private字段 (因为它是private)");
        assertFalse(constants.contains(5), "不应包含非String类型的常量");
    }

    @Test
    @DisplayName("测试 loadPublicStaticFinalConstants(Class, Class) 方法：加载有效Integer常量")
    void testLoadPublicStaticFinalConstantsByClass_ValidInteger() {
        Set<Integer> constants = ConstantsLoader.loadPublicStaticFinalConstants(MockIntegerConstants.class, Integer.class);

        assertNotNull(constants, "返回的Set不应为null");
        assertEquals(2, constants.size(), "应正确加载2个Integer常量");
        assertTrue(constants.contains(200), "应包含CODE_SUCCESS");
        assertTrue(constants.contains(404), "应包含CODE_NOT_FOUND");

        assertFalse(constants.contains("test"), "不应包含非Integer类型的常量");
    }

    @Test
    @DisplayName("测试 loadPublicStaticFinalConstants(Class, Class) 方法：加载空常量类")
    void testLoadPublicStaticFinalConstantsByClass_EmptyClass() {
        Set<Object> constants = ConstantsLoader.loadPublicStaticFinalConstants(MockEmptyConstants.class, Object.class);
        assertNotNull(constants, "返回的Set不应为null");
        assertTrue(constants.isEmpty(), "空常量类应返回空Set");
    }

//    @Test
//    @DisplayName("测试 loadPublicStaticFinalConstants(Class, Class) 方法：类型不匹配")
//    void testLoadPublicStaticFinalConstantsByClass_TypeMismatch() {
//        // 尝试将 String 常量加载为 Integer 类型，期望抛出 ClassCastException
//        ConstantsLoadException exception = assertThrows(ConstantsLoadException.class, () -> {
//            ReflectionConstantsLoader.loadPublicStaticFinalConstants(MockStringConstants.class, Integer.class);
//        }, "类型不匹配时应抛出 ConstantsLoadException");
//
//        assertTrue(exception.getMessage().contains("Type mismatch"), "异常消息应包含 'Type mismatch'");
//        assertTrue(exception.getCause() instanceof ClassCastException, "底层原因应为 ClassCastException");
//    }

    @Test
    @DisplayName("测试 loadPublicStaticFinalConstants(Class, Class) 方法：加载不符合规范的常量类")
    void testLoadPublicStaticFinalConstantsByClass_InvalidConstants() {
        // MockInvalidConstants 中没有符合 public static final String 要求的字段，因此结果应为空
        Set<String> constants = ConstantsLoader.loadPublicStaticFinalConstants(MockInvalidConstants.class, String.class);
        assertNotNull(constants, "返回的Set不应为null");
        assertTrue(constants.isEmpty(), "不符合规范的常量类应返回空Set");
    }


    @Test
    @DisplayName("测试 loadStaticStringConstants 方法：加载各种静态String常量")
    void testLoadStaticStringConstants_MixedStatics() {
        // 这个测试预期会因访问 private static 字段而抛出异常，
        // 除非 loadConstantsInternal 内部显式地为 private 字段调用 setAccessible(true)。
        // 如果你的设计是只加载 public 字段而不访问 private 字段，那么这个测试应该期望抛出异常。
        // 如果你的设计是通过 setAccessible(true) 访问 private 字段，那么这个测试应该会成功。

        // **根据你当前的 `ReflectionConstantsLoader` 实现（只加载 public 字段），
        // 这里的期望是抛出 `ConstantsLoadException`。**
        ConstantsLoadException exception = assertThrows(ConstantsLoadException.class, () -> {
            ConstantsLoader.loadStaticString(MixedStaticConstants.class.getName());
        }, "加载包含私有静态字段的类时，应抛出 ConstantsLoadException (IllegalAccessException)");

        assertTrue(exception.getMessage().contains("Illegal access"), "异常消息应包含 'Illegal access'");
        assertTrue(exception.getCause() instanceof IllegalAccessException, "底层原因应为 IllegalAccessException");
    }

    // 如果 loadStaticStringConstants 被设计为成功加载所有 static String (包括 private)
    // 并且 ReflectionConstantsLoader 内部使用了 field.setAccessible(true)
    // 那么这个测试应该是：
    /*
    @Test
    @DisplayName("测试 loadStaticStringConstants 方法：成功加载所有静态String常量 (包括私有)")
    void testLoadStaticStringConstants_SuccessLoadingAllStatics() {
        Set<String> constants = ReflectionConstantsLoader.loadStaticStringConstants(MixedStaticConstants.class.getName());
        assertNotNull(constants);
        assertEquals(4, constants.size(), "应加载所有4个静态String常量");
        assertTrue(constants.contains("public_final_str"));
        assertTrue(constants.contains("private_final_str"));
        assertTrue(constants.contains("public_non_final_str"));
        assertTrue(constants.contains("private_non_final_str"));
        assertFalse(constants.contains("instance_str"));
        assertFalse(constants.contains(100));
    }
    */

    @Test
    @DisplayName("测试 loadStaticStringConstants 方法：加载空常量类")
    void testLoadStaticStringConstants_EmptyClass() {
        Set<String> constants = ConstantsLoader.loadStaticString(MockEmptyConstants.class.getName());
        assertNotNull(constants, "返回的Set不应为null");
        assertTrue(constants.isEmpty(), "空常量类应返回空Set");
    }

    @Test
    @DisplayName("测试 loadStaticStringConstants 方法：加载不存在的类")
    void testLoadStaticStringConstants_ClassNotFound() {
        String nonExistentClass = "com.nonexistent.NonExistentClass";
        ConstantsLoadException exception = assertThrows(ConstantsLoadException.class, () -> {
            ConstantsLoader.loadStaticString(nonExistentClass);
        }, "加载不存在的类应抛出 ConstantsLoadException");

        assertTrue(exception.getMessage().contains("Class not found"), "异常消息应包含 'Class not found'");
        assertTrue(exception.getCause() instanceof ClassNotFoundException, "底层原因应为 ClassNotFoundException");
    }


    // --- loadConstantsWithFilter 方法的测试 ---

    @Test
    @DisplayName("测试 loadConstantsWithFilter 方法：自定义过滤器 - 只加载公共静态Final String")
    void testLoadConstantsWithFilter_PublicStaticFinalString() {
        Predicate<Field> filter = field ->
                Modifier.isPublic(field.getModifiers()) &&
                        Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers());

        Set<String> constants = ConstantsLoader.load(MockStringConstants.class.getName(), filter, String.class);

        assertNotNull(constants, "返回的Set不应为null");
        assertEquals(3, constants.size(), "应加载3个公共静态final String常量");
        assertTrue(constants.contains("ACTIVE"));
        assertTrue(constants.contains("INACTIVE"));
        assertTrue(constants.contains("TYPE_A"));
    }

    @Test
    @DisplayName("测试 loadConstantsWithFilter 方法：自定义过滤器 - 加载所有Error开头的String")
    void testLoadConstantsWithFilter_PrefixedStringConstants() {
        Predicate<Field> filter = field ->
                Modifier.isPublic(field.getModifiers()) && // 通常这种场景下还是关心公共字段
                        Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers()) &&
                        field.getType() == String.class &&
                        field.getName().startsWith("ERROR_");

        Set<String> constants = ConstantsLoader.load(PrefixedConstants.class.getName(), filter, String.class);

        assertNotNull(constants, "返回的Set不应为null");
        assertEquals(2, constants.size(), "应加载2个以ERROR_开头的String常量");
        assertTrue(constants.contains("E001"));
        assertTrue(constants.contains("E002"));
        assertFalse(constants.contains("Info"));
    }

    @Test
    @DisplayName("测试 loadConstantsWithFilter 方法：自定义过滤器 - 加载私有静态Final String (预期抛出异常)")
    void testLoadConstantsWithFilter_PrivateStaticFinalString_ExpectedException() {
        // ⚠️重要：如果你的 ReflectionConstantsLoader 未修改为 field.setAccessible(true)，
        // 则此测试预期会抛出 ConstantsLoadException。
        // 如果你的 ReflectionConstantsLoader 内部使用了 setAccessible(true)，则此测试应改为成功断言。

        Predicate<Field> filter = field ->
                !Modifier.isPublic(field.getModifiers()) && // 非公共
                        Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers()) &&
                        field.getType() == String.class;

        ConstantsLoadException exception = assertThrows(ConstantsLoadException.class, () -> {
            ConstantsLoader.load(MixedStaticConstants.class.getName(), filter, String.class);
        }, "尝试加载私有静态final字段时应抛出 ConstantsLoadException");

        assertTrue(exception.getMessage().contains("Illegal access"), "异常消息应包含 'Illegal access'");
        assertTrue(exception.getCause() instanceof IllegalAccessException, "底层原因应为 IllegalAccessException");
    }

    @Test
    @DisplayName("测试 loadConstantsWithFilter 方法：自定义过滤器 - 加载所有静态 Integer 类型")
    void testLoadConstantsWithFilter_StaticInteger() {
        Predicate<Field> filter = field ->
                Modifier.isStatic(field.getModifiers()) &&
                        Integer.class.isAssignableFrom(field.getType());
        Set<Integer> constants = ConstantsLoader.load(MixedStaticConstants.class.getName(), filter, Integer.class);
        assertNotNull(constants, "返回的Set不应为null");
        assertEquals(1, constants.size(), "应加载1个静态Integer常量");
        assertTrue(constants.contains(100));
    }

    @Test
    @DisplayName("测试 loadConstantsWithFilter 方法：expectedType 为 null，返回 Set<Object>")
    void testLoadConstantsWithFilter_NullExpectedType() {
        // 加载所有公共静态final字段，不指定类型
        Predicate<Field> filter = field ->
                Modifier.isPublic(field.getModifiers()) &&
                        Modifier.isStatic(field.getModifiers()) &&
                        Modifier.isFinal(field.getModifiers());

        Set<Object> constants = ConstantsLoader.load(MockStringConstants.class.getName(), filter, null);

        assertNotNull(constants, "返回的Set不应为null");
        // 应该包含 String 和 int (int 会被自动装箱为 Integer)
        assertEquals(4, constants.size(), "应加载4个公共静态final常量 (String 和 Integer)");
        assertTrue(constants.contains("ACTIVE"));
        assertTrue(constants.contains("INACTIVE"));
        assertTrue(constants.contains("TYPE_A"));
        assertTrue(constants.contains(5)); // MAX_ATTEMPTS
    }

    @Test
    @DisplayName("测试 loadConstantsWithFilter 方法：加载不存在的类")
    void testLoadConstantsWithFilter_ClassNotFound() {
        String nonExistentClass = "com.nonexistent.AnotherNonExistentClass";
        Predicate<Field> filter = field -> true; // 任意过滤器

        ConstantsLoadException exception = assertThrows(ConstantsLoadException.class, () -> {
            ConstantsLoader.load(nonExistentClass, filter, String.class);
        }, "加载不存在的类应抛出 ConstantsLoadException");

        assertTrue(exception.getMessage().contains("Class not found"), "异常消息应包含 'Class not found'");
        assertTrue(exception.getCause() instanceof ClassNotFoundException, "底层原因应为 ClassNotFoundException");
    }
}
