package com.github.hugh.util;

import jodd.util.StringUtil;

/**
 * 字符串工具
 *
 * @author huhg
 * @since 1.0.6
 */
public class StringUtils {

    private StringUtils(){}
    /**
     * 根据Unicode编码完美的判断中文汉字和符号
     *
     * @param car 字符串
     * @return boolean
     */
    private static boolean isChinese(char car) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(car);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * 完整的判断中文汉字和符号
     *
     * @param string 字符串
     * @return boolean
     */
    public static boolean isChinese(String string) {
        if (EmptyUtils.isEmpty(string)) {
            return false;
        }
        char[] ch = string.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取字符串中的纯数字
     *
     * @param str 字符串
     * @return String 数字
     */
    public static String getNumber(String str) {
        StringBuilder newString = new StringBuilder();
        if (EmptyUtils.isNotEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    newString.append(str.charAt(i));
                }
            }
        }
        return newString.toString();
    }

    /**
     * 截取字符最后一个之前的所有字符串
     * <ul>
     * <li>例：字符串：https://github.com/HughWick/toolbox</li>
     * <li>返回字符串：https://github.com/HughWick</li>
     * </ul>
     *
     * @param value 字符串
     * @param cha   拼接的字符
     * @return String 获取指定最后一个字符之前所有字符串
     */
    public static String before(String value, String cha) {
        if (EmptyUtils.isEmpty(value)) {
            return value;
        }
        if (value.contains(cha)) {
            int lastIndexOf = value.lastIndexOf(cha);
            return value.substring(0, lastIndexOf);
        }
        return value;
    }

    /**
     * 截取字符最后一个之后的所有字符串
     * <ul>
     * <li>例：字符串：C:\\Users\\Lenovo\\Desktop\\0e9f4beeb6a5423585c6eabda21a63ee.jpg</li>
     * <li>返回字符串：0e9f4beeb6a5423585c6eabda21a63ee.jpg</li>
     * </ul>
     *
     * @param value 字符串
     * @param cha   拼接的字符
     * @return String 获取指定最后一个字符之后所有字符串
     * @since 1.2.2
     */
    public static String after(String value, String cha) {
        if (EmptyUtils.isEmpty(value)) {
            return value;
        }
        if (value.contains(cha)) {
            int lastIndexOf = value.lastIndexOf(cha) + 1;
            return value.substring(lastIndexOf);
        }
        return value;
    }


    /**
     * 查询字符串中的对应varchar的长度
     * <ul>
     * <li>由于旧的Mysql数据库一个中文算2个字节、本方法将字符串中的中文按2个长度进行合计</li>
     * </ul>
     *
     * @param value 字符串
     * @return int 长度
     * @since 1.1.3
     */
    public static int varcharSize(String value) {
        int length = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {  /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
            String temp = value.substring(i, i + 1);  /* 获取一个字符 */
            if (temp.matches(chinese)) {     /* 判断是否为中文字符 */
                length += 2;  /* 中文字符长度为2 */
            } else {
                length += 1;  /* 其他字符长度为1 */
            }
        }
        return length;
    }

    /**
     * 左补信息
     *
     * @param original     原始字符串
     * @param targetLength 目标长度
     * @param unit         补的元素
     * @return 结果
     * @since 1.3.1
     */
    public static String leftPadding(final String original, final int targetLength,
                                     final char unit) {
        //1. fast-return
        final int originalLength = original.length();
        if (originalLength >= targetLength) {
            return original;
        }
        //2. 循环补零
        StringBuilder stringBuilder = new StringBuilder(targetLength);
        for (int i = originalLength; i < targetLength; i++) {
            stringBuilder.append(unit);
        }
        stringBuilder.append(original);
        return stringBuilder.toString();
    }

    /**
     * 左补信息
     * 默认左补零 0
     *
     * @param original     原始字符串
     * @param targetLength 目标长度
     * @return String 结果
     * @since 1.3.1
     */
    public static String leftPadding(final String original,
                                     final int targetLength) {
        return leftPadding(original, targetLength, '0');
    }

    /**
     * 驼峰命名转下划线,小写
     *
     * @param camelStr 驼峰字符串
     * @return String 下划线小写字符串
     * @since 1.3.1
     */
    public static String camelToUnderline(String camelStr) {
        if (StringUtil.isEmpty(camelStr)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        char[] chars = camelStr.toCharArray();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰命名转下划线,大写
     * <p>调用{@link #camelToUnderline(String)}驼峰转下划线命名方式</p>
     *
     * @param camelStr 驼峰字符串
     * @return String 下划线大写字符串
     * @since 1.3.1
     */
    public static String camelToUnderlineUppercase(String camelStr) {
        String str = camelToUnderline(camelStr);
        return str == null ? null : str.toUpperCase();
    }
}
