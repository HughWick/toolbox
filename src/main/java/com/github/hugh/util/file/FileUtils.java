package com.github.hugh.util.file;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.StringUtils;
import lombok.Cleanup;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

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
     * @return boolean {@code true}存在返回
     */
    public static boolean urlFileExist(String url) {
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
            e.printStackTrace();
            return false;
        }
    }

    /**
     * URL 文件不存在
     *
     * @param url 网址链接
     * @return boolean {@code true} 文件不存在返回
     * @since 1.4.15
     */
    public static boolean urlNotFileExist(String url) {
        return !urlFileExist(url);
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
                String directory = StringUtils.before(file.getCanonicalPath(), File.separator);
                delEmptyDir(directory);//删除目录
            }
        }
    }

    /**
     * 通过url访问图片信息后、存储到本地
     * <p>V1.5.1 后多线程使用{@link FileUtils#downloadByNio(String, String)}</p>
     *
     * @param fileUrl  网络资源地址
     * @param savePath 保存路径
     * @return boolean 成功返回true
     */
    public static boolean downloadByStream(String fileUrl, String savePath) {
        try {
            /* 将网络资源地址传给,即赋值给url */
            URL url = new URL(fileUrl);
            /* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            @Cleanup DataInputStream in = new DataInputStream(connection.getInputStream());
            /* 此处也可用BufferedInputStream与BufferedOutputStream  需要保存的路径*/
            @Cleanup DataOutputStream out = new DataOutputStream(new FileOutputStream(savePath));
            /* 将参数savePath，即将截取的图片的存储在本地地址赋值给out输出流所指定的地址 */
            byte[] buffer = new byte[4096];
            int count;
            /* 将输入流以字节的形式读取并写入buffer中 */
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            /* 关闭输入输出流以及网络资源的固定格式 */
            connection.disconnect();
            return true;/* 网络资源截取并存储本地成功返回true */
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 网络TRL中下载图片
     * <p>采用nio从URL流数据创建字节通道。然后使用文件输出流将其写入文件。</p>
     * <p>使用了{@link Cleanup}进行通道与流的关闭。</p>
     *
     * @param uri  URL
     * @param path 存放文件的路径
     * @return boolean {@code true}存储成功
     * @since 1.5.1
     */
    public static boolean downloadByNio(String uri, String path) {
        if (urlNotFileExist(uri)) {
            return false;
        }
        try {
            URL url = new URL(uri);
            @Cleanup ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            @Cleanup FileOutputStream fos = new FileOutputStream(path);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
