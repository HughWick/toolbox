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
     *
     * @param inputStream 输入流
     * @param charset     字符编码
     * @return String 字符串
     */
    public static String toString(InputStream inputStream, String charset) {
        try (ByteArrayOutputStream boa = new ByteArrayOutputStream()) {
            int len = 0;
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
     * <p>注意：这里并不会关闭输入流，需要外部自行处理。</p>
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
}
