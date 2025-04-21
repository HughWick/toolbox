package com.github.hugh.http;

import com.github.hugh.json.gson.JsonObjects;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

/**
 * Okhttp get请求测试
 */
class OkHttpGetTest {

    @Test
    void testGet() throws IOException {
        String url = "https://sudo.191ec.com/silver-web-shop/manual/readInfo2";
        JsonObjects json = new JsonObjects();
        json.addProperty("recipientAddr", "湖南省湘西土家族苗族自治州花垣县花垣镇花垣县公安局农业园警务执勤室");
        String s1 = OkHttpUtils.get(url, json.toJson());
        var map = new HashMap<>();
        map.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        String s2 = OkHttpUtils.get(url, map);
        assertEquals("1", new JsonObjects(s1).getString("status"));
        assertEquals("1", new JsonObjects(s2).getString("status"));
        String result3 = OkHttpUtils.get(url);
        assertEquals("-5", new JsonObjects(result3).getString("status"));
    }

    @Test
    void testGetTimeout() {
        String url = "https://www.google.com";
        final SocketTimeoutException socketTimeoutException = assertThrowsExactly(SocketTimeoutException.class, () -> {
            OkHttpUtils.get(url);
        });
        final SocketTimeoutException socketTimeoutException2 = assertThrowsExactly(SocketTimeoutException.class, () -> {
            OkHttpUtils.getReJsonObjects(url, null, 5);
        });
        assertEquals("Connect timed out", socketTimeoutException.getMessage());
        assertEquals("Connect timed out", socketTimeoutException2.getMessage());
    }

    @Test
//    @Timeout(value = 11)
    void testGetTimeout2() throws IOException {
        String url = "https://www.google.com";
//        String result3 = OkHttpUtils.get(url, 8);
        final SocketTimeoutException socketTimeoutException = assertThrowsExactly(SocketTimeoutException.class, () -> {
            String result3 = OkHttpUtils.get(url, 8);
        });
        assertEquals("Connect timed out", socketTimeoutException.getMessage());
    }

    @Test
    void testGetReJsonObjects() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("size", 1);
        String url2 = "http://factory.hnlot.com.cn/v2/contracts/find";
        final String s = OkHttpUtils.get(url2);
        assertEquals(new JsonObjects(s).toJson(), OkHttpUtils.getReJsonObjects(url2).toJson());
        assertEquals(new JsonObjects(OkHttpUtils.get(url2 + "?size=1")).toJson(), OkHttpUtils.getReJsonObjects(url2, params).toJson());
    }


    @Test
    void get() throws IOException {
        String appkey = "4a5de70025a7425dabeef6e8ea752976";
        String appSecret = "NeMs1DFG8xFARwZeSlRZwlT22ayY5oIbkgZg1uCziQ3LfSgqcPN4qGydAt7s3jMW";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = "";
//        try {
//            signature = MD5.getMD5((appkey + appSecret + timestamp).getBytes("UTF-8"));
//        signature = Md5Utils.lowerCase(appkey + appSecret + timestamp);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        Map<String, Object> params = new HashMap<>();
        params.put("appkey", appkey);
        params.put("timestamp", timestamp);
        params.put("signature", signature);
        params.put("type", "oauth_token");
//        String resultStr = YmHttpUtil.HttpPost(UrlConfig.YIN_MENG_TOKEN, params);
        String str = OkHttpUtils.postForm("https://ym.191ec.com/silver-web/oauth/token", params);
        assertEquals(-2 , new JsonObjects(str).getInt("status"));
//        System.out.println("--->>" + str);
//        if (StringEmptyUtils.isNotEmpty(resultStr)) {
//            return JSONObject.fromObject(resultStr);
//        } else {
//            return ReturnUtils.error("请求获取tok失败,服务器繁忙！");
//        }
    }

    @Test
    void testGet2() throws IOException {
        String url = "https://ym.191ec.com/silver-web-shop/vshop/getCMTotal";
        Map<String, String> params = new HashMap<>();
//        params.put("Cookie:","SESSION=efd29682-fbb7-49c1-96e3-4ecb5dd0e311");
        String s = OkHttpUtils.get(url, null, params);
        System.out.println(s);
    }


    @Test
    void testGetIn() throws IOException {
//        https://dev.hnlot.com.cn/v2/business/problem/getList?problemType_in=6,7,30,31&status=0
        String url = "";
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", "1,2");
        JsonObjects reJsonObjects = OkHttpUtils.getReJsonObjects("https://dev.hnlot.com.cn/v2/business/problem/getList", map);
        System.out.println(reJsonObjects);
    }

//    @Test
//    void testlogs() throws IOException {
//        Map<String, Object> map = new HashMap<>();
//        Map<String, Object> map2 = new HashMap<>();
//        map.put("name", "账上的");
//        map.put("sex_in", "a,b,d");
//        map.put("birthday", System.currentTimeMillis());
//        map.put("create", "2022-01-10 22:33:10");
//        map.put("testList", "1,2,3");
//        map2.put("age", 1);
//        map2.put("name", "dc");
//        map.put("woman", map2);
//        System.out.println(map);
//        System.out.println(JsonObjectUtils.toJson(map));
//        String s = OkHttpUtils.get("http://localhost:7900/user/json", map);
//        System.out.println(s);
//        String s2 = OkHttpUtils.get("http://localhost:7900/user/object", map);
//        String s = OkHttpUtils.get("http://localhost:7900/user/json?name=as&woman={\"age\":\"1\"}" );
//    }

}
