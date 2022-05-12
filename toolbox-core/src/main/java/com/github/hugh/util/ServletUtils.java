package com.github.hugh.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * servlet Utils
 *
 * @author Hugh
 * @since 2.1.2
 **/
public class ServletUtils {

    /**
     * 循环所有request中的参数放入至map
     *
     * @param request 客户端的请求
     * @return Map
     */
    public static <K, V> Map<K, V> getParams(HttpServletRequest request) {
        Map map = new LinkedHashMap<>();
        if (request == null) {
            return map;
        }
        Enumeration<String> isKey = request.getParameterNames();
        while (isKey.hasMoreElements()) {
            String key = isKey.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 遍历查询参数
     * <ul>
     * <li>并且移除其中的{@code page}、{@code size}键</li>
     * </ul>
     *
     * @param request 请求头
     * @return Map
     */
    public static <K, V> Map<K, V> getParamsDeleteLimit(HttpServletRequest request) {
        return getParams(request, "page", "size");
    }

    /**
     * 遍历查询参数、并且移除其中指定多余键
     *
     * @param request 客户端的请求
     * @param keys    需要删除的key
     * @return Map
     */
    public static <K, V> Map<K, V> getParams(HttpServletRequest request, String... keys) {
        Map map = getParams(request);
        if (keys == null) {
            return map;
        }
        MapUtils.removeKeys(map, keys);
        return map;
    }
}
