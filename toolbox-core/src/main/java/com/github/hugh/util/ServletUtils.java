package com.github.hugh.util;

import com.github.hugh.constant.StrPool;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.io.StreamUtils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * servlet Utils
 *
 * @author Hugh
 * @since 2.1.2
 **/
public class ServletUtils {

    private ServletUtils() {
    }

    public static final String[] PAGE_PARAMS = {"page", "size"};

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
        return getParams(request, PAGE_PARAMS);
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

    /**
     * 根据 Content-Type 和请求体，获取指定键（key）对应的表单数据。
     * 默认使用 {@code \\r\\n\\r\\n} 作为请求体内容分隔符。
     *
     * @param contentType Content-Type 头部信息
     * @param body        请求体内容
     * @param key         要获取的表单数据的键名
     * @return 指定键对应的表单数据值，如果找不到则返回 null
     * @since 2.7.2
     */
    public static String getFormData(String contentType, String body, String key) {
        return getFormData(contentType, body, key, "\\r\\n\\r\\n");
    }

    /**
     * 根据 Content-Type、请求体和分隔符，获取指定键（key）对应的表单数据。
     *
     * @param contentType Content-Type 头部信息
     * @param body        请求体内容
     * @param key         要获取的表单数据的键名
     * @param regex       请求体内容分隔符的正则表达式
     * @return 指定键对应的表单数据值，如果找不到则返回 null
     * @since 2.7.2
     */
    public static String getFormData(String contentType, String body, String key, String regex) {
        Map<String, String> formDataMap = getFormDataMap(contentType, body, regex);
        return formDataMap.get(key);
    }

    /**
     * 从form-data格式的请求体中获取所有参数，并将参数名与值存入Map中返回。
     *
     * @param contentType 请求头中的 Content-Type 字段
     * @param body        请求体内容
     * @param regex       用于拆分参数值的正则表达式（例如："\r\n"）
     * @return 存储所有参数名与值的Map
     * @since 2.7.2
     */
    public static Map<String, String> getFormDataMap(String contentType, String body, String regex) {
        // 从 Content-Type 头部信息中获取分隔符
        Pattern pattern = Pattern.compile("boundary=(.*)");
        Matcher matcher = pattern.matcher(contentType);
        String boundary = "";
        if (matcher.find()) {
            boundary = matcher.group(1);
        }
        // 根据请求体内容分隔符将参数拆分
        String[] parts = body.split(StrPool.DASHED + StrPool.DASHED + boundary);
        // 创建一个用于存储参数的Map
        Map<String, String> formData = new HashMap<>();
        // 遍历每个部分
        for (String part : parts) {
            // 检查 Content-Disposition 头部信息
            Pattern namePattern = Pattern.compile("Content-Disposition: form-data; name=\"(.*?)\"");
            Matcher nameMatcher = namePattern.matcher(part);
            // 如果匹配到 name 字段的部分
            if (nameMatcher.find()) {
                String name = nameMatcher.group(1);
                String[] valueSplit = part.split(regex);
                if (valueSplit.length > 1) {
                    // 获取参数值
                    String value = valueSplit[1].strip().trim();
                    formData.put(name, value);
                } else {
                    formData.put(name, null);
                }
            }
        }
        return formData;
    }
}
