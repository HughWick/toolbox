package com.github.hugh.components.entity;

import com.github.hugh.bean.dto.EntityCompareResult;
import com.github.hugh.model.GsonTest;
import com.github.hugh.model.Student;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 实体比较测试类
 */
class EntityCompareTest {

    @Test
    void test01() throws IntrospectionException {
        Student student1 = new Student();
        student1.setName("hugh");
        student1.setAge(18);
        student1.setAccount("Male");
        Student student2 = new Student();
        student2.setName("Jerry");
        student2.setAge(19);
        student2.setAccount("Male");
        List<EntityCompareResult> entityCompareResults = EntityCompare.on(student1, student2).compareProperties();
        assertEquals(2, entityCompareResults.size());
    }


    @Test
    void testEmpty() throws IntrospectionException {
        Student student1 = new Student();
//        student1.setName("hugh");
//        student1.setAge(18);
//        student1.setAccount("Male");
        Student student2 = new Student();
//        student2.setName("Jerry");
//        student2.setAge(19);
//        student2.setAccount("Male");
        List<EntityCompareResult> entityCompareResults = EntityCompare.on(student1, student2 , "").compareProperties();
        assertEquals(0, entityCompareResults.size());
    }

    @Test
    void test02() throws IntrospectionException {
        Student student1 = new Student();
        GsonTest gsonTest = new GsonTest();
        List<EntityCompareResult> entityCompareResults = EntityCompare.on(student1, gsonTest).compareProperties();
        assertEquals(0, entityCompareResults.size());
    }

    @Test
    void testIgnore() throws IntrospectionException, InterruptedException {
        Date date = new Date();
        GsonTest gsonTest1 = new GsonTest();
        gsonTest1.setCode("status");
        gsonTest1.setAge(29);
        GsonTest gsonTest2 = new GsonTest();
        gsonTest2.setCode("status");
        gsonTest2.setAge(28);
        List<EntityCompareResult> entityCompareResults = EntityCompare.on(gsonTest1, gsonTest2, "age").compareProperties();
        assertEquals(0, entityCompareResults.size());
        gsonTest1.setCreated(date);
        Thread.sleep(500);
        gsonTest2.setCreated(new Date());
        List<EntityCompareResult> entityCompareResults2 = EntityCompare.on(gsonTest1, gsonTest2 ).compareProperties();
        assertEquals(2, entityCompareResults2.size());
    }
}
