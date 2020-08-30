package com.github.hugh.util;

import com.github.hugh.core.DateCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * 针对 LocalDateTime
 * <p>如需使用Date类工具 @see {@link  com.github.hugh.util.DateUtils} </p>
 *
 * @author hugh
 * @date 2020年4月2日
 * @since 1.8
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
     * <li>格式：yyyy-MM-dd HH:mm:ss</li>
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
     * @return
     */
    public static String firstDayOfMonth() {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());// 设置月初起始日期
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC, dateTime);
    }

    /**
     * 获取月份最后一天
     *
     * @return
     */
    public static String lastDayOfMonth() {
        LocalDate localDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());// 设置月末日期
        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC, dateTime);
    }

    /**
     * 获取上月末日期时间点
     *
     * @return
     */
    public static String endOfLastMonth() {
        return endOfLastMonth(YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    public static String endOfLastMonth(String pattern) {
        LocalDate earlyLastMonth = LocalDate.now().minusMonths(1)// 设置提前月数
                .with(TemporalAdjusters.lastDayOfMonth());// 设置月末最后一天
        LocalDateTime localDateTime = LocalDateTime.of(earlyLastMonth, LocalTime.MAX);
        return ofPattern(pattern, localDateTime);
    }

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
     */
    public static boolean ex(String time) {
        LocalDateTime t = LocalDateTime.parse(time, DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC));
        return t.isBefore(LocalDateTime.now());
    }


}
