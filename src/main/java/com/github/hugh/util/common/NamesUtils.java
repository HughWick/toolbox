package com.github.hugh.util.common;

/**
 * 姓名工具类
 *
 * @author hugh
 * @since 1.2.8
 */
public class NamesUtils {

    /**
     * 空字符串
     */
    private static final String EMPTY = "";
    /**
     * 长度0
     */
    private static final int ZERO = 0;
    /**
     * 长度1
     */
    private static final int ONE = 1;
    /**
     * 长度2
     */
    private static final int TWO = 2;
    /**
     * 长度3
     */
    private static final int THREE = 3;
    /**
     * 长度4
     */
    private static final int FOUR = 4;

    /**
     * 将字符串中的中文姓名脱敏
     * <p>根据不同名字的长度返回不同的显示数据</p>
     * <ul>
     * <li>例:</li>
     * <li>张三 to *三</li>
     * <li>王月如 to 王*如</li>
     * <li>欧阳震华 to 欧**华</li>
     * <li>麦麦提·赛帕克 to 麦麦提·***</li>
     * </ul>
     *
     * @param str 字符串
     * @return String 脱敏后的姓名字符串
     */
    public static String encrypt(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 1) {
            return str;
        } else if (length == TWO) {
            return "*" + right(str, 1);
        } else if (length == THREE) {
            return left(str, ONE) + "*" + right(str, ONE);
        } else if (length == FOUR) {
            return left(str, ONE) + "**" + right(str, ONE);
        } else if (length > 4) {
            String mid = "·";// 少数名族分隔符
            if (str.contains(mid)) {
                String[] arr = str.split(mid);
                StringBuilder t = new StringBuilder(arr[0]);
                t.append(mid);
                int last = arr[1].length();
                for (int i = 0; i < last; i++) {
                    t.append("*");
                }
                str = t.toString();
            }
        }
        return str;
    }

    /**
     * 字符串向左截取
     *
     * @param str 字符串
     * @param len 长度
     * @return java.lang.String
     */
    private static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < ZERO) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(ZERO, len);
    }

    /**
     * 字符串向右截取
     *
     * @param str 字符串
     * @param len 长度
     * @return java.lang.String
     */
    private static String right(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < ZERO) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }
}
