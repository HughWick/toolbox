package com.github.hugh.util.gson;

import com.github.hugh.constant.DateCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.support.instance.Instance;
import com.github.hugh.util.gson.adapter.MapTypeAdapter;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * 针对Gson进行二次封装处理工具类
 *
 * @author hugh
 * @version 1.3.0
 */
public class JsonObjectUtils {

    /**
     * 单例的创建Gson
     *
     * @return Gson
     * @since 1.3.12
     */
    public static Gson gson() {
        return Instance.getInstance().singleton(Gson.class);
    }

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
    public static boolean isJsonNull(JsonElement jsonElement) {
        return jsonElement == null || jsonElement.isJsonNull();
    }

    /**
     * 将Object 解析为 JsonObject
     * <p>Object为空时返回{@code null}</p>
     *
     * @param object 待解析参数
     * @param <T>    入参泛型
     * @return JsonObject {@link JsonObject}
     * @since 1.3.4
     */
    public static <T> JsonObject parse(T object) {
        JsonElement jsonElement = JsonParser.parseString(String.valueOf(object));
        return isJsonNull(jsonElement) ? null : jsonElement.getAsJsonObject();
    }

    /**
     * 将Object 解析为 JsonArray
     * <p>Object为空时返回{@code null}</p>
     *
     * @param object 待解析参数
     * @param <T>    入参泛型
     * @return JsonArray {@link JsonArray}
     * @since 1.3.4
     */
    public static <T> JsonArray parseArray(T object) {
        JsonElement jsonElement = JsonParser.parseString(String.valueOf(object));
        return isJsonNull(jsonElement) ? null : jsonElement.getAsJsonArray();
    }

    /**
     * 将jsonArray 转换为{@link ArrayList}
     * <p>
     * 由于使用了{@link MapTypeAdapter} 转换器 ，默认返回的结果对象为{@link LinkedTreeMap}
     * </p>
     *
     * @param jsonArray 数组
     * @return ArrayList 集合
     * @since 1.3.7
     */
    public static <T> List<T> toArrayList(JsonArray jsonArray) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Type type = new TypeToken<ArrayList<T>>() {
        }.getType();
        Gson gson = gsonBuilder
                .registerTypeAdapter(type, new MapTypeAdapter()).create();
        return gson.fromJson(jsonArray, type);
    }

    /**
     * jsonObject 转换为{@link HashMap}
     *
     * <p>使用的自定义的{@link MapTypeAdapter}解析器,重写了数值转换校验</p>
     *
     * @param jsonObject json
     * @param <K>        键泛型
     * @param <V>        value泛型
     * @return Map
     * @since 1.4.0
     */
    public static <K, V> Map<K, V> toMap(JsonObject jsonObject) {
        String strJson = toJson(jsonObject);
        Type type = new TypeToken<Map<K, V>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(type, new MapTypeAdapter()).create();
        return gson.fromJson(strJson, type);
    }

    /**
     * 将Json转换为指定实体
     * <p>由于{@link Gson#fromJson(Reader, Type)}无法静态调用，故而这里进行二次封装</p>
     *
     * @param json     参数
     * @param classOfT 转换类型
     * @param <T>      转换泛型
     * @return T
     * @since 1.3.7
     */
    public static <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return gson().fromJson(json, classOfT);
    }

    /**
     * 将Json字符串转换为指定实体
     *
     * @param json     字符串json
     * @param classOfT 实体类
     * @param <T>      实体
     * @return T 实体类
     * @since 1.3.12
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson().fromJson(json, classOfT);
    }

    /**
     * 将Json字符串转换为指定实体
     *
     * @param json       json字符串
     * @param classOfT   实体class
     * @param dateFormat 日期格式
     * @param <T>        实体类型
     * @return 1.4.10
     */
    public static <T> T fromJson(String json, Class<T> classOfT, String dateFormat) {
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        return gson.fromJson(json, classOfT);
    }

    /**
     * 实体转Json
     * <p>调用{@link #toJson(Object, String)} 将实体转换为json字符串,日期对象转换时,默认转换为:yyyy-MM-dd HH:mm:ss格式</p>
     *
     * @param entity 实体类型
     * @param <T>    实体类型
     * @return String json字符串
     * @since 1.3.12
     */
    public static <T> String toJson(T entity) {
        return toJson(entity, DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 实体转Json,并且指定日期格式
     *
     * @param entity     实体类型
     * @param dateFormat 日期格式
     * @param <T>        实体类型
     * @return String json字符串
     * @since 1.3.12
     */
    public static <T> String toJson(T entity, String dateFormat) {
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        return gson.toJson(entity);
    }

    /**
     * 将字符串转化为指定实体类
     * <p>该方法主要作用与解析日期时、json字符串中的值为时间戳(long)类型时</p>
     *
     * @param value    json字符串
     * @param classOfT 类
     * @param <T>      实体类型
     * @return T 实体
     * @since 1.6.13
     */
    public static <T> T fromJsonTimeStamp(String value, Class<T> classOfT) {
        GsonBuilder builder = new GsonBuilder();
        //注册一个日期解析器、将时间戳转换为Date 类型
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (jsonElement, typeOfT, context) ->
                new Date(jsonElement.getAsJsonPrimitive().getAsLong()));
        Gson gson = builder.create();
        return gson.fromJson(value, classOfT);
    }
}
