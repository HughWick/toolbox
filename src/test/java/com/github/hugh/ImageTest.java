package com.github.hugh;

import com.github.hugh.util.file.FileUtils;
import com.github.hugh.util.file.ImageUtils;

import java.util.Date;

/**
 * @author AS
 * @date 2020/9/17 15:53
 */
public class ImageTest {

    public static void main(String[] args) {
//        CatchPic pic = new CatchPic();/* 创建实例 */
        //需要下载的URL
        String photoUrl = "https://www.hnlot.com.cn/DaHua/capture/4K03702PAA39BD4/2020/8/27/ffd8f7e378a94fdcbde323cd79de2ffd.jpg";

        // 截取最后/后的字符串
        String fileName = new Date().getTime() + ".png";

        //图片保存路径
        String filePath = "D:/img/";
        FileUtils.createDir(filePath);
        /* 调用函数，并且进行传参 */
        boolean flag = ImageUtils.saveUrl(photoUrl, filePath + fileName);
        System.out.println("Run ok!\n Get URL file " + flag);
        System.out.println(filePath);
        System.out.println(fileName);
    }

}
