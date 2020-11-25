package com.github.hugh.util;

import com.google.common.base.CaseFormat;
import jodd.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 *
 * @author huhg
 * @since 1.0.6
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 空白信息的表达式
     *
     * @since 1.3.1
     */
    private static final Pattern BLANK_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * 空字符串
     */
    private static final String EMPTY = "";

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
     * 校验字符串中是否存在中文
     *
     * @param string 字符串
     * @return boolean
     */
    public static boolean isContainChinese(String string) {
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
     * 校验字符串不是全中文名称
     *
     * @param string 字符串
     * @return boolean {@code true}
     * @since 1.4.3
     */
    public static boolean isNotContainChinese(String string) {
        return !isContainChinese(string);
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
    public static String leftPadding(final String original, final int targetLength, final char unit) {
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
    public static String leftPadding(final String original, final int targetLength) {
        return leftPadding(original, targetLength, '0');
    }

    /**
     * 驼峰命名转下划线,小写
     * <p>该转换方式调用{@link CaseFormat#to(CaseFormat, String)}</p>
     *
     * @param camelStr 驼峰字符串
     * @return String 下划线小写字符串
     * @since 1.3.1
     */
    public static String camelToUnderline(String camelStr) {
        if (StringUtil.isEmpty(camelStr)) {
            return null;
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelStr);
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

    /**
     * 将字符串转换为驼峰写法.
     * <ul>
     * <li>注：这是 mybatis-gen 源码</li>
     * <li>支持字符串格式_,-,@,#,$, ,/,{@code &}</li>
     * </ul>
     *
     * @param inputString             输入字符串
     * @param firstCharacterUppercase 首字母是否大写。
     * @return String 驼峰写法
     * @since 1.3.1
     */
    public static String getCamelCase(String inputString, boolean firstCharacterUppercase) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            switch (c) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;
                default:
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }
        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
        return sb.toString();
    }

    /**
     * 将字符串中所有空格替换为指定元素
     *
     * @param string      原始字符串
     * @param replacement 待替换的文本
     * @return String 替换后结果
     * @since 1.3.1
     */
    public static String replaceAnyBlank(final String string, final String replacement) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }
        Matcher m = BLANK_PATTERN.matcher(string);
        String result = m.replaceAll(replacement);
        //160 &nbsp;
        result = result.replaceAll("\\u00A0", replacement);
        return result;
    }

    /**
     * 替换掉任意空格为空
     * <p>调用{@link #replaceAnyBlank(String, String)}替换字符串中所有空格</p>
     *
     * @param string 原始字符串
     * @return String 替换后结果
     * @since 1.3.1
     */
    public static String replaceAnyBlank(final String string) {
        return replaceAnyBlank(string, EMPTY);
    }

    /**
     * 去除前后指定字符
     *
     * <p>调用示例：System.out.println(trim(", ashuh  ",","));</p>
     *
     * @param source 目标字符串
     * @param beTrim 要删除的指定字符
     * @return 删除之后的字符串
     * @since 1.4.0
     */
    public static String trim(String source, String beTrim) {
        if (source == null) {
            return "";
        }
        source = source.trim(); // 循环去掉字符串首的beTrim字符
        if (source.isEmpty()) {
            return "";
        }
        String beginChar = source.substring(0, 1);
        if (beginChar.equalsIgnoreCase(beTrim)) {
            source = source.substring(1, source.length());
            beginChar = source.substring(0, 1);
        }
        // 循环去掉字符串尾的beTrim字符
        String endChar = source.substring(source.length() - 1, source.length());
        if (endChar.equalsIgnoreCase(beTrim)) {
            source = source.substring(0, source.length() - 1);
            endChar = source.substring(source.length() - 1, source.length());
        }
        return source;
    }
}
