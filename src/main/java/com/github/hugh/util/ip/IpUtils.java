package com.github.hugh.util.ip;

import com.github.hugh.util.regex.RegexUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * IP工具类
 *
 * @author hugh
 * @since 1.0.4
 */
public class IpUtils {

    private IpUtils() {
    }

    /**
     * IP点号分隔符
     */
    private static final String REGEX_IP = "\\.";

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request http请求
     * @return String ip
     */
    public static String get(HttpServletRequest request) {
        // X-Forwarded-For：Squid 服务代理
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // Proxy-Client-IP：apache 服务代理
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // WL-Proxy-Client-IP：weblogic 服务代理
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // HTTP_CLIENT_IP：有些代理服务器
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // X-Real-IP：nginx服务代理
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ip != null && ip.length() != 0) {
            ip = ip.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 随机生成国内IP地址
     *
     * @return String ip
     */
    public static String random() {
        // ip范围
        int[][] range = {{607649792, 608174079}, // 36.56.0.0-36.63.255.255
                {1038614528, 1039007743}, // 61.232.0.0-61.237.255.255
                {1783627776, 1784676351}, // 106.80.0.0-106.95.255.255
                {2035023872, 2035154943}, // 121.76.0.0-121.77.255.255
                {2078801920, 2079064063}, // 123.232.0.0-123.235.255.255
                {-1950089216, -1948778497}, // 139.196.0.0-139.215.255.255
                {-1425539072, -1425014785}, // 171.8.0.0-171.15.255.255
                {-1236271104, -1235419137}, // 182.80.0.0-182.92.255.255
                {-770113536, -768606209}, // 210.25.0.0-210.47.255.255
                {-569376768, -564133889}, // 222.16.0.0-222.95.255.255
        };
        Random random = new Random();
        int index = random.nextInt(10);
        return num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
    }

    /**
     * 将十进制转换成ip地址
     *
     * @param ip IP
     * @return String
     */
    private static String num2ip(int ip) {
        int[] b = new int[4];
        b[0] = ((ip >> 24) & 0xff);
        b[1] = ((ip >> 16) & 0xff);
        b[2] = ((ip >> 8) & 0xff);
        b[3] = (ip & 0xff);
        return b[0] + "." + b[1] + "." + b[2] + "." + b[3];
    }

    /**
     * 功能：判断一个IP是不是在一个网段下的
     * 格式：isInRange("192.168.8.3", "192.168.9.10/22");
     *
     * @param ip   IP
     * @param cidr
     * @return boolean
     * @since 1.7.2
     */
    public static boolean isInRange(String ip, String cidr) {
        String[] ips = ip.split(REGEX_IP);
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split(REGEX_IP);
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);
        return (ipAddr & mask) == (cidrIpAddr & mask);
    }

    /**
     * 功能：根据位数返回IP总数
     * 格式：parseIpMaskRange("192.192.192.1", "23")
     *
     * @param mask 掩码
     * @return int
     * @since 1.7.2
     */
    public static int getIpCount(String mask) {
        return BigDecimal.valueOf(Math.pow(2, 32 - Integer.parseInt(mask))).setScale(0, BigDecimal.ROUND_DOWN).intValue();//IP总数，去小数点
    }

    /**
     * 把long类型的Ip转为一般Ip类型：xx.xx.xx.xx
     *
     * @param ip IP
     * @return String
     * @since 1.7.2
     */
    public static String getIpFromLong(Long ip) {
        String s1 = String.valueOf((ip & 4278190080L) / 16777216L);
        String s2 = String.valueOf((ip & 16711680L) / 65536L);
        String s3 = String.valueOf((ip & 65280L) / 256L);
        String s4 = String.valueOf(ip & 255L);
        return s1 + "." + s2 + "." + s3 + "." + s4;
    }

