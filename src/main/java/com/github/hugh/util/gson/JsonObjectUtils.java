package com.github.hugh.util.gson;

import com.github.hugh.exception.ToolboxException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 针对Gson进行二次封装处理工具类
 *
 * @author hugh
 * @version 1.3.0
 */
public class JsonObjectUtils {

    /**
     * <ul>
     *     <li>说明：针对{@link com.google.gson.JsonObject}在get其中value时、返回的结果集会默认带上两个""(双引号),需要二次调用{@link JsonElement#getAsString()}方法进行转义成常规没有双引号的结果</li>
     *     <li>但是如果key在JsonObject内没有值时会返回null，导致二次条用需要二次调用其{@link JsonElement#getAsString()}方法出现空指针异常,所以该方法进行二次判断</li>
     * </ul>
     *
     * @param jsonObject json
     * @param key        KEY
     * @return String
     */
    public static String getString(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return jsonElement == null ? null : jsonElement.getAsString();
    }

    /**
     * 以空值安全的方式从Map中获取一个Integer.
     * <ul>
     *     <li>说明：针对{@link com.google.gson.JsonObject}在get其中value时、返回的结果集会默认带上两个""(双引号),需要二次调用{@link JsonElement#getAsInt()}方法进行转义成常规没有双引号的结果</li>
     *     <li>但是如果key在JsonObject内没有值时会返回null，导致二次条用需要二次调用其{@link JsonElement#getAsInt()} ()}方法出现空指针异常,所以该方法进行二次判断</li>
     * </ul>
     *
     * @param jsonObject json
     * @param key        KEY
     * @return Integer
     */
    public static Integer getInteger(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return jsonElement == null ? null : jsonElement.getAsInt();
    }

    /**
     * 以空值安全的方式从Map中获取一个int.
     * <p>从{@link #getInteger(JsonObject, String)}获取Integer. </p>
     *
     * @param jsonObject json
     * @param key        Key
     * @return int 在json中获取key下面的value返回为null时,返回{@code 0}
     */
    public static int getInt(JsonObject jsonObject, String key) {
        Integer integer = getInteger(jsonObject, key);
        return integer == null ? 0 : integer;
    }

    public static Long getLong(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return jsonElement == null ? null : jsonElement.getAsLong();
    }

    /**
     * @param jsonObject json
     * @param key        Key
     * @return long 在json中获取key的long返回为null时,返回{@code 0}
     */
    public static long getLongValue(JsonObject jsonObject, String key) {
        Long aLong = getLong(jsonObject, key);
        return aLong == null ? 0 : aLong;
    }

    public static Double getDouble(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return jsonElement == null ? null : jsonElement.getAsDouble();
    }

    /**
     * @param jsonObject json
     * @param key        Key
     * @return double 在json中获取key的double结果为null时,返回{@code 0}
     */
    public static double getDoubleValue(JsonObject jsonObject, String key) {
        Double aDouble = getDouble(jsonObject, key);
        return aDouble == null ? 0 : aDouble;
    }

    public static JsonObject getJsonObject(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return jsonElement == null ? null : jsonElement.getAsJsonObject();
    }

    public static JsonArray getJsonArray(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return jsonElement == null ? null : jsonElement.getAsJsonArray();
    }

    /**
     * 统一的获取JsonObject中元素方法
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return JsonElement
     */
    private static JsonElement get(JsonObject jsonObject, String key) {
        if (jsonObject == null) {
            throw new ToolboxException("JsonObject is null !");
        }
        return jsonObject.get(key);
    }
}
