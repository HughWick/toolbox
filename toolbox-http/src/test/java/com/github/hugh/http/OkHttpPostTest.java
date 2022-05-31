package com.github.hugh.http;

import com.github.hugh.json.gson.JsonObjects;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * okhttp 工具 post请求方式测试类
 */
@Slf4j
public class OkHttpPostTest {

    @Test
    void postFormReJsonObjectsTest() throws IOException {
        String ymUrl = "https://sudo.191ec.com/silver-web-shop/manual/readInfo2";
        Map<String, Object> params = new HashMap<>();
        params.put("size", 1);
        var map = new HashMap<>();
        map.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        System.out.println("===3>>" + OkHttpUtils.postFormReJsonObjects(ymUrl));
        System.out.println("===4>>" + OkHttpUtils.postFormReJsonObjects(ymUrl, map));
        var data = new HashMap<>();
        data.put("appid", 2020114837);
        data.put("appsecret", "1f091a6d2ad111ebbd3400163e0b8359");
        String httpTop = "https://api.wl1688.net/iotc/getway ";
        System.out.println("===5>>" + OkHttpUtils.postJsonReJsonObjects(httpTop));
        System.out.println("===6>>" + OkHttpUtils.postJsonReJsonObjects(httpTop, data));
    }

    @Test
    void postFromTest() throws IOException {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        log.info(OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", objectObjectHashMap));
        log.info(OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2"));
        OkHttpClient okHttpClient = OkHttpUtils.buildClient(15, 15);
        log.info(OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", objectObjectHashMap, okHttpClient));
//        log.error(OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", JSONObject.fromObject(objectObjectHashMap)));
//        log.error(OkHttpUtils.postForm("", JSONObject.fromObject(objectObjectHashMap)));
    }

    @Test
    void postJson() {
        String str = "https://sudo.191ec.com/silver-web-shop/manual/readInfo2";
        Map<String, Object> map = new HashMap<>();
        map.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        Map<String, Object> head = new HashMap<>();
        head.put("a", "1");
//        JSONObject json = new JSONObject();
//        json.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
//        try {
//            System.out.println("-postJson-1>" + OkHttpUtils.postJson("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
//            System.out.println("-postJson-2>>" + OkHttpUtils.postJson("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json.toString()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void postCookieTest() throws IOException {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        String s0 = OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", objectObjectHashMap);
        System.out.println(s0);
        String url = "http://hyga1.hnlot.com.cn:8000/v2/user/userLogin/login";
        String url2 = "http://hyga1.hnlot.com.cn:8000/v2/user/user/getLoginUser";
        Map<String, Object> map = new HashMap<>();
        map.put("userAccount", "fengtao");
        map.put("userPassword", "88888888");
        String s = OkHttpUtils.postFormCookie(url, map);
        System.out.println(s);
        JsonObjects byCookieReJsonObjects = OkHttpUtils.getByCookieReJsonObjects(url2);
        System.out.println("--->>" + byCookieReJsonObjects);
    }
}
