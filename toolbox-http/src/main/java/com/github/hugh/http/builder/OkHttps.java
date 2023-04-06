package com.github.hugh.http.builder;

import com.github.hugh.constant.StrPool;
import com.github.hugh.http.UrlUtils;
import com.github.hugh.http.constant.MediaTypes;
import com.github.hugh.http.exception.ToolboxHttpException;
import com.github.hugh.http.model.FileFrom;
import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.Jsons;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.ListUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 封装OkHttp 工具类
 *
 * @author hugh
 * @since 2.5.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OkHttps {
    /**
     * 默认超时时间，单位：秒
     */
    private static final int timeout = 10;
    private String url;// 接口地址
    /**
     * HTTP 请求的请求体信息。
     */
    private Object body;
    private long time; // 请求响应时间
    /**
     * HTTP 请求的请求头信息。
     */
    private Map<String, String> header;
    private int connectTimeout; // 连接超时时间，单位：秒
    private int readTimeout; // 读取超时时间，单位：秒
    private int writeTimeout; // 写入超时时间，单位：秒
    private boolean isSendCookies = false; // 是否发送 Cookie 信息
    /**
     * 用于存储多个文件来源信息的列表，其中每个 FileFrom 对象包括文件的路径、大小、类型等属性。
     */
    private List<FileFrom> fileFrom;

    /**
     * 用于进行 HTTP/HTTPS 请求的客户端对象。该对象封装了连接池、超时设置、请求拦截器、响应拦截器等功能，可以根据实际情况进行配置。
     */
    private OkHttpClient okHttpClient;

    /**
     * 用于管理 TCP 连接的连接池对象，用于复用并限制 TCP 连接数量。通过使用连接池，可以在多个请求之间共享连接，避免建立和关闭连接的开销。ConnectionPool 对象包括最大连接数、保持时间等属性，可以根据实际情况进行配置。
     */
    private ConnectionPool connectionPool;

    /**
     * 设置请求的 URL，用于发起 HTTPS 请求。
     *
     * @param url 请求的目标 URL 地址。
     * @return 返回 OkHttps 实例，以支持链式调用。
     */
    public OkHttps setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 设置 HTTP 请求的请求体信息。
     *
     * @param body HTTP 请求的请求体信息。
     * @return 返回当前 OkHttps 实例，以便支持链式调用。
     */
    public OkHttps setBody(Object body) {
        this.body = body;
        return this;
    }

    /**
     * 设置 HTTP 请求的请求头信息。
     *
     * @param header HTTP 请求的请求头信息。
     * @return 返回当前 OkHttps 实例，以便支持链式调用。
     */
    public OkHttps setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    /**
     * 设置连接超时时间。
     *
     * @param connectTimeout 连接超时时间，单位为秒。
     * @return 返回当前对象，用于链式调用。
     */
    public OkHttps setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * 设置读取超时时间。
     *
     * @param readTimeout 读取超时时间，单位为秒。
     * @return 返回当前对象，用于链式调用。
     */
    public OkHttps setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * 设置写入超时时间。
     *
     * @param writeTimeout 写入超时时间，单位为秒。
     * @return 返回当前对象，用于链式调用。
     */
    public OkHttps setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    /**
     * 设置是否发送 cookie 的方法。
     *
     * @param flag 是否发送 cookie 的标志，true 表示发送，false 表示不发送。
     * @return 返回一个 OkHttps 实例以支持方法链接调用。
     */
    public OkHttps isSendCookie(boolean flag) {
        this.isSendCookies = flag;
        return this;
    }

    /**
     * 设置文件来源，使用单个 FileFrom 对象
     *
     * @param fileFrom 文件来源
     * @return 当前 OkHttps 实例
     */
    public OkHttps setFileFrom(FileFrom fileFrom) {
        this.fileFrom = Collections.singletonList(fileFrom);
        return this;
    }

    /**
     * 设置文件来源，使用一个包含多个 FileFrom 对象的列表
     *
     * @param fileNameList 文件来源列表
     * @return 当前 OkHttps 实例
     */
    public OkHttps setFileFrom(List<FileFrom> fileNameList) {
        this.fileFrom = fileNameList;
        return this;
    }

    /**
     * 设置 OkHttpClient 对象。
     *
     * @param okHttpClient OkHttpClient 对象
     * @return 返回当前对象，便于链式调用
     */
    public OkHttps setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    /**
     * 设置连接池对象。
     *
     * @param connectionPool 连接池对象
     * @return 当前 OkHttps 对象
     */
    public OkHttps setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        return this;
    }

    /**
     * 发送 GET 请求，并获取 HTTP 响应消息。
     *
     * @return 返回 OkHttps 实例，以支持链式调用其他方法。
     * @throws IOException 如果发送请求失败，则抛出异常。
     */
    public OkHttpsResponse doGet() throws IOException {
        // 确保URL不为null或空
        verifyUrlEmpty();
        // 如果提供了查询参数，则将其添加到URL中
        url = UrlUtils.urlParam(url, this.body);
        newOkhttpClient();
        // 构建请求对象
        final Request.Builder request = new Request.Builder().url(url);
        if (this.header != null) {
            // 如果提供了请求头，将其添加到请求中
            Headers headers = Headers.of(this.header);
            request.headers(headers);
        }
        String result;
        if (this.isSendCookies) {
            result = send(request.build(), HttpClient.cookieClient);
        } else {
            result = send(request.build(), okHttpClient);
        }
        // 发送请求并返回响应
        return new OkHttpsResponse(result);
    }

    /**
     * 创建 OkHttpClient 对象，并在 okHttpClient 为 null 时使用默认的 OkHttpClient。
     * 注意：该方法会直接覆盖属性 okHttpClient 的值。
     */
    private void newOkhttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            if (connectTimeout > 0) {
                okHttpClientBuilder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
            } else {
                okHttpClientBuilder.connectTimeout(timeout, TimeUnit.SECONDS);
            }
            if (readTimeout > 0) {
                okHttpClientBuilder.readTimeout(readTimeout, TimeUnit.SECONDS);
            } else {
                okHttpClientBuilder.readTimeout(timeout, TimeUnit.SECONDS);
            }
            if (writeTimeout > 0) {
                okHttpClientBuilder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
            } else {
                okHttpClientBuilder.writeTimeout(timeout, TimeUnit.SECONDS);
            }
            if (connectionPool != null) {
                okHttpClientBuilder.connectionPool(connectionPool);
            }
            okHttpClient = okHttpClientBuilder.build();
        }
    }

    /**
     * 执行带有表单数据的 POST 请求。
     *
     * @return 服务器返回的响应结果
     * @throws IOException 如果发生 I/O 错误
     */
    public OkHttpsResponse doPostForm() throws IOException {
        verifyUrlEmpty();
        final String params = UrlUtils.jsonParse(this.body);
        if (this.isSendCookies) {
            return doPost(MediaTypes.APPLICATION_FORM_URLENCODED, params, HttpClient.cookieClient);
        } else {
            newOkhttpClient();
            return doPost(MediaTypes.APPLICATION_FORM_URLENCODED, params, okHttpClient);
        }
    }

    /**
     * 执行带有 JSON 数据的 POST 请求。
     *
     * @return 服务器返回的响应结果
     * @throws IOException 如果发生 I/O 错误
     */
    public OkHttpsResponse doPostJson() throws IOException {
        verifyUrlEmpty();
        // 创建OkHttpClient实例并设置超时值
        if (this.isSendCookies) {
            okHttpClient = HttpClient.cookieClient;
        } else {
            newOkhttpClient();
        }
        if (this.body == null) {
            return doPost(MediaTypes.APPLICATION_JSON_UTF8, StrPool.EMPTY, okHttpClient);
        }
        if (this.body instanceof String) {
            return doPost(MediaTypes.APPLICATION_JSON_UTF8, String.valueOf(this.body), okHttpClient);
        }
        return doPost(MediaTypes.APPLICATION_JSON_UTF8, GsonUtils.toJson(this.body), okHttpClient);
    }

    /**
     * 发送 HTTP POST 请求的方法。
     *
     * @param mediaType    发送请求主体内容的媒体类型。
     * @param body         发送请求的主体内容。
     * @param okHttpClient 用于发送请求的 OkHttpClient 实例。
     * @return 返回一个 OkHttpsResponse 对象，包含服务器返回的响应信息。
     * @throws IOException 如果请求发送失败，则抛出 IOException 异常。
     */
    private OkHttpsResponse doPost(MediaType mediaType, String body, OkHttpClient okHttpClient) throws IOException {
        RequestBody requestBody = RequestBody.create(mediaType, body);
        final Request.Builder request = new Request.Builder().url(url).post(requestBody);
        if (this.header != null) {
            Headers headers = Headers.of(this.header);
            request.headers(headers);
        }
        return new OkHttpsResponse(send(request.build(), okHttpClient));
    }

    /**
     * 上传文件的方法
     *
     * @return OkHttpsResponse
     * @throws IOException
     */
    public <K, V> OkHttpsResponse uploadFile() throws IOException {
        MultipartBody.Builder requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        // 如果请求体不为空，则将其转换为 Map 对象，并遍历 Map 中的每一项，添加到请求体中
        if (EmptyUtils.isNotEmpty(this.body)) {
            final Map<K, V> params = new Jsons(this.body).toMap();
            for (Map.Entry<K, V> entry : params.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        if (ListUtils.isEmpty(this.fileFrom)) {
            throw new ToolboxHttpException("file is null");
        }
        // 遍历文件列表，将每个文件添加到请求体中
        for (FileFrom file : this.fileFrom) {
            if (EmptyUtils.isEmpty(file.getKey())) {
                throw new ToolboxHttpException("upload file key is null");
            }
            RequestBody fileBody = RequestBody.create(file.getFileMediaType(), new File(file.getPath()));
            requestBody.addFormDataPart(file.getKey(), file.getName(), fileBody);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build();
        if (this.isSendCookies) {
            okHttpClient = HttpClient.cookieClient;
        } else {
            newOkhttpClient();
        }
        final String result = send(request, okHttpClient);
        return new OkHttpsResponse(result);
    }

    /**
     * 检查URL是否为空，如果为空，则抛出异常。
     *
     * @throws ToolboxHttpException 如果URL为空，则抛出ToolboxHttpException异常。
     */
    private void verifyUrlEmpty() {
        if (EmptyUtils.isEmpty(this.url)) {// 检查 URL 是否为空
            throw new ToolboxHttpException("url is null");
        }
    }

    /**
     * 统一发送请求
     *
     * @param request      请求内容
     * @param okHttpClient OkHttpClient
     * @return String 返回结果
     * @throws IOException IO异常
     */
    private static String send(Request request, OkHttpClient okHttpClient) throws IOException {
        try (Response response = okHttpClient.newCall(request).execute()) {
            ResponseBody body1 = response.body();
            if (body1 == null) {
                throw new ToolboxHttpException("result params is null ");
            }
            return body1.string();
        }
    }
}
