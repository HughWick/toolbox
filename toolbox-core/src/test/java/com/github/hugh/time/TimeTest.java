package com.github.hugh.time;

import com.github.hugh.util.TimeUtils;
import org.junit.jupiter.api.Test;

/**
 * @author AS
 * @date 2020/8/31 15:22
 */
public class TimeTest {

    @Test
    public void test01() {
        long start = System.currentTimeMillis();
        System.out.println("-1-->>" + TimeUtils.checkTimestamp(start + "", 1800000));
        System.out.println("-2-->>" + TimeUtils.checkTimestamp(start + "1528858736043", 1800000));
//        System.out.println("-3-->>" + TimeUtils.checkTimestamp(start + "1528858736043", 1800000));
        System.out.println("-4-->>" + TimeUtils.checkTimestamp("1528858736043", 1800000));
        System.out.println("--->>" + TimeUtils.getTime());
        System.out.println("--->>" + TimeUtils.endOfLastMonth());
    }

    @Test
    public void test02() {
        String start = "2020-06-08 00:00:00";
        String end = "2020-06-10 00:00:00";
        System.out.println("--->>" + TimeUtils.isCrossDay(start, end));
        System.out.println("--->>" + TimeUtils.isCrossDay(start, end, 2));
    }

    @Test
    public void test03() {
        String timeStart = "2019-12-11";
        String timeEnd = "2020-12-20";
        TimeUtils.collectLocalDates(timeStart, timeEnd).forEach(System.out::println);
    }

    @Test
    void test04() {
        String timeStart = "2019-12-11 00:00:00";
        String timeEnd = "2020-12-20 23:59:59";
        long l = TimeUtils.differMilli(timeStart, timeEnd);
        System.out.println(l);
        System.out.println("---1>>" + TimeUtils.getMonth());
        System.out.println("---2>>" + TimeUtils.getDay());
        System.out.println("--3->>" + TimeUtils.isThisYear(12));
    }

    @Test
    void test05() {
        System.out.println("=2=昨日开始日期==>>" + TimeUtils.getYesterdayStartTime());
        System.out.println("=4=昨日结束日期==>>" + TimeUtils.getYesterdayEndTime());
    }

    @Test
    void testTime(){
        System.out.println("--1---当前日期---->>" + TimeUtils.getTime());
        System.out.println("--2---当前日期---->>" + TimeUtils.now());
        System.out.println("--月的第1天--->>" + TimeUtils.firstDayOfMonth());
        System.out.println("----最后1天----->>" + TimeUtils.lastDayOfMonth());
        System.out.println("---上个月的第1天------>>" + TimeUtils.earlyLastMonth());
        System.out.println("---上个月的最后1天------>>" + TimeUtils.endOfLastMonth());
        System.out.println("--在当前系统时间之后-->>" + TimeUtils.exceedSystem("2020-04-17 13:59:59"));
    }
}
