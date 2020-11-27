package com.github.hugh;

import com.github.hugh.util.OkHttpUtils;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author AS
 * @date 2020/8/31 16:41
 */
public class OkHttpTest {

    @Test
    public void test01() {
        JSONObject json = new JSONObject();
        json.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        try {
            for (int i = 0; i < 10000; i++) {
                System.out.println(i + "--1->>" + OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
            }
            System.out.println("--2>>" + OkHttpUtils.postJson("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
            System.out.println("--3->>" + OkHttpUtils.postFormReJSON("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
            System.out.println("--4->>" + OkHttpUtils.get("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
            System.out.println("--5->>" + OkHttpUtils.postFormReJsonObject("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test02() throws InterruptedException {
        JSONObject params = new JSONObject();
        params.put("ZH", "admin");
        params.put("MM", "88888888");
        try {
            System.out.println("--1->>" + OkHttpUtils.postFormCookie("https://www.hnlot.com.cn/ptpz/yonghu/login", params));
            Thread.sleep(5000);
            System.out.println("--2->>" + OkHttpUtils.postFormCookie("https://www.hnlot.com.cn/ptpz/juese/selectJueSe", params));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test03() throws IOException {
        String url = "http://localhost:7900/testHeader";
        JSONObject json = new JSONObject();
        Map<String, String> headerContent = new HashMap<>();
        headerContent.put("test_token", UUID.randomUUID().toString());
        headerContent.put("test_token_02", UUID.randomUUID().toString());
        headerContent.put("int_test", "123");
        System.out.println("--->" + OkHttpUtils.postForm(url, json, headerContent));
    }
}
