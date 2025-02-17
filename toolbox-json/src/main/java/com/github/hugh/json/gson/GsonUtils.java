package com.github.hugh.json.gson;

import com.alibaba.fastjson.JSON;
import com.github.hugh.constant.DateCode;
import com.github.hugh.constant.StrPool;
import com.github.hugh.json.exception.ToolboxJsonException;
import com.github.hugh.json.gson.adapter.CustomDateTypeAdapter;
import com.github.hugh.json.gson.adapter.MapTypeAdapter;
import com.github.hugh.util.DateUtils;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.MapUtils;
import com.github.hugh.util.regex.RegexUtils;
import com.google.common.base.Suppliers;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;

/**
 * gson 工具类
 *
 * @author hugh
 * @since 2.4.10
 */
@Slf4j
public class GsonUtils {
    public GsonUtils() {
    }

    /**
     * [年-月-日 时:分:秒]完整的日期格式
     */
    protected static final String YEAR_MONTH_DAY_HOUR_MIN_SEC = "yyyy-MM-dd HH:mm:ss";

    /**
     * 单例模式
     */
    private static Supplier<Gson> gsonSupplier;

    /**
     * 单例的创建Gson
     *
     * @return Gson
     * @since 1.3.12
     */
    public static synchronized Gson getInstance() {
        if (gsonSupplier == null) {
            Supplier<Gson> easyRedisSupplier = Gson::new;
            gsonSupplier = Suppliers.memoize(easyRedisSupplier::get);
        }
        return gsonSupplier.get();
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
        return getAsString(jsonElement);
    }

