package com.github.hugh.crypto.base;

import com.github.hugh.crypto.Md5Utils;
import com.github.hugh.crypto.exception.CryptoException;
import com.github.hugh.crypto.model.SecureDTO;
import com.github.hugh.util.DateUtils;

import java.util.Date;

/**
 * 数据加密工具类
 *
 * @author hugh
 * @since 2.1.11
 */
public class SecureUtils {

    private SecureUtils() {
    }

    /**
     * 数据加密
     *
     * @param appkey    key
     * @param token     token
     * @param data      数据
     * @param notifyUrl 回调URL
     * @param timestamp 时间戳
     * @return String
     */
    public static String signature(String appkey, String token, String data, String notifyUrl, String timestamp) {
        return Md5Utils.lowerCase(appkey + token + data + notifyUrl + timestamp);
    }

    /**
     * 验证签名
     * <p>
     * 默认增加验证时间戳是否超过30分钟
     * </p>
     *
     * @param secureDTO 参数信息
     * @return boolean
     */
    public static boolean verifySignature(SecureDTO secureDTO) {
        return verifySignature(secureDTO, 30);
    }

    /**
     * 验证
     *
     * @param secureDTO  参数实体类
     * @param difference 相差多少分钟
     * @return boolean
     */
    public static boolean verifySignature(SecureDTO secureDTO, int difference) {
        if (secureDTO == null) {
            throw new CryptoException("secureDTO is null");
        }
        // 验证时间戳有没有超过30分钟
        if ((difference > 0) && !verifyTimestamp(Long.parseLong(secureDTO.getTimestamp()), difference)) {
            return false;
        }
        String signature = signature(secureDTO.getAppkey(), secureDTO.getToken(), secureDTO.getData(), secureDTO.getNotifyUrl(), secureDTO.getTimestamp());
        return signature.equals(secureDTO.getSign());
    }

    /**
     * 验证时间戳
     *
     * @param timestamp  时间戳
     * @param difference 相差值
     * @return boolean
     */
    public static boolean verifyTimestamp(long timestamp, int difference) {
        Date date = DateUtils.parseTimestamp(timestamp);
        long l = DateUtils.minutesDifference(date, new Date());
        return l < difference;
    }
}
