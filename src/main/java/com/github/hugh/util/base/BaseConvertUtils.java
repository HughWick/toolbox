package com.github.hugh.util.base;

/**
 * 进制转换工具类
 *
 * @author hugh
 * @since 1.1.0
 */
public class BaseConvertUtils {

    /**
     * 十进制转16进制
     *
     * @param decimal 十进制
     * @return long 十六进制
     */
    public static String tenToSixteen(int decimal) {
        return Integer.toHexString(decimal);
    }

    /**
     * 十进制转二进制
     *
     * @param decimal 十进制
     * @return String 二进制
     */
    public static String tenToBinary(int decimal) {
        return Integer.toBinaryString(decimal);
    }

    /**
     * 二进制转十进制
     *
     * @param binary 二进制
     * @return String  十进制
     */
    public static String binaryToTen(String binary) {
        return Integer.valueOf(binary, 2).toString();
    }

    /**
     * 十进制转二进制
     *
     * @param num    需要转换的十进制数
     * @param digits 保留二进制的位数（左边补零时才生效）
     * @return String 补0后的二进制
     */
    public static String toBinary(int num, int digits) {
        String cover = Integer.toBinaryString(1 << digits).substring(1);
        String str = Integer.toBinaryString(num);
        return str.length() < digits ? cover.substring(str.length()) + str : str;
    }
}
