package com.github.hugh.time;

import com.github.hugh.constant.DateCode;
import com.github.hugh.util.DateUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 日期测试类
 */
class DateTest {

    // 测速 cst时间格式转换
    @Test
    void testParse() throws ParseException {
        String strDateObj = "Mon Mar 21 18:02:11 CST 2022";
        assertEquals(28, strDateObj.length());
        SimpleDateFormat sdf = new SimpleDateFormat(DateCode.CST_FORM, Locale.US);
        Date d = sdf.parse(strDateObj);
        assertEquals(1647856931000L, d.getTime());
        Date parse = DateUtils.parseDate(strDateObj, DateCode.CST_FORM);
        assertNotNull(parse);
        assertEquals(parse.getTime(), d.getTime());
    }

    @Test
    void testDate01() {
        String str = "2020-06-04 13:00:21";
        assertTrue(DateUtils.isDateFormat(str));
        assertEquals("Thu Jun 04 00:00:00 CST 2020", DateUtils.parseDate(str).toString());
//        System.out.println("-2-->" + );
        final boolean dateFormat = DateUtils.isDateFormat(DateUtils.getDate(DateCode.YEAR_MONTH_DAY), DateCode.YEAR_MONTH_DAY);
        assertTrue(dateFormat);
//        System.out.println("--3->" + );
//        System.out.println("--4->" + DateUtils.getDate(1));
        assertEquals("20200604130021", DateUtils.toStringTime(str));
//        System.out.println("--5->" + DateUtils.getDate(DateUtils.toStringTime(str)));
//        assertEquals(TimeUtils.getYear() + "" + TimeUtils.getMonth() + "" + TimeUtils.getDay(), DateUtils.getDateSign());
//        System.out.println("--6->" + DateUtils.getDateSign());
        String s = DateUtils.toStringDate("20200604130021");
        assertTrue(DateUtils.isDateFormat(s));
//        System.out.println(s);
        System.out.println("--根据当前时间的一天前的起始时间->" + DateUtils.getDayBeforeStartTime());
    }

    @Test
    void test02() {
        String str = "2020-06-04 13:00:21";
        final Date parse = DateUtils.parse(str);
        assertEquals("2020-06-01 13:00:21", DateUtils.setMonthFirstDay(parse));
        assertEquals("2020-06-30 23:59:59", DateUtils.setMonthLastDay(parse));
//        assertEquals(DateUtils.parseDate() + " 23:59:59", DateUtils.setMonthLastDay(null));
//        System.out.println("-1-->" + DateUtils.setMonthFirstDay(parse));
//        System.out.println("-2-->" + DateUtils.setMonthLastDay(new Date()));
//        System.out.println("-2-null->" + DateUtils.setMonthLastDay(null));
//        System.out.println("-3-->" + DateUtils.getMonthBeforeStartTime());
//        System.out.println("-4-->" + DateUtils.getMonthBeforeEndTime());
        assertEquals(str, DateUtils.ofPattern(parse));
        assertEquals("2020-06-04", DateUtils.format(parse));
    }

    // 测试日期是否是今天
    @Test
    void testIsToday() {
        Date date1 = new Date();
        assertTrue(DateUtils.isToday(date1));
        assertTrue(DateUtils.isToday(DateUtils.ofPattern(date1)));
        assertFalse(DateUtils.isToday(DateUtils.parse("2022-01-03 22:11:22")));
        assertInstanceOf(Date.class, DateUtils.dateStrToDate(date1.toString()));
    }


    @Test
    void test04() {
//        @Test
//        void test04() {
        long remainingMilliSec = DateUtils.getEarlyMorningSec();
        assertNotNull(remainingMilliSec);
        assertTrue(remainingMilliSec > 0);

        Date iniHour = DateUtils.getIniHour();
        assertNotNull(iniHour);

        Date endHour = DateUtils.getEndHour();
        assertNotNull(endHour);

//        assertTrue(endHour.isAfter(iniHour));
//        }
//        System.out.println("-距离凌晨还剩余多少毫秒-->>" + DateUtils.getEarlyMorningSec());
//        System.out.println("--获取小时整点时间->>" + DateUtils.getIniHour());
//        System.out.println("--获取当前小时的结束时间点->>" + DateUtils.getEndHour());
    }

