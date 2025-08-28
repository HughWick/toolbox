package com.github.hugh.util;

import com.github.hugh.constant.DateCode;
import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    // 测试 cst时间格式转换
    @Test
    void testParse() throws ParseException {
        String cstFormDate = "Mon Mar 21 18:02:11 CST 2022";
        assertEquals(28, cstFormDate.length());
        SimpleDateFormat sdf = new SimpleDateFormat(DateCode.CST_FORM, Locale.US);
        Date date1 = sdf.parse(cstFormDate);
        assertEquals(1647856931000L, date1.getTime());
        Date parse2 = DateUtils.parseDate(cstFormDate, DateCode.CST_FORM);
        assertNotNull(parse2);
        assertEquals(parse2.getTime(), date1.getTime());
        String inputDateStr = "Jul 11 2020";
        String date2 = DateUtils.parseDateFormatStr(inputDateStr, "MMM dd yyyy", Locale.US, DateCode.YEAR_MONTH_DAY);
        assertEquals("Jul 11 2020", inputDateStr);
        assertEquals("2020-07-11", date2);

//        String str3 = "NaN-NaN-NaN NaN:NaN:NaN";
//        Date parse3 = DateUtils.parse(str3, "yyyy-MM-dd HH:mm:00");

    }

    @Test
    void parseDate_millisecondFormat() {
        String dateStr = "2023-12-05 10:30:00.123";
        Date expectedDate = new Date(1701743400123L); // 2023-12-05 10:30:00.123 的毫秒值
        Date parsedDate = DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss.SSS");
        assertEquals(expectedDate, parsedDate);
    }

