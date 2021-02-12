package com.github.hugh.util.common;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.EmptyUtils;

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
     * @return boolean {@code true} 合法
     */
    public static boolean isIdCard(String idCard) {
        if (EmptyUtils.isEmpty(idCard)) {
            return false;
        }
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
     * 将身份证号码字符串只保留前三后四位,其他替换为*
     *
     * @param idCard 身份证号码
     * @return String 加密后身份证号码
     */
    public static String encrypt(String idCard) {
        return encrypt(idCard, 3, 4);
    }

    /**
     * 将字符串的身份证号码按照指定的前后保留位数进行脱敏
     *
     * @param idCard 身份证号码
     * @param before 前保留位数
     * @param rear   后保留位数
     * @return String 加密后身份证号码
     * @since 1.2.9
     */
    public static String encrypt(String idCard, int before, int rear) {
        if (EmptyUtils.isEmpty(idCard)) {
            return idCard;
        }
        int length = idCard.length();
        if (before < 0) {
            throw new ToolboxException("number before error !");
        }
        if (rear < 0) {
            throw new ToolboxException("number rear error !");
        }
        // 字符串长度小于前后要脱敏保留的数之和
        int total = before + rear;
        if (length <= total) {
            throw new ToolboxException("id card number length error !");
        }
        return idCard.replaceAll("(?<=\\w{" + before + "})\\w(?=\\w{" + rear + "})", "*");
    }
}
