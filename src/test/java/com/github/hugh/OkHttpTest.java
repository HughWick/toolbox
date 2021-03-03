package com.github.hugh;

import com.github.hugh.util.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
@Slf4j
public class OkHttpTest {

    @Test
    public void test01() {
        JSONObject json = new JSONObject();
        json.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        try {
//            for (int i = 0; i < 10000; i++) {
//                System.out.println(i + "--1->>" + OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
//            }
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

    @Test
    public void test04() throws Exception {
        String url = "http://localhost:7010/email/email/send";
        val params = new HashMap<>();
        params.put("recipient", "136438455@qq.com");
        params.put("title", "标题");
        val fileMap = new HashMap<>();
        fileMap.put("鬼灭之刃1.jpg", "D:\\OneDrive\\图片\\鬼灭之刃\\78133846_p0.jpg");
        fileMap.put("鬼灭之刃2.jpg", "D:\\OneDrive\\图片\\鬼灭之刃\\78382590_p4.jpg");
        fileMap.put("鬼灭之刃3.jpg", "D:\\OneDrive\\图片\\鬼灭之刃\\79733019_p0.jpg");
        String result = OkHttpUtils.upload(url, params, "file", fileMap);

        System.out.println("--->" + result);
    }

    public void tem() throws IOException {
        JSONObject json = new JSONObject();
        json.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        log.info(OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
        log.debug(OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
        log.error(OkHttpUtils.postForm("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json));
    }


    public static void main(String[] args) throws IOException {
        OkHttpTest test = new OkHttpTest();
        test.tem();
//        val map =  new HashMap<>();
//        map.put("1",1);
//        map.put(1,1);
//        System.out.println("--->>"+map);
//        var str = "123c";
//        str = "abcdef";
//        System.out.println("--->>"+str);
//        val map = new HashMap<>();
//        val map2 = new JSONObject();
//        map.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
//        map2.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
//        System.out.println("--1->>" + OkHttpUtils.postFormReJsonObject("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", map));
//        System.out.println("--2->>" + OkHttpUtils.postFormReJsonObject("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", map2));

    }
}
