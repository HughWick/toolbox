package com.github.hugh.util;

import com.github.hugh.support.TimeoutOperation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * java 1.8 后时间对象
 *
 * @author AS
 * @date 2020/8/31 15:22
 */
class TimeTest {

    @Test
    void test01() {
        long start = System.currentTimeMillis();
        assertTrue(TimeUtils.checkTimestamp(start + "", 1800000));
        assertFalse(TimeUtils.checkTimestamp(start + "1528858736043", 1800000));
        assertFalse(TimeUtils.checkTimestamp("1528858736043", 1800000));
        assertTrue(DateUtils.isDateFormat(TimeUtils.now()));
    }

    // 校验开始日期与结束日期是否跨天
    @Test
    void testIsCrossDay() {
        String start = "2020-06-08 00:00:00";
        String end = "2020-06-10 00:00:00";
        assertFalse(TimeUtils.isCrossDay(start, end));
        assertTrue(TimeUtils.isCrossDay(start, end, 2));
    }

//    @Test
//    public void test03() {
//        String timeStart = "2019-12-11";
//        String timeEnd = "2020-12-20";
//        TimeUtils.collectLocalDates(timeStart, timeEnd).forEach(System.out::println);
//    }

    @Test
    void testGetYearMonthDay() {
//        assertEquals(2023, TimeUtils.getYear());
//        assertEquals(7, TimeUtils.getMonth());
//        assertEquals(4, TimeUtils.getDay());
//        assertEquals(20, TimeUtils.getHour());
//        assertEquals(10, TimeUtils.getMinute());
//        System.out.println(TimeUtils.getSecond());
//        String timeStart = "2019-12-11 00:00:00";
//        String timeEnd = "2020-12-20 23:59:59";
//        long l = TimeUtils.differMilli(timeStart, timeEnd);
//        System.out.println(l);
//        System.out.println("---1>>" + TimeUtils.getMonth());
//        System.out.println("---2>>" + TimeUtils.getDay());
//        System.out.println("--3->>" + TimeUtils.isThisYear(12));

    }

    // 测试获取昨日时间
    @Test
    void test05() {
        Date date = DateUtils.getDayBeforeStartTime();
        assertEquals(DateUtils.ofPattern(date), TimeUtils.getYesterdayStartTime());
        System.out.println("=4=昨日结束日期==>>" + TimeUtils.getYesterdayEndTime());
    }

    @Test
    void testTime() {
        assertEquals(TimeUtils.getTime(), TimeUtils.now());
//        System.out.println("--1---当前日期---->>" + TimeUtils.getTime());
//        System.out.println("--2---当前日期---->>" + TimeUtils.now());
        System.out.println("--月的第1天--->>" + TimeUtils.firstDayOfMonth());
        System.out.println("----最后1天----->>" + TimeUtils.lastDayOfMonth());
        System.out.println("---上个月的第1天------>>" + TimeUtils.earlyLastMonth());
        System.out.println("---上个月的最后1天------>>" + TimeUtils.endOfLastMonth());
        assertTrue(TimeUtils.exceedSystem("2020-04-17 13:59:59"));
        assertFalse(TimeUtils.exceedSystem("2120-04-17 13:59:59"));
//        System.out.println("--在当前系统时间之后-->>" + TimeUtils.exceedSystem("2020-04-17 13:59:59"));
    }

    // gga utc时间+8小时
    @Test
    void testCst() {
        String strDate = "020938.000";
        String format = "HHmmss.SSS";
        LocalTime localTime = TimeUtils.toCstTime(strDate, format);
        assertEquals("10:09:38", localTime.toString());
    }

    @Test
    void testExecuteTimeoutSeconds() {
        AtomicBoolean executed1 = new AtomicBoolean(false); // 用于记录是否执行了超时操作
        TimeoutOperation logOperation1 = () -> executed1.set(true); // 定义超时操作
        TimeUtils.executeTimeout(2, 4, logOperation1); // 执行被测方法
        assertFalse(executed1.get()); // 时间戳早于等于超时阈值，不应该执行超时操作
    }

    @Test
    void logExecuteTimeoutNanos() {
        AtomicBoolean executed1 = new AtomicBoolean(false); // 用于记录是否执行了超时操作
        TimeoutOperation logOperation1 = () -> executed1.set(true); // 定义超时操作
        TimeUtils.executeTimeout(2000, 200, logOperation1); // 执行被测方法
        assertTrue(executed1.get()); // 时间戳早于等于超时阈值，不应该执行超时操作
    }

    @Test
    void testExecuteTimeoutMillis() {
        AtomicBoolean executed1 = new AtomicBoolean(false); // 用于记录是否执行了超时操作
        TimeoutOperation logOperation1 = () -> executed1.set(true); // 定义超时操作
        TimeUtils.executeTimeout(400, 200, logOperation1); // 执行被测方法
        assertTrue(executed1.get()); // 时间戳早于等于超时阈值，不应该执行超时操作
    }

    @Test
    void testIsExecuteTimeout() {
        assertTrue(TimeUtils.isExecuteTimeout(400, 200));
        assertFalse(TimeUtils.isExecuteTimeout(200, 400));
    }

    @Test
    void testDateToLocalTime() {
        // 创建一个Date对象
        Date date = new Date(1639974073000L); // 2021-12-20 12:21:13  北京时间
        // 调用convert方法，将Date对象转换为LocalDateTime对象
        LocalDateTime localDateTime = TimeUtils.convert(date, ZoneId.of("Asia/Shanghai"));
        // 验证转换后的LocalDateTime对象是否正确
        assertEquals(2021, localDateTime.getYear());
        assertEquals(12, localDateTime.getMonthValue());
        assertEquals(20, localDateTime.getDayOfMonth());
        assertEquals(12, localDateTime.getHour());
        assertEquals(21, localDateTime.getMinute());
        assertEquals(13, localDateTime.getSecond());
        Date date2 = new Date(1702955655000L); // 2021-12-20 12:21:13  北京时间
        LocalDateTime localDateTime2= TimeUtils.convertShanghai(date2);
        // 验证转换后的LocalDateTime对象是否正确
        assertEquals(2023, localDateTime2.getYear());
        assertEquals(12, localDateTime2.getMonthValue());
        assertEquals(19, localDateTime2.getDayOfMonth());
        assertEquals(11, localDateTime2.getHour());
        assertEquals(14, localDateTime2.getMinute());
        assertEquals(15, localDateTime2.getSecond());
    }
}
