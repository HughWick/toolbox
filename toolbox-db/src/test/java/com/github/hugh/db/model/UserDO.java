package com.github.hugh.db.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserDO {
    private long id;
    private Date gmtModified;
    private Date gmtCreate;
    private String realName;
    private String userName;
}
