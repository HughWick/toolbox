package com.github.hugh.time;

import com.github.hugh.util.DateUtils;
import com.github.hugh.util.TimeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
//        System.out.println("-1-->>" + );
//        System.out.println("-2-->>" + TimeUtils.checkTimestamp(start + "1528858736043", 1800000));
//        System.out.println("-3-->>" + TimeUtils.checkTimestamp(start + "1528858736043", 1800000));
//        System.out.println("-4-->>" + TimeUtils.checkTimestamp("1528858736043", 1800000));
//        System.out.println("--->>" + TimeUtils.getTime());
//        System.out.println("--->>" + TimeUtils.endOfLastMonth());
    }

    // 校验开始日期与结束日期是否跨天
    @Test
    void testIsCrossDay() {
        String start = "2020-06-08 00:00:00";
        String end = "2020-06-10 00:00:00";
        assertFalse(TimeUtils.isCrossDay(start, end));
        assertTrue(TimeUtils.isCrossDay(start, end, 2));
    }

    @Test
    public void test03() {
        String timeStart = "2019-12-11";
        String timeEnd = "2020-12-20";
        TimeUtils.collectLocalDates(timeStart, timeEnd).forEach(System.out::println);
    }

    @Test
    void testGetYearMonthDay() {
        assertEquals(2022, TimeUtils.getYear());
        assertEquals(7, TimeUtils.getMonth());
        assertEquals(4, TimeUtils.getDay());
        assertEquals(20, TimeUtils.getHour());
        assertEquals(10, TimeUtils.getMinute());
        System.out.println(TimeUtils.getSecond());
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
        System.out.println("--1---当前日期---->>" + TimeUtils.getTime());
        System.out.println("--2---当前日期---->>" + TimeUtils.now());
        System.out.println("--月的第1天--->>" + TimeUtils.firstDayOfMonth());
        System.out.println("----最后1天----->>" + TimeUtils.lastDayOfMonth());
        System.out.println("---上个月的第1天------>>" + TimeUtils.earlyLastMonth());
        System.out.println("---上个月的最后1天------>>" + TimeUtils.endOfLastMonth());
        System.out.println("--在当前系统时间之后-->>" + TimeUtils.exceedSystem("2020-04-17 13:59:59"));
    }

    // gga utc时间+8小时
    @Test
    void testCst() {
        String strDate = "020938.000";
        String format = "HHmmss.SSS";
        LocalTime localTime = TimeUtils.toCstTime(strDate, format);
        assertEquals("10:09:38", localTime.toString());
    }
}
