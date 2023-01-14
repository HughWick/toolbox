package com.github.hugh.json.gson;

import com.google.gson.JsonObject;

/**
 * gson 封装类
 * <p>
 * 由于使用{@link GsonUtils}的方法获取值时需要每次将转{@link JsonObject}作为参数传入
 * </p>
 * <p>
 * 所以该类在new 对象时将需要转换的参数传入，然后转换成{@link JsonObject}
 * </p>
 * 而后只需要使用get方法获取对象中的参数即可。
 *
 * @author hugh
 * @since 2.4.11
 */
public class Jsons extends JsonObjects {

    public Jsons() {
        super();
    }

    public <T> Jsons(T object) {
        super(object);
    }
}
