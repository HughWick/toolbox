package com.github.hugh.util.gson;

import com.google.gson.JsonArray;

/**
 * @author hugh
 * @version 1.3.0
 */
public class JsonArrayUtils {

    /**
     * @param jsonArray 数组
     * @return boolean 空返回{@code true}
     * @since 1.3.9
     */
    public static boolean isEmpty(JsonArray jsonArray) {
        return JsonObjectUtils.isJsonNull(jsonArray) || jsonArray.size() <= 0;
    }

    /**
     * @param jsonArray 数组
     * @return boolean 不为空返回{@code true}
     * @since 1.3.9
     */
    public static boolean isNotEmpty(JsonArray jsonArray) {
        return !isEmpty(jsonArray);
    }
}
