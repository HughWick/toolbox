package com.github.hugh.util.file;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
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
     * 根据文件的路径、校验file文件是否为正常可读取的文件
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
     * <li>通过迭获取文件的流后、读取文件获取后缀、如果能获取到后缀名称则标识文件能正常读取、否则代表文件异常</li>
     * </ul>
     *
     * @param file 文件
     * @return boolean {@code true} 文件能被正常读取
     */
    public static boolean isImage(File file) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(file)) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (iter.hasNext()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}