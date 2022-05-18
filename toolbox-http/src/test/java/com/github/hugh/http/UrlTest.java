package com.github.hugh.http;

import com.github.hugh.json.gson.JsonObjectUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UrlTest {

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
        System.out.println(toJson);
        System.out.println(JsonObjectUtils.parse(toJson));

        String s = UrlUtils.urlParam("localhost:8020", map);
        System.out.println("--->>" + s);
    }
}
