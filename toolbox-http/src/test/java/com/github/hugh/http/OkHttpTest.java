package com.github.hugh.http;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author AS
 * @date 2020/8/31 16:41
 */
//@Slf4j
//public class OkHttpTest {

//    @Test
//    public void test03() throws IOException {
//        String url = "http://localhost:7900/testHeader";
//        JSONObject json = new JSONObject();
//        Map<String, String> headerContent = new HashMap<>();
//        headerContent.put("test_token", UUID.randomUUID().toString());
//        headerContent.put("test_token_02", UUID.randomUUID().toString());
//        headerContent.put("int_test", "123");
//        System.out.println("--->" + OkHttpUtils.postForm(url, json, headerContent));
//    }

//    @Test
//    public void test05() throws Exception {
//        JSONObject json = new JSONObject();
//        val receiveUser = new ArrayList<>();
//        receiveUser.add("234260245@qq.com");
//        receiveUser.add("1378321226@qq.com");
//        receiveUser.add("729076704@qq.com");
//        json.put("recipient", receiveUser);
//        json.put("title", "测试标题");
//        json.put("content", "文本内容");
//        json.put("template", "模板0001");
//        System.out.println("---->" + json.toString());
//        String s = OkHttpUtils.postJson("http://localhost:7010/email/email/sendTemplates", json);
//        System.out.println("--->>" + s);
//    }

//    public static OkHttpClient buildClient(int connectTimeout, int readTimeout) {
//        OkHttpClient client = Instance.getInstance().singleton(OkHttpClient.class);
//        client.newBuilder().connectTimeout(connectTimeout, TimeUnit.SECONDS)
//                .readTimeout(readTimeout, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)
//                .build();
//        return client;
//    }
//}
