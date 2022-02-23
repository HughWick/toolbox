package com.github.hugh.util;

import com.github.hugh.bean.dto.PingDTO;
import com.github.hugh.constant.CharsetCode;
import com.github.hugh.util.system.OsUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
    private PingUtils() {

    }

    /**
     * 发送ping命令 并且验证ping次数是否与ping成功的次数相同
     *
     * @param ipAddress IP地址
     * @param pingCount ping次数
     * @param timeOut   超时时间毫秒
     * @return boolean {@code true}
     * @since 2.1.6
     */
    public static boolean send(String ipAddress, int pingCount, int timeOut) {
        try {
            return getConnectedCount(ipAddress, pingCount, timeOut) == pingCount;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }
    }

    /**
     * 发送ping IP 命令，验证ip
     *
     * @param ipAddress IP
     * @param pingCount ping次数
     * @param timeOut   超时时间毫秒
     * @return int  ping成功的次数
     * @since 2.1.6
     */
    public static int getConnectedCount(String ipAddress, int pingCount, int timeOut) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        String pingCommand = appendCmd(ipAddress, pingCount, timeOut);
        Process process = runtime.exec(pingCommand);
        if (process == null) {
            return -1;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
        try (BufferedReader in = new BufferedReader(inputStreamReader)) {
            // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            int connectedCount = 0;
            String line;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);// 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            }
            return connectedCount;
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
        return matcher.find() ? 1 : 0;
    }

    /**
     * 发送多次Ping
     *
     * @param ipAddress IP地址
     * @param pingCount ping次数
     * @param timeOut   超时时间
     * @return List
     */
    public static List<String> batch(String ipAddress, int pingCount, int timeOut) {
        List<String> strs = new ArrayList<>();
        String pingCommand = appendCmd(ipAddress, pingCount, timeOut);
        try {
            Process pro = Runtime.getRuntime().exec(pingCommand);
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    pro.getInputStream(), CharsetCode.GBK));
            String line;
            while ((line = buf.readLine()) != null) {
                if (!"".equals(line)) {//不等于空字符串时
                    strs.add(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strs;
    }

    /**
     * 根据系统不通拼接对应的 ping的命令
     *
     * @param ip      IP地址
     * @param count   ping次数
     * @param timeOut 超时时间、只在windows命令中有用
     * @return String 拼接后的命令字符串
     */
    private static String appendCmd(String ip, int count, int timeOut) {
        String pingCommand;
        if (OsUtils.isWindows()) {
            pingCommand = "ping " + ip + " -n " + count + " -w " + timeOut; //此命令是windows格式的命令
        } else {
            pingCommand = "ping " + ip + " -c " + count;
        }
        return pingCommand;
    }

    /**
     * 根据ip地址发送ping 请求
     * <p>注：默认发送4次ping、并且window命令下默认超时时间为10000ms</p>
     *
     * @param ip IP地址
     * @return {@link PingDTO}
     * @since 1.6.4
     */
    public static PingDTO ping(String ip) {
        return ping(ip, 4, 10000);
    }

    /**
     * Ping
     *
     * @param ip      IP地址
     * @param count   ping的次数
     * @param timeOut 超时时间
     * @return {@link PingDTO}
     * @since 1.6.4
     */
    public static PingDTO ping(String ip, int count, int timeOut) {
        String cmd = appendCmd(ip, count, timeOut);
        return send(cmd);
    }

    /**
     * 通过ip获取信息,loss:丢包率，delay:延时
     *
     * @param cmd 命令
     * @return {@link PingDTO}
     * @since 1.6.4
     */
    private static PingDTO send(String cmd) {
        PingDTO ping = new PingDTO();
        String line;
        String loss = "";//丢包率
        String delay = "";//延时
        try {
            //获取当前进程运行对象
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            try (InputStream inputStream = process.getInputStream();
                 InputStreamReader isReader = new InputStreamReader(inputStream, CharsetCode.GB_2312);
                 BufferedReader reader = new BufferedReader(isReader)) {
                StringBuilder buffer = new StringBuilder();
                if (OsUtils.isWindows()) {//Windows系统执行结果解析
                    while ((line = reader.readLine()) != null) {
                        //丢包率
                        if (line.contains("%")) {
                            loss = line.substring(line.lastIndexOf("=") + 1, line.indexOf("%") + 1);
                            if (loss.contains("(")) {
                                loss = loss.substring(loss.indexOf("(") + 1).strip();
                            }
                        }
                        //网络延时
                        if ((line.contains(",") || line.contains("，")) && line.contains("=") && line.contains("ms")) {
                            delay = line.substring(line.lastIndexOf("=") + 1, line.lastIndexOf("ms") + 2).strip();
                        }
                        buffer.append(line).append("\n");
                    }
                } else {//Linux系统执行结果解析
                    while ((line = reader.readLine()) != null) {
                        //丢包率
                        if (line.contains("%")) {
                            String[] msg = null;
                            if (line.contains(",")) {
                                msg = line.split(",");
                            } else if (line.contains("，")) {
                                msg = line.split("，");
                            }
                            assert msg != null;
                            if (msg.length > 0) {
                                loss = msg[2].substring(0, msg[2].indexOf("%") + 1).strip();
                            }
                        }
                        //网络延时
                        if (line.contains("/")) {
                            String[] msg = line.split("=");
                            String[] names = msg[0].split("/");
                            String[] values = msg[1].split("/");
                            for (int i = 0; i < names.length; i++) {
                                String str = names[i];
                                if ("avg".equalsIgnoreCase(str)) {
                                    delay = values[i];
                                    break;
                                }
                            }
                        }
                        buffer.append(line).append("\n");
                    }
                }
                // 替换百分号
                loss = loss.replace("%", "");
                delay = delay.replace("ms", "");
                if (EmptyUtils.isNotEmpty(loss)) {
                    ping.setLoss(Integer.parseInt(loss));
                }
                if (EmptyUtils.isNotEmpty(delay)) {
                    ping.setDelay(Double.parseDouble(delay));
                } else {
                    ping.setDelay(-1);// 延迟
                    ping.setStatus(-1);//无法ping通
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            ping.setStatus(-2);
        }
        return ping;
    }
}
