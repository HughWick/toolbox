package com.github.hugh.components.datetime;

import com.github.hugh.constant.DateCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.DateUtils;
import com.github.hugh.util.DoubleMathUtils;

import java.time.Duration;
import java.util.Date;

/**
 * 时间计算处理类
 *
 * @author hugh
 * @since 2.5.13
 */
public class TimeCalc {

    private final Date begin; // 开始时间，用于表示一个时间段的起始点
    private final Date end; // 结束时间，用于表示一个时间段的结束点
    boolean chineseFormat = true; // 是否使用中文格式，如果为true，则时间格式将以中文形式显示
    boolean isEnablePrecision = true; // 是否启用精确显示，如果为true，则输出的时间可以包含更详细的信息（如小时、分钟、秒）

    static final String DAY_CHINESE = "天";
    static final String HOURS_CHINESE = "小时";
    static final String MINUTES_CHINESE = "分";
    static final String SECONDS_CHINESE = "秒";


    /**
     * 设置是否使用中文格式。
     *
     * @param chineseFormat 是否使用中文格式
     * @return 返回当前对象，用于链式调用
     */
    public TimeCalc setChineseFormat(boolean chineseFormat) {
        this.chineseFormat = chineseFormat;
        return this;
    }

    /**
     * 设置是否启用精确时间。
     *
     * @param isEnablePrecision 是否启用精确时间
     * @return 返回当前对象，用于链式调用
     */
    public TimeCalc setEnablePrecision(boolean isEnablePrecision) {
        this.isEnablePrecision = isEnablePrecision;
        return this;
    }

