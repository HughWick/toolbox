package com.github.hugh.util.secrect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * appkey生成工具类
 *
 * @author hugh
 * @version 1.0.0
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
        String appKey = uuid.toString().replace("-", "");
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
        List<String> l = new ArrayList<>();
        for (char rand : rands) {
            l.add(String.valueOf(rand));
        }
        String ss = l.toString();
        String appSecret = ss.replace("[", "").replace("]", "").replace(",", "").replace(" ", "");
        return appSecret.strip();
    }
}
