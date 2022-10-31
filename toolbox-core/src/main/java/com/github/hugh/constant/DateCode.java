package com.github.hugh.constant;

/**
 * 常用日期格式
 *
 * @author hugh
 * @since 1.0.0
 */
public class DateCode {

    /**
     * [年-月]的日期格式
     */
    public static final String YEAR_MONTH = "yyyy-MM";

    /**
     * [年月]的日期格式
     */
    public static final String YEAR_MONTH_SIMPLE = "yyyyMM";

    /**
     * [年-月-日]的日期格式
     */
    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";

    /**
     * [年月日]的日期格式
     */
    public static final String YEAR_MONTH_DAY_SIMPLE = "yyyyMMdd";


    /**
     * [年-月-日 时:分:秒]完整的日期格式
     */
    public static final String YEAR_MONTH_DAY_HOUR_MIN_SEC = "yyyy-MM-dd HH:mm:ss";

    /**
     * [年月日时分秒]去掉格式符号后的完整日期格式
     */
    public static final String YEAR_MONTH_DAY_HOUR_MIN_SEC_SIMPLE = "yyyyMMddHHmmss";

    /**
     * 时分秒
     */
    public static final String HOUR_MIN_SEC_FORMAT_SIMPLE = "HHmmss";

    /**
     * 日期对象转字符串后的cst时间格式
     */
    public static final String CST_FORM = "EEE MMM dd HH:mm:ss zzz yyyy";
}
