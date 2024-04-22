package com.github.hugh.util.file;

import com.github.hugh.exception.ToolboxException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(file)) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(imageInputStream);
            return iter.hasNext();
        } catch (IOException ioException) {
            throw new ToolboxException(ioException);
        }
    }

    /**
     * 检查给定的字节数组是否表示一个有效的 Base64 编码的图片。
     *
     * @param bytes 要检查的字节数组
     * @return 如果给定的字节数组表示一个有效的图片，则返回 true；否则返回 false
     * @since 2.7.5
     */
    public static boolean isBase64Image(byte[] bytes) {
        return isBase64Image(new ByteArrayInputStream(bytes));
    }

    /**
     * 检查给定的输入流是否包含一个有效的 Base64 编码的图片。
     *
     * @param inputStream 要检查的输入流
     * @return 如果给定的输入流包含一个有效的图片，则返回 true；否则返回 false
     * @since 2.7.5
     */
    public static boolean isBase64Image(InputStream inputStream) {
        try (inputStream) {
            BufferedImage image = ImageIO.read(inputStream);
            return image != null;
        } catch (Exception exception) {
            throw new ToolboxException(exception);
        }
    }

    /**
     * 检查给定的字节数组是否表示一个无效的 Base64 编码的图片。
     *
     * @param bytes 要检查的字节数组
     * @return 如果给定的字节数组表示一个无效的图片，则返回 true；否则返回 false
     * @since 2.7.5
     */
    public static boolean isNotBase64Image(byte[] bytes) {
        return !isBase64Image(bytes);
    }

    /**
     * 检查给定的输入流是否不包含一个有效的 Base64 编码的图片。
     *
     * @param inputStream 要检查的输入流
     * @return 如果给定的输入流不包含一个有效的图片，则返回 true；否则返回 false
     * @since 2.7.5
     */
    public static boolean isNotBase64Image(InputStream inputStream) {
        return !isBase64Image(inputStream);
    }
}