package com.github.hugh.util.annotation;

import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationUtilsTest {


    @Test
    void testGetDbValueFromMethod() {
        MyService myService = new MyService();

        // 使用参数对象进行测试
        AnnotationFieldParams userDbParams = new AnnotationFieldParams(myService, "getUserData", DbChoose.class, "value");
        assertEquals("user_db", AnnotationUtils.getAnnotationField(userDbParams));

        AnnotationFieldParams productDbParams = new AnnotationFieldParams(myService, "getProductData", DbChoose.class, "value");
        assertEquals("product_db", AnnotationUtils.getAnnotationField(productDbParams));

        AnnotationFieldParams otherDbParams = new AnnotationFieldParams(myService, "getOtherData", DbChoose.class, "value");
        assertNull(AnnotationUtils.getAnnotationField(otherDbParams));

        AnnotationFieldParams userDescriptionParams = new AnnotationFieldParams(myService, "getUserData", Description.class, "value");
        assertEquals("获取用户数据", AnnotationUtils.getAnnotationField(userDescriptionParams));

        // 测试方法不存在的情况 (保持原有的测试方式，更直接)
        assertThrows(NoSuchMethodException.class, () -> myService.getClass().getMethod("nonExistentMethod"));

        // 测试空对象
        assertNull(AnnotationUtils.getAnnotationField(new AnnotationFieldParams(null, "getUserData", DbChoose.class, "value")));

        // 测试空方法名
        assertNull(AnnotationUtils.getAnnotationField(new AnnotationFieldParams(myService, "", DbChoose.class, "value")));

        // 测试空注解
        assertNull(AnnotationUtils.getAnnotationField(new AnnotationFieldParams(myService, "getUserData", null, "value")));

        //测试空字段名
        assertNull(AnnotationUtils.getAnnotationField(new AnnotationFieldParams(myService, "getUserData", DbChoose.class, null)));
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface DbChoose {
    String value() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Description {
    String value() default "";
}

class MyService {
    @DbChoose("user_db")
    @Description("获取用户数据")
    public void getUserData() {
        // ...
    }

    @DbChoose("product_db")
    public void getProductData() {
        // ...
    }

    public void getOtherData() {
        // ...
    }
}