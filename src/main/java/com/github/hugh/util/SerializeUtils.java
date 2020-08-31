package com.github.hugh.util;

import java.io.*;

/**
 * 序列化辅助类
 *
 * @author hugh
 * @version java 1.7以上
 */
public final class SerializeUtils {
    private SerializeUtils() {
    }

    /**
     * 序列化
     *
     * @param object 参数
     * @return byte[]
     */
    public static byte[] toBytes(Object object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * 反序列化
     *
     * @param bytes 字节数组
     * @return Object
     */
    public static Object toObject(byte[] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}
