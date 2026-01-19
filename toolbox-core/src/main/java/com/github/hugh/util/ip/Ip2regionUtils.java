package com.github.hugh.util.ip;

import com.github.hugh.bean.dto.Ip2regionDTO;
import com.github.hugh.components.IpResolver;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.io.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.lionsoul.ip2region.xdb.Version;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * 基于 ip2region IP查询国家省市工具
 * <p><a href="https://github.com/lionsoul2014/ip2region">...</a></p>
 *
 * @author hugh
 * @since 1.5.2
 */
@Slf4j
public class Ip2regionUtils {

    private Ip2regionUtils() {
    }

    /**
     * ip数据文件目录
     */
    private static final String XDB_PATH = "/ip2region/ip2region.xdb";

    /**
     * 全局复用的 Searcher 实例 (ip2region v2 的 Searcher 在内存模式下是线程安全的)
     */
    private static Searcher searcher;

    /**
     * 使用静态代码块或静态内部类实现一次性加载
     * 这里为了防止启动时文件不存在导致整个应用崩溃，采用懒加载（第一次调用时初始化）
     */
    private static void initSearcher() {
        if (searcher != null) {
            return;
        }
        // 双重检查锁，防止并发初始化
        synchronized (IpUtils.class) {
            if (searcher != null) {
                return;
            }
            long start = System.nanoTime();
            try (InputStream inputStream = StreamUtils.getInputStream(XDB_PATH)) {
                if (inputStream == null) {
                    throw new ToolboxException("IP data file not found: " + XDB_PATH);
                }
                // 将整个 xdb 文件加载到内存 (cBuff)
                var cBuff = Searcher.loadContentFromInputStream(inputStream);
                // 使用内容 buffer 创建 Searcher
                // 这里的 Version.IPv4 取决于你引入的 jar 包版本，确保参数匹配
                searcher = Searcher.newWithBuffer(Version.IPv4, cBuff);
                log.info("Ip2region loaded successfully, cost: {} ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
            } catch (Exception e) {
                log.error("Failed to load ip2region.xdb", e);
                // 抛出异常，或者让 searcher 保持 null，在调用时处理
                throw new ToolboxException("failed to create content cached searcher: " + e.getMessage());
            }
        }
    }

    /**
     * 根据IP地址解析省市区信息
     *
     * @param ip    IP地址
     * @return String 返回字符串格式：国家|大区|省份|城市|运营商
     */
    public static String getCityInfo(String ip) {
        //  基础校验
        if (ip == null || ip.trim().isEmpty()) {
            return null; // 或者返回 "未知"
        }
        // 懒加载初始化
        if (searcher == null) {
            initSearcher();
        }
        try {
            // 执行查询 (纯内存操作，微秒级)
            // Searcher 在完全加载到内存后是线程安全的，可以直接并发调用
            return searcher.search(ip);
        } catch (Exception e) {
            log.warn("IP parse error for ip: {}, error: {}", ip, e.getMessage());
            return null; // 或者返回 "未知"
        }
    }

    /**
     * 根据IP地址解析国家、省份、城市、运营商信息
     * 应直接使用{@link com.github.hugh.components.IpResolver}
     *
     * @param ip    IP
     * @return {@link Ip2regionDTO}
     * @since 2.7.5
     */
    @Deprecated
    public static Ip2regionDTO parse(String ip) {
        return IpResolver.on(ip).parse();
    }
}
