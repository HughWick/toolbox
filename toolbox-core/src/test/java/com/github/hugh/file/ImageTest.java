package com.github.hugh.file;

import com.github.hugh.util.file.FileUtils;
import com.github.hugh.util.file.ImageUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @author AS
 * @date 2020/9/17 15:53
 */
public class ImageTest {

    @Test
    public void test01() {
        String str = "C:\\Users\\AS\\Desktop\\Es_aaVKVcAogC45.jpg";
        String str2 = "C:\\Users\\AS\\Desktop\\Es_aaVKVcAogC45.j";
        System.out.println("-1-->>" + ImageUtils.isImage(str));
        System.out.println("--2->>" + ImageUtils.isImage(str2));
    }

    @Test
    public void test02() {
        try {
            var a = 2;
            try {
                int i = a / 0;
            } finally {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("==========");
    }

    public static void main(String[] args) {
//        CatchPic pic = new CatchPic();/* 创建实例 */
        //需要下载的URL
        String photoUrl = "https://cmmop.hnlot.com.cn/capture/DaHua/capture/3K02281PAJ00016/2021/2/2/f331dbb64caa4480bf92c8a335b3e02a.jpg";
        // 截取最后/后的字符串
        String fileName = new Date().getTime() + ".png";
        //图片保存路径
        String filePath = "D:/img/";
        FileUtils.createDir(filePath);
        /* 调用函数，并且进行传参 */
        boolean flag = FileUtils.downloadByStream(photoUrl, filePath + fileName);
        System.out.println("Run ok!\n Get URL file " + flag);
        System.out.println(filePath);
        System.out.println(fileName);
    }

}
