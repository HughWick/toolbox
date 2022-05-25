package com.github.hugh.time;

import com.github.hugh.constant.DateCode;
import com.github.hugh.util.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 日期测试类
 */
public class DateTest {

    @Test
    void test01() {
        String str = "2020-06-04 13:00:21";
        System.out.println("--1->" + DateUtils.isDateFormat(str));
        System.out.println("-2-->" + DateUtils.parseDate(str));
        System.out.println("--3->" + DateUtils.getDate("yyyy-MM-dd"));
        System.out.println("--4->" + DateUtils.getDate(1));
        System.out.println("===>>" + DateUtils.toStringTime(str));
//        System.out.println("--5->" + DateUtils.getDate(DateUtils.toStringTime(str)));
        System.out.println("--6->" + DateUtils.getDateSign());
        System.out.println("--根据当前时间的一天前的起始时间->" + DateUtils.getDayBeforeStartTime());
    }

    @Test
    void test02() {
//        String str = "2020-06-04 13:00:21";
        System.out.println("-1-->" + DateUtils.setMonthFirstDay(new Date()));
        System.out.println("-2-->" + DateUtils.setMonthLastDay(new Date()));
        System.out.println("-2-null->" + DateUtils.setMonthLastDay(null));
        System.out.println("-3-->" + DateUtils.getMonthBeforeStartTime());
        System.out.println("-4-->" + DateUtils.getMonthBeforeEndTime());
        System.out.println("-5-->" + DateUtils.ofPattern(new Date()));
        System.out.println("-6-->" + DateUtils.format(new Date()));
        assertEquals("a", "a");
    }

    @Test
    void test03() {
        Date date1 = new Date();
        System.out.println("--1->>" + date1);
        System.out.println("--2->>" + DateUtils.isToday(date1));
        System.out.println("--2->>" + DateUtils.isToday(DateUtils.ofPattern(date1)));
        System.out.println("-3-->>" + DateUtils.dateStrToDate(date1.toString()));
    }

    @Test
    void test04() {
        System.out.println("-距离凌晨还剩余多少毫秒-->>" + DateUtils.getEarlyMorningSec());
        System.out.println("--获取小时整点时间->>" + DateUtils.getIniHour());
        System.out.println("--获取当前小时的结束时间点->>" + DateUtils.getEndHour());
    }

    @Test
    void test05() {
        Date begin = DateUtils.parseTimestamp(1617943680000L);
        Date end = DateUtils.parseTimestamp(1617948600000L);
        System.out.println("--1->>" + DateUtils.minutesDifference(begin, end));
        System.out.println("--2->>" + DateUtils.secondsDifference(begin, end));
        System.out.println("3--->>" + DateUtils.format(DateUtils.getMin(begin, 5), DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));

        Date date = DateUtils.parseDate("2022-01-22 00:00:00");
        boolean b = DateUtils.checkTimeOut(date, 2);
        System.out.println("---4-->>>" + b);
        System.out.println("---5-->>>" + DateUtils.checkTimeOut(DateUtils.parseDate("2022-01-22 10:00:00"), 2));
    }

    @Test
    void testIsAcrossYear() {
        String startStr = "2020-01-04";
        String endStr = "2020-01-04";
        boolean acrossYear = DateUtils.isNotAcrossYear(startStr, endStr);
        assertFalse(acrossYear);
        boolean acrossYear2 = DateUtils.isNotAcrossYear(startStr, "2022-04-04");
        assertTrue(acrossYear2);
    }

    @Test
    void testIsValidDate() {
        String timeStart = "2022-05-11 00:00:00";
        boolean validDate = DateUtils.isValidDate(DateUtils.parse(timeStart), 30);
        assertTrue(validDate);
        assertFalse(DateUtils.isValidDate(DateUtils.parse(timeStart), 3));
    }

    @Test
    void testIsDateFormat() {
        String timeStart = "2022-05-11 00:00:00";
        boolean dateFormat = DateUtils.isDateFormat(timeStart, DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC);
        assertTrue(dateFormat);
        assertFalse(DateUtils.isDateFormat(timeStart + "a", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
    }
}
