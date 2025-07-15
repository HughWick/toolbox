package com.github.hugh.crypto.components;

import com.github.hugh.constant.EncryptCode;
import com.github.hugh.util.base.BaseConvertUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件哈希工具类，用于计算文件的MD5、SHA-256等哈希值。
 *
 * @since 3.0.8
 */
public class FileHash {

    /**
     * 为给定文件使用指定算法生成加密哈希值。
     *
     * @param file      要进行哈希计算的文件。
     * @param algorithm 要使用的哈希算法（例如：FileHasher.ALGORITHM_MD5, FileHasher.ALGORITHM_SHA256）。
     * @return 文件的哈希值的十六进制字符串表示。
     * @throws IOException              如果文件读取过程中发生I/O错误。
     * @throws NoSuchAlgorithmException 如果指定的哈希算法不可用（例如，拼写错误或JRE不支持）。
     * @throws IllegalArgumentException 如果文件为null或不存在，或者算法名称为null或为空。
     */
    public static String getHash(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
        // 参数校验
        if (file == null) {
            throw new IllegalArgumentException("文件不能为null。");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("文件不存在: " + file.getAbsolutePath());
        }
        if (algorithm == null || algorithm.trim().isEmpty()) {
            throw new IllegalArgumentException("哈希算法名称不能为null或空。");
        }
        // 使用 try-with-resources 确保文件输入流自动关闭
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            // 获取指定算法的 MessageDigest 实例
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[4096]; // 缓冲区大小
            int numRead; // 每次读取的字节数
            // 循环读取文件内容并更新摘要
            while ((numRead = fileInputStream.read(buffer)) > 0) {
                messageDigest.update(buffer, 0, numRead);
            }
            // 完成哈希计算，获取哈希字节数组
            byte[] digest = messageDigest.digest();
            return BaseConvertUtils.hexBytesToString(digest).toLowerCase();
        }
    }

    /**
     * 为给定文件生成 MD5 哈希值。
     * 该方法允许直接传入文件路径字符串。
     *
     * @param filePath 要进行哈希计算的文件路径字符串。
     * @return 文件的 MD5 哈希值的十六进制字符串表示。
     * @throws IOException              如果文件读取过程中发生I/O错误。
     * @throws NoSuchAlgorithmException 如果MD5算法不可用（这种情况极不可能发生，因为MD5是标准算法）。
     * @throws IllegalArgumentException 如果文件路径为null、空或文件不存在。
     */
    public static String getMD5(String filePath) throws IOException, NoSuchAlgorithmException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为null或空。");
        }
        return getMD5(new File(filePath)); // 调用已有的 File 参数版本
    }

    /**
     * 为给定文件生成 MD5 哈希值。
     * 这是 {@link #getHash(File, String)} 方法的一个便捷调用，使用MD5算法。
     *
     * @param file 要进行哈希计算的文件。
     * @return 文件的 MD5 哈希值的十六进制字符串表示。
     * @throws IOException              如果文件读取过程中发生I/O错误。
     * @throws NoSuchAlgorithmException 如果MD5算法不可用（这种情况极不可能发生，因为MD5是标准算法）。
     */
    public static String getMD5(File file) throws IOException, NoSuchAlgorithmException {
        return getHash(file, EncryptCode.MD5);
    }

    /**
     * 为给定文件生成 SHA-256 哈希值。
     * 该方法允许直接传入文件路径字符串。
     *
     * @param filePath 要进行哈希计算的文件路径字符串。
     * @return 文件的 SHA-256 哈希值的十六进制字符串表示。
     * @throws IOException              如果文件读取过程中发生I/O错误。
     * @throws NoSuchAlgorithmException 如果SHA-256算法不可用。
     * @throws IllegalArgumentException 如果文件路径为null、空或文件不存在。
     */
    public static String getSHA256(String filePath) throws IOException, NoSuchAlgorithmException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为null或空。");
        }
        return getSHA256(new File(filePath)); // 调用已有的 File 参数版本
    }

    /**
     * 为给定文件生成 SHA-256 哈希值。
     * 这是 {@link #getHash(File, String)} 方法的一个便捷调用，使用SHA-256算法。
     *
     * @param file 要进行哈希计算的文件。
     * @return 文件的 SHA-256 哈希值的十六进制字符串表示。
     * @throws IOException              如果文件读取过程中发生I/O错误。
     * @throws NoSuchAlgorithmException 如果SHA-256算法不可用。
     * @throws IllegalArgumentException 如果文件为null或不存在。
     */
    public static String getSHA256(File file) throws IOException, NoSuchAlgorithmException {
        return getHash(file, EncryptCode.SHA_256);
    }
}
