package com.github.hugh.util;

import com.github.hugh.constant.DateCode;

import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * 获取当前日期字符串
     * <ul>
     * <li>格式：yyyy-MM-dd HH:mm:ss</li>
     * </ul>
     *
     * @return String
     */
    public static String now() {
        return now(YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 将日期转为对应的日期格式字符串
     *
     * @param pattern 格式 例：yyyy-MM-dd HH:mm:ss yyyy-MM-dd
     * @return String 字符串的日期
     */
    public static String now(String pattern) {
        return ofPattern(pattern, LocalDateTime.now());
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
        if (EmptyUtils.isEmpty(format)) {
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
     * @param year 年(1-12)
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
     * 获取时
     *
     * @return the hour-of-day, from 0 to 23
     * @since 2.3.6
     */
    public static int getHour(){
        return LocalDateTime.now().getHour();
    }

    /**
     * 获取分
     *
     * @return the minute-of-hour, from 0 to 59
     * @since 2.3.6
     */
    public static int getMinute(){
        return LocalDateTime.now().getMinute();
    }

    /**
     * 获取秒
     *
     * @return the second-of-minute, from 0 to 59
     * @since 2.3.6
     */
    public static int getSecond(){
        return LocalDateTime.now().getSecond();
    }

    /**
     * 获取当前日期字符串
     * <ul>
     * <li>格式：yyyy-MM-dd HH:mm:ss</li>
     * </ul>
     *
     * @return String
     */
    @Deprecated
    public static String getTime() {
        return getTime(YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 将日期转为对应的日期格式字符串
     *
     * @param pattern 格式 例：yyyy-MM-dd HH:mm:ss yyyy-MM-dd
     * @return String 字符串的日期
     */
    @Deprecated
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
     * @param pattern 日期格式
     * @return String 返回格式
     */
    public static String earlyLastMonth(String pattern) {
        LocalDate earlyLastMonth = LocalDate.now().minusMonths(1)// 设置提前月数
                .with(TemporalAdjusters.firstDayOfMonth());// 设置月起始日
        LocalDateTime localDateTime = LocalDateTime.of(earlyLastMonth, LocalTime.MIN);
        return ofPattern(pattern, localDateTime);
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
     * @param maxAllow   允许的最大误差时间 毫秒
     * @return boolean {@code true} 在范围内
     */
    public static boolean checkTimestamp(String clientTime, long maxAllow) {
        if (clientTime.length() != 13) {
            return false;
        }
        long time;
        long sys = System.currentTimeMillis();
        try {
            time = Long.parseLong(clientTime);
            if (!((sys - time) <= maxAllow && (time - sys) <= maxAllow)) {
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
    public static long differMilli(String startTime, String endTime) {
        if (EmptyUtils.isEmpty(startTime) || EmptyUtils.isEmpty(endTime)) {
            return -1;
        }
        LocalDateTime start = parseTime(startTime);
        if (start == null) {
            return -1;
        }
        LocalDateTime end = parseTime(endTime);
        if (end == null) {
            return -1;
        }
        Instant s = start.toInstant(ZoneOffset.ofHours(8));//转换时增加8时区
        Instant e = end.toInstant(ZoneOffset.ofHours(8));
        return ChronoUnit.MILLIS.between(s, e);
    }

    /**
     * 获取指定日期后的第N天
     * <ul>
     * <li>注：{@link LocalDate} 为没有时分秒的日期对象。</li>
     * </ul>
     *
     * @param localDate 日期
     * @param days      天数
     * @return LocalDate 指定日期后的第N天
     * @since 1.3.2
     */
    public static LocalDate getNextNDay(LocalDate localDate, int days) {
        return localDate.with(temporal -> temporal.plus(days, ChronoUnit.DAYS));
    }

    /**
     * 校验结束日期是否超过开始日期一天
     * <p>调用{@link #isCrossDay(String, String, int)}进行校验</p>
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return boolean {@code true}
     * @since 1.3.2
     */
    public static boolean isCrossDay(String start, String end) {
        return isCrossDay(start, end, 1);
    }

    /**
     * 校验开始日期与结束日期是否跨天
     * <ul>
     * <li>例：
     * 开始日期：2020-06-08 00:00:00
     * 结束日期:2020-06-09 00:00:00
     * 结果{@code true}
     * </li>
     * </ul>
     *
     * @param start 开始日期 格式：yyyy-MM-dd HH:mm:ss
     * @param end   结束日期 格式：yyyy-MM-dd HH:mm:ss
     * @param days  天数
     * @return boolean {@code true} 跨天
     * @since 1.3.2
     */
    public static boolean isCrossDay(String start, String end, int days) {
        LocalDate startDate = LocalDate.parse(start, DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC));
        LocalDate endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_HOUR_MIN_SEC));
        startDate = getNextNDay(startDate, days);//开始日期加N天后的日期
        return startDate.isEqual(ChronoLocalDate.from(endDate));   //与结束日期比对
    }

    /**
     * 收集起始时间到结束时间之间所有的时间并以字符串集合方式返回
     *
     * @param timeStart 开始日期:yyyy-MM-dd
     * @param timeEnd   结束日期:yyyy-MM-dd
     * @return List
     * @since 1.4.5
     */
    public static List<String> collectLocalDates(String timeStart, String timeEnd) {
        return collectLocalDates(LocalDate.parse(timeStart), LocalDate.parse(timeEnd));
    }

    /**
     * 收集起始时间到结束时间之间所有的时间并以字符串集合方式返回
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return List
     * @since 1.4.5
     */
    public static List<String> collectLocalDates(LocalDate start, LocalDate end) {
        // 用起始时间作为流的源头，按照每次加一天的方式创建一个无限流
        return Stream.iterate(start, localDate -> localDate.plusDays(1))
                // 截断无限流，长度为起始时间和结束时间的差+1个
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                // 由于最后要的是字符串，所以map转换一下
                .map(LocalDate::toString)
                // 把流收集为List
                .collect(Collectors.toList());
    }
}
