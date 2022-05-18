package com.github.hugh.json;

import com.github.hugh.json.gson.JsonObjectUtils;
import com.github.hugh.json.gson.JsonObjects;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonObjectTest {

    @Test
    void parseJsonObjectTest(){
        List<Object> list = new ArrayList();
        list.add("list");
        list.add("list1");
        list.add("list2");
        String[] array = new String[1];
        array[0] = "a";
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map.put("a", 1);
        map.put("array", array);
        map.put("list", list);
        map2.put("map","sajkh");
        map.put("map2", map2);
        System.out.println(JsonObjectUtils.toJson(map));
        System.out.println(new JsonObjects(map));
    }
}
