package com.github.hugh.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * @author hugh
 * @version 1.0.0
 */
public class JSONTest {

    @Test
    public void test01() {
        String json = "{\"id\":\"1\",\"name\":\"Json技术\"}";
        JSONObject obj = JSON.parseObject(json);
        System.out.println("--->>" + obj.get("id"));
        System.out.println("--->>" + obj.get("id2"));
    }
}
