package com.github.hugh.util.file;

import com.github.hugh.constant.StrPool;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.DoubleMathUtils;
import com.github.hugh.util.StringUtils;
import com.github.hugh.util.io.StreamUtils;
import com.github.hugh.util.net.UrlUtils;
import com.github.hugh.util.regex.RegexUtils;
import com.google.common.io.Files;
import lombok.Cleanup;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.regex.Matcher;

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
     * 删除文件下所有空文件夹
     *
     * @param dir 文件
     */
    public static void delEmptyDir(File dir) {
        File[] dirs = dir.listFiles();
        if (dirs == null) {
            return;
        }
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
            //遍历删除所有文件
            for (File f : files) {
                deleteDir(f);
            }
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
        if (file.exists() && file.delete()) { // 文件存在，并且删除成功
            String directory = StringUtils.before(file.getCanonicalPath(), File.separator);
            delEmptyDir(directory);//删除目录
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
    public static boolean downloadByNio(String uri, String path) throws IOException {
        if (UrlUtils.resourceNotExists(uri)) {
            return false;
        }
        try (InputStream inputStream = new URL(uri).openStream();
             ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            return true;
        } catch (IOException e) {
            throw new IOException();
        }
    }

    /**
     * 根据图片路径读取文件并转化为Byte[]
     * <p>调用{@link #toByteArray(File)}</p>
     *
     * @param path 文件路径
     * @return byte[] 文件数组
     * @since 1.3.6
     */
    public static byte[] toByteArray(String path) throws IOException {
        File file = new File(path);
        return toByteArray(file);
    }

    /**
     * <p>使用{@link Files#asByteSource(File)}读取文件</p>
     *
     * @param file 文件
     * @return byte
     * @throws IOException IO异常
     * @since 1.5.2
     */
    public static byte[] toByteArray(File file) throws IOException {
        if (file == null) {
            throw new ToolboxException("file is null !");
        }
        if (!file.exists()) {
            throw new ToolboxException("file not exists !");
        }
        return Files.asByteSource(file).read();
    }

    /**
     * 格式化文件大小显示，默认包含单位，根据文件大小返回相应的大小字符串（带单位）。
     *
     * @param fileSize 文件大小
     * @return String 格式化后的文件大小：XX MB、XX GB
     * @since 2.2.2
     */
    public static String formatFileSize(long fileSize) {
        return formatFileSize(fileSize, true);
    }

    /**
     * 格式化文件大小显示，返回相应的大小字符串（带单位）。
     *
     * @param fileSize    文件大小
     * @param includeUnit 是否包含单位（true 包含单位，false 不包含单位）
     * @return 格式化后的文件大小字符串
     * @since 2.7.15
     */
    public static String formatFileSize(long fileSize, boolean includeUnit) {
        String wrongSize = "0";
        if (fileSize <= 0) {
            return wrongSize;
        }
        int kb = 1024; // 定义 KB 的计算常量
        int mb = kb * kb; // 定义 MB 的计算常量
        int gb = mb * kb; // 定义 GB 的计算常量
        if (fileSize < kb) {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format((double) fileSize) + (includeUnit ? "B" : StrPool.EMPTY);
        } else if (fileSize < mb) {
            return DoubleMathUtils.div(fileSize, kb, 2) + (includeUnit ? "KB" : StrPool.EMPTY);
        } else if (fileSize < gb) {
            return DoubleMathUtils.div(fileSize, mb, 2) + (includeUnit ? "MB" : StrPool.EMPTY);
        } else {
            return DoubleMathUtils.div(fileSize, gb, 2) + (includeUnit ? "GB" : StrPool.EMPTY);
        }
    }

    /**
     * 文件大小换算
     *
     * @param file 文件
     * @return String 格式化后的文件大小：XX MB、XX GB
     * @since 2.2.2
     */
    public static String formatFileSize(File file) {
        if (!file.exists() || !file.isFile()) {
            return "-1";
        }
        return formatFileSize(file.length());
    }

    /**
     * 根据文件路径，格式化文件大小显示，返回相应的大小字符串（带单位）。
     *
     * @param path 文件路径
     * @return String 格式化后的文件大小：XX MB、XX GB
     * @since 2.2.2
     */
    public static String formatFileSize(String path) {
        return formatFileSize(new File(path));
    }

    /**
     * 读取文件内容并返回字符串形式的文件内容。
     *
     * @param filePath 文件路径
     * @return 字符串形式的文件内容
     * @since 2.5.11
     */
    public static String readContent(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException ioException) {
            throw new ToolboxException(ioException);
        }
        return contentBuilder.toString();
    }

    /**
     * 读取文件内容并返回字符串形式的文件内容。
     *
     * @param file 文件
     * @return 字符串形式的文件内容
     * @since 2.5.11
     */
    public static String readContent(File file) {
        return readContent(file.getPath());
    }

    /**
     * 将图像文件转换为Base64字符串表示。
     *
     * @param image 图像文件对象
     * @return 转换后的Base64字符串
     * @since 2.5.14
     */
    public static String imageToBase64Str(File image) {
        try (InputStream inputStream = new FileInputStream(image)) {
            return imageToBase64Str(inputStream);
        } catch (IOException ioException) {
            throw new ToolboxException(ioException.getMessage());
        }
    }

    /**
     * 将图像文件转换为Base64字符串表示。
     *
     * @param imagePath 图像文件路径
     * @return 转换后的Base64字符串
     * @since 2.5.14
     */
    public static String imageToBase64Str(String imagePath) {
        return imageToBase64Str(new File(imagePath));
    }

    /**
     * 将Base64字符串表示的图像数据解码并保存为图像文件。
     *
     * @param base64Str Base64字符串表示的图像数据
     * @param path      要保存图像文件的路径
     * @since 2.5.14
     */
    public static void base64StrToImage(String base64Str, String path) {
        // 移除 MIME 类型前缀
        String data = base64Str;
        Matcher matcher = RegexUtils.BASE64_DATA_URI_REGEX.matcher(base64Str);
        if (matcher.matches()) {
            data = matcher.group(2);
        } else if (base64Str.contains("data:")) { // 如果包含data: 但不匹配完整格式，则抛出异常
            throw new IllegalArgumentException("Invalid Base64 data URI format.");
        }
        // 解密
        byte[] decodedBytes = Base64.getDecoder().decode(data);
        // 文件夹不存在则自动创建
        File tempFile = new File(path);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
        try (OutputStream out = new FileOutputStream(tempFile)) {
            out.write(decodedBytes);
            out.flush();
        } catch (Exception exception) {
            throw new ToolboxException(exception.getMessage());
        }
    }

    /**
     * 将输入流转换为Base64编码的字符串表示。
     *
     * @param inputStream 输入流，用于读取图片数据
     * @return Base64编码的图片字符串
     * @since 2.7.5
     */
    public static String imageToBase64Str(InputStream inputStream) {
        try {
            // 将InputStream转换为byte数组
            byte[] bytes = StreamUtils.toByteArray(inputStream);
            // 将byte数组转换为Base64编码的字符串
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException ioException) {
            throw new ToolboxException(ioException);
        }
    }

    /**
     * 从 Base64 编码的字符串中提取 MIME 类型
     *
     * @param base64String Base64 编码的字符串，通常以 "data:image/..." 或 "data:application/pdf;base64,..." 开头
     * @return 返回 MIME 类型（例如："image/jpeg"），如果字符串不是有效的 Base64 数据 URI，则返回 null
     * @since 2.7.19
     */
    public static String getMimeType(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;  // 如果输入字符串为空或 null，则返回 null
        }
        // 使用正则表达式匹配 Base64 数据 URI 格式
        Matcher matcher = RegexUtils.BASE64_DATA_URI_REGEX.matcher(base64String);
        // 如果匹配成功，提取出 MIME 类型部分
        if (matcher.matches()) {
            return matcher.group(1);  // group(1) 表示正则匹配结果中的 MIME 类型部分
        }
        // 如果没有匹配到数据 URI 格式，则返回 null
        return null;
    }

    /**
     * 根据 MIME 类型获取对应的文件扩展名
     *
     * @param mimeType MIME 类型（例如："image/jpeg"）
     * @return 返回文件扩展名（例如："jpg"），如果没有找到匹配的 MIME 类型，则返回 null
     * @since 2.7.19
     */
    public static String getExtensionFromMimeType(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
            return null;  // 如果输入的 MIME 类型为空或 null，则返回 null
        }
        // 根据常见的 MIME 类型返回相应的文件扩展名
        switch (mimeType) {
            case "image/jpeg":
                return "jpg";  // JPEG 图片对应的扩展名是 .jpg
            case "image/png":
                return "png";  // PNG 图片对应的扩展名是 .png
            case "image/gif":
                return "gif";  // GIF 图片对应的扩展名是 .gif
            case "application/pdf":
                return "pdf";  // PDF 文件对应的扩展名是 .pdf
            case "text/plain":
                return "txt";  // 纯文本文件对应的扩展名是 .txt
            // ... 可以在这里添加更多的 MIME 类型与文件扩展名的映射
            default:
                // 如果没有找到对应的扩展名，则返回 null
                return null;
        }
    }
}
