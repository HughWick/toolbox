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
     * 新版(v4) ip数据文件目录
     */
    private static final String XDB_V4_PATH = "/ip2region/ip2region_v4.xdb";


    /**
     * 全局复用的 Searcher 实例
     */
    private static Searcher searcher;
    private static Searcher searcherV4;

    /**
     * 独立的锁对象，防止旧版和新版初始化时互相阻塞
     */
    private static final Object LOCK_OLD = new Object();
    private static final Object LOCK_V4 = new Object();

    /**
     * 懒加载初始化 旧版 Searcher
     */
    private static void initSearcher() {
        if (searcher != null) {
            return;
        }
        synchronized (LOCK_OLD) {
            if (searcher == null) {
                searcher = buildSearcher(XDB_PATH);
            }
        }
    }

    /**
     * 懒加载初始化 新版(v4) Searcher
     */
    private static void initSearcherV4() {
        if (searcherV4 != null) {
            return;
        }
        synchronized (LOCK_V4) {
            if (searcherV4 == null) {
                searcherV4 = buildSearcher(XDB_V4_PATH);
            }
        }
    }

    /**
     * 提取出的公共 Searcher 构建逻辑
     *
     * @param path xdb文件路径
     * @return Searcher 实例
     */
    private static Searcher buildSearcher(String path) {
        long start = System.nanoTime();
        try (InputStream inputStream = StreamUtils.getInputStream(path)) {
            if (inputStream == null) {
                throw new ToolboxException("IP data file not found: " + path);
            }
            // 将整个 xdb 文件加载到内存 (cBuff)
            var cBuff = Searcher.loadContentFromInputStream(inputStream);
            // 创建 Searcher (保留你原有的 Version.IPv4 传参方式)
            Searcher newSearcher = Searcher.newWithBuffer(Version.IPv4, cBuff);
            log.debug("Ip2region [{}] loaded successfully, cost: {} ms",
                    path, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
            return newSearcher;
        } catch (Exception e) {
            log.error("Failed to load ip2region file: {}", path, e);
            throw new ToolboxException("failed to create content cached searcher for " + path + ": " + e.getMessage());
        }
    }

    /**
     * 【兼容旧版】根据IP地址解析省市区信息
     *
     * @param ip IP地址
     * @return String 返回旧版字符串格式：国家|大区|省份|城市|运营商
     */
    public static String getCityInfo(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return null;
        }
        if (searcher == null) {
            initSearcher();
        }
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            log.warn("IP parse error for ip: {} using OLD xdb, error: {}", ip, e.getMessage());
            return null;
        }
    }

    /**
     * 根据IP地址解析省市区信息 (v4版本)
     *
     * @param ip IP地址
     * @return String 返回 v4 版的字符串格式
     */
    public static String getCityInfoV4(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return null;
        }
        if (searcherV4 == null) {
            initSearcherV4();
        }
        try {
            return searcherV4.search(ip);
        } catch (Exception e) {
            log.warn("IP parse error for ip: {} using V4 xdb, error: {}", ip, e.getMessage());
            return null;
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
