package com.github.hugh.time;

import com.github.hugh.constant.DateCode;
import com.github.hugh.util.DateUtils;
import com.github.hugh.util.TimeUtils;
import org.junit.Test;

import java.util.Date;

public class DateTest {

    public static void main(String[] args) {
        System.out.println("-----当前日期---->>" + TimeUtils.getTime());
        System.out.println("==昨日开始日期==>>" + TimeUtils.getStartedYesterday());
        System.out.println("--月的第1天--->>" + TimeUtils.firstDayOfMonth());
        System.out.println("----最后1天----->>" + TimeUtils.lastDayOfMonth());
        System.out.println("---上个月的第1天------>>" + TimeUtils.earlyLastMonth());
        System.out.println("---上个月的最后1天------>>" + TimeUtils.endOfLastMonth());
        System.out.println("--在当前系统时间之后-->>" + TimeUtils.exceedSystem("2020-04-17 13:59:59"));
    }

    @Test
    public void test01() {
        String str = "2020-06-04 13:00:21";
        System.out.println("--->" + DateUtils.isDateFormat(str));
        System.out.println("--->" + DateUtils.parseDate(str));
        System.out.println("--->" + DateUtils.getDate("yyyy-MM-dd"));
        System.out.println("--->" + DateUtils.getDate(1));
        System.out.println("--->" + DateUtils.getDate(DateUtils.toStringTime(str)));
        System.out.println("--->" + DateUtils.getDateSign());
        System.out.println("--根据当前时间的一天前的起始时间->" + DateUtils.getDayBeforeStartTime());
    }

    @Test
    public void test02() {
//        String str = "2020-06-04 13:00:21";
        System.out.println("--->" + DateUtils.setMonthFirstDay(new Date()));
        System.out.println("--->" + DateUtils.setMonthLastDay(new Date()));
        System.out.println("--->" + DateUtils.getMonthBeforeStartTime());
        System.out.println("--->" + DateUtils.getMonthBeforeEndTime());
    }

    @Test
    public void test03() {
        Date date1 = new Date();
        System.out.println("--->>" + date1);
        System.out.println("--->>" + DateUtils.dateStrToDate(date1.toString()));
    }

    @Test
    public void test04() {
        System.out.println("-距离凌晨还剩余多少毫秒-->>" + DateUtils.getEarlyMorningSec());
        System.out.println("--获取小时整点时间->>" + DateUtils.getIniHour());
        System.out.println("--获取当前小时的结束时间点->>" + DateUtils.getEndHour());
    }

    @Test
    public void test05() {
        Date begin = DateUtils.parseTimestamp(1617943680000L);
        Date end = DateUtils.parseTimestamp(1617948600000L);
        System.out.println("--1->>" + DateUtils.minutesDifference(begin, end));
        System.out.println("2--->>" + DateUtils.format(DateUtils.getMin(begin, 5), DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
    }
}
