package com.github.hugh.model;

import java.util.Date;

/**
 * @author AS
 * @date 2020/8/31 9:24
 */
public class Student {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmouont() {
        return amouont;
    }

    public void setAmouont(double amouont) {
        this.amouont = amouont;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    private long id;
    private int age;
    private String name;
    private double amouont;
    private Date birthday;
}
