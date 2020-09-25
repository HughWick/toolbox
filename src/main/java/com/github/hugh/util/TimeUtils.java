package com.github.hugh.util;

import com.github.hugh.constant.DateCode;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * 针对 LocalDateTime
 * <p>如需使用Date类工具 @see {@link  com.github.hugh.util.DateUtils} </p>
 *
 * @author hugh
 * @version java 1.8
 * @since 1.0.0
 */
public class TimeUtils extends DateCode {
    private TimeUtils() {

    }

    /**
     * 统一格式化LocalDateTime 入口
     * <p>默认format为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param format 格式
     * @param time   LocalDateTime实体
     * @return String 返回根据对应格式，格式化后的字符串
     */
    private static String ofPattern(String format, LocalDateTime time) {
        if (format == null || "".equals(format)) {
            format = YEAR_MONTH_DAY_HOUR_MIN_SEC;
        }
        return DateTimeFormatter.ofPattern(format).format(time);
    }

    /**
     * 当字符串日期转换为 LocalDateTime
     * <ul>
     * <li>默认格式为：yyyy-MM-dd HH:mm:ss</li>
     * </ul>
     *
     * @param timeStr 字符串日期
     * @return LocalDateTime
     * @since 1.2.2
     */
    public static LocalDateTime parseTime(String timeStr) {
        return parseTime(timeStr, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 将字符串日期转换为指定格式的日期对象
     *
     * @param strTime 字符串的日期
     * @param format  日期格式
     * @return LocalDateTime
     * @since 1.2.2
     */
    private static LocalDateTime parseTime(String strTime, String format) {
        try {
            return LocalDateTime.parse(strTime, DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取年份
     *
     * @return the year, from MIN_YEAR to MAX_YEAR
     */
    public static int getYear() {
        return LocalDateTime.now().getYear();
    }

    /**
     * 校验年与当前日期的年份一致
     *
     * @param year 年(1-13)
     * @return boolean {@code true} 同一年
     */
    public static boolean isThisYear(int year) {
        return getYear() == year;
    }

    /**
     * 获取月数
     *
     * @return the month-of-year, from 1 to 12
     */
    public static int getMonth() {
        return LocalDateTime.now().getMonthValue();
    }

    /**
     * 获取日
     *
     * @return the day-of-month, from 1 to 31
     */
    public static int getDay() {
        return LocalDateTime.now().getDayOfMonth();
    }

    /**
     * 获取当前日期字符串
     * <ul>
     * <li>格式：yyyy-MM-dd HH:mm:ss</li>
     * </ul>
     *
     * @return String
     */
    public static String getTime() {
        return getTime(YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 将日期转为对应的日期格式字符串
     *
     * @param pattern 格式 例：yyyy-MM-dd HH:mm:ss yyyy-MM-dd
     * @return String 字符串的日期
     */
    public static String getTime(String pattern) {
        return ofPattern(pattern, LocalDateTime.now());
    }

    /**
     * 获取当前月份第一天的起始时间
     *
     * @return String
     */
    public static String firstDayOfMonth() {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());// 设置月初起始日期
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC, dateTime);
    }

    /**
     * 获取月份最后一天
     *
     * @return String
     */
    public static String lastDayOfMonth() {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());// 设置月末日期
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC, dateTime);
    }

    /**
     * 获取上月末日期时间点
     *
     * @return String
     */
    public static String endOfLastMonth() {
        return endOfLastMonth(YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 获取上个月末的最后一天时间
     *
     * @param pattern 日期格式
     * @return String
     */
    public static String endOfLastMonth(String pattern) {
        LocalDate earlyLastMonth = LocalDate.now().minusMonths(1)// 设置提前月数
                .with(TemporalAdjusters.lastDayOfMonth());// 设置月末最后一天
        LocalDateTime localDateTime = LocalDateTime.of(earlyLastMonth, LocalTime.MAX);
        return ofPattern(pattern, localDateTime);
    }

    /**
     * 上个月月末时间
     *
     * @return String 日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static String earlyLastMonth() {
        return earlyLastMonth(YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 获取上个月的月初时间
     *
     * @return String 返回格式
     */
    public static String earlyLastMonth(String pattern) {
        LocalDate earlyLastMonth = LocalDate.now().minusMonths(1)// 设置提前月数
                .with(TemporalAdjusters.firstDayOfMonth());// 设置月起始日
        LocalDateTime localDateTime = LocalDateTime.of(earlyLastMonth, LocalTime.MIN);
        return ofPattern(pattern, localDateTime);
    }

    /**
     * 获取昨天时间
     *
     * @param format 格式，为空时默认为yyyy-MM-dd
     * @param time   LocalTime
     * @return String 日期字符串
     */
    private static String getYesterday(String format, LocalTime time) {
        if (format == null || "".equals(format)) {
            format = YEAR_MONTH_DAY;// 默认为：yyyy-MM-dd
        }
        if (time == null) {
            time = LocalTime.MIN;// 默认为日的起始时间
        }
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().plusDays(-1), time);
        return ofPattern(format, localDateTime);
    }

    /**
     * 获取昨天起始日期
     *
     * @return String  yyyy-MM-dd HH:mm:ss 日期字符串
     */
    public static String getStartedYesterday() {
        return getYesterday(YEAR_MONTH_DAY_HOUR_MIN_SEC, LocalTime.MIN);
    }

    /**
     * 获取结束日期
     *
     * @return String  yyyy-MM-dd HH:mm:ss 日期字符串
     */
    public static String getEndedYesterday() {
        return getYesterday(YEAR_MONTH_DAY_HOUR_MIN_SEC, LocalTime.MAX);
    }

    /**
     * 校验格式为 yyyy-MM-dd HH:mm:ss 的日期字符串是否在系统时间之前
     *
     * @param time 日期字符串
     * @return {@code true} 系统时间之前
     * @since 1.2.2
     */
    public static boolean exceedSystem(String time) {
        LocalDateTime t = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC));
        return t.isBefore(LocalDateTime.now());
    }

    /**
     * 校验时间戳是否超过设定
     *
     * @param clientTime 客户端时间戳
     * @param max_allow  允许的最大误差时间 毫秒
     * @return boolean {@code true} 在范围内
     */
    public static boolean checkTimestamp(String clientTime, long max_allow) {
        if (clientTime.length() != 13) {
            return false;
        }
        long time, sys = System.currentTimeMillis();
        try {
            time = Long.parseLong(clientTime);
            if (!((sys - time) <= max_allow && (time - sys) <= max_allow)) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取昨天开始日期
     * <ul>
     * <li>格式：yyyy-MM-dd 23:59:59</li>
     * </ul>
     *
     * @return String 日期
     * @since 1.1.3
     */
    public static String getYesterdayStartTime() {
        LocalDateTime localDateTime = LocalDateTime.now()
                .withHour(0)//小时
                .withMinute(0)//分钟
                .withSecond(0)//秒
                .withNano(0)//毫秒
                .plusDays(-1);//前一天
        return ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC, localDateTime);
    }

    /**
     * 获取昨日结束日期
     * <ul>
     * <li>格式：yyyy-MM-dd 23:59:59</li>
     * </ul>
     *
     * @return String 日期
     * @since 1.1.3
     */
    public static String getYesterdayEndTime() {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999)
                .plusDays(-1);
        return ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC, localDateTime);
    }


    /**
     * 校验开始日期与结束日期相差多少毫秒
     * <ul>
     * <li>注：return @{code -1} 时达标参数异常</li>
     * </ul>
     *
     * @param startTime 开始日期 格式：yyyy-MM-dd HH:mm:ss
     * @param endTime   结束日期 格式：yyyy-MM-dd HH:mm:ss
     * @return long 相差毫秒数
     * @since 1.2.4
     */
    public static long differ(String startTime, String endTime) {
        if (EmptyUtils.isEmpty(startTime) || EmptyUtils.isEmpty(endTime)) {
            return -1;
        }
        LocalDateTime start = TimeUtils.parseTime(startTime);
        if (start == null) {
            return -1;
        }
        LocalDateTime end = TimeUtils.parseTime(endTime);
        if (end == null) {
            return -1;
        }
        Instant s = start.toInstant(ZoneOffset.ofHours(8));//转换时增加8时区
        Instant e = end.toInstant(ZoneOffset.ofHours(8));
        return ChronoUnit.MILLIS.between(s, e);
    }
}
