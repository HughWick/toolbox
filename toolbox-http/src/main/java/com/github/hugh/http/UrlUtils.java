package com.github.hugh.http;

import com.github.hugh.constant.StrPool;
import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.JsonObjectUtils;
import com.github.hugh.json.gson.JsonObjects;
import com.github.hugh.json.gson.Jsons;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.StringUtils;
import com.google.gson.JsonElement;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * url 工具类
 *
 * @author Hugh
 * @since 2.0.0
 **/
public class UrlUtils {

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
}
