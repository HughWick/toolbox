package com.github.hugh.model;

import lombok.Data;

import java.util.Date;

@Data
public class GsonTest {
    private String code;
    private String message;
    private int age;
    private double amount;
    private boolean switchs;
    private Date created;
    private Date updated;

}
