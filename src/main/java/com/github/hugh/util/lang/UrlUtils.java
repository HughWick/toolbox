package com.github.hugh.util.lang;

import com.github.hugh.util.EmptyUtils;
import net.sf.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

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
     * @return String 拼接好参数的Get请求url
     * @since 1.3.0
     */
    public static String urlParam(String url, JSONObject data) {
        if (EmptyUtils.isEmpty(url)) {
            return "";
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
     * @return String 拼接后的URL参数字符串
     */
    public static String jsonParse(JSONObject json) {
        if (json == null || json.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = String.valueOf(iterator.next());
            String value = String.valueOf(json.get(key));
            value = URLEncoder.encode(value, StandardCharsets.UTF_8);//将参数转换为urlEncoder码
            sb.append(key).append("=").append(value).append("&");
        }
        sb.deleteCharAt(sb.length() - 1); // 删除结尾符号
        return sb.toString();
    }
}
