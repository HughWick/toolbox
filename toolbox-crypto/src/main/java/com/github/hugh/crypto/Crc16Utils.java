package com.github.hugh.crypto;

import com.github.hugh.crypto.exception.CryptoException;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.regex.RegexUtils;

/**
 * CRC16相关计算工具类
 * <p>encode: utf-8</p>
 *
 * @author hugh
 * @since 2.0.1
 */
public class Crc16Utils {

    private Crc16Utils() {
    }

    private static final byte[] crc16_tab_h = {(byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1, (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0, (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40};

    private static final byte[] crc16_tab_l = {(byte) 0x00, (byte) 0xC0, (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03,
            (byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06, (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5,
            (byte) 0xC4, (byte) 0x04, (byte) 0xCC, (byte) 0x0C, (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF,
            (byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA, (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09,
            (byte) 0x08, (byte) 0xC8, (byte) 0xD8, (byte) 0x18, (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB,
            (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE, (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D,
            (byte) 0x1C, (byte) 0xDC, (byte) 0x14, (byte) 0xD4, (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17,
            (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12, (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1,
            (byte) 0xD0, (byte) 0x10, (byte) 0xF0, (byte) 0x30, (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3,
            (byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6, (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35,
            (byte) 0x34, (byte) 0xF4, (byte) 0x3C, (byte) 0xFC, (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F,
            (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A, (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9,
            (byte) 0xF8, (byte) 0x38, (byte) 0x28, (byte) 0xE8, (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B,
            (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E, (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED,
            (byte) 0xEC, (byte) 0x2C, (byte) 0xE4, (byte) 0x24, (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7,
            (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2, (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21,
            (byte) 0x20, (byte) 0xE0, (byte) 0xA0, (byte) 0x60, (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3,
            (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6, (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65,
            (byte) 0x64, (byte) 0xA4, (byte) 0x6C, (byte) 0xAC, (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F,
            (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A, (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9,
            (byte) 0xA8, (byte) 0x68, (byte) 0x78, (byte) 0xB8, (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B,
            (byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E, (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD,
            (byte) 0xBC, (byte) 0x7C, (byte) 0xB4, (byte) 0x74, (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7,
            (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2, (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71,
            (byte) 0x70, (byte) 0xB0, (byte) 0x50, (byte) 0x90, (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53,
            (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56, (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95,
            (byte) 0x94, (byte) 0x54, (byte) 0x9C, (byte) 0x5C, (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F,
            (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A, (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59,
            (byte) 0x58, (byte) 0x98, (byte) 0x88, (byte) 0x48, (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B,
            (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E, (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D,
            (byte) 0x4C, (byte) 0x8C, (byte) 0x44, (byte) 0x84, (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47,
            (byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42, (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81,
            (byte) 0x80, (byte) 0x40};

    /**
     * 计算CRC16校验
     *
     * @param data 需要计算的数组
     * @return int CRC16校验值
     */
    private static int calcCrc16(byte[] data) {
        return calcCrc16(data, 0, data.length);
    }

    /**
     * 计算CRC16校验
     *
     * @param data   需要计算的数组
     * @param offset 起始位置
     * @param len    长度
     * @return int CRC16校验值
     */
    private static int calcCrc16(byte[] data, int offset, int len) {
        return calcCrc16(data, offset, len, 0xffff);
    }

    /**
     * 计算CRC16校验
     *
     * @param data   需要计算的数组
     * @param offset 起始位置
     * @param len    长度
     * @param preval 之前的校验值
     * @return int CRC16校验值
     */
    private static int calcCrc16(byte[] data, int offset, int len, int preval) {
        int ucCRCHi = (preval & 0xff00) >> 8;
        int ucCRCLo = preval & 0x00ff;
        int iIndex;
        for (int i = 0; i < len; ++i) {
            iIndex = (ucCRCLo ^ data[offset + i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ crc16_tab_h[iIndex];
            ucCRCHi = crc16_tab_l[iIndex];
        }
        return ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
    }

    /**
     * 生成十位数随机码
     * <p>前8位为uuid截取</p>
     * <p>后两位为校验码</p>
     *
     * @return String
     */
    public static String generate() {
        return generate(8);
    }

    /**
     * 生成指定长度的crc
     * <p>根据指定长度生成crc 编码</p>
     * <p>PS：字符串后两位都为校验码</p>
     * <p>例：如果生成一个10位的crc,结果则生成返回为12位长度的字符串,后两位固定为校验码</p>
     *
     * @param length 长度
     * @return String
     * @since 1.4.12
     */
    public static String generate(int length) {
        return generate(length, 2);
    }

    /**
     * 生成指定长度的小写+数组组合的crc字符串
     * <p>根据指定长度生成crc 编码</p>
     * <p>PS：字符串后两位都为校验码</p>
     * <p>例：如果生成一个10位的crc,结果则生成返回为12位长度的字符串,后两位固定为校验码</p>
     *
     * @param length 长度
     * @return String
     * @since 2.4.3
     */
    public static String generateLowerCase(int length) {
        return generateLowerCase(length, 2);
    }


    /**
     * 生成指定长度的小写+数组组合的crc字符串
     * <p>根据指定长度生成crc 编码</p>
     * <p>例：如果生成一个10位的crc,结果则生成返回为12位长度的字符串,后两位固定为校验码</p>
     *
     * @param length       长度
     * @param verifyLength 验证码长度
     * @return String
     * @since 2.4.4
     */
    public static String generateLowerCase(int length, int verifyLength) {
        return generate(length, verifyLength, false);
    }

    /**
     * 生成指定长度的大写+数组组合的crc字符串
     * <p>根据指定长度生成crc 编码</p>
     *
     * @param length   长度
     * @param verifyLe 验证码长度
     * @return String
     * @since 2.4.3
     */
    public static String generate(int length, int verifyLe) {
        return generate(length, verifyLe, true);
    }

    /**
     * 生成指定长度的crc
     * <p>根据指定长度生成crc 编码</p>
     *
     * @param length       长度
     * @param verifyLength 验证码长度
     * @param flag         大小写标识
     * @return String
     * @since 2.4.3
     */
    private static String generate(int length, int verifyLength, boolean flag) {
        if (length <= 0) {
            throw new CryptoException("length error");
        }
        if (RegexUtils.isNotEvenNumber(verifyLength)) {
            throw new CryptoException("is not even number");
        }
        if(length <= verifyLength){
            throw new CryptoException("code length less than verify length error");
        }
        String random = AppkeyUtils.generate().substring(0, length);
        String code = random + getVerCode(random, verifyLength);//根据指定位数随机码、计算一个crc16的校验码
        if (flag) {
            return code.toUpperCase();
        }
        return code.toLowerCase();
    }

    /**
     * 根据字符串中的数据计算出 CRC 16 验证码
     *
     * @param data         字符串
     * @param verifyLength 验证码长度
     * @return String 验证码
     */
    public static String getVerCode(String data, int verifyLength) {
        int length = data.length() / 2;
        byte[] byteArray = new byte[length];
        for (int i = 0; i < length; i++) {
            byteArray[i] = (byte) Short.parseShort(data.substring(i * 2, (i * 2) + 2), 16);
        }
        int crc = calcCrc16(byteArray);
        int temp = verifyLength / 2;
        String a = String.format("%04x", crc).toUpperCase().substring(temp, verifyLength);
        String b = String.format("%04x", crc).toUpperCase().substring(0, temp);
        return a + b;
    }

    /**
     * 验证校验码是否正确
     * <p>由于命名不规范，标记为过期，使用{{@link #verifyCode(String)}}</p>
     *
     * @param str 字符串
     * @return boolean {@code true} 正确
     */
    @Deprecated
    public static boolean checkCode(String str) {
        return verifyCode(str);
    }

    /**
     * 验证校验码是否正确
     *
     * @param str 字符串
     * @return boolean {@code true} 正确
     * @since 2.4.3
     */
    public static boolean verifyCode(String str) {
        return verifyCode(str, 2);
    }

    /**
     * 验证校验码是否正确
     *
     * @param str          字符串
     * @param verifyLength 验证码长度
     * @return boolean {@code true} 正确
     * @since 2.4.3
     */
    public static boolean verifyCode(String str, int verifyLength) {
        if (EmptyUtils.isEmpty(str)) {
            return false;
        }
        if (RegexUtils.isNotEvenNumber(verifyLength)) {
            throw new CryptoException("is not even number");
        }
        try {
            String checkBody = str.substring(0, str.length() - verifyLength);// 随机码截取掉后两位验证码
            String sign = str.substring(str.length() - verifyLength);  // 获取验证码
            String checkCode = getVerCode(checkBody, verifyLength);
            return sign.equalsIgnoreCase(checkCode);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 计算CRC16 modbus校验码
     * <p>
     * 默认将字符串转化为16进制
     * </p>
     * <p>
     * 不交换高低位
     * </p>
     *
     * @param data 需要校验的字符串
     * @return String 校验码
     * @since 2.3.3
     */
    public static String getModbusChecksum(String data) {
        return getModbusChecksum(data, false);
    }

    /**
     * 计算CRC16 modbus校验码
     * <p>
     * 默认将字符串转化为16进制
     * </p>
     *
     * @param data     需要校验的字符串
     * @param sequence 交换高低位开关：false-低位在前，高位在后、true-地位在后，高位在前
     * @return String 校验码
     * @since 2.3.3
     */
    public static String getModbusChecksum(String data, boolean sequence) {
        data = data.replace(" ", "");
        int len = data.length();
        if (((len % 2) != 0)) {
            return "0000";
        }
        int num = len / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
        return getModbusChecksum(para, sequence);
    }

    /**
     * 计算CRC16校验码
     * <p>
     * 不交换高低位
     * </p>
     *
     * @param bytes 字节数组
     * @return {@link String} 校验码
     * @since 2.3.3
     */
    public static String getModbusChecksum(byte[] bytes) {
        return getModbusChecksum(bytes, false);
    }

    /**
     * 根据数组计算CRC16校验码
     *
     * @param bytes    字节数组
     * @param sequence 交换高低位开关：false-低位在前，高位在后、true-地位在后，高位在前
     * @return {@link String} 校验码
     * @since 2.3.3
     */
    public static String getModbusChecksum(byte[] bytes, boolean sequence) {
        //CRC寄存器全为1
        int crcHex = 0x0000ffff;
        //多项式校验值
        int polynomial = 0x0000a001;
        for (byte aByte : bytes) {
            crcHex ^= (aByte & 0x000000ff);
            for (int j = 0; j < 8; j++) {
                if ((crcHex & 0x00000001) != 0) {
                    crcHex >>= 1;
                    crcHex ^= polynomial;
                } else {
                    crcHex >>= 1;
                }
            }
        }
        //结果转换为16进制
        String result = Integer.toHexString(crcHex).toUpperCase();
        if (result.length() != 4) {
            StringBuilder stringBuilder = new StringBuilder("0000");
            result = stringBuilder.replace(4 - result.length(), 4, result).toString();
        }
        //交换高低位
        if (sequence) {
            return result.substring(2, 4) + result.substring(0, 2);
        }
        return result;
    }

    /**
     * 验证modbus内容是否与校验码一致
     * <p>
     * 不交换高低位
     * </p>
     *
     * @param data     需要甲酸校验码的字符串（不包含校验码）
     * @param checksum 校验码，4位
     * @return {@link String} 校验码
     * @since 2.3.3
     */
    public static boolean verifyModbus(String data, String checksum) {
        return verifyModbus(data, checksum, false);
    }

    /**
     * 验证modbus内容是否与校验码一致
     *
     * @param data     需要甲酸校验码的字符串（不包含校验码）
     * @param checksum 校验码，4位
     * @param sequence 交换高低位开关：false-低位在前，高位在后、true-地位在后，高位在前
     * @return {@link String} 校验码
     * @since 2.3.3
     */
    public static boolean verifyModbus(String data, String checksum, boolean sequence) {
        if (EmptyUtils.isEmpty(data)) {
            throw new CryptoException("data is empty");
        }
        if (EmptyUtils.isEmpty(checksum)) {
            throw new CryptoException("checksum is empty");
        }
        checksum = checksum.replace(" ", "").trim().strip();
        String modbusChecksum = getModbusChecksum(data, sequence);
        return modbusChecksum.equals(checksum);
    }
}
