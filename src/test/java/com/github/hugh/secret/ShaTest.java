package com.github.hugh.secret;

import com.github.hugh.util.OkHttpUtils;
import com.github.hugh.util.secrect.ShaUtils;
import org.junit.Test;

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
}
