package com.github.hugh.util.common;

import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.base.IdCardValidator;

/**
 * 身份证工具类
 *
 * @author hugh
 * @version 1.2.8
 */
public class IdCardUtils {

    /**
     * 验证所有的身份证的合法性
     * <ul>
     *     <li>该方法包含一代身份证15位的验证方法</li>
     * </ul>
     *
     * @param idCard 身份证号码
     * @return boolean 合法返回true，否则返回false
     */
    public static boolean isIdCard(String idCard) {
        if (idCard.length() == 15) {
            return IdCardValidator.is15Place(idCard);
        }
        return IdCardValidator.is18Place(idCard);
    }

    /**
     * 字符串不是15位的身份证号码
     *
     * @param idCard 身份证号码
     * @return boolean {@code true} 不合法
     */
    public static boolean isNotIdCard(String idCard) {
        return !isIdCard(idCard);
    }

    /**
     * 校验字符串是18位的身份证号码
     *
     * @param idCard 身份证号码
     * @return boolean {@code true} 正确
     */
    public static boolean is18Place(String idCard) {
        return IdCardValidator.is18Place(idCard);
    }

    /**
     * 字符串不是18位的身份证号码
     *
     * @param idCard 身份证号码
     * @return boolean {@code true} 不合法
     */
    public static boolean isNot18Place(String idCard) {
        return !IdCardValidator.is18Place(idCard);
    }

    /**
     * 将身份证号码字符串只保留前三后四脱,其他替换为*
     *
     * @param idCard 身份证号码
     * @return String 加密后身份证号码
     */
    public static String encrypt(String idCard) {
        if (EmptyUtils.isEmpty(idCard) || (idCard.length() < 8)) {
            return idCard;
        }
        return idCard.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }
}
