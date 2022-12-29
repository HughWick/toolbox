package com.github.hugh.util;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.io.StreamUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
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
     * @param <K>     key 类型
     * @param <V>     value 类型
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
     * @param <K>     key 类型
     * @param <V>     value 类型
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
     * @param <K>     key 类型
     * @param <V>     value 类型
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

    /**
     * 获取请求体
     *
     * @param request {@link ServletRequest}
     * @return String 获得请求体
     * @since 2.4.9
     */
    public static String getBody(ServletRequest request) {
        try (final BufferedReader reader = request.getReader()) {
            return StreamUtils.read(reader);
        } catch (IOException ioException) {
            throw new ToolboxException(ioException);
        }
    }
}
