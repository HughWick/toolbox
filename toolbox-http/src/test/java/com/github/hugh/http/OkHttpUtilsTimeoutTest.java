package com.github.hugh.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OkHttpUtils 超时机制测试")
class OkHttpUtilsTimeoutTest {
    // 10.255.255.1 或 192.0.2.1（RFC 5737 测试保留 IP），不会响应 SYN 包，能够可靠触发连接超时
    private static final String BLACKHOLE_URL = "http://10.255.255.1:81";

    @Test
    @DisplayName("连接不可达黑洞 IP 时，默认 get 方法应在达到连接超时后抛出 SocketTimeoutException")
    void shouldThrowSocketTimeoutExceptionWhenConnectionTimesOutOnGet() {
        SocketTimeoutException exception = assertThrows(SocketTimeoutException.class, () -> {
            // 注意：若 get 方法没有超时参数，可能需要等待较长时间，建议带上超时时间参数（如 2 秒）
            OkHttpUtils.get(BLACKHOLE_URL, 2);
        });
        // 兼容不同平台系统的错误信息，只要提示中包含 timeout / timed out 即可
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().toLowerCase().contains("time"),
                "Exception message should indicate a timeout");
    }

    @Test
    @DisplayName("连接不可达黑洞 IP 时，getReJsonObjects 应抛出 SocketTimeoutException")
    void shouldThrowSocketTimeoutExceptionWhenConnectionTimesOutOnGetReJsonObjects() {
        SocketTimeoutException exception = assertThrows(SocketTimeoutException.class, () -> {
            // 超时参数设置为 2 秒
            OkHttpUtils.getReJsonObjects(BLACKHOLE_URL, null, 2);
        });

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().toLowerCase().contains("time"));
    }
}
