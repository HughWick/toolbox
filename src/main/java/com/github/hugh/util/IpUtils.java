package com.github.hugh.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * IP工具类
 *
 * @author hugh
 */
public class IpUtils {

    private IpUtils() {
    }

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
        Random rdint = new Random();
        int index = rdint.nextInt(10);
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
}
