package com.github.hugh.util;

import com.github.hugh.constant.guava.CharMatchers;
import com.google.common.base.CaseFormat;
import jodd.util.StringUtil;

import java.util.regex.Pattern;

/**
 * 字符串工具
 *
 * @author hugh
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
     * <p>不包含小数点</p>
     *
     * @param str 字符串
     * @return String 0-9数字
     */
    public static String getNumber(String str) {
        return CharMatchers.NUMBER_CHAR_MATCHER.retainFrom(str);
    }

    /**
     * 获取字符串中的数字及小数点
     * <p>包含字符串中的小数点</p>
     *
     * @param str 字符串
     * @return String 保留小数点的数值
     */
    public static String getDouble(String str) {
        return CharMatchers.NUMBERS_AND_DECIMAL_POINTS.retainFrom(str);
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
     * 计算字符串中的对应varchar的长度
     * <ul>
     * <li>由于旧的Mysql数据库一个中文算2个字节、本方法将字符串中的中文按2个长度进行合计</li>
     * </ul>
     *
     * @param value 字符串
     * @return int 长度
     * @since 1.1.3
     */
    public static int varcharSize(String value) {
        var length = 0;
        var chinese = "[\u0391-\uFFE5]";
        for (var i = 0; i < value.length(); i++) {  /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
            var temp = value.substring(i, i + 1);  /* 获取一个字符 */
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
     * @return String
     * @since 1.3.1
     */
    public static String leftPadding(final String original, final int targetLength, final char unit) {
        //1. fast-return
        final int originalLength = original.length();
        if (originalLength >= targetLength) {
            return original;
        }
        //2. 复制需要补充的长度 减掉 源数据的长度 加上 源字符串
        return (String.valueOf(unit).repeat(targetLength - originalLength)) +
                original;
    }

    /**
     * 左补信息
     * 默认左补零 0
     *
     * @param original     原始字符串
     * @param targetLength 目标长度
     * @return String
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
        var stringBuilder = new StringBuilder();
        var nextUpperCase = false;
        for (var i = 0; i < inputString.length(); i++) {
            var charAt = inputString.charAt(i);
            switch (charAt) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (stringBuilder.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;
                default:
                    if (nextUpperCase) {
                        stringBuilder.append(Character.toUpperCase(charAt));
                        nextUpperCase = false;
                    } else {
                        stringBuilder.append(Character.toLowerCase(charAt));
                    }
                    break;
            }
        }
        if (firstCharacterUppercase) {
            stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
        }
        return stringBuilder.toString();
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
        var matcher = BLANK_PATTERN.matcher(string);
        String result = matcher.replaceAll(replacement);
        result = result.replace("\\u00A0", replacement);
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
     * 去除前后空格
     * <p>由于{@link String#strip()}无法去除一些特定的字符，所以再调用一次{@link String#trim()}再次去除
     * </p>
     *
     * @param value 字符串
     * @return String
     * @since 2.0.2
     */
    public static String trim(String value) {
        return value.trim().strip();
    }

    /**
     * 去除前后指定字符
     *
     * <p>调用示例：System.out.println(trim(", ashuh  ",","));</p>
     *
     * @param source 目标字符串
     * @param beTrim 要删除的指定字符
     * @return String 删除之后的字符串
     * @since 1.4.0
     */
    public static String trim(String source, String beTrim) {
        if (source == null) {
            return "";
        }
        source = source.strip().trim();
        if (source.isEmpty()) {
            return "";
        }
        var beginChar = source.substring(0, 1);
        if (beginChar.equalsIgnoreCase(beTrim)) {
            source = source.substring(1);
        }
        // 循环去掉字符串尾的beTrim字符
        var endChar = source.substring(source.length() - 1);
        if (endChar.equalsIgnoreCase(beTrim)) {
            source = source.substring(0, source.length() - 1);
        }
        return source;
    }

    /**
     * 将字符串按照指定的字符与次数进行切割
     * <ul>
     * <li>注：返回结果中首字符为指定切割的字符</li>
     * <li>例：源字符串 http://hyga.hnlot.com.cn:8000/capture/DaHua/capture/6G0BEB9GA12F70A/2021/1/17/9946090cb09b4986af8615174e862b9e.jpg</li>
     * <li>获取"/"出现的第4次后的所有字符，结果为：/DaHua/capture/6G0BEB9GA12F70A/2021/1/17/9946090cb09b4986af8615174e862b9e.jpg</li>
     * </ul>
     *
     * @param string 源字符串
     * @param chr    匹配的字符
     * @param index  次数
     * @return String 字符串
     * @since 1.5.6
     */
    public static String after(String string, String chr, int index) {
        int index1 = indexOf(string, chr, index);
        return string.substring(index1);
    }

    /**
     * 根据字符串中指定字符的次数获取对应所在的下标
     *
     * @param string 源字符串
     * @param chr    匹配的字符
     * @param index  次数
     * @return int 位置下标
     * @since 1.5.6
     */
    public static int indexOf(String string, String chr, int index) {
        var pattern = Pattern.compile(chr);
        var findMatcher = pattern.matcher(string);
        var number = 0;
        while (findMatcher.find()) {
            number++;
            if (number == index) {//当指定出现次数满足时停止
                break;
            }
        }
        return findMatcher.start();
    }
}
