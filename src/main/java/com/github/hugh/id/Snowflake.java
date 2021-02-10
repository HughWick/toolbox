package com.github.hugh.id;

import com.github.hugh.util.DateUtils;
import com.github.hugh.util.EmptyUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 雪花算法 </p>
 *
 * <pre> Created: 2019/3   </pre>
 * [理解分布式id生成算法SnowFlake](https://segmentfault.com/a/1190000011282426?utm_source=tag-newest)
 *
 * @author hugh
 * @since 1.0.2
 */
public class Snowflake {
    /**
     * 起始的时间戳
     */
    private static final long START_STMP = 1288834974657L;

    /**
     * 每一部分占用的位数
     */
    private static final long SEQUENCE_BIT = 12; // 序列号占用的位数
    private static final long MACHINE_BIT = 5; // 机器标识占用的位数
    private static final long DATA_CENTER_BIT = 5;// 数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private static final long MAX_DATA_CENTER_NUM = ~(-1L << DATA_CENTER_BIT);
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private static final long TIMESTMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    private long dataCenterId; // 数据中心
    private long machineId; // 机器标识
    private long sequence = 0L; // 序列号
    private long lastStmp = -1L;// 上一次时间戳

    public Snowflake() {
        this.dataCenterId = getDataCenterId(MAX_DATA_CENTER_NUM);
        this.machineId = getMaxWorkerId(dataCenterId, MAX_MACHINE_NUM);
    }

    /**
     * @param machineId    工作机器ID
     * @param dataCenterId 序列号
     */
    public Snowflake(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    /**
     * 获取 maxWorkerId
     *
     * @param dataCenterId 客户端编号
     * @param maxWorkerId  机器标识
     * @return 最大机器标识
     */
    protected static long getMaxWorkerId(long dataCenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(dataCenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (EmptyUtils.isNotEmpty(name)) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * 数据标识id部分
     *
     * @param maxDataCenterId 最大标识
     * @return 最大标识
     */
    private static long getDataCenterId(long maxDataCenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (maxDataCenterId + 1);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * 产生下一个ID
     *
     * @return long
     */
    public synchronized long nextId() {
        long currStmp = timeGen();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }
        if (currStmp == lastStmp) {// 相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {// 同一毫秒的序列数已经达到最大
                currStmp = getNextMill();
            }
        } else { // 不同毫秒内，序列号置为0
            sequence = 0L;
        }
        lastStmp = currStmp;
        long time = (currStmp - START_STMP) << TIMESTMP_LEFT;// 时间戳部分
        return time | dataCenterId << DATA_CENTER_LEFT // 数据中心部分
                | machineId << MACHINE_LEFT // 机器标识部分
                | sequence; // 序列号部分
    }

    /**
     * 等待直到下一个毫秒
     *
     * @return 等待直到下一个毫秒
     */
    private long getNextMill() {
        long mill = timeGen();
        while (mill <= lastStmp) {
            mill = timeGen();
        }
        return mill;
    }

    /**
     * 获取时间戳
     *
     * @return 时间戳
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    /**
     * 通过雪花ID结果。进行解析
     * <p>反解析结果 例：{date=2020-08-28 22:54:30, sequence=146, workerId=30, dataCenter=12}</p>
     *
     * @param id 雪花ID
     * @return Map
     */
    public static Map decompile(long id) {
        String binaryId = Long.toBinaryString(id);// ID转二进制字符串
        int len = binaryId.length();// 长度
        int sequenceStart = (int) (len < SEQUENCE_BIT ? 0 : len - SEQUENCE_BIT);
        int workerStart = (int) (len < DATA_CENTER_LEFT ? 0 : len - DATA_CENTER_LEFT);
        int timeStart = (int) (len < TIMESTMP_LEFT ? 0 : len - TIMESTMP_LEFT);
        String sequence = binaryId.substring(sequenceStart, len);// 自增序号
        String workerId = sequenceStart == 0 ? "0" : binaryId.substring(workerStart, sequenceStart);
        String dataCenterId = workerStart == 0 ? "0" : binaryId.substring(timeStart, workerStart);
        String time = timeStart == 0 ? "0" : binaryId.substring(0, timeStart);
        Map<String,Object> item = new HashMap<>();
        item.put("sequence", Integer.valueOf(sequence, 2));// 二进制转自增ID
        item.put("workerId", Integer.valueOf(workerId, 2));// 二进制转机器ID
        item.put("dataCenter", Integer.valueOf(dataCenterId, 2));// 二进制转数据中心ID
        long diffTime = Long.parseLong(time, 2);// 二进制转long
        long timeLong = diffTime + START_STMP;// 加上初初始时间
        item.put("date", DateUtils.formatTimestamp(timeLong));
        return item;
    }
}
