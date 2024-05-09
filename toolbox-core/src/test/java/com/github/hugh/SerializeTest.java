package com.github.hugh;

import com.github.hugh.model.Student;
import com.github.hugh.util.MapUtils;
import com.github.hugh.util.SerializeUtils;
import org.junit.jupiter.api.Test;

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
//            System.out.println("-1-->" + Arrays.toString(bytes));
            assertEquals(str1, Arrays.toString(bytes));
            Student student = (Student) SerializeUtils.toObject(bytes);
//            System.out.println("--->" + student);
//            System.out.println("--->" + GsonU.fromObject(student));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
