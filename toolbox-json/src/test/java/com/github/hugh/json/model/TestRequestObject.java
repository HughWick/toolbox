package com.github.hugh.json.model;

import lombok.Data;
// request 请求转换对象测试类
@Data
public class TestRequestObject {
    public long birthday;
    public String name;//
    public String userId;//
    public Sub content;

    @Data
    static class Sub {
        private String hostSerialNumber;
    }

}