    /**
     * 把xx.xx.xx.xx类型的转为long类型的
     *
     * @param ip IP
     * @return long
     * @since 1.7.2
     */
    public static Long getIpFromString(String ip) {
        String ipTemp = ip;
        long ipLong = Long.parseLong(ipTemp.substring(0, ipTemp.indexOf('.')));
        ipTemp = ipTemp.substring(ipTemp.indexOf('.') + 1);
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf('.')));
        ipTemp = ipTemp.substring(ipTemp.indexOf(".") + 1);
        ipLong = ipLong * 256
                + Long.parseLong(ipTemp.substring(0, ipTemp.indexOf('.')));
        ipTemp = ipTemp.substring(ipTemp.indexOf('.') + 1);
        ipLong = ipLong * 256 + Long.parseLong(ipTemp);
        return ipLong;
    }

    /**
     * 根据掩码位获取掩码
     *
     * @param maskBit 掩码位数，如"28"、"30"
     * @return String
     * @since 1.7.2
     */
    public static String getMaskByMaskBit(String maskBit) {
        return "".equals(maskBit) ? "error, maskBit is null !" : getNetmask(maskBit);
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return String 起始IP的字符串表示
     * @since 1.7.2
     */
    public static String calcBeginIp(String ip, String maskBit) {
        return getIpFromLong(getBeginIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return long 起始IP的长整型表示
     * @since 1.7.2
     */
    public static Long getBeginIpLong(String ip, String maskBit) {
        return getIpFromString(ip) & getIpFromString(getMaskByMaskBit(maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return String 终止IP的字符串表示
     * @since 1.7.2
     */
    public static String calcEndIp(String ip, String maskBit) {
        return getIpFromLong(getEndIpLong(ip, maskBit));
    }

    /**
     * 根据 ip/掩码位 计算IP段的终止IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return long 终止IP的长整型表示
     * @since 1.7.2
     */
    public static Long getEndIpLong(String ip, String maskBit) {
        return getBeginIpLong(ip, maskBit)
                + ~getIpFromString(getMaskByMaskBit(maskBit));
    }

    /**
     * 根据掩码位获取子网掩码
     *
     * @param maskBit 掩码位 1-32
     * @return String netmask
     * @since 1.7.2
     */
    public static String getNetmask(String maskBit) {
        switch (maskBit) {
            case "1":
                return "128.0.0.0";
            case "2":
                return "192.0.0.0";
            case "3":
                return "224.0.0.0";
            case "4":
                return "240.0.0.0";
            case "5":
                return "248.0.0.0";
            case "6":
                return "252.0.0.0";
            case "7":
                return "254.0.0.0";
            case "8":
                return "255.0.0.0";
            case "9":
                return "255.128.0.0";
            case "10":
                return "255.192.0.0";
            case "11":
                return "255.224.0.0";
            case "12":
                return "255.240.0.0";
            case "13":
                return "255.248.0.0";
            case "14":
                return "255.252.0.0";
            case "15":
                return "255.254.0.0";
            case "16":
                return "255.255.0.0";
            case "17":
                return "255.255.128.0";
            case "18":
                return "255.255.192.0";
            case "19":
                return "255.255.224.0";
            case "20":
                return "255.255.240.0";
            case "21":
                return "255.255.248.0";
            case "22":
                return "255.255.252.0";
            case "23":
                return "255.255.254.0";
            case "24":
                return "255.255.255.0";
            case "25":
                return "255.255.255.128";
            case "26":
                return "255.255.255.192";
            case "27":
                return "255.255.255.224";
            case "28":
                return "255.255.255.240";
            case "29":
                return "255.255.255.248";
            case "30":
                return "255.255.255.252";
            case "31":
                return "255.255.255.254";
            case "32":
                return "255.255.255.255";
            default:
                return "-1";
        }
    }

    /**
     * 根据IP和位数（掩码）返回该IP网段的所有IP
     * <p>格式：parseIpMaskRange("192.192.192.1.", "23")</p>
     *
     * @param ip   ip
     * @param mask 掩码
     * @return List IP网段的所有IP
     * @since 1.7.2
     */
    public static List<String> parseIpMaskRange(String ip, String mask) {
        List<String> list = new ArrayList<>();
        if ("32".equals(mask)) {
            list.add(ip);
        } else {
            String startIp = getBeginIpStr(ip, mask);
            String endIp = calcEndIp(ip, mask);
            if (!"31".equals(mask)) {
                String subStart = startIp.split(REGEX_IP)[0] + "." + startIp.split(REGEX_IP)[1] + "." + startIp.split(REGEX_IP)[2] + ".";
                String subEnd = endIp.split(REGEX_IP)[0] + "." + endIp.split(REGEX_IP)[1] + "." + endIp.split(REGEX_IP)[2] + ".";
                startIp = subStart + (Integer.parseInt(startIp.split(REGEX_IP)[3]) + 1);
                endIp = subEnd + (Integer.parseInt(endIp.split(REGEX_IP)[3]) - 1);
            }
            list = parseIpRange(startIp, endIp);
        }
        return list;
    }

    /**
     * 转换ip范围
     *
     * @param startIp 起始IP地址
     * @param endIp   结束IP地址
     * @return List  起始与结束范围内的所有IP
     * @since 1.7.2
     */
    public static List<String> parseIpRange(String startIp, String endIp) {
        List<String> ips = new ArrayList<>();
        String[] startIpArr = startIp.split(REGEX_IP);
        String[] endIpArr = endIp.split(REGEX_IP);
        int[] int_ipf = new int[4];
        int[] int_ipt = new int[4];
        for (int i = 0; i < 4; i++) {
            int_ipf[i] = Integer.parseInt(startIpArr[i]);
            int_ipt[i] = Integer.parseInt(endIpArr[i]);
        }
        for (int A = int_ipf[0]; A <= int_ipt[0]; A++) {
            for (int B = (A == int_ipf[0] ? int_ipf[1] : 0); B <= (A == int_ipt[0] ? int_ipt[1]
                    : 255); B++) {
                for (int C = (B == int_ipf[1] ? int_ipf[2] : 0); C <= (B == int_ipt[1] ? int_ipt[2]
                        : 255); C++) {
                    for (int D = (C == int_ipf[2] ? int_ipf[3] : 0); D <= (C == int_ipt[2] ? int_ipt[3]
                            : 255); D++) {
                        ips.add(A + "." + B + "." + C + "." + D);
                    }
                }
            }
        }
        return ips;
    }

    /**
     * 根据 ip/掩码位 计算IP段的起始IP 如 IP串 218.240.38.69/30
     *
     * @param ip      给定的IP，如218.240.38.69
     * @param maskBit 给定的掩码位，如30
     * @return String 起始IP的字符串表示
     * @since 1.7.2
     */
    public static String getBeginIpStr(String ip, String maskBit) {
        return getIpFromLong(getBeginIpLong(ip, maskBit));
    }

    /**
     * 比较两个ip地址是否在同一个网段中
     * <ul>
     *  <li>
     * IP地址范围： 0.0.0.0～255.255.255.255，包括了mask地址。 IP地址划分
     *  </li>
     *  <li>
     * A类地址：1.0.0.1～126.255.255.254 B类地址：128.0.0.1～191.255.255.254
     *  </li>
     *  <li>
     * C类地址：192.168.0.0～192.168.255.255 D类地址：224.0.0.1～239.255.255.254
     *  </li>
     *  <li>
     * E类地址：240.0.0.1～255.255.255.254 如何判断两个IP地址是否是同一个网段中:
     *  </li>
     * 要判断两个IP地址是不是在同一个网段，就将它们的IP地址分别与子网掩码做与运算，得到的结果一网路号，如果网路号相同，就在同一子网，否则，不在同一子网。
     *  <li>
     * 例：假定选择了子网掩码255.255.254.0，现在分别将上述两个IP地址分别与掩码做与运算，如下图所示：
     *  </li>
     * <li>
     * 211.95.165.24 11010011 01011111 10100101 00011000
     * </li>
     * <li>
     * 255.255.254.0 11111111 11111111 111111110 00000000
     * </li>
     * <li>
     * 与的结果是:
     * 11010011 01011111 10100100 00000000
     * </li>
     * <li>
     * 211.95.164.78 11010011 01011111 10100100 01001110
     * </li>
     * <li>
     * 255.255.254.0 11111111 11111111 111111110 00000000
     * </li>
     * <li>
     * 与的结果是:
     * 11010011 01011111 10100100 00000000
     * </li>
     * <li>
     * 可以看出,得到的结果(这个结果就是网路地址)都是一样的，因此可以判断这两个IP地址在同一个子网。
     * 如果没有进行子网划分，A类网路的子网掩码为255.0.0.0，B类网路的子网掩码为255.255.0.0，C类网路的子网掩码为255.255.255.0，预设情况子网掩码为255.255.255.0
     * </li>
     * </ul>
     *
     * @param ip1  第一个IP地址
     * @param ip2  第二个IP地址
     * @param mask 子网掩码
     * @return boolean
     * @since 2.0.3
     */
    public static boolean checkSameSegment(String ip1, String ip2, String mask) {
        if (RegexUtils.isNotIp(ip1)) {
            return false;
        }
        if (RegexUtils.isNotIp(ip2)) {
            return false;
        }
        int maskInt = getIpV4Value(mask);
        int ipValue1 = getIpV4Value(ip1);
        int ipValue2 = getIpV4Value(ip2);
        return (maskInt & ipValue1) == (maskInt & ipValue2);
    }

    /**
     * 计算ipv4 地址int值
     *
     * @param ipOrMask ip或子网掩码
     * @return int
     * @since 2.0.3
     */
    public static int getIpV4Value(String ipOrMask) {
        byte[] addr = getIpV4Bytes(ipOrMask);
        int address1 = addr[3] & 0xFF;
        address1 |= ((addr[2] << 8) & 0xFF00);
        address1 |= ((addr[1] << 16) & 0xFF0000);
        address1 |= ((addr[0] << 24) & 0xFF000000);
        return address1;
    }

    /**
     * 获取ipV4的位元组
     *
     * @param ipOrMask ip或子网掩码
     * @return byte[]
     * @since 2.0.3
     */
    public static byte[] getIpV4Bytes(String ipOrMask) {
        try {
            String[] addrs = ipOrMask.split("\\.");
            int length = addrs.length;
            byte[] addr = new byte[length];
            for (int index = 0; index < length; index++) {
                addr[index] = (byte) (Integer.parseInt(addrs[index]) & 0xff);
            }
            return addr;
        } catch (Exception e) {
        }
        return new byte[4];
    }
}
