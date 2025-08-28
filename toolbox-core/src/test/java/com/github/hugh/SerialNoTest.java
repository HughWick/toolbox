package com.github.hugh;

import com.github.hugh.constant.DateCode;
import com.github.hugh.util.RandomUtils;
import com.github.hugh.util.TimeUtils;
import org.junit.jupiter.api.Test;

/**
 * 序列号生成器
 */
public class SerialNoTest {

    @Test
    void test01() {
        String zhu = generate("T0000", 1, "");
        System.out.println(zhu.length());
        System.out.println(zhu);
    }

    /**
     * 生成序列号的方法，根据传入的topStr、count和reservedBit参数生成唯一的序列号
     *
     * @param topStr      前缀字符串，用于定制化序列号
     * @param count       自增数，用于生成序列号的一部分
     * @param reservedBit 预留位，作为序列号的一部分
     * @return 生成的唯一序列号
     */
    public static synchronized String generate(String topStr, int count, String reservedBit) {
        String strCount = String.format("%05d", count);
        // 获取当前时间，格式为年月日，例如：20240325
        String time = TimeUtils.now(DateCode.YEAR_MONTH_DAY_SIMPLE);
        // 生成一个3位的随机数
        int ramCount = RandomUtils.number(3);
        // 计算校验位 = 去掉毫秒的10位时间戳 + 自增数 + 随机数 % 3
        long checkBit = Long.parseLong(strCount + ramCount) % 3;
        // 拼接生成的序列号并返回
        return topStr + time + strCount + ramCount + reservedBit + checkBit;
    }
}
