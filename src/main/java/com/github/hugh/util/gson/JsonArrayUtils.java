package com.github.hugh.util.gson;

import com.google.gson.JsonArray;

/**
 * @author hugh
 * @version 1.3.0
 */
public class JsonArrayUtils {

    /**
     * 判断{@link JsonArray} 是否为空
     *
     * @param jsonArray 数组
     * @return boolean 空返回{@code true}
     * @since 1.3.9
     */
    public static boolean isEmpty(JsonArray jsonArray) {
        return JsonObjectUtils.isJsonNull(jsonArray) || jsonArray.size() <= 0;
    }

    /**
     * 判断{@link JsonArray} 不为空
     * <p>调用{@link #isEmpty(JsonArray)}反转结果</p>
     *
     * @param jsonArray 数组
     * @return boolean {@code true}不为空
     * @since 1.3.9
     */
    public static boolean isNotEmpty(JsonArray jsonArray) {
        return !isEmpty(jsonArray);
    }
}
