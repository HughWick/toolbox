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
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
     * 判断给定的字符串是否是有效的 Base64 编码图片。
     *
     * <p>该方法首先将输入的字符串转换为字节数组，然后调用 {@link #isBase64Image(byte[])} 方法判断字节数组是否是有效的 Base64 编码图片。</p>
     *
     * @param text 待检查的字符串，通常是 Base64 编码的图片数据。
     * @return 如果字符串是有效的 Base64 编码图片，则返回 {@code true}；否则返回 {@code false}。
     * @since 2.8.1
     */
    public static boolean isBase64Image(String text) {
        return isBase64Image(text.getBytes());
    }

    /**
     * 判断输入流是否为有效的 Base64 编码图片。
     * <p>
     * 该方法会根据输入流的内容判断其是否为 Base64 编码的图片数据，
     * 包括支持带有 `data:image/xxx;base64,` 前缀的 Base64 字符串。
     *
     * @param inputStream 输入流
     * @return 如果是有效的 Base64 图片，返回 true；否则返回 false
     * @since 2.7.5
     */
    public static boolean isBase64Image(InputStream inputStream) {
        try (InputStream autoCloseInputStream = inputStream) {
            byte[] imageBytes = autoCloseInputStream.readAllBytes(); // 读取输入流中的所有字节
            String inputString = new String(imageBytes, StandardCharsets.UTF_8); // 假设输入是 UTF-8 编码的字符串
            // 检查是否以 data:image/ 开头并包含 ;base64,，若是，则尝试处理 Base64 图片
            if (inputString.startsWith("data:image/") && inputString.contains(";base64,")) {
                return processBase64Image(inputString);
            }
            // 如果没有找到 Base64 前缀，尝试直接处理作为普通的 Base64 字符串
            try {
                return processRawBase64(inputString);
            } catch (IllegalArgumentException e) {
                // 如果 Base64 格式无效，则尝试处理为原始的图片数据
                return processRawImage(imageBytes);
            }
        } catch (IOException ioException) {
            throw new ToolboxException("Error reading input stream", ioException);
        }
    }

    /**
     * 处理带有 Base64 前缀的图片数据。
     * <p>
     * 该方法将 Base64 编码字符串中的图片数据部分提取出来并解码为字节数组，
     * 然后验证是否为有效的图片数据。
     * </p>
     *
     * @param base64String 带有 Base64 编码的图片字符串
     * @return 如果解码后的图片有效，返回 true；否则返回 false
     */
    private static boolean processBase64Image(String base64String) {
        try {
            // 提取 Base64 内容部分
            String base64Content = base64String.substring(base64String.indexOf(";base64,") + ";base64,".length());
            // 解码 Base64 字符串
            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
            return isValidImage(decodedBytes); // 验证解码后的字节是否为有效图片
        } catch (IllegalArgumentException e) {
            return false; // 如果 Base64 编码无效，返回 false
        }
    }

    /**
     * 处理普通的 Base64 编码字符串。
     * <p>
     * 该方法将输入的 Base64 编码字符串进行解码，并验证解码后的字节是否为有效的图片数据。
     *
     * @param base64String 普通的 Base64 编码字符串
     * @return 如果解码后的图片有效，返回 true；否则返回 false
     */
    private static boolean processRawBase64(String base64String) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String); // 解码 Base64 字符串
            return isValidImage(decodedBytes); // 验证解码后的字节是否为有效图片
        } catch (IllegalArgumentException illegalArgumentException) {
            throw illegalArgumentException; // 如果 Base64 格式无效，重新抛出异常
        }
    }

    /**
     * 尝试将字节数组作为原始图片数据进行读取。
     * <p>
     * 该方法将字节数组转换为字节流并通过 `ImageIO.read()` 读取，
     * 如果读取到有效的图片数据，则返回 true。
     *
     * @param imageBytes 原始图片字节数组
     * @return 如果读取到有效的图片，返回 true；否则返回 false
     */
    private static boolean processRawImage(byte[] imageBytes) {
        try (ByteArrayInputStream rawStream = new ByteArrayInputStream(imageBytes)) {
            BufferedImage image = ImageIO.read(rawStream); // 使用 ImageIO 读取图片
            return image != null; // 如果读取到有效图片，则返回 true
        } catch (IOException ioException) {
            return false; // 如果读取失败，则返回 false
        }
    }

    /**
     * 验证解码后的字节数组是否为有效的图片数据。
     * <p>
     * 该方法通过 `ImageIO.read()` 尝试读取字节数组，如果能够成功读取为图片，则说明字节数组有效。
     *
     * @param decodedBytes 解码后的字节数组
     * @return 如果字节数组代表有效的图片，返回 true；否则返回 false
     */
    private static boolean isValidImage(byte[] decodedBytes) {
        try (ByteArrayInputStream decodedStream = new ByteArrayInputStream(decodedBytes)) {
            BufferedImage image = ImageIO.read(decodedStream); // 使用 ImageIO 读取字节流
            return image != null; // 如果读取到有效的图片，则返回 true
        } catch (IOException ioException) {
            return false; // 如果读取失败，则返回 false
        }
    }

    /**
     * 判断给定的字符串是否不是有效的 Base64 编码图片。
     * <p>
     * 该方法首先会判断输入的 Base64 字符串是否为 null 或为空，如果是，则返回 true，表示该字符串不是有效的 Base64 编码图片。
     * 然后将字符串转换为字节数组，调用内部的 `isBase64Image(byte[] bytes)` 方法检查该字节数组是否符合 Base64 编码图片的标准格式。
     * 如果该字节数组符合标准格式，则返回 false，表示是有效的 Base64 编码图片；否则返回 true。
     *
     * @param base64String 要判断的 Base64 字符串
     * @return 如果该字符串不是有效的 Base64 编码图片，返回 true；否则返回 false
     * @since 2.8.1
     */
    public static boolean isNotBase64Image(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return true;
        }
        byte[] bytes = base64String.getBytes(StandardCharsets.UTF_8);
        return !isBase64Image(bytes);
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