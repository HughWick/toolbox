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
     *
     * @param idCard 身份证号码
     * @return boolean 合法返回true，否则返回false
     */
    public boolean isIdCard(String idCard) {
        if (idCard.length() == 15) {
            return IdCardValidator.is15Place(idCard);
        }
        return IdCardValidator.is18Place(idCard);
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
