package com.github.hugh.util.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 由于使用{@link JsonObjectUtils}的方法获取值时需要每次将转{@link JsonObject}作为参数传入，所以该类在new 对象时将需要转换的参数传入，然后转换成{@link JsonObject}
 * 而后只需要使用get方法获取对象中的参数即可。
 *
 * @author hugh
 * @since 1.5.2
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
     * 获取gson中对应的{@link JsonObject}后放入封装的{@link JsonObjects}中，方便后去使用
     *
     * @param key 键
     * @return {@link JsonObjects}
     */
    public JsonObjects jsonObjects(String key) {
        return new JsonObjects<>(getJsonObject(jsonObject, key));
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
     * @return int json中获取key的Integer值,没有返回{@code 0}
     */
    public int getInt(String key) {
        return getInt(jsonObject, key);
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
}
