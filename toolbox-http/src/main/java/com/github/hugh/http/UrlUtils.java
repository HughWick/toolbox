package com.github.hugh.http;

import com.github.hugh.constant.StrPool;
import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.Jsons;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.StringUtils;
import com.google.gson.JsonElement;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * url 工具类
 *
 * @author Hugh
 * @since 2.0.0
 **/
public class UrlUtils {
    private UrlUtils() {
    }

    /**
     * 在url后拼接参数
     *
     * @param url  URL
     * @param data 参数
     * @param <T>  参数类型
     * @return String 拼接好参数的Get请求url
     * @since 1.3.0
     */
    public static <T> String urlParam(String url, T data) {
        if (EmptyUtils.isEmpty(url)) {
            return StrPool.EMPTY;
        }
        if (EmptyUtils.isEmpty(data)) {
            return url;
        }
        String last = url.substring(url.length() - 1);
        if (!"?".equals(last)) {// 当最后一个字符不为"?"时进行拼接
            url += "?";
        }
        String params = jsonParse(data);
        url += params;
        return url;
    }

    /**
     * 将json中键值对拼接为url对应的参数
     *
     * @param json 参数
     * @param <T>  参数类型
     * @return String 拼接后的URL参数字符串
     */
    public static <T> String jsonParse(T json) {
        if (json == null) {
            return StrPool.EMPTY;
        }
        Jsons jsonObjects = new Jsons(json);
        if (jsonObjects.isNull()) {
            return StrPool.EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, JsonElement> entrySet : jsonObjects.entrySet()) {
            String asString = GsonUtils.getAsString(entrySet.getValue());
            if (asString == null) {
                asString = StrPool.EMPTY;
            }
            String value = URLEncoder.encode(asString, StandardCharsets.UTF_8);//将参数转换为urlEncoder码
            stringBuilder.append(entrySet.getKey()).append("=").append(value).append("&");
        }
        return StringUtils.trimLastPlace(stringBuilder);
    }

    /**
     * 解析 URL 参数并将其转换为 Map
     * <p>
     * 该方法将传入的 URL 参数字符串解析为键值对并返回一个 Map。默认使用 UTF-8 编码格式进行解码。
     * </p>
     *
     * @param urlParams 要解析的 URL 参数字符串，格式为 "key1=value1&key2=value2"。
     * @return 返回一个包含解析后参数的 Map，键和值都经过 URL 解码。
     * @since 2.7.4
     */
    public static Map<String, String> parseUrl(String urlParams) {
        return parseUrl(urlParams, StandardCharsets.UTF_8.name());
    }

    /**
     * 解析 URL 参数并将其转换为 Map（支持自定义编码格式）
     * <p>
     * 该方法将传入的 URL 参数字符串解析为键值对并返回一个 Map。可以指定自定义的编码格式进行解码。
     * </p>
     *
     * @param urlParams 要解析的 URL 参数字符串，格式为 "key1=value1&key2=value2"。
     * @param encoding  用于解码 URL 参数的字符编码格式，通常使用 UTF-8 或其他编码。
     * @return 返回一个包含解析后参数的 Map，键和值都经过 URL 解码。
     */
    public static Map<String, String> parseUrl(String urlParams, String encoding) {
        // 如果 URL 参数字符串为空，则返回一个空的 Map
        if (urlParams == null || urlParams.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return parseUrlInternal(urlParams, encoding);
        } catch (UnsupportedEncodingException e) {
            // 如果编码不支持，抛出异常
            throw new IllegalArgumentException("Unsupported encoding: " + encoding, e);
        }
    }

    /**
     * 内部方法：解析 URL 参数并将其转换为 Map
     * <p>
     * 该方法将传入的 URL 参数字符串解析为键值对并返回一个 Map。
     * </p>
     *
     * @param urlParams 要解析的 URL 参数字符串，格式为 "key1=value1&key2=value2"。
     * @param encoding  用于解码 URL 参数的字符编码格式。
     * @return 返回一个包含解析后参数的 Map，键和值都经过 URL 解码。
     * @throws UnsupportedEncodingException 如果提供的编码格式不支持，抛出此异常。
     */
    private static Map<String, String> parseUrlInternal(String urlParams, String encoding) throws UnsupportedEncodingException {
        Map<String, String> paramMap = new HashMap<>();
        String[] params = urlParams.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            // 对键和值进行解码，并放入 Map 中
            String key = URLDecoder.decode(keyValue[0], encoding);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], encoding) : StrPool.EMPTY;
            paramMap.put(key, value);
        }
        return paramMap;
    }

//    /**
//     * 解析 URL 参数字符串为 Map 的方法
//     *
//     * @param urlParams 包含 URL 参数的字符串
//     * @return 解析后的参数 Map
//     * @since 2.7.4
//     */
//    public static Map<String, String> parseUrl(String urlParams) {
//        // 如果 URL 参数字符串为空，则返回一个空的 Map
//        if (EmptyUtils.isEmpty(urlParams)) {
//            return new HashMap<>();
//        }
//        Map<String, String> paramMap = new HashMap<>();
//        String[] params = urlParams.split("&");
//        for (String param : params) {
//            String[] keyValue = param.split("=");
//            // 对键和值进行解码，并放入 Map 中
//            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
//            String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
//            paramMap.put(key, value);
//        }
//        return paramMap;
//    }

    /**
     * 将 URL 参数字符串解析为 Jsons 对象
     *
     * @param urlParams 包含 URL 参数的字符串
     * @return 解析后的 Jsons 对象
     * @since 2.7.4
     */
    public static Jsons parseUrlJson(String urlParams) {
        return Jsons.on(parseUrl(urlParams));
    }
}
