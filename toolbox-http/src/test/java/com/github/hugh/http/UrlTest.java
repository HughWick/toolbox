package com.github.hugh.http;

import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.Jsons;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        assertNull(jsons.getString("linkid"));
    }


    // 测试常规的 URL 参数解析，默认使用 UTF-8 编码
    @Test
    void testParseUrlWithDefaultEncoding() {
        String urlParams = "key1=value1&key2=value2&key3=value3";
        Map<String, String> result = UrlUtils.parseUrl(urlParams);
        // 验证解析结果
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertEquals("value3", result.get("key3"));
    }

    // 测试 URL 参数中包含空值
    @Test
    void testParseUrlWithEmptyValue() {
        String urlParams = "key1=value1&key2=&key3=value3";
        Map<String, String> result = UrlUtils.parseUrl(urlParams);
        // 验证解析结果
        assertEquals(3, result.size());
        assertEquals("value1", result.get("key1"));
        assertEquals("", result.get("key2")); // 空值
        assertEquals("value3", result.get("key3"));
    }

    // 测试 URL 参数中的编码情况
    @Test
    void testParseUrlWithEncodedCharacters() {
        String urlParams = "key1=value%201&key2=value%2B2&key3=value%23";
        Map<String, String> result = UrlUtils.parseUrl(urlParams);

        // 验证解码后的值
        assertEquals(3, result.size());
        assertEquals("value 1", result.get("key1"));
        assertEquals("value+2", result.get("key2"));
        assertEquals("value#", result.get("key3"));
    }

    // 测试空的 URL 参数字符串
    @Test
    void testParseUrlWithEmptyString() {
        String urlParams = "";
        Map<String, String> result = UrlUtils.parseUrl(urlParams);
        // 验证返回一个空的 Map
        assertTrue(result.isEmpty());
    }

    // 测试 null 的 URL 参数字符串
    @Test
    void testParseUrlWithNullString() {
        Map<String, String> result = UrlUtils.parseUrl(null);
        // 验证返回一个空的 Map
        assertTrue(result.isEmpty());
    }

    // 测试 URL 参数中有多个相同的 key（例如：key1=value1&key1=value2）
    @Test
    void testParseUrlWithMultipleSameKeys() {
        String urlParams = "key1=value1&key1=value2";
        Map<String, String> result = UrlUtils.parseUrl(urlParams);
        // 验证返回的 Map 只有最后一个值
        assertEquals(1, result.size());
        assertEquals("value2", result.get("key1")); // 只有最后一个值会保留
    }

    // 测试自定义编码格式（例如 ISO-8859-1）
    @Test
    void testParseUrlWithCustomEncoding() {
        String urlParams = "key1=value%20with%20spaces&key2=normalvalue";
        String encoding = "ISO-8859-1";
        Map<String, String> result = UrlUtils.parseUrl(urlParams, encoding);
        // 验证解码后的值
        assertEquals(2, result.size());
        assertEquals("value with spaces", result.get("key1"));
        assertEquals("normalvalue", result.get("key2"));
    }

    // 测试自定义编码格式（异常情况，当编码不支持时）
    @Test
    void testParseUrlWithUnsupportedEncoding() {
        String urlParams = "key1=value1&key2=value2";
        String encoding = "unsupported-encoding"; // 非法编码格式
        // 验证抛出 IllegalArgumentException 异常
        assertThrows(IllegalArgumentException.class, () -> {
            UrlUtils.parseUrl(urlParams, encoding);
        });
    }
}
