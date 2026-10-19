package com.github.hugh.http.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OkHttps POST 参数提交测试 - ReqRes 登录接口")
class ReqResLoginTest {

    private static final String HTTPS_REQRES_URL = "https://reqres.in";

    @Test
    @DisplayName("登录成功：提供正确的账号和密码时，应成功返回 token")
    void shouldReturnTokenWhenLoginSuccess() throws IOException {
        String loginUrl = HTTPS_REQRES_URL + "/api/login";

        OkHttpsResponse response = OkHttps
                .url(loginUrl)
                .setParam("email", "eve.holt@reqres.in")
                .setParam("password", "cityslicka")
                .doPostJson();

        // 1. 验证响应不为空
        assertNotNull(response);

        // 2. 验证业务 token 字段存在且非空
        String token = response.toJsons().getString("token");
        assertNotNull(token, "登录成功时应当返回 token");
        assertFalse(token.trim().isEmpty(), "Token 内容不应为空");

        // 3. （可选）如果封装支持获取状态码，建议断言 200
        // assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("登录失败：缺少密码时，应返回对应的错误信息 Missing password")
    void shouldReturnErrorWhenPasswordIsMissing() throws IOException {
        String loginUrl = HTTPS_REQRES_URL + "/api/login";

        // 故意只传 email，不传 password 触发报错
        OkHttpsResponse response = OkHttps
                .url(loginUrl)
                .setParam("email", "eve.holt@reqres.in")
                .doPostJson();

        assertNotNull(response);

        // ReqRes 缺少密码时的标准响应是 {"error": "Missing password"}
        assertTrue(response.toJsons().isEquals("error", "Missing password"),
                "未提供密码时，服务端应返回 Missing password");
    }
}
