package com.github.hugh.util;


import com.github.hugh.exception.ToolboxException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类
 *
 * @author hugh
 */
public class RandomUtils {
    private RandomUtils() {
    }

    /**
     * 用于随机选的数字
     */
    private static final String BASE_NUMBER = "0123456789";

    /**
     * 用于随机选的字符
     */
    private static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";

    /**
     * 用于随机选的字符和数字
     */
    private static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER;

    /**
     * 获取随机数生成器对象<br>
     * ThreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。
     *
     * @return {@link ThreadLocalRandom}
     */
    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 获取{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)
     *
     * @return {@link SecureRandom}
     */
    public static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new ToolboxException(e);
        }
    }

    /**
     * 获取随机数产生器
     *
     * @param isSecure 是否为强随机数生成器 (RNG)
     * @return {@link Random}
     * @see #getSecureRandom()
     * @see #getRandom()
     */
    public static Random getRandom(boolean isSecure) {
        return isSecure ? getSecureRandom() : getRandom();
    }

    /**
     * 随机的字符
     *
     * @param length 长度
     * @return 随机的数字
     */
    public static String randomChar(final int length) {
        return randomString(BASE_CHAR, length);
    }

    /**
     * 随机的字符数字
     *
     * @param length 长度
     * @return 随机的数字
     */
    public static String randomCharNumber(final int length) {
        return randomString(BASE_CHAR_NUMBER, length);
    }

    /**
     * 随机的数字
     * <p>该方法返回为字符串类型数字</p>
     * <p>注：当长度为2时、会产生01这种长度的数字字符串</p>
     *
     * @param length 长度
     * @return String 随机的数字
     */
    public static String randomNumber(final int length) {
        return randomString(BASE_NUMBER, length);
    }

    /**
     * 根据需要的数字长度来生成随机数
     * <p>由于{@link com.github.hugh.util.RandomUtils#randomNumber} 会生成补0的两位随机数 例：04</p>
     * <p>该方法不会产生补0的两位随机数</p>
     *
     * @param length 长度
     * @return int 随机数
     */
    public static int number(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1D) {
            random += 0.1D;
        }
        for (int i = 0; i < length; i++) {
            num *= 10;
        }
        return (int) (random * num);
    }

    /**
     * 获得一个随机的字符串
     *
     * @param baseString 随机字符选取的样本
     * @param length     字符串的长度
     * @return 随机字符串
     */
    public static String randomString(String baseString, int length) {
        final StringBuilder sb = new StringBuilder();
        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        for (int i = 0; i < length; i++) {
            int number = ThreadLocalRandom.current().nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 随机选择列表中的一个元素
     *
     * @param list 列表
     * @param <T>  泛型
     * @return 结果
     */
    public static <T> T random(final List<T> list) {
        if (ListUtils.isEmpty(list)) {
            return null;
        }
        final int size = list.size();
        final Random random = ThreadLocalRandom.current();
        int index = random.nextInt(size);
        return list.get(index);
    }
}
