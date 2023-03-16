package com.github.hugh.crypto;

import com.github.hugh.constant.StrPool;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * appkey生成工具类
 *
 * @author hugh
 * @since 2.0.1
 */
public class AppkeyUtils {
    private AppkeyUtils() {
    }

    /**
     * 生成一个32位的UUID
     *
     * @return String 去除"-"后的UUID
     */
    public static String generate() {
        UUID uuid = UUID.randomUUID();
        String appKey = uuid.toString().replace("-", StrPool.EMPTY);
        return appKey.strip();
    }

    /**
     * 生成64位的secret
     *
     * @return String
     */
    public static String generateSecret() {
        String a = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] rands = new char[64];
        for (int i = 0; i < rands.length; i++) {
            int rand = (int) (Math.random() * a.length());
            rands[i] = a.charAt(rand);
        }
        List<String> stringList = new ArrayList<>();
        for (char rand : rands) {
            stringList.add(String.valueOf(rand));
        }
        String str = stringList.toString();
        String appSecret = str.replace("[", StrPool.EMPTY)
                .replace("]", StrPool.EMPTY)
                .replace(StrPool.COMMA, StrPool.EMPTY)
                .replace(StrPool.SPACE, StrPool.EMPTY);
        return appSecret.strip();
    }
}
