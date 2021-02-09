package com.github.hugh.support;

import com.github.hugh.util.gson.JsonObjectUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 由于使用{@link JsonObjectUtils}的方法获取值时需要每次将转{@link JsonObject}作为参数传入，故而该类在创建该class时将需要转换的参数传入，然后转换成{@link JsonObject}
 * 而后只需要使用get方法获取对象中的参数即可。
 *
 * @author hugh
 * @since 1.5.3
 */
public class JsonObjects<T> extends JsonObjectUtils {

    private JsonObject jsonObject;

    public JsonObjects(T object) {
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
     * @param key 键
     * @return {@link JsonObject}
     */
    public JsonObjects json(String key) {
        return new JsonObjects<>(getJsonObject(jsonObject, key));
    }

    /**
     * get{@link JsonArray}
     *
     * @param key 键
     * @return {@link JsonArray}
     */
    public JsonArray getJsonArray(String key) {
        return getJsonArray(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的{@link JsonObject}值。
     *
     * @param key 键
     * @return {@link JsonObject}
     */
    public JsonObject getJsonObject(String key) {
        return getJsonObject(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的字符串值。
     *
     * @param key 键
     * @return String
     */
    public String getString(String key) {
        return getString(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的int值。
     *
     * @param key 键
     * @return int
     */
    public int getInt(String key) {
        return getInt(jsonObject, key);
    }

    /**
     * 方便的方法获取{@link JsonObject}中对应key的double值。
     *
     * @param key 键
     * @return double
     */
    public double getDoubleValue(String key) {
        return getDoubleValue(jsonObject, key);
    }
}
