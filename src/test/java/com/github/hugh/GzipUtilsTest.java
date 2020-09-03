package com.github.hugh;

import com.github.hugh.util.GzipUtils;

/**
 * @author AS
 * @date 2020/9/3 11:19
 */
public class GzipUtilsTest {

    public static void main(String[] args) {
        String str =
                "时间22lastUpdateTime%22%3A%222011-10-28+9%3A39%3A41%22%2C%22smsList%22%3A%5B%7B%22liveState%22%3A%221";
        System.out.println("原长度：" + str.length());
        try {
            System.out.println("压缩后字符串：" + GzipUtils.compress(str).length());
            System.out.println("解压缩后字符串：" + GzipUtils.uncompress(GzipUtils.compress(str)));
//            System.out.println("解压缩后字符串：" + uncompressToString(GZIPUtils.compress(str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
