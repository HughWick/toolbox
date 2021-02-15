package com.github.hugh.util.io;

import com.github.hugh.constant.CharsetCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.system.OsUtils;
import jodd.io.StreamUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Java 流处理工具类
 *
 * @author hugh
 * @version 1.7
 * @since 1.3.5
 */
public class StreamUtils {

    /**
     * 获取文件对应输入流
     * <ul>
     * <li>本地环境下，xx.properties 最终是放在 Web 应用下的 /WEB-INF/classes 文件夹下，不能被 System 类加载器获取到，所以加载失败.</li>
     * <li>所以windows环境下使用 getClass().getResouceAsStream(String) 去加载资源.</li>
     * </ul>
     *
     * @param filePath 文件路径
     * @return InputStream 输入流
     */
    public static InputStream getInputStream(final String filePath) {
        InputStream inputStream;
        try {
            inputStream = new URL(filePath).openStream();
        } catch (MalformedURLException localMalformedURLException) {
            try {
                inputStream = new FileInputStream(filePath);
            } catch (Exception localException2) {
                ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
                if (localClassLoader == null) {
                    localClassLoader = StreamUtil.class.getClassLoader();
                }
                if (OsUtils.isWindows()) {
                    inputStream = localClassLoader.getClass().getResourceAsStream(filePath);
                } else { // linux jar包情况下
                    inputStream = localClassLoader.getResourceAsStream(filePath);
                }
                if (inputStream == null) {
                    throw new ToolboxException("Could not find file: " + filePath);
                }
            }
        } catch (IOException localIOException1) {
            throw new ToolboxException(localIOException1);
        }
        return inputStream;
    }

    /**
     * InputStream转byte[]
     * <p>warn:该方法不会关闭InputStream,请自行关闭!</p>
     *
     * @param input 输入流
     * @return byte[]
     * @throws IOException IO流异常
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * 将InputStream 输出至指定的文件
     * <p>内部调用{@link #toFile(InputStream, File)} </p>
     *
     * @param inputStream 输入流
     * @param filePath    文件路径
     */
    public static void toFile(InputStream inputStream, String filePath) {
        toFile(inputStream, new File(filePath));
    }

    /**
     * 将InputStream 输出至指定的文件
     *
     * @param inputStream 输入流
     * @param file        文件
     */
    public static void toFile(InputStream inputStream, File file) {
        try (OutputStream os = new FileOutputStream(file)) {
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将输入流转换为字符串
     * <p>warn:该方法不会关闭InputStream,请自行关闭!</p>
     *
     * @param inputStream 输入流
     * @param charset     字符编码
     * @return String 字符串
     */
    public static String toString(InputStream inputStream, String charset) {
        try (ByteArrayOutputStream boa = new ByteArrayOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                boa.write(buffer, 0, len);
            }
            byte[] result = boa.toByteArray();
            return new String(result, charset);
        } catch (Exception e) {
            throw new ToolboxException(e);
        }
    }

    /**
     * 流转换为UTF-8字符串
     *
     * <p>内部调用{@link #toString(InputStream, String)}编码格式默认为UTF-8</p>
     *
     * @param inputStream 输入流
     * @return String 字符串
     */
    public static String toString(InputStream inputStream) {
        return toString(inputStream, CharsetCode.UTF_8);
    }

    /**
     * 将字符串转输入流
     *
     * @param str 字符串
     * @return InputStream
     */
    public InputStream toInputStream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * 关闭流
     *
     * @param closeable 可关闭的对象资源
     */
    public void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            throw new ToolboxException(e);
        }
    }

    /**
     * 根据图片路径读取文件并转化为Byte[]
     *
     * @param path 文件路径
     * @return byte[] 文件数组
     * @since 1.3.6
     */
    public static byte[] resourceToByteArray(String path) {
        try (InputStream inputStream = getInputStream(path)) {
            return toByteArray(inputStream);
        } catch (IOException ioe) {
            throw new ToolboxException(ioe);
        }
    }
}
