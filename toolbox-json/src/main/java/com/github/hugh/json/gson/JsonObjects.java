package com.github.hugh.json.gson;

import com.google.gson.JsonObject;

/**
 * 由于使用{@link JsonObjectUtils}的方法获取值时需要每次将转{@link JsonObject}作为参数传入，所以该类在new 对象时将需要转换的参数传入，然后转换成{@link JsonObject}
 * 而后只需要使用get方法获取对象中的参数即可。
 *
 * @author hugh
 * @since 1.5.2
 * @deprecated 因明名方式过长，为了简化，2.4.11后使用{@link Jsons}
 */
@Deprecated
public class JsonObjects extends Jsons {

    public JsonObjects() {
        super();
    }

    public <T> JsonObjects(T object) {
        super(object);
    }

    /**
     * 获取gson中对应的{@link JsonObject}后放入封装的{@link JsonObjects}中，方便后续使用
     *
     * @param key 键
     * @return {@link JsonObjects}
     * @since 1.7.4
     */
    public JsonObjects getThis(String key) {
        final Jsons aThis = super.getThis(key);
        return new JsonObjects(aThis.toJson());
    }

    /**
     * 向Json对象中中添加key-value
     *
     * @param key   键
     * @param value 值
     * @param <E>   value 参数类型
     * @return JsonObjects
     * @since 1.7.6
     */
    public <E> JsonObjects addProperty(String key, E value) {
        return (JsonObjects) super.addProperty(key, value);
    }
}
