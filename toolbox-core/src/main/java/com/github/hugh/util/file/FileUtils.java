package com.github.hugh.util.file;

import com.github.hugh.constant.enums.FileTypeEnum;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.DoubleMathUtils;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.StringUtils;
import com.github.hugh.util.base.BaseConvertUtils;
import com.github.hugh.util.io.StreamUtils;
import com.google.common.io.Files;
import lombok.Cleanup;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DecimalFormat;
import java.util.Base64;

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
            if (url.equalsIgnoreCase(uc.getURL().toString())) {
                in.close();
            }
            return true;
        } catch (Exception e) {
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
        if (file.exists()) {
            if (file.delete()) {//删除文件
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
    @Deprecated
    public static boolean downloadByStream(String fileUrl, String savePath) {
        try {
            URL url = new URL(fileUrl);
            /* 将网络资源地址传给,即赋值给url */
            /* 此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流 */
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            var dataInputStream = new DataInputStream(httpURLConnection.getInputStream());
            /* 此处也可用BufferedInputStream与BufferedOutputStream  需要保存的路径*/
            var dataOutputStream = new DataOutputStream(new FileOutputStream(savePath));
            try (dataInputStream; dataOutputStream) {
                /* 将参数savePath，即将截取的图片的存储在本地地址赋值给out输出流所指定的地址 */
                byte[] buffer = new byte[4096];
                int count;
                /* 将输入流以字节的形式读取并写入buffer中 */
                while ((count = dataInputStream.read(buffer)) > 0) {
                    dataOutputStream.write(buffer, 0, count);
                }
                /* 关闭输入输出流以及网络资源的固定格式 */
                httpURLConnection.disconnect();
                return true;/* 网络资源截取并存储本地成功返回true */
            }
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
    public static boolean downloadByNio(String uri, String path) throws IOException {
        if (urlNotFileExist(uri)) {
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
     * 文件大小换算
     *
     * @param fileSize 文件大小
     * @return String 格式化后的文件大小：XX MB、XX GB
     * @since 2.2.2
     */
    public static String formatFileSize(long fileSize) {
        String wrongSize = "0";
        if (fileSize <= 0) {
            return wrongSize;
        }
        int kb = 1024;//定义KB的计算常量
        int mb = kb * kb;//定义MB的计算常量
        int gb = mb * kb;//定义GB的计算常量
        if (fileSize < kb) {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format((double) fileSize) + "B";
        } else if (fileSize < mb) {
            return DoubleMathUtils.div(fileSize, kb, 2) + "KB";
        } else if (fileSize < gb) {
            return DoubleMathUtils.div(fileSize, mb, 2) + "MB";
        } else {
            return DoubleMathUtils.div(fileSize, gb, 2) + "GB";
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
     * 文件大小换算
     *
     * @param path 文件路径
     * @return String 格式化后的文件大小：XX MB、XX GB
     * @since 2.2.2
     */
    public static String formatFileSize(String path) {
        return formatFileSize(new File(path));
    }

    /**
     * 获取文件真实类型
     *
     * @param filePath 文件路径
     * @return String 文件类型
     * @throws FileNotFoundException 文件未找到异常
     * @since 2.4.6
     */
    public static String getFileType(String filePath) throws FileNotFoundException {
        return getFileType(new File(filePath));
    }

    /**
     * 获取文件真实类型
     * <p>
     *
     * </p>
     *
     * @param file 文件
     * @return String 文件类型
     * @throws FileNotFoundException 文件未找到异常
     * @since 2.4.6
     */
    public static String getFileType(File file) throws FileNotFoundException {
        return getFileType(new FileInputStream(file), true);
    }

    /**
     * 根据输入流获取文件类型。
     * <p>
     * 该方法不会关闭{@link InputStream}，如需获取完后关闭流则调用{@link  #getFileType(InputStream, boolean)}
     * </p>
     *
     * @param inputStream 输入流
     * @return 文件类型，如果无法确定类型则返回null
     * @since 2.5.15
     */
    public static String getFileType(InputStream inputStream) {
        return getFileType(inputStream, false);
    }

    /**
     * 获取文件真实类型
     * <p>zip文件头与xlsx近似，无法准确判断</p>
     *
     * @param inputStream 输入流
     * @param closeStream 是否关闭输入流，true表示关闭，false表示不关闭
     * @return String 文件类型，如果无法确定类型则返回null
     * @since 2.4.6
     */
    public static String getFileType(InputStream inputStream, boolean closeStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            String fileHead = getFileHead(bis);
            if (EmptyUtils.isEmpty(fileHead)) {
                return null;
            }
            FileTypeEnum[] fileTypes = FileTypeEnum.values();
            for (FileTypeEnum type : fileTypes) {
                if (fileHead.startsWith(type.getValue())) {
                    return type.name().toLowerCase();
                }
            }
            return null;
        } catch (IOException ioException) {
            throw new ToolboxException(ioException.getMessage());
        } finally {
            if (closeStream) {
                StreamUtils.close(inputStream);
            }
        }
    }

    /**
     * 获取文件流中文件头内容，并且转十六进制
     *
     * @param inputStream 文件流
     * @return String 文件头十六进制
     * @since 2.4.6
     */
    private static String getFileHead(InputStream inputStream) {
        byte[] bytes = new byte[28];
        try {
            inputStream.read(bytes, 0, 28);
        } catch (IOException ioException) {
            throw new ToolboxException(ioException);
        }
        return BaseConvertUtils.hexBytesToString(bytes);
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
        byte[] data;
        try (InputStream inputStream = new FileInputStream(image)) {
            // 读取图片数据流
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException ioException) {
            throw new ToolboxException(ioException.getMessage());
        }
        // 对数据进行加密操作
        return Base64.getEncoder().encodeToString(data);
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
        // 解密
        byte[] b = Base64.getDecoder().decode(base64Str);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        // 文件夹不存在则自动创建
        File tempFile = new File(path);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdirs();
        }
        try (OutputStream out = new FileOutputStream(tempFile)) {
            out.write(b);
            out.flush();
        } catch (Exception exception) {
            throw new ToolboxException(exception.getMessage());
        }
    }
}
