package com.github.hugh.http;

import com.github.hugh.json.gson.JsonObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * URL 工具类测试
 */
class UrlTest {

    @Test
    void testUrlParam() {
        String[] array = new String[1];
        array[0] = "a";
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("array", Arrays.toString(array));
        map.put("array_ori", array);
//        System.out.println(JSONObject.fromObject(map));
        String toJson = JsonObjectUtils.toJson(map);
        assertEquals(toJson, JsonObjectUtils.parse(toJson).toString());
        String result1 = "localhost:8020?a=1&array=%5Ba%5D&array_ori=%5B%22a%22%5D";
        assertEquals(result1, UrlUtils.urlParam("localhost:8020", map));
        assertEquals("", UrlUtils.urlParam("", map));
    }

    @Test
    void test02(){
        assertEquals("", UrlUtils.jsonParse(null));
        assertEquals("", UrlUtils.jsonParse("{}"));
    }
}
