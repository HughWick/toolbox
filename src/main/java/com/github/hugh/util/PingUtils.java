package com.github.hugh.util;

import com.github.hugh.util.system.OsUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ping的工具类
 *
 * @author hugh
 * @since 1.3.2
 */
public class PingUtils {

    /**
     * 单次ping IP是否能够正常访问
     *
     * @param ipAddress IP地址
     * @param pingCount ping次数
     * @param timeOut   超时时间毫秒
     * @return boolean {@code true}
     */
    public static boolean send(String ipAddress, int pingCount, int timeOut) {
        BufferedReader in = null;
        Runtime runtime = Runtime.getRuntime();
        String pingCommand;
        if (OsUtils.isWindows()) {//将要执行的ping命令
            pingCommand = "ping " + ipAddress + " -n " + pingCount + " -w " + timeOut;//此命令是windows格式的命令
        } else {
            pingCommand = "ping " + ipAddress + " -c " + pingCount;//linux
        }
        try { // 执行命令并获取输出
            Process process = runtime.exec(pingCommand);
            if (process == null) {
                return false;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            in = new BufferedReader(inputStreamReader);// 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            int connectedCount = 0;
            String line;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);// 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            }
            return connectedCount == pingCount;
        } catch (Exception ex) {
            ex.printStackTrace();   // 出现异常则返回假
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
     *
     * @param line 字符串
     * @return int 耗时
     */
    private static int getCheckResult(String line) {
        Pattern pattern;
        if (OsUtils.isWindows()) {
            pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
        } else {//linux例：64 bytes from 192.168.1.13: icmp_seq=0 ttl=63 time=0.641 ms 判断是否有ttl与ms
            pattern = Pattern.compile("(\\d+|\\s+ms)(\\s+)(ttl=\\d+)", Pattern.CASE_INSENSITIVE);
        }
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
        }
        return 0;
    }


    /**
     * 发送多次Ping
     *
     * @param ipAddress IP地址
     * @param pingCount ping次数
     * @param timeOut   超时时间
     * @return List<String>
     */
    public static List<String> batch(String ipAddress, int pingCount, int timeOut) {
        List<String> strs = new ArrayList<>();
        String pingCommand;
        if (OsUtils.isWindows()) {
            pingCommand = "ping " + ipAddress + " -n " + pingCount + " -w " + timeOut;
        } else {
            pingCommand = "ping " + ipAddress + " -c " + pingCount;
        }
        try {
            Process pro = Runtime.getRuntime().exec(pingCommand);
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    pro.getInputStream(), "GBK"));
            String line;
            while ((line = buf.readLine()) != null) {
                if (!"".equals(line)) {//不等于空字符串时
                    strs.add(line);
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return strs;
    }
}
