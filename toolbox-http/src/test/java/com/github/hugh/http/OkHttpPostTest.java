package com.github.hugh.http;

import com.github.hugh.json.gson.JsonObjects;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * okhttp 工具 post请求方式测试类
 */
@Slf4j
class OkHttpPostTest {

    @Test
    void postFormReJsonObjectsTest() throws IOException {
        String ymUrl = "https://sudo.191ec.com/silver-web-shop/manual/readInfo2";
        var map = new HashMap<>();
        map.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        JsonObjects jsonObjects1 = OkHttpUtils.postFormReJsonObjects(ymUrl);
        assertEquals("-5", jsonObjects1.getString("status"));
        JsonObjects jsonObjects2 = OkHttpUtils.postFormReJsonObjects(ymUrl, map);
        assertEquals("1", jsonObjects2.getString("status"));
    }

    @Test
    void testPostJson() throws IOException {
        String httpTop = "https://api.wl1688.net/iotc/getway ";
        JsonObjects jsonObjects1 = OkHttpUtils.postJsonReJsonObjects(httpTop);
        assertEquals(400, jsonObjects1.getInt("status"));
        var data = new HashMap<>();
        data.put("appid", 2020114837);
        data.put("appsecret", "1f091a6d2ad111ebbd3400163e0b8359");
        JsonObjects jsonObjects2 = OkHttpUtils.postJsonReJsonObjects(httpTop, data);
        assertEquals(10001, jsonObjects2.getInt("code"));
    }

    @Test
    void postFromTest() throws IOException {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        String s1 = OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", objectObjectHashMap);
        assertEquals("1", new JsonObjects(s1).getString("status"));
        String s2 = OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2");
        assertEquals("-5", new JsonObjects(s2).getString("status"));
        assertEquals("请求参数不能为空!", new JsonObjects(s2).getString("msg"));
        OkHttpClient okHttpClient = OkHttpUtils.buildClient(15, 15);
        String s3 = OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", objectObjectHashMap, okHttpClient);
        assertEquals("1", new JsonObjects(s3).getString("status"));
    }

    @Test
    void postJson() {
        String str = "https://sudo.191ec.com/silver-web-shop/manual/readInfo2";
        Map<String, Object> map = new HashMap<>();
        map.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        Map<String, Object> head = new HashMap<>();
        head.put("a", "1");
    }

    // 测试cookie 请求
    @Test
    void postCookieTest() throws IOException {
        String url = "https://cmmop.hnlot.com.cn/v2/user/userLogin/login";
        String url2 = "https://cmmop.hnlot.com.cn/v2/user/user/getLoginUser";
        Map<String, Object> map = new HashMap<>();
        map.put("userAccount", "mushi");
        map.put("userPassword", "8566889");
        String s = OkHttpUtils.postFormCookie(url, map);
        assertEquals("0000" , new JsonObjects(s).getString("code"));
        JsonObjects byCookieReJsonObjects = OkHttpUtils.getByCookieReJsonObjects(url2);
        assertEquals("0000" , byCookieReJsonObjects.getString("code"));
    }
}
