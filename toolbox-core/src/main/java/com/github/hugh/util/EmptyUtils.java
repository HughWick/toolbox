package com.github.hugh.util;


import java.util.Collection;
import java.util.Map;

/**
 * 字符串工具类
 *
 * @author hugh
 * @since 0.0.1
 */
public class EmptyUtils {
    private EmptyUtils() {
    }

    /**
     * 判断对象是否Empty(null或元素为0)
     *
     * <ul>
     * <li>isEmpty(null) = true</li>
     * <li>isEmpty("null") = true</li>
     * <li>isEmpty("") = true</li>
     * <li>isEmpty(" ") = true</li>
     * <li>isEmpty("abc") = false</li>
     * </ul>
     *
     * @param <T> 对象泛型
     * @param obj 待检查对象
     * @return boolean {@code true}空
     */
    public static <T> boolean isEmpty(T obj) {
        if (obj == null || obj == "") {
            return true;
        } else if (obj instanceof String[]) {
            return ((String[]) obj).length == 0;
        } else if (obj instanceof int[]) {
            return ((int[]) obj).length == 0;
        } else if (obj instanceof String) {
            // 去除首位的空白，包括英文和其他所有语言中的空白字符
            String str = obj.toString().strip().trim();
            return str.length() == 0 || "null".equals(str);// 长度等于0 或者 为字符串的null 返回true
        } else if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty
     * <p>实用于对String Collection及其子类 Map及其子类</p>
     *
     * @param <T> 对象泛型
     * @param obj 待检查对象
     * @return boolean {@code true}不为空
     * <ul>
     * <li>isNotEmpty(null)     = false</li>
     * <li>isNotEmpty("null")   = false</li>
     * <li>isNotEmpty("")       = false</li>
     * <li>isNotEmpty(" ")      = true</li>
     * <li>isNotEmpty("abc")    = true</li>
     * </ul>
     */
    public static <T> boolean isNotEmpty(T obj) {
        return !isEmpty(obj);
    }
}
