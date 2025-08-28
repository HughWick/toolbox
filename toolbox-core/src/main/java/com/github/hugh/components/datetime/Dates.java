package com.github.hugh.components.datetime;

import com.github.hugh.constant.DateCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.DateUtils;
import com.github.hugh.util.EmptyUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * {@code Dates} 类是一个日期处理工具类，
 * 旨在简化日期字符串或时间戳的解析、格式化和操作。
 * <p>
 * 该类封装了 {@code java.util.Date} 对象，并提供了链式调用的方法，
 * 以方便地处理日期字符串的解析和格式设置。
 * </p>
 * <p>
 * 它支持将各种形式的日期文本（包括标准格式日期字符串和时间戳）
 * 解析为 {@code Date} 对象，并允许灵活地指定或自动推断解析格式。
 * </p>
 *
 * @author hugh
 * @since 2.7.5
 */
public class Dates {

    // 日期对象
    private Date date;
    // 待解析的日期字符串
    private Object text;
    // 解析日期时使用的格式
    private String format;

    /**
     * 构造方法，根据给定的日期文本创建一个 {@code Dates} 实例。
     * <p>
     * 使用默认的日期时间格式 {@code DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC}
     * (即 "yyyy-MM-dd HH:mm:ss") 进行解析。
     * </p>
     *
     * @param text 待解析的日期字符串或时间戳。
     */
    public Dates(Object text) {
        this(text, null);
    }

    /**
     * 构造方法，根据给定的日期文本和指定的格式创建一个 {@code Dates} 实例。
     * <p>
     * 如果传入的 {@code format} 为空或 {@code null}，
     * 则会回退到使用默认的日期时间格式 {@code DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC}。
     * </p>
     *
     * @param text   待解析的日期字符串或时间戳。
     * @param format 解析日期时使用的格式字符串，例如 "yyyy-MM-dd HH:mm:ss"。
     */
    public Dates(Object text, String format) {
        this.text = text;
        if (EmptyUtils.isEmpty(format)) {
            format = DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC;
        }
        this.format = format;
    }

    public Dates setFormat(String format) {
        this.format = format;
        return this;
    }

    /**
     * 静态工厂方法，用于创建 Dates 实例。
     *
     * @param text 待解析的日期字符串
     * @return 新的 Dates 实例
     */
    public static Dates on(Object text) {
        return new Dates(text);
    }

    /**
     * 解析日期字符串或时间戳。
     * <p>
     * 该方法尝试将存储在 {@code this.text} 中的字符串按照 {@code this.format} 进行日期解析。
     * 如果首次解析失败，会进一步判断异常的根源是否为 {@link java.text.ParseException}
     * 并且 {@code this.text} 是否为有效的时间戳（秒级或毫秒级）。
     * <p>
     * 如果是时间戳，则会尝试将其解析为日期。
     * 如果既不是有效日期格式也不是有效时间戳，则会重新抛出原始解析异常。
     * </p>
     *
     * @return 当前 {@link Dates} 对象本身，其中 {@code date} 字段包含了成功解析后的日期。
     * @throws ToolboxException 如果日期字符串既不符合指定的格式也不是有效的时间戳，
     *                          或者在解析时间戳过程中出现任何错误，则抛出此异常。
     */
    public Dates parse() {
        try {
            // 尝试解析日期字符串
            date = DateUtils.parse(this.text, this.format);
            return this;
        } catch (ToolboxException toolboxException) {
            // 检查异常的原因是否是 ParseException
            if (toolboxException.getCause() instanceof ParseException && (DateUtils.isTimestamp(String.valueOf(this.text)))) {
                // 如果是时间戳，则尝试解析时间戳
                if (DateUtils.isTimestampInMilli(String.valueOf(this.text))) {
                    date = DateUtils.parseTimestamp(String.valueOf(this.text));
                } else {
                    String s = DateUtils.formatTimestampSecond(String.valueOf(this.text));
                    date = DateUtils.parse(s);
                }
                return this;
            }
            // 如果解析失败且不是时间戳，则抛出异常
            throw new ToolboxException(toolboxException.getCause());
        }
    }

    /**
     * 获取解析后的日期对象。
     *
     * @return 解析后的日期对象
     */
    public Date toDate() {
        return date;
    }

    /**
     * 使用默认格式（年-月-日 时:分:秒）格式化日期对象为字符串。
     *
     * @return 格式化后的日期字符串
     */
    public String format() {
        return format(DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC);
    }

    /**
     * 使用指定格式格式化日期对象为字符串。
     *
     * @param format 指定的日期格式
     * @return 格式化后的日期字符串
     */
    public String format(String format) {
        return DateUtils.format(date, format);
    }
}
