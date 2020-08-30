package com.github.hugh.core;

public class DateCode {

    /**
     * 去掉格式(-)符号后完整：年月
     */
    public static final String YEAR_MONTH_SIMPLE = "yyyyMM";

    /**
     * 日期格式：年-月
     */
    public static final String YEAR_MONTH = "yyyy-MM";


    /**
     * 日期格式：年-月-日
     */
    public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";

    /**
     * 去掉格式符号后完整：年月日
     */
    public static final String YEAR_MONTH_DAY_SIMPLE = "yyyyMMdd";


    /**
     * 完整的日期格式
     * <p>年-月-日 时:分:秒</p>
     */
    public static final String YEAR_MONTH_DAY_HOUR_MIN_SEC = "yyyy-MM-dd HH:mm:ss";

    /**
     * 去掉格式符号后日期
     * <p>年月日时分秒</p>
     */
    public static final String YEAR_MONTH_DAY_HOUR_MIN_SEC_SIMPLE = "yyyyMMddHHmmss";
}
