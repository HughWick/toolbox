package com.github.hugh.util;

import com.github.hugh.constant.DateCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.support.TimeoutOperation;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    // 测试 parseTime 方法，使用默认格式 YEAR_MONTH_DAY_HOUR_MIN_SEC
    @Test
    void testParseTimeWithDefaultFormat() {
        String timeStr = "2025-01-20T09:26:09";
//        LocalDateTime result = TimeUtils.parseTime(timeStr);
        ToolboxException exception = assertThrows(ToolboxException.class, () -> {
            TimeUtils.parseTime(timeStr);
        });
        assertEquals("java.time.format.DateTimeParseException: Text '2025-01-20T09:26:09' could not be parsed at index 10", exception.getMessage());
    }

    // 测试 parseTime 方法，使用指定格式
    @Test
    void testParseTimeWithCustomFormat() {
        String timeStr = "2025-01-20 09:26:09";
        String format = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime result = TimeUtils.parseTime(timeStr, format);
        assertNotNull(result);
        assertEquals(2025, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(20, result.getDayOfMonth());
        assertEquals(9, result.getHour());
        assertEquals(26, result.getMinute());
        assertEquals(9, result.getSecond());
    }

    // 测试 parseTime 方法，格式不匹配时返回 null
    @Test
    void testParseTimeWithInvalidFormat() {
        String timeStr = "2025-01-20T09:26:09";
        String format = "yyyy-MM-dd'T'HH:mm:ss";
        LocalDateTime result = TimeUtils.parseTime(timeStr, format);
        assertNotNull(result); // 格式不匹配，应该返回 null
    }

    // 测试 getYear 方法
    @Test
    void testGetYear() {
        int year = TimeUtils.getYear();
        assertEquals(LocalDateTime.now().getYear(), year); // 应该返回当前年份
    }

    // 测试 isThisYear 方法，年份一致时返回 true
    @Test
    void testIsThisYearWithSameYear() {
        int currentYear = LocalDateTime.now().getYear();
        boolean result = TimeUtils.isThisYear(currentYear);
        assertTrue(result); // 当前年份与传入年份一致，应该返回 true
    }

    // 测试 isThisYear 方法，年份不一致时返回 false
    @Test
    void testIsThisYearWithDifferentYear() {
        int currentYear = LocalDateTime.now().getYear();
        boolean result = TimeUtils.isThisYear(currentYear + 1); // 假设下一年
        assertFalse(result); // 当前年份与传入年份不一致，应该返回 false
    }

    // 测试 getMonth 方法
    @Test
    void testGetMonth() {
        int month = TimeUtils.getMonth();
        assertEquals(LocalDateTime.now().getMonthValue(), month); // 应该返回当前月份
    }

    // 测试 getDay 方法
    @Test
    void testGetDay() {
        int day = TimeUtils.getDay();
        assertEquals(LocalDateTime.now().getDayOfMonth(), day); // 应该返回当前日期
    }

    // 测试 getHour 方法
    @Test
    void testGetHour() {
        int hour = TimeUtils.getHour();
        assertEquals(LocalDateTime.now().getHour(), hour); // 应该返回当前小时
    }

    // 测试 getMinute 方法
    @Test
    void testGetMinute() {
        int minute = TimeUtils.getMinute();
        assertEquals(LocalDateTime.now().getMinute(), minute); // 应该返回当前分钟
    }

    // 测试 getSecond 方法
    @Test
    void testGetSecond() {
        int second = TimeUtils.getSecond();
        assertEquals(LocalDateTime.now().getSecond(), second); // 应该返回当前秒
    }

    // 测试获取昨日时间
    @Test
    void test05() {
        Date date = DateUtils.getDayBeforeStartTime();
        assertEquals(DateUtils.ofPattern(date), TimeUtils.getYesterdayStartTime());
        System.out.println("=4=昨日结束日期==>>" + TimeUtils.getYesterdayEndTime());
    }

    @Test
    void getDayBeforeEndTime_ShouldReturnYesterdayEnd() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date  expectedYesterdayEnd = calendar.getTime();
        Date yesterdayEnd = DateUtils.getDayBeforeEndTime();
        assertEquals(expectedYesterdayEnd, yesterdayEnd, "The end time of yesterday should be calculated correctly.");
    }

    @Test
    void testTime() {
        assertEquals(TimeUtils.getTime(), TimeUtils.now());
//        System.out.println("--1---当前日期---->>" + TimeUtils.getTime());
//        System.out.println("--2---当前日期---->>" + TimeUtils.now());
//        System.out.println("--月的第1天--->>" + TimeUtils.firstDayOfMonth());
//        System.out.println("----最后1天----->>" + TimeUtils.lastDayOfMonth());
//        System.out.println("---上个月的第1天------>>" + TimeUtils.earlyLastMonth());
//        System.out.println("---上个月的最后1天------>>" + TimeUtils.endOfLastMonth());
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
    // 测试 collectLocalDates 方法，正常日期范围
    @Test
    void testCollectLocalDatesNormalRange() {
        String start = "2025-01-01";
        String end = "2025-01-05";
        List<String> result = TimeUtils.collectLocalDates(start, end);
        assertNotNull(result);
        assertEquals(5, result.size()); // 结果应该包含 5 个日期
        assertEquals("2025-01-01", result.get(0));
        assertEquals("2025-01-05", result.get(4));
    }

    // 测试 collectLocalDates 方法，起始和结束日期相同
    @Test
    void testCollectLocalDatesSameDate() {
        String start = "2025-01-01";
        String end = "2025-01-01";
        List<String> result = TimeUtils.collectLocalDates(start, end);
        assertNotNull(result);
        assertEquals(1, result.size()); // 结果应该只包含 1 个日期
        assertEquals("2025-01-01", result.get(0));
    }

    // 测试 collectLocalDates 方法，跨越闰年
    @Test
    void testCollectLocalDatesLeapYear() {
        String start = "2024-02-28"; // 闰年
        String end = "2024-03-01";
        List<String> result = TimeUtils.collectLocalDates(start, end);
        assertNotNull(result);
        assertEquals(3, result.size()); // 结果应该包含 3 个日期
        assertEquals("2024-02-28", result.get(0));
        assertEquals("2024-03-01", result.get(2));
    }

    // 测试 collectLocalDates 方法，跨越一个月
    @Test
    void testCollectLocalDatesOneMonth() {
        String start = "2025-01-30";
        String end = "2025-02-02";
        List<String> result = TimeUtils.collectLocalDates(start, end);
        assertNotNull(result);
        assertEquals(4, result.size()); // 结果应该包含 4 个日期
        assertEquals("2025-01-30", result.get(0));
        assertEquals("2025-02-02", result.get(3));
    }

    // 测试 collectLocalDates 方法，日期逆序时返回空
    @Test
    void testCollectLocalDatesReverseOrder() {
        String start = "2025-01-05";
        String end = "2025-01-01";
        // 断言抛出 IllegalArgumentException 异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            TimeUtils.collectLocalDates(start, end);
        });
        assertEquals("End date cannot be before start date", exception.getMessage());
    }

    // 测试开始时间和结束时间完全相同
    @Test
    void testEqualTimes() {
        String startTime = "2025-01-20 09:26:09";
        String endTime = "2025-01-20 09:26:09";
        long result = TimeUtils.differMilli(startTime, endTime);
        assertEquals(0, result, "开始时间和结束时间相同，返回0毫秒");
    }

    // 测试开始时间比结束时间早
    @Test
    void testStartBeforeEnd() {
        String startTime = "2025-01-20 09:00:00";
        String endTime = "2025-01-20 09:30:00";
        long result = TimeUtils.differMilli(startTime, endTime);
        assertEquals(1800000, result, "开始时间比结束时间早，返回相差的毫秒数");
    }

    // 测试开始时间比结束时间晚
    @Test
    void testStartAfterEnd() {
        String startTime = "2025-01-20 10:00:00";
        String endTime = "2025-01-20 09:30:00";
        long result = TimeUtils.differMilli(startTime, endTime);
        assertEquals(-1800000, result, "开始时间比结束时间晚，返回相差的负毫秒数");
    }

    // 测试开始时间或结束时间为空
    @Test
    void testEmptyTime() {
        String startTime = "2025-01-20 09:30:00";
        String endTime = "";
        long result = TimeUtils.differMilli(startTime, endTime);
        assertEquals(-1, result, "传入空的时间，应该返回-1");
    }

    // 测试无效时间格式
    @Test
    void testInvalidTimeFormat() {
        String startTime = "2025-01-20 09:30:00";
        String endTime = "2025-01-20 09:30:60"; // 不合法的秒数
//        long result = TimeUtils.differMilli(startTime, endTime);
        ToolboxException exception = assertThrows(ToolboxException.class, () -> {
            TimeUtils.differMilli(startTime, endTime);
        });
        assertEquals("java.time.format.DateTimeParseException: Text '2025-01-20 09:30:60' could not be parsed: Invalid value for SecondOfMinute (valid values 0 - 59): 60", exception.getMessage());
    }

    // 测试跨天的时间差
    @Test
    public void testCrossDay() {
        String startTime = "2025-01-20 23:59:59";
        String endTime = "2025-01-21 00:00:01";
        long result = TimeUtils.differMilli(startTime, endTime);
        assertEquals(2000, result, "跨天的时间差，返回相差的毫秒数");
    }

    // 测试相差几秒钟的时间
    @Test
    public void testFewSeconds() {
        String startTime = "2025-01-20 09:30:00";
        String endTime = "2025-01-20 09:30:10";
        long result = TimeUtils.differMilli(startTime, endTime);
        assertEquals(10000, result, "开始和结束时间相差10秒，返回10000毫秒");
    }

    @Test
    public void testFirstDayOfMonth() {
        String firstDay = TimeUtils.firstDayOfMonth();
        // 获取当前时间的年月日
        String expectedDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay() // 获取当天最早时间（00:00:00）
                .format(DateTimeFormatter.ofPattern(DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));

        // 断言返回的日期是否与预期相符
        assertEquals(expectedDate, firstDay);
    }

    @Test
    public void testLastDayOfMonth() {
        String lastDay = TimeUtils.lastDayOfMonth();

        // 获取当前时间的年月日
        String expectedDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
                .atTime(LocalTime.MAX) // 获取当天最晚时间（23:59:59.999999999）
                .format(DateTimeFormatter.ofPattern(DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));

        // 断言返回的日期是否与预期相符
        assertEquals(expectedDate, lastDay);
    }

    @Test
    public void testEndOfLastMonth() {
        String lastMonthEnd = TimeUtils.endOfLastMonth();

        // 计算上个月的最后一天
        String expectedDate = LocalDate.now().minusMonths(1) // 获取上个月
                .with(TemporalAdjusters.lastDayOfMonth()) // 获取上月的最后一天
                .atTime(LocalTime.MAX) // 设置为当天的最晚时间
                .format(DateTimeFormatter.ofPattern(DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC));

        // 断言返回的日期是否与预期相符
        assertEquals(expectedDate, lastMonthEnd);
    }
    @Test
    void testEndOfLastMonthCustomFormat() {
        String lastMonthEndCustomFormat = TimeUtils.endOfLastMonth("yyyy/MM/dd HH:mm:ss");
        // 计算上个月的最后一天，使用自定义格式
        String expectedDate = LocalDate.now().minusMonths(1) // 获取上个月
                .with(TemporalAdjusters.lastDayOfMonth()) // 获取上月的最后一天
                .atTime(LocalTime.MAX) // 设置为当天的最晚时间
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        // 断言返回的日期是否与预期相符
        assertEquals(expectedDate, lastMonthEndCustomFormat);
    }

    @Test
    public void testEarlyLastMonthDefaultFormat() {
        String earlyLastMonth = TimeUtils.earlyLastMonth();

        // 计算上个月的第一天的日期和时间
        String expectedDate = LocalDate.now().minusMonths(1) // 获取上个月
                .with(TemporalAdjusters.firstDayOfMonth()) // 获取上个月的第一天
                .atStartOfDay() // 设置为00:00:00时刻
                .format(DateTimeFormatter.ofPattern(DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC)); // 使用默认格式
        // 断言返回的日期格式是否与预期相符
        assertEquals(expectedDate, earlyLastMonth);
    }

    @Test
    public void testEarlyLastMonthCustomFormat() {
        String earlyLastMonthCustomFormat = TimeUtils.earlyLastMonth("yyyy/MM/dd HH:mm:ss");

        // 计算上个月的第一天的日期和时间，使用自定义格式
        String expectedDate = LocalDate.now().minusMonths(1) // 获取上个月
                .with(TemporalAdjusters.firstDayOfMonth()) // 获取上个月的第一天
                .atStartOfDay() // 设置为00:00:00时刻
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")); // 使用自定义格式

        // 断言返回的日期格式是否与预期相符
        assertEquals(expectedDate, earlyLastMonthCustomFormat);
    }
    @Test
    public void testEarlyLastMonthCustomDatePattern() {
        String earlyLastMonthCustomDate = TimeUtils.earlyLastMonth("dd/MM/yyyy");

        // 计算上个月的第一天的日期，使用自定义日期格式
        String expectedDate = LocalDate.now().minusMonths(1) // 获取上个月
                .with(TemporalAdjusters.firstDayOfMonth()) // 获取上个月的第一天
                .atStartOfDay() // 设置为 00:00:00 时刻
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); // 使用自定义格式

        // 断言返回的日期格式是否与预期相符
        assertEquals(expectedDate, earlyLastMonthCustomDate);
    }
}
