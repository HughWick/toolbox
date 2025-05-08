package com.github.hugh.util.net;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * <p>URL 工具类。</p>
 *
 * <p>提供处理 URL 相关操作的静态工具方法。</p>
 * <p>此类不应被实例化。</p>
 *
 * @since 3.0.1
 */
public class UrlUtils {

    private UrlUtils() {
    }

    // 定义默认的超时时间常量
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000; // 默认连接超时 5 秒
    private static final int DEFAULT_READ_TIMEOUT = 10000;  // 默认读取超时 10 秒

    /**
     * <p>建立与指定URL的连接并设置连接和读取超时。</p>
     *
     * <p>此方法负责校验URL字符串、创建 {@link java.net.URL} 对象、打开 {@link java.net.URLConnection}，
     * 并设置连接超时 (5秒) 和读取超时 (10秒)。</p>
     *
     * @param urlString 要连接的URL字符串，不能为 null 或空。
     * @return 配置好的 {@link java.net.URLConnection} 对象。
     * @throws IOException              如果发生网络或I/O错误，例如URL格式不正确、打开连接失败等。
     * @throws IllegalArgumentException 如果 {@code urlString} 参数为 null 或空字符串。
     */
    public static URLConnection openConnection(String urlString) throws IOException, IllegalArgumentException {
        if (urlString == null || urlString.trim().isEmpty()) {
            throw new IllegalArgumentException("URL string cannot be null or empty");
        }
        URL url = new URL(urlString);
        return openConnection(url);
    }