    /**
     * 使用默认日期时间格式构造一个时间计算器实例
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param <S>       开始时间的类型
     * @param <E>       结束时间的类型
     */
    public <S, E> TimeCalc(S startTime, E endTime) {
        this(startTime, endTime, DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 使用默认日期时间格式构造一个时间计算器实例
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param format    日期时间格式
     * @param <S>       开始时间的类型
     * @param <E>       结束时间的类型
     */
    public <S, E> TimeCalc(S startTime, E endTime, String format) {
        this.begin = DateUtils.parse(startTime, format);
        this.end = DateUtils.parse(endTime, format);
    }

    /**
     * 创建一个时间计算器实例
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param <S>       开始时间的类型
     * @param <E>       结束时间的类型
     * @return 时间计算器实例
     */
    public static <S, E> TimeCalc on(S startTime, E endTime) {
        return new TimeCalc(startTime, endTime);
    }

    /**
     * 创建一个时间计算器实例
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param format    日期时间格式
     * @param <S>       开始时间的类型
     * @param <E>       结束时间的类型
     * @return 时间计算器实例
     */
    public static <S, E> TimeCalc on(S startTime, E endTime, String format) {
        return new TimeCalc(startTime, endTime, format);
    }

    /**
     * 将时间转换为分钟，并返回相应的字符串表示。
     *
     * @return 分钟数的字符串表示
     */
    public String toMinutes() {
        // 计算时间差，单位为秒
        final long seconds = secondsDiff();
        // 将秒数转换为 Duration 对象
        Duration duration = Duration.ofSeconds(seconds);
        // 获取分钟数
        long minutes = duration.toMinutes();
        if (chineseFormat) {
            // 中文格式
            return minutes + MINUTES_CHINESE;
        } else {
            // 非中文格式
            return String.valueOf(minutes);
        }
    }

    /**
     * 将时间转换为小时，并返回相应的字符串表示。
     *
     * @return 小时数的字符串表示
     */
    public String toHours() {
        // 计算时间差，单位为秒
        final long seconds = secondsDiff();
        // 将秒数转换为 Duration 对象
        Duration duration = Duration.ofSeconds(seconds);
        double hours;
        if (isEnablePrecision) { // 开启精准计算
            int secondsPerHour = 60 * 60;
            hours = DoubleMathUtils.div(seconds, secondsPerHour, 2);
        } else {
            hours = duration.toHours();
        }
        if (chineseFormat) {
            // 中文格式
            return isEnablePrecision ? hours + HOURS_CHINESE : ((int) hours) + HOURS_CHINESE;
        } else {
            // 非中文格式
            return isEnablePrecision ? String.valueOf(hours) : String.valueOf((int) hours);
        }
    }

    /**
     * 将时间转换为天数，并返回相应的字符串表示。
     *
     * @return 天数的字符串表示
     */
    public String toDays() {
        // 计算时间差，单位为秒
        final long seconds = secondsDiff();
        // 将秒数转换为 Duration 对象
        Duration duration = Duration.ofSeconds(seconds);
        double days;
        if (isEnablePrecision) { // 开启精准计算
            int secondsPerDay = 24 * 60 * 60;
            days = DoubleMathUtils.div(seconds, secondsPerDay, 2);
        } else {
            days = duration.toDays();
        }
        if (chineseFormat) {
            // 中文格式
            return isEnablePrecision ? days + DAY_CHINESE : ((int) days) + DAY_CHINESE;
        } else {
            // 非中文格式
            return isEnablePrecision ? String.valueOf(days) : String.valueOf((int) days);
        }
    }

    /**
     * 将时间转换为完整的格式并返回。
     *
     * @return 完整格式的时间字符串
     */
    public String toCompleteness() {
        return toCompleteness(true);
    }

    /**
     * 将时间转换为完整的格式并返回。
     *
     * @param includeSeconds 是否包含秒数（true 包含，false 不包含）
     * @return 完整格式的时间字符串
     */
    public String toCompleteness(boolean includeSeconds) {
        // 计算时间差，单位为秒
        final long seconds = secondsDiff();

        int secondsPerMinute = 60;
        int minutesPerHour = 60;
        int hoursPerDay = 24;

        // 计算总的天数
        long days = (long) DoubleMathUtils.div(seconds, (secondsPerMinute * minutesPerHour * hoursPerDay));
        // 计算剩余的秒数
        long remainingSeconds = seconds % (secondsPerMinute * minutesPerHour * hoursPerDay);

        // 计算小时数
        long hours = (long) DoubleMathUtils.div(remainingSeconds, (secondsPerMinute * minutesPerHour));
        remainingSeconds = remainingSeconds % (secondsPerMinute * minutesPerHour);

        // 计算分钟数
        long minutes = (long) DoubleMathUtils.div(remainingSeconds, secondsPerMinute);
        long remainingSecondsFinal = remainingSeconds % secondsPerMinute;

        // 根据不同的时间单位进行拼接并返回相应的字符串
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(DAY_CHINESE).append(hours).append(HOURS_CHINESE).append(minutes).append(MINUTES_CHINESE);
            if (includeSeconds) {
                sb.append(remainingSecondsFinal).append(SECONDS_CHINESE);
            }
        } else if (hours > 0) {
            sb.append(hours).append(HOURS_CHINESE).append(minutes).append(MINUTES_CHINESE);
            if (includeSeconds) {
                sb.append(remainingSecondsFinal).append(SECONDS_CHINESE);
            }
        } else if (minutes > 0) {
            sb.append(minutes).append(MINUTES_CHINESE);
            if (includeSeconds) {
                sb.append(remainingSecondsFinal).append(SECONDS_CHINESE);
            }
        } else if (includeSeconds) {
            sb.append(remainingSecondsFinal).append(SECONDS_CHINESE);
        }

        return sb.toString();
    }

    /**
     * 计算开始日期和结束日期之间的秒数差异
     *
     * @return 秒数差异
     * @throws IllegalArgumentException 如果结束日期早于开始日期
     */
    public int secondsDiff() {
        if (this.end.before(this.begin)) {
            throw new IllegalArgumentException("结束日期不能早于开始日期");
        }
        long a = this.end.getTime();
        long b = this.begin.getTime();
        return (int) DoubleMathUtils.div((a - b), 1000);
    }

    /**
     * 计算开始日期和结束日期之间的分钟差异
     *
     * @return 分钟差异
     * @throws ToolboxException 如果开始日期或结束日期为空
     */
    public long minutesDiff() {
        if (this.begin == null) {
            throw new ToolboxException(" start date is null ");
        }
        if (this.end == null) {
            throw new ToolboxException(" end date is null ");
        }
        long between = (this.end.getTime() - this.begin.getTime()) / 1000;
        return (long) DoubleMathUtils.div(between, 60);
    }
}

