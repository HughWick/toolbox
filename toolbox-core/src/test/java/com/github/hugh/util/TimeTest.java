package com.github.hugh.util;

import com.github.hugh.support.TimeoutOperation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
        LocalDateTime result = TimeUtils.parseTime(timeStr);
        assertNull(result);
    }

    // 测试 parseTime 方法，使用指定格式
    @Test
    public void testParseTimeWithCustomFormat() {
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
    public void testParseTimeWithInvalidFormat() {
        String timeStr = "2025-01-20T09:26:09";
        String format = "yyyy/MM/dd HH:mm:ss";
        LocalDateTime result = TimeUtils.parseTime(timeStr, format);
        assertNull(result); // 格式不匹配，应该返回 null
    }

    // 测试 getYear 方法
    @Test
    public void testGetYear() {
        int year = TimeUtils.getYear();
        assertEquals(LocalDateTime.now().getYear(), year); // 应该返回当前年份
    }

    // 测试 isThisYear 方法，年份一致时返回 true
    @Test
    public void testIsThisYearWithSameYear() {
        int currentYear = LocalDateTime.now().getYear();
        boolean result = TimeUtils.isThisYear(currentYear);
        assertTrue(result); // 当前年份与传入年份一致，应该返回 true
    }

    // 测试 isThisYear 方法，年份不一致时返回 false
    @Test
    public void testIsThisYearWithDifferentYear() {
        int currentYear = LocalDateTime.now().getYear();
        boolean result = TimeUtils.isThisYear(currentYear + 1); // 假设下一年
        assertFalse(result); // 当前年份与传入年份不一致，应该返回 false
    }

    // 测试 getMonth 方法
    @Test
    public void testGetMonth() {
        int month = TimeUtils.getMonth();
        assertEquals(LocalDateTime.now().getMonthValue(), month); // 应该返回当前月份
    }

    // 测试 getDay 方法
    @Test
    public void testGetDay() {
        int day = TimeUtils.getDay();
        assertEquals(LocalDateTime.now().getDayOfMonth(), day); // 应该返回当前日期
    }

    // 测试 getHour 方法
    @Test
    public void testGetHour() {
        int hour = TimeUtils.getHour();
        assertEquals(LocalDateTime.now().getHour(), hour); // 应该返回当前小时
    }

    // 测试 getMinute 方法
    @Test
    public void testGetMinute() {
        int minute = TimeUtils.getMinute();
        assertEquals(LocalDateTime.now().getMinute(), minute); // 应该返回当前分钟
    }

    // 测试 getSecond 方法
    @Test
    public void testGetSecond() {
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
    // 测试 collectLocalDates 方法，正常日期范围
    @Test
    public void testCollectLocalDatesNormalRange() {
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
    public void testCollectLocalDatesSameDate() {
        String start = "2025-01-01";
        String end = "2025-01-01";
        List<String> result = TimeUtils.collectLocalDates(start, end);
        assertNotNull(result);
        assertEquals(1, result.size()); // 结果应该只包含 1 个日期
        assertEquals("2025-01-01", result.get(0));
    }

    // 测试 collectLocalDates 方法，跨越闰年
    @Test
    public void testCollectLocalDatesLeapYear() {
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
    public void testCollectLocalDatesOneMonth() {
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

}