    /**
     * 方便的方法来获得这个元素的字符串值。
     * <p>
     * 由于gson 的{@link JsonObject#get(String)}方法返回字符串时前后带有双引号，所以需要再调用{@link JsonElement#getAsString()}
     * </p>
     * <p>
     * 如果元素为{@link JsonArray} 那么直接返回字符串
     * </p>
     *
     * @param jsonElement json元素对象
     * @return String
     * @since 2.3.0
     */
    public static String getAsString(JsonElement jsonElement) {
        if (isJsonNull(jsonElement)) {
            return null;
        }
        if (jsonElement.isJsonArray() || jsonElement.isJsonObject()) {
            return jsonElement.toString();
        }
        return jsonElement.getAsString();
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
            throw new ToolboxJsonException("JsonObject is null !");
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
        return fromJson(object, JsonObject.class);
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
     * @param <T>       泛型
     * @return List 集合
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
     * json数组转 list实体对象
     *
     * @param jsonArray json数组
     * @param clazz     对象
     * @param <T>       泛型
     * @return List
     * @since 2.2.5
     */
    public static <T> List<T> toArrayList(JsonArray jsonArray, Class<T> clazz) {
        List<T> resultList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            T result;
//            if (clazz == null) {
//                result = (T) new Jsons(jsonElement);
//            } else {
            result = JSON.parseObject(jsonElement.toString(), clazz);
//            }
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * json数组转 list实体对象
     *
     * @param str json规则的集合字符串
     * @param <T> 泛型
     * @return List
     * @since 2.3.14
     */
    public static <T> List<T> toArrayList(String str) {
        return toArrayList(str, null);
    }

    /**
     * json数组转 list实体对象
     *
     * @param str   json规则的集合字符串
     * @param clazz 对象
     * @param <T>   泛型
     * @return List
     * @since 2.3.14
     */
    public static <T> List<T> toArrayList(String str, Class<T> clazz) {
        JsonArray jsonArray = parseArray(str);
        if (jsonArray == null) {
            throw new ToolboxJsonException("array is null");
        }
        return toArrayList(jsonArray, clazz);
    }

    /**
     * 对象转换为{@link HashMap}
     *
     * <p>使用的自定义的{@link MapTypeAdapter}解析器,重写了数值转换校验</p>
     * <p>该方法转换出来的value数据类型都为{@link String}</p>
     * <p>如需要将实体中的值转换为对应类型的值，使用{@link MapUtils#entityToMap(Object)}</p>
     *
     * @param object 对象
     * @param <E>    对象泛型
     * @param <K>    键泛型
     * @param <V>    value泛型
     * @return Map
     * @since 1.4.0
     */
    public static <K, V, E> Map<K, V> toMap(E object) {
        JsonObject jsonObject;
        if (object instanceof JsonObject) {
            jsonObject = (JsonObject) object;
        } else {
            jsonObject = parse(object);
        }
        String strJson = toJson(jsonObject);
        Type type = new TypeToken<Map<K, V>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(type, new MapTypeAdapter()).create();
        return gson.fromJson(strJson, type);
    }

    /**
     * 对象转换为{@link HashMap}后，根据key进行升序排序
     *
     * @param object 对象
     * @param <E>    对象泛型
     * @param <K>    键泛型
     * @param <V>    value泛型
     * @since 2.3.11
     */
    public static <K, V, E> Map<K, V> toMapSortByKeyAsc(E object) {
        return toMapSortByKey(object, true);
    }

    /**
     * 对象转换为{@link HashMap}后，根据key进行降序排序
     *
     * @param object 对象
     * @param <E>    对象泛型
     * @param <K>    键泛型
     * @param <V>    value泛型
     * @since 2.3.11
     */
    public static <K, V, E> Map<K, V> toMapSortByKeyDesc(E object) {
        return toMapSortByKey(object, false);
    }

    /**
     * 对象转换为{@link HashMap}后，根据key进行排序
     *
     * @param object 对象
     * @param flag   true-升序、false-降序
     * @param <E>    对象泛型
     * @param <K>    键泛型
     * @param <V>    value泛型
     * @since 2.3.11
     */
    private static <K, V, E> Map<K, V> toMapSortByKey(E object, boolean flag) {
        Map objectObjectMap = toMap(object);
        if (flag) {
            return MapUtils.sortByKeyAsc(objectObjectMap);
        } else {
            return MapUtils.sortByKeyDesc(objectObjectMap);
        }
    }

    /**
     * 对象转换为{@link HashMap}后，根据value进行升序排序
     *
     * @param object 对象
     * @param <E>    对象泛型
     * @param <K>    键泛型
     * @param <V>    value泛型
     * @since 2.3.11
     */
    public static <K, V, E> Map<K, V> toMapSortByValueAsc(E object) {
        return toMapSortByValue(object, true);
    }

    /**
     * 对象转换为{@link HashMap}后，根据value进行降序排序
     *
     * @param object 对象
     * @param <E>    对象泛型
     * @param <K>    键泛型
     * @param <V>    value泛型
     * @since 2.3.11
     */
    public static <K, V, E> Map<K, V> toMapSortByValueDesc(E object) {
        return toMapSortByValue(object, false);
    }

    /**
     * 对象转换为{@link HashMap}后，根据value进行排序
     *
     * @param object 对象
     * @param flag   true-升序、false-降序
     * @param <E>    对象泛型
     * @param <K>    键泛型
     * @param <V>    value泛型
     * @since 2.3.11
     */
    private static <K, V, E> Map<K, V> toMapSortByValue(E object, boolean flag) {
        Map objectObjectMap = toMap(object);
        if (flag) {
            return MapUtils.sortByValueAsc(objectObjectMap);
        } else {
            return MapUtils.sortByValueDesc(objectObjectMap);
        }
    }

    /**
     * 将Json字符串转换为指定实体
     *
     * @param json     json字符串
     * @param classOfT 实体class
     * @param <E>      入参json类型
     * @param <T>      实体类型
     * @return 实体对象
     * @since 1.4.10
     */
    public static <E, T> T fromJson(E json, Class<T> classOfT) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Date.class, new CustomDateTypeAdapter());
            return fromJson(gsonBuilder, json, classOfT);
        } catch (JsonParseException jsonParseException) {
            log.error("JSON解析实体失败，内容：{}", json);
            throw jsonParseException;
        }
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
        return toJson(entity, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 实体转Json
     * <p>调用{@link #toJson(Object, String)} 将实体转换为json字符串,日期对象转换时,默认转换为:时间戳格式</p>
     *
     * @param entity 实体类型
     * @param <T>    实体类型
     * @return String json字符串
     * @since 2.3.10
     */
    public static <T> String toJsonTimestamp(T entity) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, typeOfSrc, context) -> {
            // convert date to long
            return new JsonPrimitive(date.getTime());
        });
        return gsonBuilder.create().toJson(entity);
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
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).disableHtmlEscaping().create();
        return gson.toJson(entity);
    }

    /**
     * 将字符串转化为指定实体类
     * <p>该方法主要作用与解析日期时、json字符串中的值为时间戳(long)类型时</p>
     * <p>版本2.2.5 之后入参更新为泛型</p>
     *
     * @param value    json字符串
     * @param classOfT 类
     * @param <E>      入参类型
     * @param <T>      实体类型
     * @return T 实体
     * @since 2.2.5
     */
    @Deprecated
    public static <E, T> T fromJsonTimeStamp(E value, Class<T> classOfT) {
        GsonBuilder builder = new GsonBuilder();
        //注册一个日期解析器、将时间戳转换为Date 类型
        builder.registerTypeAdapter(Date.class, new CustomDateTypeAdapter());
        return fromJson(builder, value, classOfT);
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个Long后转换为{@link Date}}对象.
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return Date json中获取key的Date值,没有返回{@code null}
     * @since 2.1.8
     */
    public static Date getDate(JsonObject jsonObject, String key) {
        String dateStr = getDateStr(jsonObject, key);
        if (EmptyUtils.isEmpty(dateStr)) {
            return null;
        }
        return DateUtils.parse(dateStr);
    }

    /**
     * 从JsonObject中获取一个完整时间的字符串
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @return String 完整时间格式的字符串:yyyy-MM-dd HH:mm:ss
     * @since 2.1.8
     */
    public static String getDateStr(JsonObject jsonObject, String key) {
        return getDateStr(jsonObject, key, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 从JsonObject中获取一个时间的字符串
     * <p>
     * 支持日期格式字符串：{@code yyyy-MM-dd HH:mm:ss} 、时间戳类型的字符串、日期对象字符串
     * </p>
     *
     * @param jsonObject JsonObject
     * @param key        Key
     * @param pattern    时间格式
     * @return String 时间格式的字符串
     * @since 2.1.8
     */
    public static String getDateStr(JsonObject jsonObject, String key, String pattern) {
        String string = getString(jsonObject, key);
        if (EmptyUtils.isEmpty(string)) {
            return null;
        }
        Date date;
        if (DateUtils.verifyDateStr(string, DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC)) {
            date = DateUtils.parse(string);
        } else if (RegexUtils.isNumeric(string)) {
            date = DateUtils.parseTimestamp(Long.parseLong(string));
        } else {
            date = DateUtils.dateStrToDate(string);
        }
        return date == null ? null : DateUtils.format(date, pattern);
    }

    /**
     * json对象转换为实体类
     *
     * @param gsonBuilder gson 指定转换参数类
     * @param json        json
     * @param classOfT    目标类
     * @param <E>         入参类型
     * @param <T>         出参类型
     * @return T
     */
    private static <E, T> T fromJson(GsonBuilder gsonBuilder, E json, Class<T> classOfT) {
        Gson gson = gsonBuilder.create();
        if (json instanceof JsonElement) {
            return gson.fromJson((JsonElement) json, classOfT);
        } else if (json instanceof String) {
            return gson.fromJson((String) json, classOfT);
        }
        String jsonStr = toJson(json);
        return gson.fromJson(jsonStr, classOfT);
    }

    /**
     * 判断key是否存在
     *
     * @param jsonObject   json对象
     * @param propertyName 属性名称
     * @return boolean 存在返回true
     * @since 2.2.5
     */
    public static boolean containsKey(JsonObject jsonObject, String propertyName) {
        if (jsonObject == null) {
            return false;
        }
        return jsonObject.has(propertyName);
    }

    /**
     * 验证字符串是否为 {@link JsonObject} 或  {@link JsonArray}
     *
     * @param string 字符串
     * @return boolean 是{@link JsonObject} 或  {@link JsonArray}其中一种返回{@code true}
     * @since 2.3.3
     */
    public static boolean isJsonValid(String string) {
        return isJsonObject(string) || isJsonArray(string);
    }

    /**
     * 字符串不是Json对象
     *
     * @param string 字符串
     * @return boolean
     * @since 2.3.4
     */
    public static boolean isNotJsonValid(String string) {
        return !isJsonValid(string);
    }

    /**
     * 验证字符串是否为 {@link JsonObject}
     *
     * @param str 字符串
     * @return boolean 能够解析返回{@code true}
     * @since 2.3.3
     */
    public static boolean isJsonObject(String str) {
        if (EmptyUtils.isEmpty(str)) {
            return false;
        }
        String trimmedStr = str.trim();
        if (!trimmedStr.startsWith(StrPool.CURLY_BRACKETS_START) || !trimmedStr.endsWith(StrPool.CURLY_BRACKETS_END)) {
            return false;
        }
        try {
            JsonElement jsonElement = JsonParser.parseString(trimmedStr);
            return jsonElement.isJsonObject();
        } catch (JsonParseException jsonParseException) {
            log.debug("字符串验证是JsonObject异常，内容：{}", str);
            return false;
        }
    }

    /**
     * 字符串不为{@link JsonObject}
     *
     * @param str 字符串
     * @return boolean
     * @since 2.3.4
     */
    public static boolean isNotJsonObject(String str) {
        return !isJsonObject(str);
    }

    /**
     * 验证字符串是否为 {@link JsonArray}
     *
     * @param str 字符串
     * @return boolean 是返回{@code true}
     * @since 2.3.3
     */
    public static boolean isJsonArray(String str) {
        if (EmptyUtils.isEmpty(str)) {
            return false;
        }
        String trimmedStr = str.trim();
        if (!trimmedStr.startsWith(StrPool.BRACKET_START) || !trimmedStr.endsWith(StrPool.BRACKET_END)) {
            return false;
        }
        try {
            JsonElement jsonElement = JsonParser.parseString(trimmedStr);
            return jsonElement.isJsonArray();
        } catch (JsonParseException jsonParseException) {
            log.debug("字符串验证是JsonArray异常，内容：{}", str);
            return false;
        }
    }

    /**
     * 字符串不能转换为{@link JsonArray}
     *
     * @param str 字符串
     * @return boolean
     * @since 2.3.3
     */
    public static boolean isNotJsonArray(String str) {
        return !isJsonArray(str);
    }

    /**
     * 根据key获取json对象中的值是为空
     *
     * @param jsonObject {@link JsonObject}
     * @param key        键
     * @return boolean 空返回{@code true}
     * @since 2.3.3
     */
    public static boolean isEmptyValue(JsonObject jsonObject, String key) {
        String string = getString(jsonObject, key);
        return EmptyUtils.isEmpty(string);
    }

    /**
     * 根据key获取json对象中的值不为空空
     *
     * @param jsonObject {@link JsonObject}
     * @param key        键
     * @return boolean 不为空返回{@code true}
     * @since 2.3.3
     */
    public static boolean isNotEmptyValue(JsonObject jsonObject, String key) {
        return !isEmptyValue(jsonObject, key);
    }

    /**
     * 统计字符串中json包出现的次数
     *
     * @param str 字符串
     * @return int
     * @since 2.3.10
     */
    public static int countJson(String str) {
        return parseMultipleJson(str, null).size();
    }

    /**
     * 解析字符串中的json，并将其放入list集合中
     *
     * @param str 字符串
     * @return List
     * @since 2.3.10
     */
    public static <T> List<T> parseMultipleJson(String str) {
        return parseMultipleJson(str, null);
    }

    /**
     * 解析字符串中的json，并将其放入list集合中
     * <p>
     * 由于gson无法解析同一个json字符串中两种日期格式（即存在时间戳，又有标准的年月日时分秒格式），会报错，这里使用{@link JSON#parseObject(String, Class)}进行对象转换
     * </p>
     *
     * @param str   字符串
     * @param clazz 实体类
     * @return List
     * @since 2.3.10
     */
    public static <T> List<T> parseMultipleJson(String str, Class<T> clazz) {
        if (EmptyUtils.isEmpty(str)) {
            throw new ToolboxJsonException("string is null");
        }
        List<T> item = new ArrayList<>();
        // json左括号出现的次数
        int countHead = 0;
        int headIndex = 0;
        // json 右括号出现的次数
        int countTail = 0;
        int tailIndex = 0;
        char charAtJsonHead = StrPool.CURLY_BRACKETS_START.charAt(0);
        char charAtJsonTail = StrPool.CURLY_BRACKETS_END.charAt(0);
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            // 计算第一个左括号出现的下标与次数
            if (charAt == charAtJsonHead) {
                countHead++;
                if (countHead == 1) {// json左括号起始位
                    headIndex = i;
                }
            }
            // 缓存json右括号出现位置与次数
            if (charAt == charAtJsonTail) {
                countTail++;
                tailIndex = i;
            }
            // 两个数相等，则为一组
            if ((countHead > 0 && countTail > 0) && (countHead == countTail)) {
                // 头与尾计数置零
                countHead = 0;
                countTail = 0;
                String substring = str.substring(headIndex, tailIndex + 1);
                if (clazz == null) {
                    item.add((T) new Jsons(substring));
                } else {
                    item.add(JSON.parseObject(substring, clazz));
                }
            }
        }
        return item;
    }

    /**
     * 将 XML 字符串转换为指定类型的 Java 对象
     *
     * @param <T>   转换目标类型的泛型
     * @param xml   要转换的 XML 字符串
     * @param clazz 目标 Java 类型的 Class 对象
     * @return 转换后的 Java 对象
     * @since 2.7.16
     */
    public static <T> T xmlToObject(String xml, Class<T> clazz) {
        return xmlToObject(xml, clazz, null);
    }

    /**
     * 将 XML 字符串转换为指定类型的 Java 对象，并支持指定根元素的解析
     *
     * @param <T>     转换目标类型的泛型
     * @param xml     要转换的 XML 字符串
     * @param clazz   目标 Java 类型的 Class 对象
     * @param rootKey 根元素的名称，若为 null 或空，则直接解析整个 XML
     * @return 转换后的 Java 对象
     * @since 2.7.16
     */
    public static <T> T xmlToObject(String xml, Class<T> clazz, String rootKey) {
        Jsons jsons = xmlToJson(xml);
        // 如果根元素为空，则直接将 XML 转换为目标 Java 对象
        if (EmptyUtils.isEmpty(rootKey)) {
            return jsons.formJson(clazz);
        } else {
            // 如果指定了根元素，获取该根元素的内容并转换为目标 Java 对象
            Jsons aThis = jsons.getThis(rootKey);
            return aThis.formJson(clazz);
        }
    }

    /**
     * XML字符串转JSON对象
     *
     * @param xml xml字符串
     * @return Jsons
     * @since 2.7.16
     */
    public static Jsons xmlToJson(String xml) {
        Map<String, Object> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(new StringReader(xml));
        } catch (DocumentException documentException) {
            throw new ToolboxJsonException(documentException);
        }
        Element root = document.getRootElement();
        map.put(root.getName(), elementToMap(root));
        return Jsons.on(map);
    }

    /**
     * Element对象转map对象
     *
     * @param element Element对象
     * @return map
     * @since 2.7.16
     */
    public static Map<String, Object> elementToMap(Element element) {
        Map<String, Object> map = new HashMap<>();
        for (Object child : element.elements()) {
            Element element1 = (Element) child;
            if (element1.elements().isEmpty()) {
                map.put(element1.getName(), element1.getText());
            } else {
                map.put(element1.getName(), elementToMap(element1));
            }
        }
        return map;
    }
}
