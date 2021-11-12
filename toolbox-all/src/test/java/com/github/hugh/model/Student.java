package com.github.hugh.model;

import lombok.Data;

import java.util.Date;

/**
 * @author AS
 * @date 2020/8/31 9:24
 */
@Data
public class Student {
    public Student() {

    }

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    private long id;
    private int age;
    private String name;
    private double amount;
    //    private Date birthday;
    private Date create;
    //    private List list;
    private String sex;

}
