package com.github.hugh.util;

import com.github.hugh.constant.DateCode;
import com.github.hugh.exception.ToolboxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date 类型日期使用常用工具类
 * <p>该类型主要使用java 1.8之前没有LocalDateTime 类的处理方式</p>
 *
 * @author hugh
 * @version java 1.7
 * @since 1.0.0
 */
public class DateUtils extends DateCode {
    private DateUtils() {
    }

    /**
     * 格式化日期
     *
     * @param date 日期对象
     * @return String  格式为：yyyy-MM-dd
     */
    public static String format(Date date) {
        return format(date, YEAR_MONTH_DAY);
    }

    /**
     * 格式化日期
     *
     * @param date    日期对象
     * @param pattern 格式
     * @return String 日期格式字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date);
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 获取日期{@code yyyy-MM-dd}
     *
     * @return String 当前时间
     */
    public static String getDate() {
        return format(new Date());
    }

    /**
     * 获取年月日
     *
     * @return String  yyyyMMdd
     */
    public static String getDateSign() {
        return format(new Date(), YEAR_MONTH_DAY_SIMPLE);
    }

    /**
     * 根据格式获取当前日期
     *
     * @param format 日期格式
     * @return String
     */
    public static String getDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间前后N天的起始日期
     *
     * @param day 指定天输(正数 or 负数)
     * @return Date 日期对象
     */
    public static Date getDate(int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, day);
        set(c, 0, 0, 0, 0);
        return c.getTime();
    }

    /**
     * 将字符串yyyy-MM-dd HH:mm:ss转换为yyyyMMddHHmmss 如果传入的字符串为null,则返回空值
     *
     * @param dateTime 日期格式的字符串
     * @return String 日期格式字符串
     */
    public static String toStringTime(String dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.replace("-", "").replace(" ", "").replace(":", "");
    }

    /**
     * 将字符串yyyyMMddhhmmss 转换成yyyy-MM-dd HH:mm:ss字符串格式 如果传入的字符串为null,则返回空值
     *
     * @param strDate 字符串yyyyMMddhhmmss
     * @return String 日期格式字符串
     */
    public static String toStringDate(String strDate) {
        if (strDate == null) {
            return "";
        }
        String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
        return strDate.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
    }

    /**
     * 转换日期
     *
     * @param value 字符串日期 or 日期对象
     * @return Date 默认日期对象格式:yyyy-MM-dd HH:mm:ss
     */
    public static <T> Date changeDate(T value) {
        return changeDate(value, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 根据不同日期格式，转换成对应的日期格式对象
     *
     * @param value  日期
     * @param format 日期格式
     * @return Date
     */
    public static <T> Date changeDate(T value, String format) {
        if (value instanceof Date) {
            return (Date) value;
        } else {
            return parseDate(value + "", format);
        }
    }

    /**
     * 将字符串（yyyy-MM-dd）解析成日期
     *
     * @param dateStr 日期格式的字符串
     * @return Date 日期类型对象
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, YEAR_MONTH_DAY);
    }

    /**
     * 将字符串解析成日期
     * <p>调用{@link #parseDate(String, String)}格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param dateStr 日期字符串
     * @return Date 日期类型对象
     * @since 1.3.10
     */
    public static Date parse(String dateStr) {
        return parseDate(dateStr, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 按照指定的格式，将字符串解析成日期类型对象，例如：yyyy-MM-dd,yyyy/MM/dd,yyyy/MM/dd hh:mm:ss
     *
     * @param dateStr 日期格式的字符串
     * @param format  字符串的格式
     * @return Date 日期类型对象
     */
    public static Date parseDate(String dateStr, String format) {
        if (EmptyUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat formats = new SimpleDateFormat(format);
        try {
            return formats.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将时间戳转换成日期格式的字符串
     *
     * @param timestamp 时间戳
     * @return String yyyy-MM-dd HH:mm:ss 日期字符串
     * @since 1.3.10
     */
    public static String formatTimestamp(long timestamp) {
        return formatTimestamp(timestamp, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 将时间戳转换成日期格式的字符串
     *
     * @param timestamp 时间戳
     * @param format    日期格式
     * @return String 字符串
     * @since 1.3.12
     */
    public static String formatTimestamp(long timestamp, String format) {
        if (timestamp < 0) {
            return null;
        }
        return new SimpleDateFormat(format).format(timestamp);
    }

    /**
     * 将时间戳转换为Date类型
     * <p>调用{@link #formatTimestamp(long)}格式化时间戳，后将调用{@link #parse(String)}解析成{@link Date}</p>
     *
     * @param timestamp 时间戳
     * @return Date
     * @since 1.3.10
     */
    public static Date parseTimestamp(long timestamp) {
        if (timestamp < 0) {
            return null;
        }
        String strTime = formatTimestamp(timestamp);
        return parse(strTime);
    }

    /**
     * 判断日期是否是本日，本周，本月
     *
     * @param time    需要判断的时间
     * @param pattern 日期格式
     * @return boolean
     */
    public static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);// 参数时间
        String now = sdf.format(new Date());// 当前时间
        return param.equals(now);
    }

    /**
     * 根据当前时间的一天前的起始时间
     *
     * @return Date 前一天的起始时间
     */
    public static Date getDayBeforeStartTime() {
        Date date = new Date();
        String dateStr = format(date, YEAR_MONTH_DAY);
        return getDayBeforeStartTime(dateStr);
    }

    /**
     * 根据日期字符串获取前一天的起始时间
     *
     * @param dateStr 日期字符串-格式：yyyy-MM-dd
     * @return Date 前一天的起始时间
     */
    public static Date getDayBeforeStartTime(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        Date date = parseDate(dateStr, YEAR_MONTH_DAY);
        if (date == null) {
            return null;
        }
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 根据当前时间获取一天前的结束时间
     *
     * @return Date 前一天的结束时间
     */
    public static Date getDayBeforeEndTime() {
        Date date = new Date();
        String dateStr = format(date, YEAR_MONTH_DAY);
        return getDayBeforeEndTime(dateStr);
    }

    /**
     * 根据日期字符串获取前一天的结束时间
     *
     * @param dateStr 日期字符串-格式：yyyy-MM-dd
     * @return Date 前一天的结束时间
     */
    public static Date getDayBeforeEndTime(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        Date date = parseDate(dateStr, YEAR_MONTH_DAY);
        if (date == null) {
            return null;
        }
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 判断两个日期是否为同一月
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return boolean @{code true} 同一月
     */
    public static boolean isSameMonth(Date beginDate, Date endDate) {
        try {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(beginDate);
            int month1 = cal1.get(Calendar.MONTH) + 1;
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(endDate);
            int month2 = cal2.get(Calendar.MONTH) + 1;
            boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
            return isSameYear && month1 == month2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置日期的月份开始时间
     *
     * @param date 日期对象
     * @return String
     */
    public static String setMonthFirstDay(Date date) {
        // 获取前月的第一天
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        if (date != null) {
            calendar.setTime(date);
        } else {
            calendar.setTime(new Date());
        }
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        return format(calendar.getTime(), YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 设置月份的最后一天时间
     *
     * @param date 日期对象
     * @return String {@code yyyy-MM-dd 23:59:59}
     */
    public static String setMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        } else {
            calendar.setTime(new Date());
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return format(calendar.getTime(), YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 获取上一个月的月初开始时间
     *
     * @return Date
     */
    public static Date getMonthBeforeStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取上一个月最后一天的结束时间
     *
     * @return Date
     */
    public static Date getMonthBeforeEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        // 获取最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        set(calendar, 23, 59, 59, 999);
        return calendar.getTime();
    }

    /**
     * 获取某一天开始时间，不传日期则获取今日
     *
     * @param dateStr 字符串日期格式 yyyy-MM-dd
     * @return Date
     */
    public static Date getStartTime(String dateStr) {
        Date date;
        if (EmptyUtils.isEmpty(dateStr)) {
            date = new Date();
        } else {
            date = parseDate(dateStr, YEAR_MONTH_DAY);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        set(calendar, 0, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取今天起始时间
     *
     * @return Date 今天起始时间
     */
    public static Date getTodayStartTime() {
        return getStartTime(null);
    }

    /**
     * 获取某一天结束时间,不传日期则获取今日
     *
     * @param dateStr 字符串日期格式 yyyy-MM-dd
     * @return Date
     */
    public static Date getEndTime(String dateStr) {
        Date date;
        if (EmptyUtils.isEmpty(dateStr)) {
            date = new Date();
        } else {
            date = parseDate(dateStr, YEAR_MONTH_DAY);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        set(calendar, 23, 59, 59, 999);
        return calendar.getTime();
    }

    /**
     * 获取今天结束时间
     *
     * @return Date 结束时间
     */
    public static Date getTodayEndTime() {
        return getEndTime(null);
    }

    /**
     * 获取当前一小时前的时间
     *
     * @return Date
     */
    public static Date getHourAgo() {
        return getHourAgo(null);
    }

    /**
     * 获取一小时前的时间日期，null时默认当前时间的一小时前
     *
     * @param date 日期对象
     * @return Date
     */
    public static Date getHourAgo(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(date);
        }
        // 小时-1
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 1);
        return calendar.getTime();
    }

    /**
     * 获取一个月之前的当前时间
     *
     * @return Date
     */
    public static Date getOneMonthAgo() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        return c.getTime();
    }

    /**
     * 获取当前时间的一周前时间
     *
     * @return Date 一周前时间
     */
    public static Date getWeekAgo() {
        return getWeekAgo(null);
    }

    /**
     * 获取指定日期的一周前时间
     *
     * @param date 指定日期
     * @return Date 日期
     */
    public static Date getWeekAgo(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(date);
        }
        // 过去七天
        calendar.add(Calendar.DATE, -7);
        return calendar.getTime();
    }

    /**
     * 将字符串的日期转换成日期类型，并且时分秒设定为一天的起始
     *
     * @param dateStr yyyy-mm-dd 日期格式的字符串
     * @return Date 一天的起始日期
     */
    public static Date setStartHouMinSec(String dateStr) {
        Date date = parseDate(dateStr, YEAR_MONTH_DAY);
        if (date == null) {
            throw new ToolboxException("data is null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 将字符串的日期转换成日期类型，并且时分秒设定为一天的结束时间
     *
     * @param dateStr yyyy-mm-dd 日期格式的字符串
     * @return Date 一天的结束时间
     */
    public static Date setEndHouMinSec(String dateStr) {
        Date date = parseDate(dateStr, YEAR_MONTH_DAY);
        if (date == null) {
            throw new ToolboxException("data is null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取日期中的年份
     *
     * @param date 日期对象
     * @return int 年份(1-12)
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取年份
     *
     * @return the year, from MIN_YEAR to MAX_YEAR
     * @since 1.5.0
     */
    public static int getYear() {
        return LocalDateTime.now().getYear();
    }

    /**
     * 获取日期中的月份
     *
     * @param date 日期对象
     * @return int 月份(1-12)
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取月数
     *
     * @return the month-of-year, from 1 to 12
     * @since 1.5.0
     */
    public static int getMonth() {
        return LocalDateTime.now().getMonthValue();
    }

    /**
     * 获取日期中的日
     *
     * @param date 日期对象
     * @return int 日（1-31）
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取日
     *
     * @return the day-of-month, from 1 to 31
     * @since 1.5.0
     */
    public static int getDay() {
        return LocalDateTime.now().getDayOfMonth();
    }

    /**
     * 将字符串的 yyyyMMdd、截取成yyyyMM
     *
     * @param strDate yyyyMMdd的字符串
     * @return String yyyyMM
     */
    public static String substring(String strDate) {
        if (EmptyUtils.isEmpty(strDate)) {
            return null;
        }
        return strDate.substring(0, 6);
    }

    /**
     * 判断当前日期是否是月初的第一天
     *
     * @return boolean 是月初返回true，其他返回false
     */
    public static boolean isEarlyMonth() {
        return isEarlyMonth(new Date());
    }

    /**
     * 判断是否是月初的第一天
     *
     * @param date 日期
     * @return boolean 是月初返回true，其他返回false
     */
    public static boolean isEarlyMonth(Date date) {
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    /**
     * 校验当前日期是否与传递日期一致
     *
     * @param date   需校验的日期
     * @param format 日期格式如：yyyy-MM-dd HH:mm:ss
     * @return boolean 一致返回true、其他返回false
     */
    public static boolean isNowadays(Date date, String format) {
        if (date == null) {
            return false;
        }
        Date nowDate = new Date();
        String tem = format(nowDate, format);
        String tem1 = format(date, format);
        return tem.equals(tem1);
    }

    /**
     * 校验日期是否超过日期的多少个小时
     *
     * @param date 日期
     * @param hour 小时
     * @return boolean 超过true、其它false
     */
    public static boolean checkTimeOut(Date date, int hour) {
        if (date == null || hour <= 0) {
            return true;
        }
        long mill = 3600000;
        // 当小时大于一小时的时候，根据需求乘以对应的小时毫秒
        if (hour > 1) {
            mill = hour * mill;
        }
        Date nowDate = new Date();
        long time = nowDate.getTime();
        return (time - date.getTime()) > mill;
    }

    /**
     * 获取日期当天的起始日期
     *
     * @param date 日期
     * @return Date
     */
    public static Date getStartDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        set(calendar, 0, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取日期结束时间
     *
     * @param date 日期对象
     * @return Date 日期的23:59:59
     */
    public static Date getEndDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        set(calendar, 23, 59, 59, 999);
        return calendar.getTime();
    }

    /**
     * 校验日期是否超过当前系统日期
     *
     * @param date 日期对象
     * @return boolean {@code true} 超过当前系统时间
     */
    public static boolean exceedSystem(Date date) {
        if (date == null) {
            return true;
        }
        // 当前系统日期
        Date current = new Date();
        return date.getTime() > current.getTime();
    }

    /**
     * 校验日期对象是否小于当前系统时间
     *
     * @param date 日期对象
     * @return boolean {@code true} 小于当前系统时间
     */
    public static boolean belowSystem(Date date) {
        if (date == null) {
            return true;  // 空直接返回true
        }
        Date current = new Date();
        return date.getTime() < current.getTime();
    }

    /**
     * 根据类型选中对应的起始日期并格式化后返回
     *
     * @param type 日期类型 :day-天、week-周、month-月
     * @return String
     */
    @Deprecated
    public static String chooseDate(String type) {
        String startDate = "";
        switch (type) {
            case "day":// 天
                startDate = format(getTodayStartTime(), YEAR_MONTH_DAY_HOUR_MIN_SEC);
                break;
            case "week":// 周
                startDate = format(getWeekAgo(), YEAR_MONTH_DAY_HOUR_MIN_SEC);
                break;
            case "month":// 月
                startDate = format(getOneMonthAgo(), YEAR_MONTH_DAY_HOUR_MIN_SEC);
                break;
            default:
                return null;
        }
        return startDate;
    }

    /**
     * 获取当前月份起始日期时间
     *
     * @return Date 月初时间
     */
    public static Date getMonthStartTime() {
        return getMonthStartTime(new Date());
    }

    /**
     * 获取日期月份开始时间
     *
     * @param date 日期对象
     * @return Date
     */
    public static Date getMonthStartTime(Date date) {
        if (date == null) {
            return null;
        }
        int month = getMonth(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        set(calendar, 0, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取日期中月底时间
     *
     * @param date 日期对象
     * @return Date 日期对象{@code 2019-09-30 23:59:59}
     */
    public static Date getMonthEndTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = getMonth(date);
        calendar.set(Calendar.MONTH, month - 1);
        // 获取最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        set(calendar, 23, 59, 59, 999);
        return calendar.getTime();
    }

    /**
     * 校验日期是否是今天
     *
     * @param date 日期对象
     * @return boolean {@code true}今天
     */
    public static boolean isToday(Date date) {
        if (date == null) {
            return false;
        }
        String time = format(date, YEAR_MONTH_DAY_HOUR_MIN_SEC);
        return isToday(time, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 校验日期是否是今天
     * <p>默认格式：yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 日期字符串
     * @return boolean
     */
    public static boolean isToday(String time) {
        return isToday(time, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 校验字符串日期是否为今天
     *
     * @param time      字符串日期
     * @param formatStr 格式
     * @return Boolean {@code true}今天
     */
    public static boolean isToday(String time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        // 设置传入的时间
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        Calendar c2 = Calendar.getInstance();
        // 设置当前时间
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH) + 1;
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 校验年份、月份、天是否都是同一数值
        return year1 == year2 && month1 == month2 && day1 == day2;
    }

    /**
     * 校验日期是否与格式一致
     *
     * @param time   日期
     * @param format 格式
     * @return boolean 正确返回true、其他返回false
     */
    public static boolean isValidDate(String time, String format) {
        if (com.github.hugh.util.EmptyUtils.isEmpty(time) || com.github.hugh.util.EmptyUtils.isEmpty(format)) {
            return false;
        }
        try {
            // 转换为日期
            Date date = parseDate(time, format);
            // 再将日期转换成对应格式，进行校验是否与字符串一致
            return time.equals(format(date, format));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取小时整点时间
     *
     * @return Date 日期对象 {@code 2019-07-25 14:00:00}
     */
    public static Date getIniHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前小时的结束时间点
     *
     * @return Date 日期对象 {@code 2019-07-25 09:59:59}
     */
    public static Date getEndHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 结束日期小于起始日期
     *
     * @param start 起始日期
     * @param end   结束日期
     * @return boolean {@code true} 结束日期小于起始日期
     */
    public static boolean lessThanStartDate(Date start, Date end) {
        if (start == null) {
            throw new ToolboxException(" start date is null ! ");
        }
        if (end == null) {
            throw new ToolboxException(" end date is null ! ");
        }
        return end.getTime() < start.getTime();
    }

    /**
     * 结束日期大于起始日期
     *
     * @param start 起始日期
     * @param end   结束日期
     * @return boolean {@code true} 结束日期小于起始日期
     * @since 1.4.14
     */
    public static boolean greaterThanStartDate(Date start, Date end) {
        if (start == null) {
            throw new ToolboxException(" start date is null ! ");
        }
        if (end == null) {
            throw new ToolboxException(" end date is null ! ");
        }
        return end.getTime() > start.getTime();
    }


    /**
     * 判断是否是日期的格式
     * <p>默认校验格式：yyyy-MM-dd HH:mm:ss</p>
     *
     * @param timeStr 日期格式字符串
     * @return boolean
     */
    public static boolean isDateFormat(String timeStr) {
        return isDateFormat(timeStr, YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 校验字符串是不是日期的格式
     * <p>支持格式：yy-MM、yy-MM-dd、yyyy-MM-dd HH:mm:ss</p>
     *
     * @param timeStr 日期字符串
     * @param pattern 日期格式
     * @return boolean {@code true} 字符串格式正确
     */
    public static boolean isDateFormat(String timeStr, String pattern) {
        if (timeStr == null || "".equals(timeStr)) {
            return false;
        }
        try {
            String regex;
            if (YEAR_MONTH.equals(pattern)) {//年-月
                regex = "\\d{4}-\\d{2}";
            } else if (YEAR_MONTH_DAY.equals(pattern)) {//年-月-日
                regex = "\\d{4}-\\d{2}-\\d{2}";
            } else {// 默认校验yyyy-MM-dd HH:mm:ss
                regex = "\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}";
            }
            Pattern pat = Pattern.compile(regex);// 编译正则表达式
            Matcher matcher = pat.matcher(timeStr);// 忽略大小写的写法
            if (matcher.matches()) {// 先验证格式
                Date date = parseDate(timeStr, pattern);//转换格式
                return timeStr.equals(format(date, pattern));// 验证时间
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置Calendar的小时、分钟、秒、毫秒  
     *
     * @param calendar    日历
     * @param hour        小时
     * @param minute      分
     * @param second      秒
     * @param milliSecond 毫秒
     */
    public static void set(Calendar calendar, int hour, int minute, int second, int milliSecond) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, milliSecond);
    }

    /**
     * 验证字符串时间，是否在30天内
     *
     * @param date 日期
     * @return boolean 30天内返回ture、其他返回false
     */
    public static boolean isValidDate(Date date) {
        return isValidDate(date, 30);
    }

    /**
     * 校验日期对象是否在某个范围天之内
     *
     * @param date 日期对象
     * @param day  天数
     * @return boolean 在范围内返回true、其他返回false
     */
    public static boolean isValidDate(Date date, int day) {
        if (date == null) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat(YEAR_MONTH_DAY_HOUR_MIN_SEC);// 时间格式定义
        String nowDate = format.format(new Date());// 获取当前时间日期--nowDate
        Calendar calc = Calendar.getInstance();// 获取30天前的时间日期--minDate
        calc.add(Calendar.DAY_OF_MONTH, -day);
        String minDate = format.format(calc.getTime());
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            // 获取字符串转换后的时间--strDate
            String strDate = format.format(date);
            return nowDate.compareTo(strDate) >= 0 && strDate.compareTo(minDate) >= 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 校验开始日期与结束日期是否属于同一年份
     *
     * @param startStr 起始日期字符串 支持格式：yyyy-MM-dd | yyyy-MM-dd HH:mm:ss
     * @param endStr   结束日期字符串 支持格式：yyyy-MM-dd | yyyy-MM-dd HH:mm:ss
     * @return boolean 不是同一年返回true
     */
    public static boolean isAcrossYear(String startStr, String endStr) {
        Date start;
        if (isDateFormat(startStr)) {
            start = parseDate(startStr, YEAR_MONTH_DAY_HOUR_MIN_SEC);
        } else if (isDateFormat(startStr, YEAR_MONTH_DAY)) {
            start = parseDate(startStr);
        } else {
            return true;
        }
        Date end;
        if (isDateFormat(endStr)) {
            end = parseDate(endStr, YEAR_MONTH_DAY_HOUR_MIN_SEC);
        } else if (isDateFormat(endStr, YEAR_MONTH_DAY)) {
            end = parseDate(endStr);
        } else {
            return true;
        }
        return isAcrossYear(start, end);
    }

    /**
     * 校验开始日期与结束日期是否属于同一年份
     *
     * @param start 起始日期
     * @param end   结束日期
     * @return boolean 不是同一年返回true
     */
    private static boolean isAcrossYear(Date start, Date end) {
        if (start == null) {
            return true;// 空值直接返回
        }
        int year = getYear(start);
        if (end == null) { // 结束日期为空时，使用当前日期
            end = new Date();
        }
        return year != getYear(end);
    }

    /**
     * 根据当前时间获取距离凌晨还剩余多少毫秒
     *
     * @return long 距离凌晨毫秒数
     */
    public static long getEarlyMorningSec() {
        long now = System.currentTimeMillis();// 当前毫秒数
        SimpleDateFormat sdfOne = new SimpleDateFormat(YEAR_MONTH_DAY);
        long overTime;
        try {
            overTime = (now - (sdfOne.parse(sdfOne.format(now)).getTime())) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
        // 当前时间 距离当天晚上23:59:59 秒数 也就是今天还剩多少秒
        return 24 * 60 * 60 - overTime;
    }

    /**
     * 日期对象的字符串 转 日期对象
     *
     * @param dataStr 日期对象字符串 @{code Fri Oct 09 00:00:00 CST 2020}
     * @return Date
     * @since 1.2.8
     */
    public static Date dateStrToDate(String dataStr) {
        try {
            return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(dataStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算两个时间之间相差多少分钟
     *
     * @param begin 开始时间
     * @param end   结束日期
     * @return long 相差分钟
     * @since 1.6.1
     */
    public static long minutesDifference(Date begin, Date end) {
        if (begin == null) {
            throw new ToolboxException(" start date is null ! ");
        }
        if (end == null) {
            throw new ToolboxException(" end date is null ! ");
        }
        long between = (end.getTime() - begin.getTime()) / 1000;
        return between / 60;
    }

    /**
     * 获取指定N分钟后的日期
     *
     * @param date 日期对象
     * @param min  分钟
     * @return Date
     * @since 1.6.2
     */
    public static Date getMin(Date date, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, +min);
        return calendar.getTime();
    }
}
