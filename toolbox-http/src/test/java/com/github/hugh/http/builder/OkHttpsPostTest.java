package com.github.hugh.http.builder;

import com.github.hugh.json.gson.Jsons;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OkHttpsPostTest {
    @Test
    void testPostJson() throws IOException {
        String httpTop = "https://api.wl1688.net/iotc/getway ";
        OkHttpsResponse okHttpsResponse1 = OkHttps.url(httpTop).doPostJson();
        Jsons jsons1 = okHttpsResponse1.toJsons();
        assertEquals("10001", jsons1.getString("code"));
        Map<String, Object> data = new HashMap<>();
        data.put("appid", 2020114837);
        data.put("appsecret", "1f091a6d2ad111ebbd3400163e0b8359");
        OkHttpsResponse okHttpsResponse2 = OkHttps.url(httpTop).setBody(data).doPostJson();
        Jsons jsons2 = okHttpsResponse2.toJsons();
//        JsonObjects jsonObjects2 = OkHttpUtils.postJsonReJsonObjects(httpTop, data);
        assertEquals(10001, jsons2.getInt("code"));
    }
}
