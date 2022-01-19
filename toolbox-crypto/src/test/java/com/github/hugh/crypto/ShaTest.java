package com.github.hugh.crypto;

import com.github.hugh.util.OkHttpUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AS
 * @date 2021/3/1 13:27
 */
public class ShaTest {


    @Test
    public void test01() throws IOException {
        String appkey = "ygjzNnfrEJ9cJ9PPzgI1cA";
        String timestamp = System.currentTimeMillis() + "";
        String mastersecret = "migfmCyjVwAQBooo45PUs2";
        String sign = ShaUtils.lowerCase256(appkey + timestamp + mastersecret);
        System.out.println("-sign-->>" + sign);
        System.out.println("--timestamp->>" + timestamp);
        Map<String, Object> map = new HashMap<>();
        map.put("sign", sign);
        map.put("timestamp", timestamp);
        map.put("appkey", appkey);
        String s = OkHttpUtils.postJson("https://restapi.getui.com/v2/BrzzkpEOaZ6tehriF2uKL1/auth", map);
        System.out.println("--->>" + s);
    }

    public static void main(String[] args) {
//        SHAUtil sha = new SHAUtil();
        System.out.println("SHA256加密== " + ShaUtils.lowerCase256("123"));
        System.out.println("SHA512加密== " + ShaUtils.lowerCase512("123"));
//        System.out.println("SHA256加密== " + hash("SHA-256", "123"));
    }

    @Test
   void get() throws IOException {
        String appkey = "4a5de70025a7425dabeef6e8ea752976";
        String appSecret = "NeMs1DFG8xFARwZeSlRZwlT22ayY5oIbkgZg1uCziQ3LfSgqcPN4qGydAt7s3jMW";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String signature = "";
//        try {
//            signature = MD5.getMD5((appkey + appSecret + timestamp).getBytes("UTF-8"));
        signature = Md5Utils.lowerCase(appkey + appSecret + timestamp);
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
    void test02(){
        String clientsign = ""; // 客戶端签名
        String appkey =  "";
        String timestamp = String.valueOf(System.currentTimeMillis());
//        try {
//            clientsign = MD5.getMD5((appkey + tok + paymentList.toString() + notifyurl + timestamp).getBytes("UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return ReturnUtils.warn();
//        }
    }
}
