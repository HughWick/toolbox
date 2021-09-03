package com.github.hugh.util.gson;

import com.github.hugh.util.EmptyUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 由于使用{@link JsonObjectUtils}的方法获取值时需要每次将转{@link JsonObject}作为参数传入，所以该类在new 对象时将需要转换的参数传入，然后转换成{@link JsonObject}
 * 而后只需要使用get方法获取对象中的参数即可。
 *
 * @author hugh
 * @since 1.5.2
 */
public class JsonObjects extends JsonObjectUtils {

    private JsonObject jsonObject;

    public JsonObjects() {
        this.jsonObject = new JsonObject();
    }

    public <T> JsonObjects(T object) {
        this.jsonObject = parse(object);
    }

    /**
     * jsonObject 为空
     *
     * @return boolean {@code true} jsonObject为空
     */
    public boolean isNull() {
        return isJsonNull(jsonObject);
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
     * 方便的方法获取{@link JsonObject}中对应key的{@link JsonObject}值。
     *
     * @param key 键
     * @return {@link JsonObject} json中获取key的JsonObject值,没有返回{@code null}
     */
    public JsonObject getJsonObject(String key) {
        return getJsonObject(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的字符串值。
     *
     * @param key 键
     * @return String json中获取key的Integer值,没有返回{@code null}
     */
    public String getString(String key) {
        return getString(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的int值。
     *
     * @param key 键
     * @return int 获取key的Integer值,没有返回{@code 0}
     */
    public int getInt(String key) {
        return getInt(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的{@link Integer}值。
     *
     * @param key KEY
     * @return Integer 获取key的{@link Integer}值,没有返回{@code null}
     * @since 1.5.4
     */
    public Integer getInteger(String key) {
        return getInteger(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的double值。
     *
     * @param key 键
     * @return double json中获取key的Double值,没有返回{@code 0}
     */
    public double getDoubleValue(String key) {
        return getDoubleValue(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的{@link Double}值。
     *
     * @param key Key
     * @return Double key的{@link Double}值,没有返回{@code null}
     * @since 1.5.4
     */
    public Double getDouble(String key) {
        return getDouble(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的long值。
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
     * 校验 {@link #jsonObject}中的key获取到的value值是否与相同
     *
     * @param key   KEY
     * @param value value
     * @return boolean {@code true} 相同
     * @since 1.5.11
     */
    public boolean isEquals(String key, String value) {
        if (isNull()) {
            return false;
        }
        String string = getString(key);
        if (EmptyUtils.isEmpty(key)) {
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
     *
     * @param classOfT 实体类
     * @param <T>      泛型
     * @return T 实体
     * @since 1.6.7
     */
    public <T> T formJson(Class<T> classOfT) {
        return fromJson(this.jsonObject, classOfT);
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
     * 向{{@link #jsonObject}}中添加key-value
     *
     * @param key   键
     * @param value 值
     * @since 1.7.6
     */
    public void addProperty(String key, String value) {
        if (this.jsonObject == null) {
            this.jsonObject = new JsonObject();
        }
        this.jsonObject.addProperty(key, value);
    }
}
