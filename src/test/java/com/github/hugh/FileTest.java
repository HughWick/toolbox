package com.github.hugh;

import com.github.hugh.util.FileUtils;

import java.io.File;

/**
 * @author AS
 * @date 2020/9/10 11:06
 */
public class FileTest {

    public static void main(String[] args) {
        String path = "C:\\Users\\Lenovo\\Desktop\\错误图片-1 - 副本";
        FileUtils.deleteDir(new File(path));
//        FileUtils.delEmptyDir(path);
        System.out.println("==============");
    }
}
