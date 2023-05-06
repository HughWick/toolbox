package com.github.hugh.http.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * json 测试接口返回参数
 *
 * @author AS
 * Date 2023/5/5 17:42
 */
@Data
public class JsonPlaceholderResult {
    private List<UserBean> list;


    @NoArgsConstructor
    @Data
    public static class UserBean {
        private int id;
        private int userId;
        private String title;
        private String body;
    }

}
