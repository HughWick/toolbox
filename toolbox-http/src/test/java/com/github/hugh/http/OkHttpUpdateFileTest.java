package com.github.hugh.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * okhttp file 上传文件测试
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

    @Test
    void testMinio() throws IOException {
//        String url = "http://192.168.1.213:8001/file/minio/upload";
        String url = "https://dev.hnlot.com.cn/file/minio/upload";
        String path1 = "D:\\OneDrive\\图片\\QQ图片20220206181047.png";
        String path2 = "D:\\Program Files\\Desktop\\share_0a3835617ff336a3004dbf9115dac414.png";
        // ------------------------
        String path3 = "D:\\Program Files\\Desktop\\images\\test_001.webp";
        var params = new HashMap<>();
        params.put("type", "0");
        var fileMap = new HashMap<>();
//        fileMap.put("鬼灭之刃1.png", "D:\\Program Files\\Desktop\\111.png");
//        fileMap.put("缘一.jpg", "D:\\Program Files\\Desktop\\71EcJAenv0L_.jpg");
//        fileMap.put("XXX.png", path1);
//        fileMap.put("share_0a3835617ff336a3004dbf9115dac414.png", path2);
        fileMap.put("test_001.webp", path3);
        String result = OkHttpUtils.upload(url, params, "file", fileMap);
        System.out.println("--->" + result);
    }
}
