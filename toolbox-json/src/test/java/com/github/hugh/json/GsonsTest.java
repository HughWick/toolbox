package com.github.hugh.json;

import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.JsonObjects;
import com.github.hugh.json.gson.Jsons;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoogleTest {
    @Test
    void testNewJsonObjects() {

//        String str = "{woman={name=dc, age=1}, name=账上的, sex_in=a,b,d}";
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map.put("name", "账上的");
        map.put("sex_in", "a,b,d");
        map.put("birthday", System.currentTimeMillis());
        map.put("create", "2022-01-10 22:33:10");
        map.put("testList", "1,2,3");
        map2.put("age", 1);
        map2.put("name", "dc");
        map.put("woman", map2);
//        System.out.println("--1-map>>" + map.toString());
        Jsons jsons = new Jsons(map);
        assertEquals(GsonUtils.toJson(map), jsons.toJson());
//        System.out.println(jsonObjects);
//        Assertions.assertO(new JsonObjects() , jsonObjects);
//        Map<String, String> reconstructedUtilMap = Arrays.stream(str2.split(","))
//                .map(s -> s.split("="))
//                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        //        String str2 = "{birthday=1666145184398, testList=1,2,3, woman={name=dc, age=1}, name=账上的, create=2022-01-10 22:33:10, sex_in=a,b,d}";
//        JsonObjects jsonObjects2 = new JsonObjects(str2);
//        Gson gson = new Gson();
//        Map<String, Object> map3 = new HashMap<>();
//        map3 = gson.fromJson(str, map.getClass());
//        System.out.println("==2==>>" + JSON.parseObject(str2, HashMap.class).toString());
    }
}
