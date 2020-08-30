package com.github.hugh.util;

import net.sf.json.JSONNull;

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
     * <li>isEmpty("abc") = false</li>
     * </ul>
     *
     * @param obj 待检查对象
     *  @return boolean {@code true}空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null || obj == "") {
            return true;
        } else if (obj instanceof String) {
            String str = (String) obj;
            return str.length() == 0 || (str.trim()).equals("null");// 长度等于0 或者 为字符串的null 返回true
        } else if (obj instanceof JSONNull) {
            return true;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty
     * <p>实用于对String Collection及其子类 Map及其子类</p>
     *
     * @param obj 待检查对象
     * @return boolean {@code true}不为空
     *
     * <p>null   = false</p>
     * <p>"null" = false</p>
     * <p>""     = false</p>
     * <p>" "    = true</p>
     * <p>"abc"  = true</p>
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
