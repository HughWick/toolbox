package com.github.hugh.components;

import com.github.hugh.bean.dto.Ip2regionDTO;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.ip.Ip2regionUtils;

/**
 * 提供 IP 地址解析功能的工具类。
 *
 * @since 2.7.5
 */
public class IpResolver {

    private String ip; // 要解析的 IP 地址
    private byte[] ipData; // IP 数据源
    private String spare; // 分隔符

    private static final String DEFAULT_NULL_STR = "0";
    private static final String DEFAULT_SPARE_CHAR = "\\|";

    /**
     * 构造函数，初始化要解析的 IP 地址。
     *
     * @param ip 要解析的 IP 地址
     */
    public IpResolver(String ip) {
        this.ip = ip;
    }

    /**
     * 构造函数，初始化要解析的 IP 地址和 IP 数据源。
     *
     * @param ip    要解析的 IP 地址
     * @param bytes IP 数据源
     */
    public IpResolver(String ip, byte[] bytes) {
        this.ip = ip;
        this.ipData = bytes;
    }

    /**
     * 静态工厂方法，创建一个新的 IpResolver 对象并初始化要解析的 IP 地址。
     *
     * @param ip 要解析的 IP 地址
     * @return 初始化了要解析的 IP 地址的 IpResolver 对象
     */
    public static IpResolver on(String ip) {
        return new IpResolver(ip);
    }

    /**
     * 静态工厂方法，创建一个新的 IpResolver 对象并初始化要解析的 IP 地址和 IP 数据源。
     *
     * @param ip    要解析的 IP 地址
     * @param bytes IP 数据源
     * @return 初始化了要解析的 IP 地址和 IP 数据源的 IpResolver 对象
     */
    public static IpResolver on(String ip, byte[] bytes) {
        return new IpResolver(ip, bytes);
    }

    /**
     * 设置分隔符。
     *
     * @param spare 分隔符
     * @return 当前 IpResolver 对象，用于链式调用
     */
    public IpResolver setSpare(String spare) {
        this.spare = spare;
        return this;
    }
    /**
     * 根据 IP 地址和数据源获取完整的地理位置信息。
     *
     * @return IP 地址对应的完整地理位置信息，如果解析失败返回 null
     */
    public String getComplete() {
        Ip2regionDTO parse = parse();
        if (parse == null) {
            throw new ToolboxException("解析失败，IP：" + this.ip);
        }
        if (DEFAULT_NULL_STR.equals(parse.getProvince())) {
            if (DEFAULT_NULL_STR.equals(parse.getCity())) {
                return null;
            }
            return parse.getCity();
        }
        String spareStr = this.spare == null ? "" : this.spare;
        return parse.getProvince() + spareStr + parse.getCity();
    }

    /**
     * 根据 IP 地址和数据源获取简化的地理位置信息（仅包含城市信息）。
     *
     * @return IP 地址对应的城市信息，如果解析失败返回 null
     * @since 2.7.5
     */
    public String getSimple() {
        Ip2regionDTO parse = parse();
        if (parse == null) {
            throw new ToolboxException("解析失败，IP：" + ip);
        }
        if (DEFAULT_NULL_STR.equals(parse.getCity())) {
            return null;
        }
        return parse.getCity();
    }

    /**
     * 解析 IP 地址的地理位置信息。
     *
     * @return 包含 IP 地址对应地理位置信息的 Ip2regionDTO 对象，
     * 如果解析失败返回 null
     */
    public Ip2regionDTO parse() {
        // 调用 Ip2regionUtils 工具类的方法获取城市信息
        String str = Ip2regionUtils.getCityInfo(this.ip, this.ipData);
        // 如果城市信息为空，解析失败，返回 null
        if (str == null) {
            return null;
        }
        // 使用默认分隔符分割城市信息字符串
        String[] arr = str.split(DEFAULT_SPARE_CHAR);
        // 创建 Ip2regionDTO 对象并设置各个属性值
        Ip2regionDTO ip2regionDTO = new Ip2regionDTO();
        ip2regionDTO.setCountry(arr[0]); // 设置国家
        ip2regionDTO.setRegion(arr[1]); // 设置地区
        ip2regionDTO.setProvince(arr[2]); // 设置省份
        ip2regionDTO.setCity(arr[3]); // 设置城市
        ip2regionDTO.setIsp(arr[4]); // 设置运营商
        // 返回包含地理位置信息的 Ip2regionDTO 对象
        return ip2regionDTO;
    }
}
