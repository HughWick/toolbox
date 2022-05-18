package com.github.hugh.json.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Hugh
 * @sine
 **/
@Data
public class Person {
    @JSONField(ordinal = 1, defaultValue = "0909")
    private long id;
    @JSONField(ordinal = 2, defaultValue = "188")
    private int age;
    @JSONField(ordinal = 3, defaultValue = "王五")
    private String name;
    @JSONField(ordinal = 4, defaultValue = "男")
    private String sex;
    @JSONField(ordinal = 5, defaultValue = "555")
    private String page;
}
