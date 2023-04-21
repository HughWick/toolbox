package com.github.hugh;

import com.github.hugh.bean.dto.EntityCompare;
import com.github.hugh.model.Student;
import com.github.hugh.model.Student1;
import com.github.hugh.util.EntityCompareUtils;
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
        assertEquals("[EntityCompare(fieldName=age, oldValue=18, newValue=19), EntityCompare(fieldName=name, oldValue=Tom, newValue=Jerry)]", EntityCompareUtils.compare(p1, p2, null).toString());
        final List<EntityCompare> age = EntityCompareUtils.compare(p1, p2, "age");
        assertEquals("[EntityCompare(fieldName=name, oldValue=Tom, newValue=Jerry)]", age.toString());
    }


    @Test
    void testCompareNotClass() {
        Student p1 = new Student();
        Student1 p2 = new Student1();
        final List<EntityCompare> compare = EntityCompareUtils.compare(p1, p2);
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
        final List<EntityCompare> entityCompareList = EntityCompareUtils.compare(p1, p2);
        String str1 = "[EntityCompare(fieldName=list, oldValue=null, newValue=[]), EntityCompare(fieldName=name, oldValue=Tom, newValue=Jerry)]";
        assertEquals(str1, entityCompareList.toString());
    }

    @Test
    void testData() {
        final Date date = new Date();
        Student p1 = new Student();
        p1.setCreate(date);
        Student p2 = new Student();
        p2.setCreate(date);
        final List<EntityCompare> compare = EntityCompareUtils.compare(p1, p2);
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
