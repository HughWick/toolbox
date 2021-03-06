package com.github.hugh.util.ip;

import com.github.hugh.model.dto.Ip2regionDTO;
import com.github.hugh.util.gson.JsonObjectUtils;
import com.github.hugh.util.io.StreamUtils;
import com.google.gson.JsonObject;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;

/**
 * 基于 ip2region IP查询国家省市工具
 * <p>https://github.com/lionsoul2014/ip2region</p>
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
    private static final String DB_PATH = "/ip2region/ip2region.db";

    /**
     * 根据IP地址解析省市区信息
     *
     * @param ip IP地址
     * @return String 返回字符串格式：国家|大区|省份|城市|运营商
     */
    private static String getCityInfo(String ip) {
        try {
            DbConfig config = new DbConfig();
            byte[] bytes = StreamUtils.resourceToByteArray(DB_PATH);
            DbSearcher searcher = new DbSearcher(config, bytes);
            DataBlock dataBlock = searcher.memorySearch(ip);
            return dataBlock.getRegion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据IP地址获取 国家、省份、城市、运营商信息
     * <p>例：{"country":"中国","region":"0","province":"广东省","city":"广州市","isp":"移动"}</p>
     *
     * @param ip IP
     * @return {@link JsonObject}
     */
    public static JsonObject get(String ip) {
        String str = getCityInfo(ip);
        if (str == null) {
            return null;
        }
        String[] arr = str.split("\\|");
        JsonObject item = new JsonObject();
        item.addProperty("country", arr[0]);// 国家
        item.addProperty("region", arr[1]);// 大区：华中、华东、华北、华南、华西
        item.addProperty("province", arr[2]);// 省份
        item.addProperty("city", arr[3]);// 城市
        item.addProperty("isp", arr[4]);// 运营商
        return item;
    }

    /**
     * 根据IP地址解析国家、省份、城市、运营商信息
     *
     * @param ip IP
     * @return {@link Ip2regionDTO}
     * @since 1.5.4
     */
    public static Ip2regionDTO parse(String ip) {
        JsonObject jsonObject = get(ip);
        return JsonObjectUtils.fromJson(jsonObject, Ip2regionDTO.class);
    }
}
