package com.github.hugh.util.file;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 文件工具类
 *
 * @author hugh
 * @since 1.0.5
 */
public class FileUtils {

    /**
     * 根据路径判断文件目录是否存在、不存在则进行创建
     *
     * @param path 路径
     */
    public static void createDir(String path) {
        File file = new File(path);
        if (file.exists()) {//文件存在
            return;
        }
        if (file.isDirectory()) {//文件目录存在
            return;
        }
        file.mkdirs();
    }

    /**
     * 根据url链接判断对应图片是否存在
     *
     * @param url 网址链接
     * @return boolean 存在返回true
     */
    public static boolean exist(String url) {
        if (EmptyUtils.isEmpty(url)) {
            return false;
        }
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            InputStream in = uc.getInputStream();
            if (url.equalsIgnoreCase(uc.getURL().toString()))
                in.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除文件下所有空文件夹
     *
     * @param dir 文件
     */
    public static void delEmptyDir(File dir) {
        File[] dirs = dir.listFiles();
        assert dirs != null;
        for (File file : dirs) {
            if (file.isDirectory()) {
                delEmptyDir(file);
            }
        }
        if (dir.isDirectory()) {
            dir.delete();
        }
    }

    /**
     * 根据路径删除空文件夹
     *
     * @param path 文件夹路径
     * @since 1.4.4
     */
    public static void delEmptyDir(String path) {
        delEmptyDir(new File(path));
    }

    /**
     * 删除文件夹与旗下所有文件
     *
     * @param file file
     */
    public static void deleteDir(File file) {
        if (file == null) {
            throw new ToolboxException("file is null ");
        }
        if (file.isDirectory()) {//文件为目录
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File f : files)//遍历删除所有文件
                deleteDir(f);
        }
        file.delete();
    }

    /**
     * 根据文件路径删除文件夹下所有文件及文件夹
     * <p>通过创建一个文件后调用{@link #deleteDir(File)}方法</p>
     *
     * @param path 路径
     * @since 1.4.2
     */
    public static void deleteDir(String path) {
        if (path == null) {
            return;
        }
        deleteDir(new File(path));
    }

    /**
     * 根据文件路径删除文件,后如果目录为空,也会删除
     *
     * @param path 路径
     * @since 1.4.1
     */
    public static void delFile(String path) throws IOException {
        if (path == null) {
            return;
        }
        File file = new File(path);
        if (file.exists()) {
            boolean delete = file.delete();//删除文件
            if (delete) {
                String directory = StringUtils.before(file.getCanonicalPath(), "\\");
                delEmptyDir(directory);//删除目录
            }
        }
    }
}
