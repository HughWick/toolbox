package com.github.hugh.http;

import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.Jsons;
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
        String toJson = GsonUtils.toJson(map);
        assertEquals(toJson, GsonUtils.parse(toJson).toString());
        String result1 = "localhost:8020?a=1&array=%5Ba%5D&array_ori=%5B%22a%22%5D";
        assertEquals(result1, UrlUtils.urlParam("localhost:8020", map));
        assertEquals("", UrlUtils.urlParam("", map));
        assertEquals(result1, UrlUtils.urlParam(result1, null));
    }

    @Test
    void test02() {
        assertEquals("", UrlUtils.jsonParse(null));
        assertEquals("", UrlUtils.jsonParse("{}"));
    }

    @Test
    void testParse() {
        String url1 = "result=0&balance=40967&linkid=D38468C762754406A8ECEE0D151A578B&description=%e5%8f%91%e9%80%81%e7%9f%ad%e4%bf%a1%e6%88%90%e5%8a%9f";
        Map<String, String> stringStringMap = UrlUtils.parseUrl(url1);
        assertEquals("0", stringStringMap.get("result"));
        assertEquals("发送短信成功", stringStringMap.get("description"));
        assertEquals("D38468C762754406A8ECEE0D151A578B", stringStringMap.get("linkid"));
    }

    @Test
    void testParseJson() {
        String url1 = "result=13&description=%e7%ad%be%e5%90%8d%e4%b8%8d%e6%ad%a3%e7%a1%ae%ef%bc%8c%e8%af%b7%e4%bf%ae%e6%94%b9%e5%90%8e%e5%8f%91%e9%80%81%ef%bc%81%e7%ad%be%e5%90%8d%e6%a0%bc%e5%bc%8f%ef%bc%9a%e4%be%8b%e5%a6%82%e3%80%90%e6%9f%90%e6%9f%90%e5%85%ac%e5%8f%b8%e3%80%91%ef%bc%8c%e5%ad%97%e6%95%b03-8%e4%b8%aa%e5%ad%97%ef%bc%8c%e6%9c%89%e9%97%ae%e9%a2%98%e8%af%b7%e8%81%94%e7%b3%bb%e5%ae%a2%e6%9c%8d%e4%b8%ad%e5%bf%83%e3%80%82";
        Jsons jsons = UrlUtils.parseUrlJson(url1);
        assertEquals("13", jsons.getString("result"));
        assertEquals("签名不正确，请修改后发送！签名格式：例如【某某公司】，字数3-8个字，有问题请联系客服中心。", jsons.getString("description"));
        assertEquals(null, jsons.getString("linkid"));
    }
}
