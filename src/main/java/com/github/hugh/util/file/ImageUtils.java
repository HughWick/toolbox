package com.github.hugh.util.file;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * 图片处理工具类
 *
 * @author hugh
 * @since 1.1.3
 */
public class ImageUtils {
    private ImageUtils() {
    }

    /**
     * 通过url访问图片信息后、存储到本地
     * <p>V-1.5.1 后使用{@link FileUtils#downloadByUrl(String, String)}代替该方法</p>
     *
     * @param fileUrl  网络资源地址
     * @param savePath 保存路径
     * @return boolean 成功返回true
     */
    @Deprecated
    public static boolean saveUrl(String fileUrl, String savePath) {
        try {
            /* 将网络资源地址传给,即赋值给url */
            URL url = new URL(fileUrl);
            /* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            /* 此处也可用BufferedInputStream与BufferedOutputStream  需要保存的路径*/
            DataOutputStream out = new DataOutputStream(new FileOutputStream(savePath));
            /* 将参数savePath，即将截取的图片的存储在本地地址赋值给out输出流所指定的地址 */
            byte[] buffer = new byte[4096];
            int count;
            /* 将输入流以字节的形式读取并写入buffer中 */
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();/* 后面三行为关闭输入输出流以及网络资源的固定格式 */
            in.close();
            connection.disconnect();
            return true;/* 网络资源截取并存储本地成功返回true */
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据文件的路径、校验file文件是否为正常可读取后缀的文件
     *
     * @param path 图片路径
     * @return boolean {@code true} 文件能被正常读取
     */
    public static boolean isImage(String path) {
        return isImage(new File(path));
    }

    /**
     * 校验file文件是否为正常可读取后缀的文件
     * <ul>
     * <li>由于某些图片在windows环境下打开异常、但是java file打开文件没有异常、调用C dll库解析图片时导致JVM崩溃。</li>
     * <li>该方法主要通过迭获取文件的流后、读取文件获取后缀、如果能获取到后缀名称则标识文件能正常读取、否则代表文件异常</li>
     * </ul>
     *
     * @param file 文件
     * @return boolean {@code true} 文件能被正常读取
     */
    public static boolean isImage(File file) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(file)) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (iter.hasNext()) {//文件不是图片
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}