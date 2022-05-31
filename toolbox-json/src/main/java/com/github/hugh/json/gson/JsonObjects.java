package com.github.hugh.json.gson;

import com.alibaba.fastjson.JSON;
import com.github.hugh.json.exception.ToolboxJsonException;
import com.github.hugh.json.gson.adapter.MapTypeAdapter;
import com.github.hugh.util.EmptyUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

/**
 * 由于使用{@link JsonObjectUtils}的方法获取值时需要每次将转{@link JsonObject}作为参数传入，所以该类在new 对象时将需要转换的参数传入，然后转换成{@link JsonObject}
 * 而后只需要使用get方法获取对象中的参数即可。
 *
 * @author hugh
 * @since 1.5.2
 */
public class JsonObjects extends JsonObjectUtils {

    private JsonObject jsonObject;

    /**
     * 无参构造
     */
    public JsonObjects() {
        this.jsonObject = new JsonObject();
    }

    /**
     * 带参构造
     * <p>支持字符于泛型</p>
     *
     * @param object 参数
     * @param <T>    参数类型
     */
    public <T> JsonObjects(T object) {
        if (object instanceof String) {
            this.jsonObject = parse(object);
        } else {
            this.jsonObject = parse(toJson(object));
        }
    }

    /**
     * jsonObject对象为null、或者对象长度为0
     *
     * @return boolean {@code true} jsonObject为空
     */
    public boolean isNull() {
        return isJsonNull(jsonObject) || jsonObject.size() == 0;
    }

    /**
     * jsonObject 不为空
     *
     * @return boolean {@code true} jsonObject不为空
     * @since 1.6.12
     */
    public boolean isNotNull() {
        return !isJsonNull(jsonObject);
    }

    /**
     * 获取gson中对应的{@link JsonObject}后放入封装的{@link JsonObjects}中，方便后续使用
     *
     * @param key 键
     * @return {@link JsonObjects}
     * @since 1.7.4
     */
    public JsonObjects getThis(String key) {
        return new JsonObjects(getJsonObject(jsonObject, key));
    }

    /**
     * get{@link JsonArray}
     *
     * @param key 键
     * @return {@link JsonArray} json中获取key的{@link JsonArray} 值,没有返回{@code null}
     */
    public JsonArray getJsonArray(String key) {
        return getJsonArray(jsonObject, key);
    }

    /**
     * 获取{@link JsonObject}中对应key的{@link JsonObject}值。
     *
     * @param key 键
     * @return {@link JsonObject} json中获取key的JsonObject值,没有返回{@code null}
     */
    public JsonObject getJsonObject(String key) {
        return getJsonObject(jsonObject, key);
    }

    /**
     * 获取{@link JsonObject}中对应key的字符串值。
     *
     * @param key 键
     * @return String json中获取key的Integer值,没有返回{@code null}
     */
    public String getString(String key) {
        return getString(jsonObject, key);
    }

    /**
     * 获取{@link JsonObject}中对应key的int值。
     *
     * @param key 键
     * @return int 获取key的Integer值,没有返回{@code 0}
     */
    public int getInt(String key) {
        return getInt(jsonObject, key);
    }

    /**
     * 获取{@link JsonObject}中对应key的{@link Integer}值。
     *
     * @param key KEY
     * @return Integer 获取key的{@link Integer}值,没有返回{@code null}
     * @since 1.5.4
     */
    public Integer getInteger(String key) {
        return getInteger(jsonObject, key);
    }

    /**
     * 获取{@link JsonObject}中对应key的double值。
     *
     * @param key 键
     * @return double json中获取key的Double值,没有返回{@code 0}
     */
    public double getDoubleValue(String key) {
        return getDoubleValue(jsonObject, key);
    }

    /**
     * 获取{@link JsonObject}中对应key的{@link Double}值。
     *
     * @param key Key
     * @return Double key的{@link Double}值,没有返回{@code null}
     * @since 1.5.4
     */
    public Double getDouble(String key) {
        return getDouble(jsonObject, key);
    }

    /**
     * 获取{@link JsonObject}中对应key的long值。
     *
     * @param key 键
     * @return double json中获取key的long值,没有返回{@code 0}
     * @since 1.5.2
     */
    public long getLongValue(String key) {
        return getLongValue(jsonObject, key);
    }

    /**
     * 以空值安全的方式从JsonObject中获取一个Long.
     *
     * @param key Key
     * @return Long json中没有获取key的value时返回{@code null}
     * @since 1.5.4
     */
    public Long getLong(String key) {
        return getLong(jsonObject, key);
    }

    /**
     * 校验 {@link #jsonObject}中根据key获取到的value，是否当前传入的值相同
     *
     * @param key   键
     * @param value value
     * @return boolean {@code true} 相同
     * @since 1.5.11
     */
    public boolean isEquals(String key, String value) {
        if (isNull()) {
            return false;
        }
        String string = getString(key);
        if (EmptyUtils.isEmpty(string)) {
            return false;
        }
        return string.equals(value);
    }

    /**
     * 将{@link JsonObjects#jsonObject}内的json对象转换成json格式的字符串
     *
     * @return String
     * @since 1.6.6
     */
    public String toJson() {
        return toJson(jsonObject);
    }

