package com.github.hugh.file;

import com.github.hugh.util.file.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author AS
 * @date 2020/9/10 11:06
 */
public class FileTest {

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\Lenovo\\Desktop\\3311523eaa9d64a9d278e5b5304ccae4.jpg";
//        FileUtils.deleteDir(new File(path));
//        FileUtils.delEmptyDir(path);
        File directory = new File(path);//设定为当前文件夹

        String absolutePath = directory.getAbsolutePath();
        System.out.println("File full path : " + absolutePath);
        String filePath = absolutePath.
                substring(0, absolutePath.lastIndexOf(File.separator));
        System.out.println("File path : " + filePath);
        System.out.println(directory.getCanonicalFile());//返回类型为File
        System.out.println(directory.getCanonicalPath());//获取标准的路径  ，返回类型为String
        System.out.println(directory.getAbsoluteFile());//返回类型为File
        System.out.println(directory.getAbsolutePath());//获取绝对路径，返回类型为String
        System.out.println("==============");
    }

    @Test
    public void test01() {
        String path = "C:\\Users\\Lenovo\\Desktop\\新建文件夹\\bb9b12369c80588bbed1ea6ae744875e.jpg";
        try {
//            FileUtils.delFile(path);
            FileUtils.delFile("C:\\Users\\Lenovo\\Desktop\\新建文件夹\\");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("======END=====");
    }

    @Test
    public void test02() {
//        String dir = "C:\\Users\\Lenovo\\Desktop\\新建文件夹";
//        FileUtils.deleteDir(new File(dir));
//        System.out.println("========");
//        String path = "C:\\Users\\Lenovo\\Desktop\\新建文件夹 (2)";
//        FileUtils.deleteDir(path);
        String dirs = "C:\\Users\\Lenovo\\Desktop";
        FileUtils.delEmptyDir(dirs);
        System.out.println("===END=====");
    }

    @Test
    public void test03() {
        String url = "https://ym.191ec.com/img/goodsContent/901015857951990381/b632537a5b884ecc8309222fca1d835b_1588148150570.jpg";
        System.out.println("---1>>" + FileUtils.urlFileExist(url));
        System.out.println("--2->>" + FileUtils.urlFileExist(url + "1"));
        System.out.println("--3->>" + FileUtils.urlNotFileExist(url));
        System.out.println("--4->>" + FileUtils.urlNotFileExist(url + "1"));
    }

    @Test
    public void test04() {
        String str = "http://hyga.hnlot.com.cn:8000/capture/DaHua/quality/6D0529FPAG95B23/2021/2/3/836ee391c4a749f2a5c1a0b4b6cda6a5.jpg";
        //图片保存路径
        String filePath = "D:\\img\\";
        // 截取最后/后的字符串
        String fileName = new Date().getTime() + ".png";
        FileUtils.downloadByStream(str, filePath + fileName);
        FileUtils.downloadByNio(str, filePath + fileName);
        System.out.println("===END====");
    }

    private static final String DEL_PATH = "D:\\battery-report.html";

    @Test
    public void test05() throws IOException {
        byte[] bytes = FileUtils.toByteArray(DEL_PATH);
        System.out.println("--->>" + bytes.length);
        byte[] bytes2 = FileUtils.toByteArray(DEL_PATH+"2");
    }
}
