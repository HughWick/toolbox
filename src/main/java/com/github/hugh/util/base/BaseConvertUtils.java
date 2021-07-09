package com.github.hugh.util.base;

import com.github.hugh.exception.ToolboxException;

/**
 * 进制转换工具类
 *
 * @author hugh
 * @since 1.1.0
 */
public class BaseConvertUtils {

    private BaseConvertUtils() {
    }

    /**
     * 十进制转16进制
     *
     * @param decimal 十进制
     * @return long 十六进制
     */
    public static String tenToHex(int decimal) {
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
     * <p>默认左边不进行补位</p>
     *
     * @param str 字符串
     * @return String 二进制数字符串
     * @since 1.6.14
     */
    public static String toBinary(String str) {
        return toBinary(Integer.parseInt(str), 0);
    }

    /**
     * 十进制转二进制
     *
     * @param str    字符串
     * @param digits 二进制的位数（左边补零时才生效）
     * @return String
     * @since 1.6.14
     */
    public static String toBinary(String str, int digits) {
        return toBinary(Integer.parseInt(str), digits);
    }

    /**
     * 十进制转二进制
     *
     * @param num    需要转换的十进制数
     * @param digits 保留二进制的位数（左边补零时才生效）
     * @return String 补0后的二进制
     */
    public static String toBinary(int num, int digits) {
        String str = Integer.toBinaryString(num);
        return complement(str, digits);
    }

    /**
     * 16进制转换为字符串
     *
     * @param source   源
     * @param interval 切割的标识符
     * @return String
     * @since 1.4.9
     */
    public static String hexToString(String source, String interval) {
        if (interval == null) {
            throw new ToolboxException(" interval is null ");
        }
        String[] array = source.split(interval);
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = Byte.parseByte(array[i], 16);
        }
        return new String(bytes);
    }

    /**
     * 十六进制转二进制 并且补位
     * <p>默认左边不进行补位、只做16进制转换为二进制</p>
     *
     * @param num 需要转换的十进制数
     * @return String 二进制字符串
     * @since 1.6.14
     */
    public static String sixteenToBinary(String num) {
        return sixteenToBinary(num, 0);
    }

    /**
     * 十六进制转二进制 并且补位
     *
     * @param num    需要转换的十进制数
     * @param digits 保留二进制的位数（左边补零时才生效）
     * @return String 二进制字符串
     * @since 1.6.14
     */
    public static String sixteenToBinary(String num, int digits) {
        String str = Integer.toBinaryString(Integer.valueOf(num, 16));
        return complement(str, digits);
    }

    /**
     * 转换二进制后 根据传入的不为数值 左边补0
     *
     * @param str    字符串
     * @param digits 补位数
     * @return String 补位后的二进制
     * @since 1.6.14
     */
    private static String complement(String str, int digits) {
        String cover = Integer.toBinaryString(1 << digits).substring(1);
        return str.length() < digits ? cover.substring(str.length()) + str : str;
    }

    /**
     * 十六进制转十进制
     *
     * @param hex 十六进字符串
     * @return int 十进制
     * @since 1.6.14
     */
    public static int hexToTen(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * 十六进制转十进制字符串
     *
     * @param hex 十六进字符串
     * @return String 十进制
     * @since 1.6.14
     */
    public static String hexToTenString(String hex) {
        return String.valueOf(hexToTen(hex));
    }
}
