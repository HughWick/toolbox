package com.github.hugh.http;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * User: AS
 * Date: 2022/5/19 13:01
 */
public class OkHttpUpdateFileTest {

    @Test
    void testUploadFile() throws Exception {
//        String url = "http://localhost:7010/email/email/send";
        String url = "https://gateway.hnlot.com.cn/email/email/send";
        var params = new HashMap<>();
//        String[] array = new String[1];
//        array[0] = "136438455@qq.com";
        params.put("recipient", "136438455@qq.com");
        params.put("orderId", "orderId流水号123");
        params.put("title", "标题");
        params.put("content", "文本内容");
        params.put("appkey", "278377e1fb7f47c18691b9c33a3fd80e");
        var fileMap = new HashMap<>();
        fileMap.put("鬼灭之刃1.jpg", "D:\\OneDrive\\图片\\鬼灭之刃\\78133846_p0.jpg");
        fileMap.put("鬼灭之刃2.jpg", "D:\\OneDrive\\图片\\鬼灭之刃\\78382590_p4.jpg");
        fileMap.put("鬼灭之刃3.jpg", "D:\\OneDrive\\图片\\鬼灭之刃\\79733019_p0.jpg");
        String result = OkHttpUtils.upload(url, params, "file", fileMap);
        System.out.println("--->" + result);
    }
}
