package com.github.hugh.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * sha 测试
 *
 * @author AS
 * @date 2021/3/1 13:27
 */
class ShaTest {

    @Test
    void testSha() {
        //        final String md5Sign = Md5Utils.lowerCase(appkey + timestamp + masterSecret);
//        System.out.println("-md5Sign-->>" + md5Sign);
        String appkey = "ygjzNnfrEJ9cJ9PPzgI1cA";
        String timestamp = "1669886260352";
        String masterSecret = "migfmCyjVwAQBooo45PUs2";
        String sign = ShaUtils.lowerCase256(appkey + timestamp + masterSecret);
        assertEquals(64, sign.length());
        assertEquals("c19c433a5933f1de7ca9dd8fb30fbcc97c1f03e2f4fb2aa3e5e0c839b0460e17", sign);
        final String str2 = ShaUtils.lowerCase512("123");
        assertEquals(128, str2.length());
        assertEquals("3c9909afec25354d551dae21590bb26e38d53f2173b8d3dc3eee4c047e7ab1c1eb8b85103e3be7ba613b31bb5c9c36214dc9f14a42fd7a2fdb84856bca5c44c2", str2);
//        System.out.println("--str2->>" + str2);
//        Map<String, Object> map = new HashMap<>();
//        map.put("sign", sign);
//        map.put("timestamp", timestamp);
//        map.put("appkey", appkey);
//        String s = OkHttpUtils.postJson("https://restapi.getui.com/v2/BrzzkpEOaZ6tehriF2uKL1/auth", map);
//        System.out.println("--->>" + s);
    }
}
