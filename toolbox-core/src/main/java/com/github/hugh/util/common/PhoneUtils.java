package com.github.hugh.util.common;

import com.github.hugh.util.EmptyUtils;

/**
 * 手机号码工具类
 *
 * @author hugh
 * @version 1.2.8
 */
public class PhoneUtils {

    /**
     * 手机号码前三后四脱敏
     * <ul>
     * <li>例：</li>
     * <li>格式：191****0393</li>
     * </ul>
     *
     * @param mobile 手机号码
     * @return String 脱敏后的字符串
     */
    public static String encrypt(String mobile) {
        if (EmptyUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
