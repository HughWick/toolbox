package com.github.hugh.db.model;

import lombok.Data;

import java.util.Date;

@Data
public class QueryVO {

    private String name;
    private int age;
    private long time;
    private double amount;
    private Date createDate;
    
}
