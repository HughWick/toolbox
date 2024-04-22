package com.github.hugh.components.datetime;

import com.github.hugh.constant.DateCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.DateUtils;
import com.github.hugh.util.EmptyUtils;

import java.text.ParseException;
import java.util.Date;

/**
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

    public Dates(Object text) {
        this(text, null);
    }

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
     * 解析日期字符串，并将解析结果存储到日期对象中。
     * 如果解析失败，则尝试解析为时间戳格式。
     *
     * @return 当前 Dates 实例
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
