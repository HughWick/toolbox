package com.github.hugh.support.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hugh.model.Person;
import org.junit.jupiter.api.Test;

/**
 * @author hugh
 * @version 1.0.0
 */
public class JSONTest {

    @Test
    void test01() {
        String json = "{\"id\":\"1\",\"name\":\"Json技术\"}";
        JSONObject obj = JSON.parseObject(json);
        System.out.println("--->>" + obj.get("id"));
        System.out.println("--->>" + obj.get("id2"));
    }

    @Test
    void test02() {
        String str2 = "{\"age\":2,\"amount\":15.14,\"birthday\":null,\"create\":null,\"id\":null,\"name\":\"张三\",\"create\":\"2019-04-06\"}";
//        JSONObject obj = JSON.parseObject(str2);
        Person person1 = JSON.parseObject(str2, Person.class);
        System.out.println(person1);
        System.out.println("-1--实体转json>>" + JSON.toJSONString(person1));
        var person2 = new Person();
//        student2.setName("张三");
//        student2.setAge(2222);
        System.out.println("-2--实体转json>>" + JSON.toJSONString(person2));
    }
}
