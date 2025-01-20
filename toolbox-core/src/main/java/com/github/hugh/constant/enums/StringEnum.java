package com.github.hugh.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 常用字符串枚举
 * 使用静态的常量{@link  com.github.hugh.constant.StrPool}
 *
 * @author hugh
 * @since 2.4.9
 */
@ToString
@Getter
@AllArgsConstructor
@Deprecated
public enum StringEnum {
    /**
     * 字符串的null
     */
    NULL("null"),

    /**
     * 逗号
     */
    COMMA(","),

    /**
     * 小数点
     */
    POINT("."),

    /**
     * 字符串常量：回车符 {@code "\r"} <br>
     * 解释：该字符常用于表示 Linux 系统和 MacOS 系统下的文本换行
     */
    CR("\r"),

    /**
     * 字符串常量：换行符 {@code "\n"}
     */
    LF("\n"),

    /**
     * 字符常量：反斜杠 {@code "\\"}
     */
    BACKSLASH("\\"),

    /**
     * 字符串常量：空 JSON {@code "{}"}
     */
    EMPTY_JSON("{}"),

    /**
     * 字符串常量：Windows 换行 {@code "\r\n"} <br>
     * 解释：该字符串常用于表示 Windows 系统下的文本换行
     */
    CRLF("\r\n"),
    /**
     * 空字符串
     */
    EMPTY("");

    private final String value;
}