    /**
     * <p>建立与指定 URL 的连接，并使用默认的连接和读取超时设置。</p>
     *
     * <p>此方法调用 {@link #openConnection(URL, int, int)} 方法，
     * 传递默认的连接超时 ({value #DEFAULT_CONNECT_TIMEOUT}) 和读取超时 ({value #DEFAULT_READ_TIMEOUT})。</p>
     *
     * @param url 要连接的 {@link java.net.URL} 对象。
     * @return 配置好默认超时设置的 {@link java.net.URLConnection} 对象。
     * @throws IOException              如果发生网络或 I/O 错误，例如打开连接失败。
     * @throws IllegalArgumentException 如果传入的 URL 无效 (尽管理论上如果 URL 对象已创建则应有效，但保留异常说明)。
     */
    public static URLConnection openConnection(URL url) throws IOException, IllegalArgumentException {
        return openConnection(url, DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    /**
     * <p>建立与指定 URL 的连接，并设置指定的连接和读取超时。</p>
     *
     * <p>此方法打开 {@link java.net.URLConnection}，并设置由 {@code connectTimeout} 和 {@code readTimeout} 参数指定的超时时间。</p>
     *
     * @param url            要连接的 {@link java.net.URL} 对象。
     * @param connectTimeout 连接超时时间，单位毫秒。值为 0 表示无限超时，负值无效。
     * @param readTimeout    读取超时时间，单位毫秒。值为 0 表示无限超时，负值无效。
     * @return 配置好指定超时设置的 {@link java.net.URLConnection} 对象。
     * @throws IOException              如果发生网络或 I/O 错误，例如打开连接失败。
     * @throws IllegalArgumentException 如果超时时间参数为负值。
     */
    public static URLConnection openConnection(URL url, int connectTimeout, int readTimeout) throws IOException, IllegalArgumentException {
        URLConnection connection = url.openConnection();
        // 设置连接超时
        connection.setConnectTimeout(connectTimeout);
        // 设置读取超时
        connection.setReadTimeout(readTimeout);
        return connection;
    }

    /**
     * <p>建立与指定URL的连接并获取其输入流。</p>
     *
     * <p>此方法内部调用 {@link #openConnection(String)} 建立连接，
     * 然后从连接中获取 {@link java.io.InputStream}。</p>
     *
     * @param urlString 要连接的URL字符串，不能为 null 或空。
     * @return 连接成功后获取的 {@link java.io.InputStream}。
     * @throws IOException              如果发生网络或I/O错误，例如URL格式不正确、连接超时、读取超时、获取输入流失败等。
     * @throws IllegalArgumentException 如果 {@code urlString} 参数为 null 或空字符串。
     */
    public static InputStream getUrlInputStream(String urlString) throws IOException, IllegalArgumentException {
        // 调用公共方法建立连接
        URLConnection connection = openConnection(urlString);
        // 从连接中获取输入流。getInputStream() 可能会抛出 IOException。
        return connection.getInputStream();
    }

    /**
     * <p>根据URL链接判断对应网络资源是否存在且可访问。</p>
     *
     * <p>此方法内部调用 {@link #openConnection(String)} 建立连接。</p>
     * <p>对于 HTTP/HTTPS 链接，会检查响应状态码。如果状态码在 200-299 范围内，则认为资源存在且可访问。</p>
     * <p>对于非 HTTP/HTTPS 链接 (如 file://, ftp://)，会尝试获取输入流，如果成功则认为资源存在。</p>
     * <p>此方法会尽量避免下载完整的资源内容以提高效率和减少资源消耗。</p>
     *
     * @param urlStr 网址链接字符串。
     * @return boolean {@code true} 如果资源存在且可访问，{@code false} 如果链接无效、
     * 网络错误、资源不存在 (如 404) 或其他访问问题。
     */
    public static boolean resourceExists(String urlStr) {
        // 校验和建立连接的部分被提取到 openConnectionWithTimeout
        URLConnection connection; // 声明以便在 catch 中可以引用
        try {
            connection = openConnection(urlStr); // 调用公共方法获取连接
            // --- 针对 HTTP/HTTPS 的优化：检查状态码 ---
            if (connection instanceof HttpURLConnection httpConnection) {
                // 明确使用 GET 方法 (通常也适用于 HEAD)
                httpConnection.setRequestMethod("GET");
                // 获取响应状态码。
                int responseCode = httpConnection.getResponseCode();
                // 检查状态码是否表示成功 (2xx 系列)
                boolean existsAndAccessible = (responseCode >= 200 && responseCode < 300);
                // 释放资源
                httpConnection.disconnect();
                return existsAndAccessible;
            } else {
                // --- 处理其他 URL 协议 ---
                // 使用 try-with-resources 确保输入流被关闭。
                try (InputStream ignored = connection.getInputStream()) {
                    // 如果成功获取到流，说明资源存在
                    return true;
                }
            }
        } catch (MalformedURLException malformedURLException) {
            // URL 格式不正确
            return false;
        } catch (IllegalArgumentException illegalArgumentException) {
            // 来自 openConnectionWithTimeout 的校验错误 (null 或空字符串)
            return false;
        } catch (IOException ioException) {
            // 捕获所有其他 IO 异常
            return false;
        }
    }

    /**
     * <p>根据URL链接判断对应网络资源是否<b>不存在</b>或不可访问。</p>
     *
     * <p>此方法是 {@link #resourceExists(String)} 方法的逻辑非操作。</p>
     * <p>它会调用 {@link #resourceExists(String)} 方法，如果该方法返回 {@code false}
     * (表示资源不存在、URL无效、网络错误或其他访问问题)，则此方法返回 {@code true}；
     * 如果 {@link #resourceExists(String)} 返回 {@code true} (表示资源存在且可访问)，
     * 则此方法返回 {@code false}。</p>
     *
     * @param urlStr 网址链接字符串。
     * @return boolean {@code true} 如果资源不存在或不可访问，{@code false} 如果资源存在且可访问。
     * 如果传入 null 或空字符串，将直接返回 {@code true} (因为资源不存在于有效地址)。
     */
    public static boolean resourceNotExists(String urlStr) {
        return !resourceExists(urlStr);
    }

    /**
     * 从指定的URL读取所有文本内容
     *
     * @param urlString 要读取内容的URL字符串
     * @return URL的所有文本内容字符串，如果读取失败则返回null或抛出异常
     * @throws IOException              如果发生I/O错误或URL格式不正确
     * @throws IllegalArgumentException 如果URL字符串为空或无效
     */
    public static String readContent(String urlString) throws IOException, IllegalArgumentException {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = getUrlInputStream(urlString);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            // 逐行读取文件内容
            while ((line = reader.readLine()) != null) {
                content.append(line);
                // 这里之前是 System.lineSeparator()，如果目的是保留原始网页的换行
                // 通常网页是 LF (\n)，或者根据 HTTP 响应头来判断更准确
                // 为了简单且常用，继续使用 System.lineSeparator() 或只用 \n 都可以
                content.append(System.lineSeparator()); // 添加换行符
            }
        }
        // 移除末尾可能的换行符，以避免多余空行
        if (!content.isEmpty() && (content.charAt(content.length() - 1) == '\n' || content.charAt(content.length() - 1) == '\r')) {
            content.delete(content.length() - System.lineSeparator().length(), content.length());
        }
        return content.toString();
    }

    /**
     * 从指定的URL读取所有内容作为字节数组
     *
     * @param urlString 要读取内容的URL字符串
     * @return URL的所有内容字节数组，如果读取失败则返回null或抛出异常
     * @throws IOException              如果发生I/O错误或URL格式不正确
     * @throws IllegalArgumentException 如果URL字符串为空或无效
     */
    public static byte[] readContentAsBytes(String urlString) throws IOException, IllegalArgumentException {
        try (InputStream inputStream = getUrlInputStream(urlString);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // 读取字节并写入 ByteArrayOutputStream
            byte[] buffer = new byte[4096]; // 使用一个缓冲区
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            // ByteArrayOutputStream 不需要手动关闭，其 close 方法无效
            // 将 ByteArrayOutputStream 的内容转换为字节数组
            return byteArrayOutputStream.toByteArray();
        }
    }
}