    /**
     * 将{@link #jsonObject} 转换为对应的实体
     * <p>
     * 默认使用yyyy-MM-dd HH:mm:ss 格式的字符串转化为{@link Date}对象
     * </p>
     *
     * @param classOfT 实体类
     * @param <T>      泛型
     * @return T 实体
     * @since 1.6.7
     */
    public <T> T formJson(Class<T> classOfT) {
        return fromJson(this.jsonObject, classOfT, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 将{@link #jsonObject} 转换为对应的实体
     *
     * @param classOfT 实体类
     * @param <T>      泛型
     * @return T 实体
     * @since 2.1.9
     */
    public <T> T formJson(Class<T> classOfT, String dateFormat) {
        return fromJson(this.jsonObject, classOfT, dateFormat);
    }

    /**
     * 将{@link #jsonObject} 转换为对应的实体
     * <p>该方法主要作用与解析日期时、json字符串中的值为时间戳(long)类型时</p>
     *
     * @param classOfT 实体类
     * @param <T>      泛型
     * @return T 实体
     * @since 1.6.13
     */
    public <T> T fromJsonTimeStamp(Class<T> classOfT) {
        return fromJsonTimeStamp(this.jsonObject.toString(), classOfT);
    }

    /**
     * 重写
     *
     * @return String
     */
    @Override
    public String toString() {
        return this.jsonObject.toString();
    }

    /**
     * 向{@link #jsonObject}中添加key-value
     *
     * @param key   键
     * @param value 值
     * @since 1.7.6
     */
    public <E> void addProperty(String key, E value) {
        if (this.jsonObject == null) {
            this.jsonObject = new JsonObject();
        }
        if (value instanceof String) {
            this.jsonObject.addProperty(key, (String) value);
        } else if (value instanceof Integer) {
            this.jsonObject.addProperty(key, (Integer) value);
        } else if (value instanceof Double) {
            this.jsonObject.addProperty(key, (Double) value);
        } else if (value instanceof Long) {
            this.jsonObject.addProperty(key, (Long) value);
        } else if (value instanceof Boolean) {
            this.jsonObject.addProperty(key, (Boolean) value);
        } else {
            throw new ToolboxJsonException("Not supported type : " + value.getClass().getName());
        }
    }

    /**
     * 移除{@link #jsonObject}key对应的属性
     *
     * @param key KEY
     * @return JsonElement
     * @since 2.0.0
     */
    public JsonElement removeProperty(String key) {
        return this.jsonObject.remove(key);
    }

    /**
     * 获取当前对象中的list数据，并且转换为对应的list实体集合
     *
     * @param key   获取集合的key
     * @param clazz 需要转换的类
     * @param <E>   类型
     * @return List
     * @since 2.1.7
     */
    public <E> List<E> toList(String key, Class<E> clazz) {
        JsonArray jsonArray = getJsonArray(this.jsonObject, key);
        assert jsonArray != null;
        if (clazz == null) {
            return toArrayList(jsonArray);
        }
        List<E> resultList = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            E e = JSON.parseObject(jsonElement.toString(), clazz);
            resultList.add(e);
        }
        return resultList;
    }

    /**
     * 转化为list
     * <p>
     * 由于使用了{@link MapTypeAdapter} 转换器 ，默认返回的结果对象为{@link LinkedTreeMap}
     * </p>
     *
     * @param key 获取集合的key
     * @param <E> 类型
     * @return List
     * @since 2.1.7
     */
    public <E> List<E> toList(String key) {
        return toList(key, null);
    }

    /**
     * 获取{@link JsonObject}中对应key的{@link Date}。
     *
     * @param key 键
     * @return Date 日期对象
     * @since 2.1.8
     */
    public Date getDate(String key) {
        return getDate(this.jsonObject, key);
    }

    /**
     * 获取{@link JsonObject}中对应key的日期对象，并转化为字符串。
     *
     * @param key KEY
     * @return String 完整时间格式的字符串:yyyy-MM-dd HH:mm:ss
     * @since 2.1.8
     */
    public String getDateStr(String key) {
        return getDateStr(this.jsonObject, key);
    }

    /**
     * 获取{@link JsonObject}中对应key的日期对象，并转化为字符串。
     *
     * @param key     KEY
     * @param pattern 时间格式
     * @return String
     * @since 2.1.8
     */
    public String getDateStr(String key, String pattern) {
        return getDateStr(this.jsonObject, key, pattern);
    }

    /**
     * 将json对象转化为map
     *
     * @param <K> KEY
     * @param <V> VALUE
     * @return Map
     * @since 2.2.2
     */
    public <K, V> Map<K, V> toMap() {
        return toMap(this.jsonObject);
    }

    /**
     * 判断key是否存在
     *
     * @param propertyName key名称
     * @return boolean
     * @since 2.2.5
     */
    public boolean containsKey(String propertyName) {
        return containsKey(this.jsonObject, propertyName);
    }

    /**
     * 返回这个对象的一个成员集合。这个集合是有顺序的，而这个顺序是在 元素被添加。
     *
     * @return Set
     * @since 2.3.0
     */
    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return this.jsonObject.entrySet();
    }
}