    @Test
    void test05() {
        Date begin = DateUtils.parseTimestamp(1617943680000L);
        Date end = DateUtils.parseTimestamp(1617948600000L);
        String string = "2022-11-10 16:58:57";
        assertEquals(835450, DateUtils.minutesDifference(begin, DateUtils.parse(string)));
//        System.out.println(DateUtils.minutesDifference(begin, DateUtils.parse(string)));
        assertEquals(82, DateUtils.minutesDifference(begin, end));
        assertEquals(4920, DateUtils.secondsDifference(begin, end));
        assertEquals("2021-04-09 12:53:00", DateUtils.format(DateUtils.getMin(begin, 5), DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
//        System.out.println("3--->>" + DateUtils.format(DateUtils.getMin(begin, 5), DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
        Date date = DateUtils.parseDate("2022-01-22 00:00:00");
        assertTrue(DateUtils.checkTimeOut(date, 2));
        assertTrue(DateUtils.checkTimeOut(DateUtils.parseDate("2022-01-22 10:00:00"), 2));
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

    //校验日期对象是否在某个范围天之内
    @Test
    void testIsValidDate() {
        boolean validDate = DateUtils.isValidDate(DateUtils.getDate(-3), 30);
        assertTrue(validDate);
        String timeStart = "2022-05-11 00:00:00";
        assertFalse(DateUtils.isValidDate(DateUtils.parse(timeStart), 3));
    }

    // 验证字符串是否为日期格式
    @Test
    void testIsDateFormat() {
        String timeStart1 = "2022-05-11 00:00:00";
        String timeStart2 = "2022-05-11";
        String timeStart3 = "2022-11";
        String timeStart4 = "Thu Aug 27 18:05:49 CST 2015";
//        String timeStart5 = "2022-05-11 00:00:00";
        assertTrue(DateUtils.isDateFormat(timeStart1, DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
        assertFalse(DateUtils.isDateFormat(timeStart1 + "a", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
        assertTrue(DateUtils.isDateFormat(timeStart2, DateCode.YEAR_MONTH_DAY));
        assertFalse(DateUtils.isDateFormat(timeStart2 + "b", DateCode.YEAR_MONTH_DAY));
        assertTrue(DateUtils.isDateFormat(timeStart3, DateCode.YEAR_MONTH));
        assertFalse(DateUtils.isDateFormat(timeStart3 + "b", DateCode.YEAR_MONTH));
        assertTrue(DateUtils.isDateFormat(timeStart4, DateCode.CST_FORM));
        assertFalse(DateUtils.isDateFormat("Thu Aug 27 18:05:49 _ST 2015", DateCode.CST_FORM));
        assertFalse(DateUtils.isDateFormat("Thu Aug 27 18:05:49 _ST A015", DateCode.CST_FORM));

    }

    // 测试获取指定日期
    @Test
    void testGetDate() {
        Date todayStartTime = DateUtils.getTodayStartTime();
        System.out.println(DateUtils.ofPattern(todayStartTime));
        Date todayEndTime = DateUtils.getTodayEndTime();
//        System.out.println(DateUtils.ofPattern(todayEndTime));
        Date hourAgo = DateUtils.getHourAgo();
        System.out.println(DateUtils.ofPattern(hourAgo));
    }

    @Test
    void testGetOneMonthAgo() {
        // 获取一个月前的日期
        Date oneMonthAgo = DateUtils.getOneMonthAgo();
        // 验证返回的日期是否是一个月前的日期
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date expectedDate = cal.getTime();
        assertEquals(expectedDate, oneMonthAgo);
    }

    @Test
    void testGetWeekAgo() {
        // 创建一个Calendar实例，并将其设置为当前时间
        Calendar calendar = Calendar.getInstance();
        // 获取当前时间
        Date now = new Date();
        calendar.setTime(now);
        // 调用getWeekAgo方法，获取过去七天的日期
        Date weekAgo = DateUtils.getWeekAgo(now);
        // 将Calendar实例的时间设置为一周前
        calendar.add(Calendar.DATE, -7);
        Date expected = calendar.getTime();
        // 验证getWeekAgo方法返回的日期是否等于预期结果
        assertEquals(expected, weekAgo);
    }

    // 测试UTC 转北京时间
    @Test
    void testUtcDate() {
        String rmc = "$GNRMC,063234.00,A,2813.3615,N,11256.4649,E,0.329,,201022,,,A,V*18";
        String date1 = "201022";// UTC 日期，ddmmyy(日月年)格式
        String date2 = "064231.00"; //  UTC 时间，hhmmss(时分秒)格式
        String timeStr = DateUtils.utcToCst(date1 + " " + date2, "ddMMyy HHmmss");
        assertEquals("2022-10-20 14:42:31", timeStr);
    }

    // 测试大于，小于当前日期
    @Test
    void testGreaterThanLessThan() {
        Date start1 = new Date();
        Date end1 = DateUtils.parseDate("2022-10-19 10:00:12");
        boolean b = DateUtils.lessThanStartDate(start1, end1);
        assertTrue(b);
        Date start2 = new Date();
        Date end2 = DateUtils.parseDate("2022-10-19 10:00:12");
        boolean b1 = DateUtils.greaterThanStartDate(start2, end2);
        assertFalse(b1);
        Date start3 = DateUtils.parse("2020-04-01 00:00:00");
        Date end3 = DateUtils.parseDate("2022-11-19 10:00:12");
        boolean b2 = DateUtils.greaterThanStartDate(start3, end3);
        assertTrue(b2);
    }

    @Test
    void testExceedSystem() {
        // 测试日期在当前系统时间之后的情况，应该返回 true
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 一天后
        assertTrue(DateUtils.exceedSystem(futureDate));

        // 测试日期等于当前系统时间的情况，应该返回 false
        Date currentDate = new Date();
        assertFalse(DateUtils.exceedSystem(currentDate));

        // 测试日期在当前系统时间之前的情况，应该返回 false
        Date pastDate = new Date(System.currentTimeMillis() - 86400000); // 一天前
        assertFalse(DateUtils.exceedSystem(pastDate));

        // 测试空日期的情况，应该返回 true
        assertTrue(DateUtils.exceedSystem(null));
    }

    @Test
    void testBelowSystem() {
        // 测试日期在当前系统时间之后的情况，应该返回 false
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 一天后
        assertFalse(DateUtils.belowSystem(futureDate));

        // 测试日期等于当前系统时间的情况，应该返回 false
        Date currentDate = new Date();
        assertFalse(DateUtils.belowSystem(currentDate));

        // 测试日期在当前系统时间之前的情况，应该返回 true
        Date pastDate = new Date(System.currentTimeMillis() - 86400000); // 一天前
        assertTrue(DateUtils.belowSystem(pastDate));

        // 测试空日期的情况，应该返回 true
        assertTrue(DateUtils.belowSystem(null));
    }
}
