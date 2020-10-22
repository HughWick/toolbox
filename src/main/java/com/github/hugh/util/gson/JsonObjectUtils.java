package com.github.hugh.util.gson;

import com.github.hugh.exception.ToolboxException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigDecimal;

/**
 * 针对Gson进行二次封装处理工具类
 *
 * @author hugh
 * @version 1.3.0
 */
public class JsonObjectUtils {

    /**
     * 以空值安全的方式从JsonObject中获取一个String.
     * <ul>
     * <li>说明：针对{@link com.google.gson.JsonObject}在get其中value时、返回的结果集会默认带上两个""(双引号),需要二次调用{@link JsonElement#getAsString()}方法进行转义成常规没有双引号的结果</li>
     * <li>但是如果key在JsonObject内没有值时会返回null，导致二次条用需要二次调用其{@link JsonElement#getAsString()}方法出现空指针异常,所以该方法进行二次判断</li>
     * </ul>
     *
     * @param jsonObject jsonObject
     * @param key        KEY
     * @return String  json中获取key的Integer值,没有返回{@code null}
     */
    public static String getString(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return isJsonNull(jsonElement) ? null : jsonElement.getAsString();
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个Integer.
     * <ul>
     * <li>说明：针对{@link com.google.gson.JsonObject}在get其中value时、返回的结果集会默认带上两个""(双引号),需要二次调用{@link JsonElement#getAsInt()}方法进行转义成常规没有双引号的结果</li>
     * <li>但是如果key在JsonObject内没有值时会返回null，导致二次条用需要二次调用其{@link JsonElement#getAsInt()} ()}方法出现空指针异常,所以该方法进行二次判断</li>
     * </ul>
     *
     * @param jsonObject jsonObject
     * @param key        KEY
     * @return Integer json中获取key的Integer值,没有返回{@code null}
     */
    public static Integer getInteger(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return isJsonNull(jsonElement) ? null : jsonElement.getAsInt();
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个int.
     * <p>从{@link #getInteger(JsonObject, String)}获取Integer. </p>
     *
     * @param jsonObject jsonObject
     * @param key        Key
     * @return int json中获取key的Integer值,没有返回{@code 0}
     */
    public static int getInt(JsonObject jsonObject, String key) {
        Integer integer = getInteger(jsonObject, key);
        return integer == null ? 0 : integer;
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个Long.
     * <ul>
     * <li>说明：针对{@link com.google.gson.JsonObject}在get其中value时、返回的结果集会默认带上两个""(双引号),需要二次调用{@link JsonElement#getAsLong()}方法进行转义成常规没有双引号的结果</li>
     * <li>但是如果key在JsonObject内没有值时会返回null，导致二次条用需要二次调用其{@link JsonElement#getAsLong()} ()}方法出现空指针异常,所以该方法进行二次判断</li>
     * </ul>
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return Long json中没有获取key的value时返回{@code null}
     */
    public static Long getLong(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return isJsonNull(jsonElement) ? null : jsonElement.getAsLong();
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个Long.
     * <p>从{@link #getLong(JsonObject, String)}获取Long. </p>
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return long 在json中获取key的long返回为null时,返回{@code 0}
     */
    public static long getLongValue(JsonObject jsonObject, String key) {
        Long aLong = getLong(jsonObject, key);
        return aLong == null ? 0 : aLong;
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个Long.
     * <ul>
     * <li>说明：针对{@link com.google.gson.JsonObject}在get其中value时、返回的结果集会默认带上两个""(双引号),需要二次调用{@link JsonElement#getAsDouble()}方法进行转义成常规没有双引号的结果</li>
     * <li>但是如果key在JsonObject内没有值时会返回null，导致二次条用需要二次调用其{@link JsonElement#getAsDouble()} ()}方法出现空指针异常,所以该方法进行二次判断</li>
     * </ul>
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return Double json中获取key的Double值,没有返回{@code null}
     */
    public static Double getDouble(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return isJsonNull(jsonElement) ? null : jsonElement.getAsDouble();
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个Double.
     * <p>从{@link #getDouble(JsonObject, String)}获取Double. </p>
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return double json中获取key的Double值,没有返回{@code 0}
     */
    public static double getDoubleValue(JsonObject jsonObject, String key) {
        Double aDouble = getDouble(jsonObject, key);
        return aDouble == null ? 0 : aDouble;
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个jsonObject
     * <ul>
     * <li>说明：针对{@link com.google.gson.JsonObject}在get其中value时、返回的结果集会默认带上两个""(双引号),需要二次调用{@link JsonElement#getAsJsonObject()}方法进行转义成常规没有双引号的结果</li>
     * <li>但是如果key在JsonObject内没有值时会返回null，导致二次条用需要二次调用其{@link JsonElement#getAsJsonObject()} ()}方法出现空指针异常,所以该方法进行二次判断</li>
     * </ul>
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return JsonObject json中获取key的JsonObject值,没有返回{@code null}
     */
    public static JsonObject getJsonObject(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return isJsonNull(jsonElement) ? null : jsonElement.getAsJsonObject();
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个JsonArray
     * <ul>
     * <li>说明：针对{@link com.google.gson.JsonObject}在get其中value时、返回的结果集会默认带上两个""(双引号),需要二次调用{@link JsonElement#getAsJsonArray()}方法进行转义成常规没有双引号的结果</li>
     * <li>但是如果key在JsonObject内没有值时会返回null，导致二次条用需要二次调用其{@link JsonElement#getAsJsonArray()} ()}方法出现空指针异常,所以该方法进行二次判断</li>
     * </ul>
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return JsonArray json中获取key的JsonArray值,没有返回{@code null}
     */
    public static JsonArray getJsonArray(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return isJsonNull(jsonElement) ? null : jsonElement.getAsJsonArray();
    }


    /**
     * 以空值安全的方式从JsonObject中获取一个BigDecimal.
     * <ul>
     * <li>说明：针对{@link com.google.gson.JsonObject}在get其中value时、返回的结果集会默认带上两个""(双引号),需要二次调用{@link JsonElement#getAsBigDecimal()}方法进行转义成常规没有双引号的结果</li>
     * <li>但是如果key在JsonObject内没有值时会返回null，导致二次条用需要二次调用其{@link JsonElement#getAsBigDecimal()} ()}方法出现空指针异常,所以该方法进行二次判断</li>
     * </ul>
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return JsonArray json中获取key的BigDecimal值,没有返回{@code null}
     */
    public static BigDecimal getBigDecimal(JsonObject jsonObject, String key) {
        JsonElement jsonElement = get(jsonObject, key);
        return isJsonNull(jsonElement) ? null : jsonElement.getAsBigDecimal();
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

    /**
     * 判断JsonElement是否为null 或者 {@link com.google.gson.JsonNull}
     *
     * @param jsonElement JsonObject
     * @return boolean {@code true} null返回true
     * @since 1.3.3
     */
    private static boolean isJsonNull(JsonElement jsonElement) {
        return jsonElement == null || jsonElement.isJsonNull();
    }
}
