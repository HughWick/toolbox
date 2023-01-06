package com.github.hugh.constant;

import com.github.hugh.constant.enums.StringEnum;

/**
 * 常用的字符串常量
 *
 * @author hugh
 * @since 2.4.10
 */
public class Str {

    private Str() {
    }

    /**
     * 英文逗号
     */
    public static final String COMMA = StringEnum.COMMA.getValue();

    /**
     * 英文小数点
     */
    public static final String POINT = StringEnum.POINT.getValue();

    /**
     * 反斜杠 {@code "\\"}
     */
    public static final String BACKSLASH = StringEnum.BACKSLASH.getValue();

    /**
     * 空字符串
     */
    public static final String EMPTY = StringEnum.EMPTY.getValue();

}
