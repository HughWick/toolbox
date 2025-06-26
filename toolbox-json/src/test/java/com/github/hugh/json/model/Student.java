package com.github.hugh.json.model;

import lombok.Data;

import java.util.Date;

/**
 * @author AS
 * @date 2020/8/31 9:24
 */
@Data
public class Student {

    private long id;
    private int age; // 年龄
    private String name; // 名称
    private double amount; // 金额
    private double balance;// 余额
    private Date birthday; // 生气
    private Date create; // 创建日期
    private String city;
    private String sex;
    private long system;
    private String account;
    private String accountName;
    private String accountType;
    private String password;
    private String phone;
    private String phoneType;
    private String ip;

    private String role;
    private String authorization;//权限

}
