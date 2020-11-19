package com.github.hugh.support;


import java.util.HashMap;
import java.util.Map;

/**
 * 返回信息工具类
 */
public class Responses {

    private String codeKey;

    private String codeValue;

    private String messageKey;

    private String messageValue;

    private String dataKey;

    private Object dataValue;

    public Responses(String codeKey, String codeValue, String messageKey, String messageValue) {
        this.codeKey = codeKey;
        this.codeValue = codeValue;
        this.messageKey = messageKey;
        this.messageValue = messageValue;
    }

    public Responses(String codeKey, String codeValue, String messageKey, String messageValue, String dataKey, Object dataValue) {
        this.codeKey = codeKey;
        this.codeValue = codeValue;
        this.messageKey = messageKey;
        this.messageValue = messageValue;
        this.dataKey = dataKey;
        this.dataValue = dataValue;
    }

    public Map<String, Object> success() {
        Map<String, Object> map = new HashMap<>();
        map.put(this.codeKey, this.codeValue);
        map.put(this.messageKey, this.messageValue);
        return map;
    }

    public  Map<String, Object> successData( ) {
        if (this.dataValue == null) {
            return success();
        }
        Map<String, Object> map = new HashMap<>();
        map.put(this.codeKey, this.codeValue);
        map.put(this.messageKey, this.messageValue);
        map.put(this.dataKey, this.dataValue);
        return map;
    }



    public static void main(String[] args) {
        Responses r = new Responses("status", "1", "msg", "操作成功");
        System.out.println("--->>" + r.success());
        Responses r2 = new Responses("status", "1", "msg", "操作成功","datas","object");
        System.out.println("--->>" + r2.successData());

    }
}