//    @Test
//    void testRandomTime() {
//        // 创建一个随机数生成器
//        Random random = new Random();
//        // 生成随机的小时、分钟和秒
//        int hour = random.nextInt(24); // 0到23之间的随机整数
//        int minute = random.nextInt(60); // 0到59之间的随机整数
//        int second = random.nextInt(60); // 0到59之间的随机整数
//        // 格式化成HH:mm:ss的形式
//        String formattedTime = String.format("%02d:%02d:%02d", hour, minute, second);
//        // 打印随机生成的时间
//        System.out.println("随机生成的时间为: " + formattedTime);
//    }

    @Test
    void testToStringTime() {
        String str = "2020-06-04 13:00:21";
        assertEquals("20200604130021", DateUtils.toStringTime(str));
        assertEquals("", DateUtils.toStringTime(null));
    }

    @Test
    void testDate01() {
        String str = "2020-06-04 13:00:21";
        assertTrue(DateUtils.isDateFormat(str));
        assertEquals("Thu Jun 04 00:00:00 CST 2020", DateUtils.parseDate(str).toString());
        final boolean dateFormat = DateUtils.isDateFormat(DateUtils.getDate(DateCode.YEAR_MONTH_DAY), DateCode.YEAR_MONTH_DAY);
        assertTrue(dateFormat);
//        System.out.println("--4->" + DateUtils.getDate(1));
//        System.out.println("--5->" + DateUtils.getDate(DateUtils.toStringTime(str)));
//        assertEquals(TimeUtils.getYear() + "" + TimeUtils.getMonth() + "" + TimeUtils.getDay(), DateUtils.getDateSign());
//        System.out.println("--6->" + DateUtils.getDateSign());
//        System.out.println(s);
//        System.out.println("--根据当前时间的一天前的起始时间->" + DateUtils.getDayBeforeStartTime());
    }

    @Test
    void testToStrDate() {
        String s = DateUtils.toStringDate("20200604130021");
        assertEquals("2020-06-04 13:00:21", s);
        assertTrue(DateUtils.isDateFormat(s));
        assertNull(DateUtils.toStringDate(null));
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
        Date parseData1 = DateUtils.parse(date1.toString(), DateCode.CST_FORM);
        assertNotNull(parseData1);
        assertInstanceOf(Date.class, parseData1);
//        assertInstanceOf(Date.class, DateUtils.dateStrToDate(date1.toString()));
    }


    @Test
    void testEarlyMorningSec() {
        long remainingMilliSec = DateUtils.getEarlyMorningSec();
        assertNotNull(remainingMilliSec);
        assertTrue(remainingMilliSec > 0);
    }

    /**
     * 测试getIniHour方法：验证返回的是当日当前小时的起始时间，分钟、秒、毫秒都应该为0。
     */
    @Test
    void testGetIniHour() {
        // 获取当前日期时间
        Date result = DateUtils.getIniHour();

        // 使用Calendar对比
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);

        // 验证分钟、秒和毫秒是否都为0
        assertEquals(0, calendar.get(Calendar.MINUTE));
        assertEquals(0, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));

        // 验证年份、月份、日期和小时与当前时间相同
        Calendar now = Calendar.getInstance();
        assertEquals(now.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        assertEquals(now.get(Calendar.MONTH), calendar.get(Calendar.MONTH));
        assertEquals(now.get(Calendar.DATE), calendar.get(Calendar.DATE));
        assertEquals(now.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.HOUR_OF_DAY));
    }

    /**
     * 测试getEndHour方法：验证返回的是当日当前小时的结束时间，分钟、秒、毫秒都应该是最大值（59, 59, 999）。
     */
    @Test
    void testGetEndHour() {
        // 获取当前日期时间
        Date result = DateUtils.getEndHour();

        // 使用Calendar对比
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);

        // 验证分钟、秒和毫秒是否为59和999
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(999, calendar.get(Calendar.MILLISECOND));

        // 验证年份、月份、日期和小时与当前时间相同
        Calendar now = Calendar.getInstance();
        assertEquals(now.get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        assertEquals(now.get(Calendar.MONTH), calendar.get(Calendar.MONTH));
        assertEquals(now.get(Calendar.DATE), calendar.get(Calendar.DATE));
        assertEquals(now.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    void testFormat() {
        Date begin = DateUtils.parseTimestamp(1617943680000L);
        assertEquals("2021-04-09 12:53:00", DateUtils.format(DateUtils.getMin(begin, 5), DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
        assertNull(DateUtils.format(null, null));
        assertEquals("2021-04-09", DateUtils.format(begin, null));
    }

    // 测试 getDate() 方法是否返回正确的日期格式
    @Test
    void testGetDateFormat() {
        String result = DateUtils.getDate();
        // 获取当前日期并格式化为期望的格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String expectedDate = sdf.format(new Date());
        assertEquals(expectedDate, result, "getDate() should return the current date in 'yyyy-MM-dd' format.");
    }

    // 测试 getDate() 是否返回当前日期（不包括时间）
    @Test
    void testGetCurrentDate() {
        // 获取当前日期，去掉时分秒的部分
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        String result = DateUtils.getDate();
        // 比较两个日期字符串
        assertEquals(currentDate, result, "getDate() should return the current date.");
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
        assertNull(DateUtils.parseTimestamp(-1));
//        System.out.println("3--->>" + DateUtils.format(DateUtils.getMin(begin, 5), DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
    }

    @Test
    public void testCheckTimeOut_NullDate() {
        Date date = null;
        int hour = 1;
        assertTrue(DateUtils.checkTimeOut(date, hour), "Expected true when the date is null");
    }

    @Test
    public void testCheckTimeOut_HourLessThanOrEqualZero() {
        Date date = new Date();
        assertTrue(DateUtils.checkTimeOut(date, 0), "Expected true when the hour is less than or equal to 0");
        assertTrue(DateUtils.checkTimeOut(date, -1), "Expected true when the hour is negative");
    }

    @Test
    public void testCheckTimeOut_WithinTimeLimit() {
        Date date = new Date(System.currentTimeMillis() - 30 * 60 * 1000); // 30 minutes ago
        int hour = 1;
        assertFalse(DateUtils.checkTimeOut(date, hour), "Expected false when the time difference is within the limit of 1 hour");
    }

    @Test
    public void testCheckTimeOut_ExceedsTimeLimit() {
        Date date = new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000); // 2 hours ago
        int hour = 1;
        assertTrue(DateUtils.checkTimeOut(date, hour), "Expected true when the time exceeds the limit of 1 hour");
    }

    @Test
    public void testCheckTimeOut_ExceedsTimeLimitWithHigherHours() {
        Date date = new Date(System.currentTimeMillis() - 5 * 60 * 60 * 1000); // 5 hours ago
        int hour = 3;
        assertTrue(DateUtils.checkTimeOut(date, hour), "Expected true when the time exceeds the limit of 3 hours");
    }

    @Test
    public void testCheckTimeOut_ExactlyOneHour() {
        Date date = new Date(System.currentTimeMillis() - 1 * 60 * 60 * 1000); // 1 hour ago
        int hour = 1;
        assertFalse(DateUtils.checkTimeOut(date, hour), "Expected false when the time is exactly 1 hour ago");
    }

    @Test
    void testCheckTimeOut_ExactlyTwoHours() {
        Date date = new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000); // 2 hours ago
        int hour = 2;
        assertFalse(DateUtils.checkTimeOut(date, hour), "Expected false when the time is exactly 2 hours ago with 2-hour limit");
    }

    @Test
    void testCheckTimeOut_HigherHourLimit() {
        Date date = new Date(System.currentTimeMillis() - (60 * 60 * 1000)); // 1 hour ago
        int hour = 2;
        assertFalse(DateUtils.checkTimeOut(date, hour), "Expected true when the time is 1 hour ago but the limit is 2 hours");
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
//        String timeStart3 = "Sep 13 2023 09:02:23";
//        String timeStart5 = "2022-05-11 00:00:00";
        assertTrue(DateUtils.isDateFormat(timeStart1, DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
        assertFalse(DateUtils.isDateFormat(timeStart1 + "a", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));
        assertTrue(DateUtils.isDateFormat(timeStart2, DateCode.YEAR_MONTH_DAY));
        assertFalse(DateUtils.isDateFormat(timeStart2 + "b", DateCode.YEAR_MONTH_DAY));
        String timeStart3 = "2022-11";
        assertTrue(DateUtils.isDateFormat(timeStart3, DateCode.YEAR_MONTH));
        assertFalse(DateUtils.isDateFormat(timeStart3 + "b", DateCode.YEAR_MONTH));
        String timeStart4 = "Thu Aug 27 18:05:49 CST 2015";
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
    void getMonthAgo_validDateAndPositiveMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.NOVEMBER, 15); // Set date to 2023-11-15
        Date initialDate = calendar.getTime();
        int monthsAgo = 3;
        Date result = DateUtils.getMonthAgo(initialDate, monthsAgo);
        calendar.add(Calendar.MONTH, -monthsAgo);
        Date expectedDate = calendar.getTime();

        // Compare year, month, and day for simplicity
        Calendar resultCalendar = Calendar.getInstance();
        resultCalendar.setTime(result);
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.setTime(expectedDate);

        assertEquals(expectedCalendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR));
        assertEquals(expectedCalendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH));
        assertEquals(expectedCalendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.DAY_OF_MONTH));
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

    @Test
    void testFormatTimestamp() {
        // 秒
        String str1 = "1697513319";
        String s1 = DateUtils.formatTimestampSecond(str1);
        assertEquals("2023-10-17 11:28:39", s1);
        long str2 = 1697511216L;
        String s2 = DateUtils.formatTimestampSecond(str2);
        assertEquals("2023-10-17 10:53:36", s2);
        String s3 = DateUtils.formatTimestampSecond(str1, DateCode.YEAR_MONTH_DAY);
        assertEquals("2023-10-17", s3);
        long str4 = 1697511216L;
        String s4 = DateUtils.formatTimestampSecond(str4, DateCode.YEAR_MONTH_DAY_SIMPLE);
        assertEquals("20231017", s4);
        // 毫秒
        String timeStr1 = "1697514229199";
        String timestamp1 = DateUtils.formatTimestamp(timeStr1);
        assertEquals("2023-10-17 11:43:49", timestamp1);
        long timeStr2 = 1697514508199L;
        String timestamp2 = DateUtils.formatTimestamp(timeStr2);
        assertEquals("2023-10-17 11:48:28", timestamp2);

    }

    @Test
    void testIsTimestampInMilli() {
        String str1 = "1713152121000";
        String str2 = "1713152121";
        long str3 = 1713152121L;
        assertTrue(DateUtils.isTimestampInMilli(str1));
        assertFalse(DateUtils.isTimestampInMilli(str3));
        assertFalse(DateUtils.isTimestampInMilli(str2));
        String str4 = "1713152121";
        assertTrue(DateUtils.isTimestamp(str4));


    }

    @Test
    void getStartDate_NullInput_ReturnsNull() {
        Date result = DateUtils.getStartDate(null);
        assertNull(result, "Expected null for null input");
    }

    @Test
    void getStartDate_ValidDate_ReturnsStartOfDay() {
        // 设置一个特定的日期用于测试
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.OCTOBER, 15, 14, 30, 45);
        calendar.set(Calendar.MILLISECOND, 500);
        Date date = calendar.getTime();
        Date result = DateUtils.getStartDate(date);
        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.setTime(date);
        expectedCalendar.set(Calendar.HOUR_OF_DAY, 0);
        expectedCalendar.set(Calendar.MINUTE, 0);
        expectedCalendar.set(Calendar.SECOND, 0);
        expectedCalendar.set(Calendar.MILLISECOND, 0);
        Date expectedDate = expectedCalendar.getTime();
        assertEquals(expectedDate, result, "Expected start of day date");
    }


    @Test
    void getEndDate_NullInput_ReturnsNull() {
        Date result = DateUtils.getEndDate(null);
        assertNull(result, "Expected null for null input");
    }

    @Test
    void getEndDate_ValidDate_ReturnsEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 1, 12, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        Date result = DateUtils.getEndDate(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(result);
        assertEquals(23, calendar2.get(Calendar.HOUR_OF_DAY), "Expected hour to be 23");
        assertEquals(59, calendar2.get(Calendar.MINUTE), "Expected minute to be 59");
        assertEquals(59, calendar2.get(Calendar.SECOND), "Expected second to be 59");
        assertEquals(999, calendar2.get(Calendar.MILLISECOND), "Expected millisecond to be 999");
    }

    @Test
    void parseDate_EmptyString_ReturnsNull() {
        Locale locale = Locale.getDefault();
        Date result = DateUtils.parseDate("", DateCode.YEAR_MONTH_DAY, locale);
        assertNull(result);
    }

    @Test
    void parseDate_MatchesSpecificPattern1_ReturnsDate() throws ParseException {
        Locale locale = Locale.getDefault();
        String dateStr = "2023-10-05 14:30:00.123";
        Date expectedDate = new SimpleDateFormat(DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS).parse(dateStr);
        Date result = DateUtils.parseDate(dateStr, DateCode.YEAR_MONTH_DAY, locale);
        assertEquals(expectedDate, result);
    }

    @Test
    void parseDate_MatchesSpecificPattern2_ReturnsDate() throws ParseException {
//        Locale locale = Locale.getDefault();
        String dateStr = "Thu Oct 05 14:30:00 CST 2023";
        Date expectedDate = new SimpleDateFormat(DateCode.CST_FORM, Locale.US).parse(dateStr);
        Date result = DateUtils.parseDate(dateStr, DateCode.YEAR_MONTH_DAY, Locale.US);
        assertEquals(expectedDate, result);
    }

    @Test
    void parseDate_UsesProvidedFormat_ReturnsDate() throws ParseException {
        Locale locale = Locale.getDefault();
        String dateStr = "2023-10-05";
        Date expectedDate = new SimpleDateFormat(DateCode.YEAR_MONTH_DAY).parse(dateStr);
        Date result = DateUtils.parseDate(dateStr, DateCode.YEAR_MONTH_DAY, locale);
        assertEquals(expectedDate, result);
    }

    @Test
    void parseDate_InvalidDate_ThrowsToolboxException() {
        Locale locale = Locale.getDefault();
        String dateStr = "invalid-date";
        assertThrows(ToolboxException.class, () -> DateUtils.parseDate(dateStr, DateCode.YEAR_MONTH_DAY, locale));
    }

    /**
     * 辅助方法：创建一个指定年、月、日的Date对象
     * 注意：Calendar.MONTH 是 0-based (0 for January)
     */
    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0); // month - 1 因为 Calendar.MONTH 是 0-based
        calendar.set(Calendar.MILLISECOND, 0); // 清除毫秒，确保比较精确
        return calendar.getTime();
    }
    /**
     * 辅助方法：创建一个指定年、月、日、时、分、秒、毫秒的Date对象
     * 注意：Calendar.MONTH 是 0-based (0 for January)
     */
    private Date createDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second); // month - 1 因为 Calendar.MONTH 是 0-based
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }

    @Test
    @DisplayName("shouldReturnTrueWhenDatesAreInSameYearAndMonth")
    void testIsSameMonth_SameYearSameMonth() {
        Date date1 = createDate(2023, 3, 10); // 2023年3月10日
        Date date2 = createDate(2023, 3, 25); // 2023年3月25日
        assertTrue(DateUtils.isSameMonth(date1, date2), "在同一年同一个月时应返回true");
    }

    @Test
    @DisplayName("shouldReturnFalseWhenMonthsAreDifferentInSameYear")
    void testIsSameMonth_SameYearDifferentMonth() {
        Date date1 = createDate(2023, 3, 10); // 2023年3月
        Date date2 = createDate(2023, 4, 15); // 2023年4月
        assertFalse(DateUtils.isSameMonth(date1, date2), "在同一年但不同月时应返回false");
    }

    @Test
    @DisplayName("shouldReturnFalseWhenYearsAreDifferent")
    void testIsSameMonth_DifferentYears() {
        Date date1 = createDate(2023, 3, 10); // 2023年3月
        Date date2 = createDate(2024, 3, 20); // 2024年3月
        assertFalse(DateUtils.isSameMonth(date1, date2), "在不同年时应返回false");
    }

    @Test
    @DisplayName("shouldReturnFalseWhenMonthsAndYearsAreDifferent")
    void testIsSameMonth_DifferentYearsAndMonths() {
        Date date1 = createDate(2023, 3, 10); // 2023年3月
        Date date2 = createDate(2024, 5, 20); // 2024年5月
        assertFalse(DateUtils.isSameMonth(date1, date2), "在不同年不同月时应返回false");
    }

    @Test
    @DisplayName("shouldReturnTrueWhenDatesAreAtMonthBoundary")
    void testIsSameMonth_MonthBoundary() {
        Date date1 = createDate(2023, 1, 1); // 2023年1月1日
        Date date2 = createDate(2023, 1, 31); // 2023年1月31日
        assertTrue(DateUtils.isSameMonth(date1, date2), "在月份边界也应返回true");
    }

    @Test
    @DisplayName("shouldReturnTrueWhenDatesAreAtYearBoundary")
    void testIsSameMonth_YearBoundary() {
        Date date1 = createDate(2023, 12, 15); // 2023年12月
        Date date2 = createDate(2023, 12, 30); // 2023年12月
        assertTrue(DateUtils.isSameMonth(date1, date2), "在年份边界（同年12月）也应返回true");
    }

    @Test
    @DisplayName("shouldReturnFalseWhenDatesCrossYearBoundary")
    void testIsSameMonth_AcrossYearBoundary() {
        Date date1 = createDate(2023, 12, 25); // 2023年12月
        Date date2 = createDate(2024, 1, 5);  // 2024年1月
        assertFalse(DateUtils.isSameMonth(date1, date2), "跨越年份时应返回false");
    }

    @Test
    @DisplayName("shouldReturnFalseWhenBeginDateIsNull")
    void testIsSameMonth_BeginDateIsNull() {
        Date endDate = createDate(2023, 3, 10);
        assertFalse(DateUtils.isSameMonth(null, endDate), "起始日期为null时应返回false");
        // 如果你的业务逻辑是抛出异常，则使用 assertThrows
        // assertThrows(IllegalArgumentException.class, () -> DateUtils.isSameMonth(null, endDate));
    }

    @Test
    @DisplayName("shouldReturnFalseWhenEndDateIsNull")
    void testIsSameMonth_EndDateIsNull() {
        Date beginDate = createDate(2023, 3, 10);
        assertFalse(DateUtils.isSameMonth(beginDate, null), "结束日期为null时应返回false");
        // 如果你的业务逻辑是抛出异常，则使用 assertThrows
        // assertThrows(IllegalArgumentException.class, () -> DateUtils.isSameMonth(beginDate, null));
    }

    @Test
    @DisplayName("shouldReturnFalseWhenBothDatesAreNull")
    void testIsSameMonth_BothDatesAreNull() {
        assertFalse(DateUtils.isSameMonth(null, null), "两个日期都为null时应返回false");
        // 如果你的业务逻辑是抛出异常，则使用 assertThrows
        // assertThrows(IllegalArgumentException.class, () -> DateUtils.isSameMonth(null, null));
    }

    @Test
    @DisplayName("shouldHandleLeapYearCorrectly")
    void testIsSameMonth_LeapYear() {
        Date date1 = createDate(2024, 2, 15); // 闰年2月
        Date date2 = createDate(2024, 2, 29); // 闰年2月
        assertTrue(DateUtils.isSameMonth(date1, date2), "闰年2月也应返回true");
        Date date3 = createDate(2024, 3, 1); // 闰年3月
        assertFalse(DateUtils.isSameMonth(date1, date3), "闰年跨月应返回false");
    }

    /**
     * 辅助方法：验证日期是否精确到毫秒
     * @param expectedCal 期望的日历对象
     * @param actualDate 实际的Date对象
     */
    private void assertDateEquals(Calendar expectedCal, Date actualDate) {
        Calendar actualCal = Calendar.getInstance();
        actualCal.setTime(actualDate);

        assertEquals(expectedCal.get(Calendar.YEAR), actualCal.get(Calendar.YEAR), "年份不一致");
        assertEquals(expectedCal.get(Calendar.MONTH), actualCal.get(Calendar.MONTH), "月份不一致");
        assertEquals(expectedCal.get(Calendar.DAY_OF_MONTH), actualCal.get(Calendar.DAY_OF_MONTH), "天不一致");
        assertEquals(expectedCal.get(Calendar.HOUR_OF_DAY), actualCal.get(Calendar.HOUR_OF_DAY), "小时不一致");
        assertEquals(expectedCal.get(Calendar.MINUTE), actualCal.get(Calendar.MINUTE), "分钟不一致");
        assertEquals(expectedCal.get(Calendar.SECOND), actualCal.get(Calendar.SECOND), "秒不一致");
        // 允许毫秒有微小差异，因为Date.from(Instant)转换时可能导致毫秒精度丢失或差异
        // 或者直接比较，如果你的系统对毫秒精度要求极高
        // assertEquals(expectedCal.get(Calendar.MILLISECOND), actualCal.get(Calendar.MILLISECOND), "毫秒不一致");
        assertTrue(Math.abs(expectedCal.get(Calendar.MILLISECOND) - actualCal.get(Calendar.MILLISECOND)) < 2, "毫秒不一致，允许微小误差");
    }

    // --- getMonthEndTime (原始方法) 测试 ---

    @ParameterizedTest
    @CsvSource({
            "2023, 1, 15, 2023, 1, 31",   // 普通月份
            "2024, 2, 10, 2024, 2, 29",   // 闰年2月
            "2023, 2, 10, 2023, 2, 28",   // 非闰年2月
            "2023, 4, 5,  2023, 4, 30",   // 小月
            "2023, 12, 1, 2023, 12, 31",  // 年底月份
            "2023, 1, 1,  2023, 1, 31"    // 年初月份
    })
    @DisplayName("getMonthEndTime (原始方法): 各种普通日期")
    void testGetMonthEndTime_OriginalMethod_VariousDates(int inputYear, int inputMonth, int inputDay,
                                                         int expectedYear, int expectedMonth, int expectedDay) {
        Date inputDate = createDate(inputYear, inputMonth, inputDay, 10, 30, 45, 123);
        Date resultDate = DateUtils.getMonthEndTime(inputDate);

        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.set(expectedYear, expectedMonth - 1, expectedDay, 23, 59, 59);
        expectedCalendar.set(Calendar.MILLISECOND, 999);

        assertNotNull(resultDate);
        assertDateEquals(expectedCalendar, resultDate);
    }

    @Test
    @DisplayName("getMonthEndTime (原始方法): 输入日期为null应返回null")
    void testGetMonthEndTime_OriginalMethod_NullInput() {
        assertNull(DateUtils.getMonthEndTime(null));
    }

    // --- getMonthEndTimeOptimized (优化方法) 测试 ---

    @ParameterizedTest
    @CsvSource({
            "2023, 1, 15, 2023, 1, 31",   // 普通月份
            "2024, 2, 10, 2024, 2, 29",   // 闰年2月
            "2023, 2, 10, 2023, 2, 28",   // 非闰年2月
            "2023, 4, 5,  2023, 4, 30",   // 小月
            "2023, 12, 1, 2023, 12, 31",  // 年底月份
            "2023, 1, 1,  2023, 1, 31"    // 年初月份
    })
    @DisplayName("getMonthEndTimeOptimized (优化方法): 各种普通日期")
    void testGetMonthEndTime_OptimizedMethod_VariousDates(int inputYear, int inputMonth, int inputDay,
                                                          int expectedYear, int expectedMonth, int expectedDay) {
        Date inputDate = createDate(inputYear, inputMonth, inputDay, 10, 30, 45, 123);
        Date resultDate = DateUtils.getMonthEndTime(inputDate);

        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.set(expectedYear, expectedMonth - 1, expectedDay, 23, 59, 59);
        expectedCalendar.set(Calendar.MILLISECOND, 999);

        assertNotNull(resultDate);
        assertDateEquals(expectedCalendar, resultDate);
    }

    @Test
    @DisplayName("getMonthEndTimeOptimized (优化方法): 输入日期为null应返回null")
    void testGetMonthEndTime_OptimizedMethod_NullInput() {
        assertNull(DateUtils.getMonthEndTime(null));
    }

    @Test
    @DisplayName("getMonthEndTimeOptimized (优化方法): 检查毫秒精度")
    void testGetMonthEndTime_OptimizedMethod_MillisecondPrecision() {
        // 创建一个精确到毫秒的日期
        Date inputDate = createDate(2023, 7, 14, 12, 0, 0, 500);
        Date resultDate = DateUtils.getMonthEndTime(inputDate);

        Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.set(2023, Calendar.JULY, 31, 23, 59, 59);
        expectedCalendar.set(Calendar.MILLISECOND, 999);

        assertNotNull(resultDate);
        // Date.from(Instant) 转换时可能会截断或四舍五入毫秒，所以通常不直接 ==
        // 这里验证是否在预期值附近，或者直接比较时间戳
        assertEquals(expectedCalendar.getTimeInMillis() / 1000, resultDate.getTime() / 1000, "秒级精度应一致");
        // 如果你希望精确到毫秒，你需要确保你的Java版本和JVM能够支持所有9位纳秒精度
        // 对于java.util.Date，它只能精确到毫秒。
        // 所以这里检查毫秒级精度即可。
        assertEquals(expectedCalendar.get(Calendar.MILLISECOND), actualCalForMsPrecision(resultDate).get(Calendar.MILLISECOND), "毫秒精度应为999");
    }

    private Calendar actualCalForMsPrecision(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
