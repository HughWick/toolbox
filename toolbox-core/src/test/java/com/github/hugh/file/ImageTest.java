package com.github.hugh.file;

import com.github.hugh.util.file.FileUtils;
import com.github.hugh.util.file.ImageUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 图片工具测试类
 *
 * @author AS
 * @date 2020/9/17 15:53
 */
public class ImageTest {

    @Test
    void testIsImage() {
        String image1 = "/file/image/69956256_p1.jpg";
        String path = ImageTest.class.getResource(image1).getPath();
        assertTrue(ImageUtils.isImage(path));
        String image2 = "/file/image/20200718234953_grmzy.jpeg";
        String path2 = ImageTest.class.getResource(image2).getPath();
        assertTrue(ImageUtils.isImage(path2));
        String image3 = "/file/image/Teresa.png";
        String path3 = ImageTest.class.getResource(image3).getPath();
        assertTrue(ImageUtils.isImage(path3));
//        String image4 = "/file/image/share_1b1c03ab50c37b1462ac6a1d86cf9119.png";
//        String path4 = ImageTest.class.getResource(image4).getPath();
//        assertFalse(ImageUtils.isImage(path4));
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
