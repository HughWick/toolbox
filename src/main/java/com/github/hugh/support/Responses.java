package com.github.hugh.support;


import lombok.Data;
import net.sf.json.JSONObject;

/**
 * 返回信息工具类
 * <p>方法返回结果都为{@link JSONObject}</p>
 *
 * @author hugh
 * @see 1.4.0
 */
@Data
public class Responses<T> {

    /**
     * code键
     */
    private String codeKey;

    /**
     * code值
     */
    private String codeValue;

    /**
     * 信息-key
     */
    private String messageKey;

    /**
     * 信息-value
     */
    private String messageValue;

    /**
     * 数据-key
     */
    private String dataKey;

    /**
     * 数据-value
     */
    private T dataValue;

    /**
     * 数量-key
     */
    private String countKey;

    /**
     * 数量-value
     */
    private long countValue;

    public Responses(String codeKey, String codeValue, String messageKey, String messageValue) {
        this.codeKey = codeKey;
        this.codeValue = codeValue;
        this.messageKey = messageKey;
        this.messageValue = messageValue;
    }

    public Responses(String codeKey, String codeValue, String messageKey, String messageValue, String dataKey, T dataValue) {
        this.codeKey = codeKey;
        this.codeValue = codeValue;
        this.messageKey = messageKey;
        this.messageValue = messageValue;
        this.dataKey = dataKey;
        this.dataValue = dataValue;
    }

    public Responses(String codeKey, String codeValue, String messageKey, String messageValue, String dataKey, T dataValue, String countKey, long countValue) {
        this.codeKey = codeKey;
        this.codeValue = codeValue;
        this.messageKey = messageKey;
        this.messageValue = messageValue;
        this.dataKey = dataKey;
        this.dataValue = dataValue;
        this.countKey = countKey;
        this.countValue = countValue;
    }

    /**
     * 返回信息
     *
     * @return JSONObject
     */
    public JSONObject info() {
        JSONObject json = new JSONObject();
        json.put(this.codeKey, this.codeValue);
        json.put(this.messageKey, this.messageValue);
        return json;
    }

    /**
     * 返回带数据的信息
     *
     * @return JSONObject
     */
    public JSONObject data() {
        JSONObject json = new JSONObject();
        json.put(this.codeKey, this.codeValue);
        json.put(this.messageKey, this.messageValue);
        json.put(this.dataKey, this.dataValue);
        return json;
    }

    /**
     * 返回带数据,与总数的信息
     *
     * @return JSONObject
     */
    public JSONObject complete() {
        JSONObject json = new JSONObject();
        json.put(this.codeKey, this.codeValue);
        json.put(this.messageKey, this.messageValue);
        json.put(this.dataKey, this.dataValue);
        json.put(this.countKey, this.countValue);
        return json;
    }
}
