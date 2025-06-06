package com.github.hugh.util;

import com.github.hugh.bean.dto.EntityCompareResult;
import com.github.hugh.model.Student;
import com.github.hugh.model.StudentTarget;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 实体比较工具类测试用例
 *
 * @author hugh
 */
class EntityCompareUtilsTest {

    @Test
    void testCompare() {
        Student p1 = new Student();
        p1.setName("Tom");
        p1.setAge(18);
        p1.setAccount("Male");
//        p1.setList(null);
        Student p2 = new Student();
        p2.setName("Jerry");
        p2.setAge(19);
        p2.setAccount("Male");
//        p2.setList(new ArrayList<Student>());
        assertEquals("[EntityCompareResult(fieldName=age, oldValue=18, newValue=19), EntityCompareResult(fieldName=name, oldValue=Tom, newValue=Jerry)]", EntityCompareUtils.compare(p1, p2, null).toString());
        final List<EntityCompareResult> age = EntityCompareUtils.compare(p1, p2, "age");
        assertEquals("[EntityCompareResult(fieldName=name, oldValue=Tom, newValue=Jerry)]", age.toString());
    }


    @Test
    void testCompareNotClass() {
        Student p1 = new Student();
        StudentTarget studentTarget = new StudentTarget();
        final List<EntityCompareResult> compare = EntityCompareUtils.compare(p1, studentTarget);
        assertTrue(compare.isEmpty());
    }

    @Test
    void testNull() {
        Student p1 = new Student();
        p1.setName("Tom");
//        p1.setAge(18);
        p1.setAccount("Male");
        p1.setList(null);
        Student p2 = new Student();
        p2.setName("Jerry");
//        p2.setAge(19);
        p2.setAccount("Male");
        p2.setList(new ArrayList<Student>());
        final List<EntityCompareResult> entityCompareList = EntityCompareUtils.compare(p1, p2);
        String str1 = "[EntityCompareResult(fieldName=list, oldValue=null, newValue=[]), EntityCompareResult(fieldName=name, oldValue=Tom, newValue=Jerry)]";
        assertEquals(str1, entityCompareList.toString());
    }

    @Test
    void testData() {
        final Date date = new Date();
        Student p1 = new Student();
        p1.setCreate(date);
        Student p2 = new Student();
        p2.setCreate(date);
        final List<EntityCompareResult> compare = EntityCompareUtils.compare(p1, p2);
        assertTrue(compare.isEmpty());
    }

    @Test
    void testSameObj() {
        final Date date = new Date();
        Student p1 = new Student();
        p1.setCreate(date);
        Student p2 = new Student();
        p2.setCreate(date);
        assertTrue(EntityCompareUtils.isObjSame(p1, p2));
    }

    @Test
    void testIsDiffObj() {
        final Date date = new Date();
        Student p1 = new Student();
        p1.setAccount("");
        p1.setCreate(date);
        Student p2 = new Student();
        p2.setCreate(new Date());
        assertTrue(EntityCompareUtils.isObjDiff(p1, p2));
    }
}
