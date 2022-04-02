package com.github.hugh.util.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

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

    /**
     * 创建Socket并连接到指定地址的服务器
     *
     * @param hostname 地址
     * @param port     端口
     * @return {@link Socket}
     * @throws IOException IO异常
     */
    public static Socket connect(String hostname, int port) throws IOException {
        return connect(hostname, port, -1);
    }

    /**
     * 创建Socket并连接到指定地址的服务器
     *
     * @param hostname          地址
     * @param port              端口
     * @param connectionTimeout 连接超时
     * @return {@link Socket}
     * @throws IOException IO异常
     */
    public static Socket connect(final String hostname, int port, int connectionTimeout) throws IOException {
        return connect(new InetSocketAddress(hostname, port), connectionTimeout);
    }

    /**
     * 创建Socket并连接到指定地址的服务器
     *
     * @param address           地址
     * @param connectionTimeout 连接超时时间、单位：毫秒
     * @return {@link Socket}
     * @throws IOException IO异常
     */
    public static Socket connect(InetSocketAddress address, int connectionTimeout) throws IOException {
        final Socket socket = new Socket();
        try (socket){
            if (connectionTimeout <= 0) {
                socket.connect(address);
            } else {
                socket.connect(address, connectionTimeout);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        return socket;
    }

    /**
     * 测试tcp连接
     *
     * @param hostname 主机名称
     * @param port     端口
     * @return boolean
     */
    public static boolean isConnect(String hostname, int port) {
        try {
            connect(hostname, port, 3000);
            return true;
        } catch (IOException ignored) {
        }
        return false;
    }
}
