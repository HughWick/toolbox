package com.github.hugh.http;

import com.github.hugh.http.builder.OkHttps;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

class DnsLatencyTest {

    // 目标地址 (高德API)
    private static final String TARGET_URL = "https://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=你的高德Key";
    // 注意：如果没有 Key，直接访问 https://restapi.amap.com 也可以，虽然会返错误码，但足以测试网络耗时

    @Test
    void testDnsAndNetworkLatency() throws IOException {
        System.out.println("====== 开始网络层耗时诊断 ======");

        // 1. 定义一个 EventListener 来监听 OkHttp 的底层事件
        EventListener listener = new EventListener() {
            long start, dnsStart, connectStart, secureConnectStart;

            @Override
            public void callStart(@NotNull Call call) {
                start = System.nanoTime();
                System.out.println("1. 请求开始...");
            }

            @Override
            public void dnsStart(@NotNull Call call, @NotNull String domainName) {
                dnsStart = System.nanoTime();
                System.out.println("2. 开始 DNS 解析: " + domainName);
            }

            @Override
            public void dnsEnd(@NotNull Call call, @NotNull String domainName, @NotNull List<InetAddress> inetAddressList) {
                long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - dnsStart);
                System.out.println("   [DNS 耗时] -> " + duration + " ms | 解析结果: " + inetAddressList);
            }
            @Override
            public void connectStart(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy) {
                connectStart = System.nanoTime();
                System.out.println("3. 开始建立 TCP 连接...");
            }

            @Override
            public void secureConnectStart(@NotNull Call call) {
                secureConnectStart = System.nanoTime();
                System.out.println("4. 开始 SSL/TLS 握手...");
            }

            @Override
            public void secureConnectEnd(@NotNull Call call, Handshake handshake) {
                long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - secureConnectStart);
                System.out.println("   [SSL 握手耗时] -> " + duration + " ms");
            }
            @Override
            public void connectEnd(@NotNull Call call, @NotNull InetSocketAddress inetSocketAddress, @NotNull Proxy proxy, Protocol protocol) {
                long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - connectStart);
                System.out.println("   [TCP 连接总耗时 (含SSL)] -> " + duration + " ms");
            }

            @Override
            public void callEnd(@NotNull Call call) {
                long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                System.out.println("====== 请求结束，总耗时: " + duration + " ms ======\n");
            }
        };

        // 2. 构建带有监听器的 OkHttpClient
        // 关键点：我们在这里创建一个自定义的 Client，注入到你的 OkHttps 工具类中
        OkHttpClient monitorClient = new OkHttpClient.Builder()
                .eventListener(listener) // 挂载监听器
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        // 3. 第一次请求 (Cold Request)
        // 这次会触发 DNS 解析、TCP 连接、SSL 握手
        System.out.println(">>> 发起第 1 次请求 (冷启动，预计最慢) <<<");
        try {
            // 使用你的工具类，通过 setOkHttpClient 注入
            OkHttps.url("https://restapi.amap.com") // 使用根路径测试即可，无需真实参数
                    .setOkHttpClient(monitorClient)
                    .doGet();
        } catch (Exception e) {
            // 忽略业务报错，只关注网络日志
        }

        // 4. 第二次请求 (Warm Request)
        // 这次应该复用连接，DNS、TCP、SSL 耗时应该都为 0 或极短
        System.out.println(">>> 发起第 2 次请求 (热启动，利用连接池) <<<");
        try {
            OkHttps.url("https://restapi.amap.com")
                    .setOkHttpClient(monitorClient)
                    .doGet();
        } catch (Exception e) {
        }
    }
}