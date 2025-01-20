package com.github.hugh.util.ip;

import com.github.hugh.bean.dto.Ip2regionDTO;
import com.github.hugh.components.IpResolver;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.io.StreamUtils;
import com.google.common.base.Suppliers;
import org.lionsoul.ip2region.xdb.Searcher;

import java.util.function.Supplier;

/**
 * 基于 ip2region IP查询国家省市工具
 * <p><a href="https://github.com/lionsoul2014/ip2region">...</a></p>
 *
 * @author hugh
 * @since 1.5.2
 */
public class Ip2regionUtils {

    private Ip2regionUtils() {
    }

    /**
     * ip数据文件目录
     */
    private static final String XDB_PATH = "/ip2region/ip2region.xdb";

    /**
     * 缓存数据
     */
    private static Supplier<byte[]> defaultXdbDataSupplier = null;

    /**
     * 使用本地缓存XDB数据资源
     *
     * @return byte[]
     * @since 2.3.5
     */
    private static synchronized byte[] getData() {
        if (defaultXdbDataSupplier == null) {
            Supplier<byte[]> easyRedisSupplier = () -> StreamUtils.resourceToByteArray(XDB_PATH);
            defaultXdbDataSupplier = Suppliers.memoize(easyRedisSupplier::get);
        }
        return defaultXdbDataSupplier.get();
    }

    /**
     * 根据IP地址解析省市区信息
     *
     * @param ip IP地址
     * @return String 返回字符串格式：国家|大区|省份|城市|运营商
     */
    public static String getCityInfo(String ip) {
        return getCityInfo(ip, getData());
    }

    /**
     * 根据IP地址解析省市区信息
     *
     * @param ip    IP地址
     * @param cBuff 从 dbPath 加载整个 xdb 到内存
     * @return String 返回字符串格式：国家|大区|省份|城市|运营商
     */
    public static String getCityInfo(String ip, byte[] cBuff) {
        try {
            // 1、从 dbPath 加载整个 xdb 到内存。
            if (cBuff == null) {
                cBuff = getData();
            }
            // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
            Searcher searcher = Searcher.newWithBuffer(cBuff);
            // 3、查询
            return searcher.search(ip);
        } catch (Exception exception) {
            throw new ToolboxException("failed to create content cached searcher:", exception);
        }
    }

    /**
     * 根据IP地址解析国家、省份、城市、运营商信息
     *
     * @param ip IP
     * @return {@link Ip2regionDTO}
     * @since 1.5.4
     */
    public static Ip2regionDTO parse(String ip) {
        return parse(ip, getData());
    }

    /**
     * 根据IP地址解析国家、省份、城市、运营商信息
     * 应直接使用{@link com.github.hugh.components.IpResolver}
     *
     * @param ip    IP
     * @param cBuff 从 dbPath 加载整个 xdb 到内存
     * @return {@link Ip2regionDTO}
     * @since 2.7.5
     */
    @Deprecated
    public static Ip2regionDTO parse(String ip, byte[] cBuff) {
        return IpResolver.on(ip, cBuff).parse();
    }
}
