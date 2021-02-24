package com.github.hugh.util.regex;

import java.util.regex.Pattern;

/**
 * 密码校验正则
 *
 * @author hugh
 * @since 1.5.2
 */
public class PasswordRegex {

    /**
     * <p>要包括大小写字母、数字及标点符号的其中两项，并且长度为6-20</p>
     */
    private static final Pattern ALPHABET_AND_NUMBERS_PUNCTUATION = Pattern.compile("^(?![A-Za-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,20}$");

    /**
     * 中等密码强度
     * <p>密码长度6-20位，须包含大小写字母、数字、特殊符号至少2种或以上元素！</p>
     *
     * @param password 密码
     * @return boolean
     */
    public static boolean moderate(String password) {
        return RegexUtils.isPatternMatch(password, ALPHABET_AND_NUMBERS_PUNCTUATION);
    }
}
