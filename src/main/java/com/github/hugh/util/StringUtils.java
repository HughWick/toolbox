package com.github.hugh.util;

/**
 * 字符串工具
 *
 * @author huhg
 * @since 1.0.6
 */
public class StringUtils {

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
}
