package com.github.hugh.util.lang;

import com.github.hugh.constant.StrPool;
import com.github.hugh.util.EmptyUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 字符串格式化
 *
 * @see <a href="https://github.com/dromara/hutool/blob/v5-master/hutool-core/src/main/java/cn/hutool/core/text/StrFormatter.java">strForm</a>
 * @since 2.4.9
 */
public class StringFormatter {

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param strPattern 字符串模板
     * @param argArray   参数列表
     * @return String
     */
    public static String format(String strPattern, Object... argArray) {
        return formatWith(strPattern, StrPool.EMPTY_JSON, argArray);
    }

    /**
     * 格式化字符串<br>
     * 此方法只是简单将指定占位符 按照顺序替换为参数<br>
     * 如果想输出占位符使用 \\转义即可，如果想输出占位符之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "{}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "{}", "a", "b") =》 this is {} for a<br>
     * 转义\： format("this is \\\\{} for {}", "{}", "a", "b") =》 this is \a for b<br>
     *
     * @param strPattern  字符串模板
     * @param placeHolder 占位符，例如{}
     * @param argArray    参数列表
     * @return String
     * @since 2.4.9
     */
    public static String formatWith(String strPattern, String placeHolder, Object... argArray) {
        if (EmptyUtils.isEmpty(strPattern) || EmptyUtils.isEmpty(placeHolder) || argArray == null){
            return strPattern;
        }
        final int strPatternLength = strPattern.length();
        final int placeHolderLength = placeHolder.length();
        // 初始化定义好的长度以获得更好的性能
        final StringBuilder stringBuilder = new StringBuilder(strPatternLength + 50);
        int handledPosition = 0;// 记录已经处理到的位置
        int delimIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
            delimIndex = strPattern.indexOf(placeHolder, handledPosition);
            if (delimIndex == -1) {// 剩余部分无占位符
                if (handledPosition == 0) { // 不带占位符的模板直接返回
                    return strPattern;
                }
                // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                stringBuilder.append(strPattern, handledPosition, strPatternLength);
                return stringBuilder.toString();
            }
            // 转义符
            if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == StrPool.BACKSLASH.toCharArray()[0]) {// 转义符
                if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == StrPool.BACKSLASH.toCharArray()[0]) {// 双转义符
                    // 转义符之前还有一个转义符，占位符依旧有效
                    stringBuilder.append(strPattern, handledPosition, delimIndex - 1);
                    stringBuilder.append(str(argArray[argIndex], StandardCharsets.UTF_8));
                    handledPosition = delimIndex + placeHolderLength;
                } else {
                    // 占位符被转义
                    argIndex--;
                    stringBuilder.append(strPattern, handledPosition, delimIndex - 1);
                    stringBuilder.append(placeHolder.charAt(0));
                    handledPosition = delimIndex + 1;
                }
            } else {// 正常占位符
                stringBuilder.append(strPattern, handledPosition, delimIndex);
                stringBuilder.append(str(argArray[argIndex], StandardCharsets.UTF_8));
                handledPosition = delimIndex + placeHolderLength;
            }
        }
        // 加入最后一个占位符后所有的字符
        stringBuilder.append(strPattern, handledPosition, strPatternLength);
        return stringBuilder.toString();
    }

    /**
     * 将对象转为字符串
     * <pre>
     * 	 1、Byte数组和ByteBuffer会被转换为对应字符串的数组
     * 	 2、对象数组会调用Arrays.toString方法
     * </pre>
     *
     * @param obj     对象
     * @param charset 字符集
     * @return 字符串
     */
    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof byte[]) {
            return Arrays.toString((byte[]) obj);
        } else if (obj instanceof Byte[]) {
            return Arrays.toString((Byte[]) obj);
//        } else if (obj instanceof ByteBuffer) {
//            return str(obj, charset);
        } else if (obj.getClass().isArray()) {
            return Arrays.toString((Object[]) obj);
        }
        return obj.toString();
    }
}
