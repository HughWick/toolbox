package com.github.hugh.components;

import com.github.hugh.bean.dto.Ip2regionDTO;
import com.github.hugh.constant.StrPool;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.ip.Ip2regionUtils;
import com.github.hugh.util.regex.RegexUtils;

/**
 * 提供 IP 地址解析功能的工具类。
 *
 * @since 2.7.5
 */
public class IpResolver {

    private String ip; // 要解析的 IP 地址
    private String spare; // 分隔符
    private boolean useV4 = false; // 是否使用 V4 新版解析引擎开关
    private static final String DEFAULT_NULL_STR = "0";
    private static final String DEFAULT_SPARE_CHAR = "\\|";
    // V4 版本返回的保留/内网标识
    private static final String DEFAULT_RESERVED_STR = "Reserved";
    private static final String INTERNAL_IP = "内网IP";

    /**
     * 构造函数，初始化要解析的 IP 地址。
     *
     * @param ip 要解析的 IP 地址
     */
    public IpResolver(String ip) {
        this.ip = ip;
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
     * 开启 V4 解析模式
     * 业务端调用示例: IpResolver.on(ip).useV4().getComplete();
     * @since 3.0.28
     */
    public IpResolver useV4() {
        this.useV4 = true;
        return this;
    }

    /**
     * 判断解析出的节点是否为无效值 (兼容旧版的 0 和 新版的 Reserved)
     *
     * @param val 解析出的节点值
     * @since 3.0.28
     */
    private boolean isInvalid(String val) {
        return val == null
                || val.trim().isEmpty()
                || DEFAULT_NULL_STR.equals(val)
                || DEFAULT_RESERVED_STR.equalsIgnoreCase(val); // 忽略大小写匹配
    }

    /**
     * 根据 IP 地址和数据源获取完整的地理位置信息。
     * <p>
     * 此方法首先调用 parse() 方法解析 IP 地址，然后根据解析结果构造完整的地理位置字符串。
     * 如果解析失败，将抛出 ToolboxException 异常。如果解析结果中的省份和城市为默认空字符串，
     * 则返回 null；否则，根据解析结果返回完整的地理位置信息。
     * </p>
     *
     * @return IP 地址对应的完整地理位置信息，如果解析失败或地理位置信息不完整则返回 null
     */
    public String getComplete() {
        Ip2regionDTO parse = parse();
        if (parse == null) {
            throw new ToolboxException("解析失败，IP：" + this.ip);
        }
        String province = parse.getProvince();
        String city = parse.getCity();
        boolean isProvInvalid = isInvalid(province);
        boolean isCityInvalid = isInvalid(city);
        if (INTERNAL_IP.equals(parse.getCity())) {
            return INTERNAL_IP;
        }
        // 如果省份和城市均为无效值（例如内网IP），返回 null
        if (isProvInvalid && isCityInvalid) {
            return null;
        }
        // 如果省份无效，但城市有效，仅返回城市
        if (isProvInvalid) {
            return city;
        }
        //如果省份有效，但城市无效，仅返回省份 (避免出现 "湖南省|0" 或 "湖南省|Reserved")
        if (isCityInvalid) {
            return province;
        }
        String spareStr = this.spare == null ? StrPool.EMPTY : this.spare;
        return parse.getProvince() + spareStr + parse.getCity();
    }

    /**
     * 根据 IP 地址和数据源获取简化的地理位置信息（仅包含城市信息）。
     *
     * @return IP 地址对应的城市信息，如果解析失败返回 null
     * @since 2.7.5
     */
    public String getCity() {
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
        if (RegexUtils.isNotIp(this.ip)) {
            return null;
        }
        Ip2regionDTO ip2regionDTO = new Ip2regionDTO();
        // 【上层预判】如果是内网 IP，直接返回自定义的内网标识，不查询底层库
        if (isInternalIp(this.ip)) {
            ip2regionDTO.setProvince(INTERNAL_IP);
            ip2regionDTO.setCity(INTERNAL_IP);
            ip2regionDTO.setIsp(INTERNAL_IP);
            ip2regionDTO.setRegion(INTERNAL_IP);
            return ip2regionDTO;
        }
        // 根据标识调用对应的底层工具类
        String str = this.useV4 ? Ip2regionUtils.getCityInfoV4(this.ip) : Ip2regionUtils.getCityInfo(this.ip);
        if (str == null) {
            return null;
        }
        String[] arr = str.split(DEFAULT_SPARE_CHAR);
        if (this.useV4) {
            // --- V4 新版格式解析映射 ---
            // 格式：中国|湖南省|张家界市|电信|CN
            ip2regionDTO.setCountry(arr[0]);
            ip2regionDTO.setProvince(arr[1]);
            ip2regionDTO.setCity(arr[2]);
            ip2regionDTO.setIsp(arr[3]);
            // V4 没有"大区"(华南/华东)，可以将最后的 CN(国家码) 放入 Region，或者留空
            ip2regionDTO.setRegion(arr.length > 4 ? arr[4] : StrPool.EMPTY);
        } else {
            // --- 旧版格式解析映射 ---
            // 格式：中国|华东|浙江省|杭州市|电信
            ip2regionDTO.setCountry(arr[0]);
            ip2regionDTO.setRegion(arr[1]);
            ip2regionDTO.setProvince(arr[2]);
            ip2regionDTO.setCity(arr[3]);
            ip2regionDTO.setIsp(arr[4]);
        }
        return ip2regionDTO;
    }

    /**
     * 判断是否为内网/局域网/回环 IP
     *
     * @param ip IP 地址
     * @return true:是，false:否
     * @since 3.0.28
     */
    private boolean isInternalIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }
        if ("127.0.0.1".equals(ip) || "localhost".equalsIgnoreCase(ip)) {
            return true;
        }
        // 简单的正则匹配内网网段 (10.x.x.x, 172.16.x.x-172.31.x.x, 192.168.x.x)
        // 追求极致性能的话，建议将 IP 转为 long 型进行位运算判断
        return ip.startsWith("10.")
                || ip.startsWith("192.168.")
                || ip.matches("^172\\.(1[6-9]|2[0-9]|3[0-1])\\..*");
    }
}
