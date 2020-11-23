package com.github.hugh;

import com.github.hugh.util.file.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

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
        String path = "C:\\Users\\Lenovo\\Desktop\\新建文件夹\\0a3bf21a1f884bf9b22cfa43d6746ee2.jpg";
        try {
            FileUtils.delFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("======END=====");
    }
    @Test
    public void test02() {
        String dir = "C:\\Users\\Lenovo\\Desktop\\新建文件夹";
        FileUtils.deleteDir(new File(dir));

        System.out.println("========");
        String path = "C:\\Users\\Lenovo\\Desktop\\新建文件夹 (2)";
        FileUtils.deleteDir(path);
        System.out.println("===END=====");
    }
}
