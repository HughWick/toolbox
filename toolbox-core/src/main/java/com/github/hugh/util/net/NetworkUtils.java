package com.github.hugh.util.net;

import java.net.InetAddress;

/**
 * 网络工具类
 *
 * @author Hugh
 * @since 2.1.7
 **/
public class NetworkUtils {

    /**
     * 检测IP地址是否能ping通
     *
     * @param ip IP地址
     * @return boolean 返回是否ping通
     */
    public static boolean ping(String ip) {
        return ping(ip, 200);
    }

    /**
     * 检测IP地址是否能ping通
     *
     * @param ip      IP地址
     * @param timeout 检测超时（毫秒）
     * @return boolean 是否ping通
     */
    public static boolean ping(String ip, int timeout) {
        try {
            return InetAddress.getByName(ip).isReachable(timeout); // 当返回值是true时，说明host是可用的，false则不可。
        } catch (Exception ex) {
            return false;
        }
    }
}
