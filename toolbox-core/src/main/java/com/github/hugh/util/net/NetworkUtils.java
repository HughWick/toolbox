package com.github.hugh.util.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 网络工具类
 *
 * @author Hugh
 * @since 2.1.7
 **/
public class NetworkUtils {

    private NetworkUtils() {
    }

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
     * 使用默认超时时间（200毫秒）测试指定IP地址是否可达。
     *
     * @param ip 目标IP地址
     * @return 如果ping测试失败，返回true；否则返回false
     * @since 2.7.19
     */
    public static boolean pingFail(String ip) {
        // 使用默认超时200毫秒
        return pingFail(ip, 200);
    }

    /**
     * 使用指定的超时时间测试指定IP地址是否可达。
     *
     * @param ip      目标IP地址
     * @param timeout 超时时间（毫秒）
     * @return 如果ping测试失败，返回true；否则返回false
     * @since 2.7.19
     */
    public static boolean pingFail(String ip, int timeout) {
        // ping方法返回false表示ping失败，所以取反返回true表示失败
        return !ping(ip, timeout);
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
        try (socket) {
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
        return isConnect(hostname, port, 3000);
    }

    /**
     * 检查指定的主机名和端口是否在给定的超时时间内可达。
     *
     * @param hostname 要连接的主机名
     * @param port     要连接的端口
     * @param timeout  超时时间（以毫秒为单位）
     * @return 如果连接成功则返回true，否则返回false
     * @since 2.7.11
     */
    public static boolean isConnect(String hostname, int port, int timeout) {
        try {
            connect(hostname, port, timeout);
            return true;
        } catch (IOException ignored) {
        }
        return false;
    }

    /**
     * 检查指定的主机名和端口是否在默认超时时间（3000毫秒）内不可达。
     *
     * @param hostname 要连接的主机名
     * @param port     要连接的端口
     * @return 如果连接不成功则返回true，否则返回false
     * @since 2.7.11
     */
    public static boolean isNotConnect(String hostname, int port) {
        return !isConnect(hostname, port);
    }

    /**
     * 检查指定的主机名和端口是否在给定的超时时间内不可达。
     *
     * @param hostname 要连接的主机名
     * @param port     要连接的端口
     * @param timeout  超时时间（以毫秒为单位）
     * @return 如果连接不成功则返回true，否则返回false
     * @since 2.7.11
     */
    public static boolean isNotConnect(String hostname, int port, int timeout) {
        return !isConnect(hostname, port, timeout);
    }

    /**
     * 查看本机某端口是否被占用
     *
     * @param port 端口号
     * @return boolean 如果被占用则返回true，否则返回false
     * @since 2.0.11
     */
    public static boolean isLocalsPortUsing(int port) {
        boolean flag = true;
        try {
            flag = isPortUsing("127.0.0.1", port);
        } catch (Exception exception) {
        }
        return flag;
    }

    /**
     * 根据IP和端口号，查询其是否被占用
     *
     * @param host IP
     * @param port 端口号
     * @return boolean 如果被占用，返回true；否则返回false
     * @throws UnknownHostException IP地址不通或错误，则会抛出此异常
     * @since 2.0.11
     */
    public static boolean isPortUsing(String host, int port) throws UnknownHostException {
        boolean flag = false;
        InetAddress theAddress = InetAddress.getByName(host);
        try (Socket ignored = new Socket(theAddress, port)) {
            flag = true;
        } catch (IOException e) {
            //如果所测试端口号没有被占用，那么会抛出异常，这里利用这个机制来判断
            //所以，这里在捕获异常后，什么也不用做
        }
        return flag;
    }
}