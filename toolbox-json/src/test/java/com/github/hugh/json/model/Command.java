package com.github.hugh.json.model;

import lombok.Data;

import java.util.Date;

/**
 * 命令测试实体
 * User: AS
 * Date: 2022/8/25 15:35
 */
@Data
public class Command {

    private String action;
    private String a0f000001;
    private String a0f000002;
    private String a0f000003;
    private Date a0f000004;
    private Date create;
    private demo a01000005;
    private demo2 testObject;

    @Data
    public static class demo {
        private String id;
    }
    @Data
    public static class demo2 {
        private String id;
    }


}