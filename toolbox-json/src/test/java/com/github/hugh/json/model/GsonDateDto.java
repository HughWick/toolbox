package com.github.hugh.json.model;

import lombok.Data;

import java.util.Date;

/**
 * 测试gson转日期对象类
 * User: AS
 * Date: 2022/10/31 13:59
 */
@Data
public class GsonDateDto {

    private long id;
    private String serialNo;
    private int age;
    private String name;
    private double amount;
    private Date birthday;
    private Date createDate;
    private long systemDate;
    private String sex;
}
