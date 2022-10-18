package com.github.hugh.http;

import com.github.hugh.json.gson.JsonObjects;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Okhttp get请求测试
 */
public class OkHttpGetTest {

    @Test
    void getTest() throws IOException {
        String str = "https://sudo.191ec.com/silver-web-shop/manual/readInfo2";
        var map = new HashMap<>();
        map.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        JsonObjects json = new JsonObjects();
        json.addProperty("recipientAddr", "湖南省湘西土家族苗族自治州花垣县花垣镇花垣县公安局农业园警务执勤室");
        System.out.println("get-1->>" + OkHttpUtils.get("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json.toJson()));
        System.out.println("get-2->>" + OkHttpUtils.get("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", map));
//        System.out.println("get===8>>" + OkHttpUtils.getReJsonObjects(str, json));
        Map<String, Object> params = new HashMap<>();
        params.put("size", 1);
        String str2 = "http://factory.hnlot.com.cn/v2/contracts/find";
        System.out.println("===1>>" + OkHttpUtils.getReJsonObjects(str2));
        System.out.println("===2>>" + OkHttpUtils.getReJsonObjects(str2, params));
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
        System.out.println("--->>" + str);
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
        OkHttpUtils.getReJsonObjects("https://dev.hnlot.com.cn/v2/business/problem/getList" , map);
    }

}
