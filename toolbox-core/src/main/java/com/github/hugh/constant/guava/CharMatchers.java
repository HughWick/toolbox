package com.github.hugh.constant.guava;

import com.google.common.base.CharMatcher;

/**
 * guava 常用的CharMatcher
 *
 * @author Hugh
 * @since 2.0.0
 **/
public class CharMatchers {

    /**
     * 数字0-9
     */
    public static final CharMatcher NUMBER_CHAR_MATCHER = CharMatcher.inRange('0', '9');

    /**
     * 数字与小数点
     */
    public static final CharMatcher NUMBERS_AND_DECIMAL_POINTS = NUMBER_CHAR_MATCHER.or(CharMatcher.is('.'));

}
