package com.github.hugh.json.alibaba;

import com.alibaba.fastjson.JSON;
import com.github.hugh.json.gson.JsonObjectUtils;
import com.github.hugh.json.model.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * 阿里 fastjson 测试类
 * User: AS
 * Date: 2022/8/23 11:40
 */
public class JsonTest {

    @Test
    void testDate() {
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"system\":1625024713000,\"id\":1,\"name\":\"张三\",\"create\":\"1625024713000\"}";
        Student student1 = JsonObjectUtils.fromJsonTimeStamp(str, Student.class);
//        System.out.println(student1.toString());
        String s1 = JsonObjectUtils.toJsonTimestamp(student1);
        String s2 = JSON.toJSONString(student1);
        System.out.println("--->>>" + s1);
        System.out.println("---json>><" + s2);
        System.out.println("---333>><" + JsonObjectUtils.toJsonTimestamp(new Date()));
        Assertions.assertEquals(s1, s2);
    }
}
