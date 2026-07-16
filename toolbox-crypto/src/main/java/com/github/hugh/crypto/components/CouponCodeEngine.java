package com.github.hugh.crypto.components;

import com.github.hugh.crypto.Crc16Utils;
import com.soundicly.jnanoidenhanced.jnanoid.NanoIdUtils;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * 优惠券码生成与校验引擎
 * <p>本类采用 <b>"随机载荷 (NanoID) + CRC16 校验位"</b> 的设计方案。
 * 可以在不查询数据库的情况下，快速本地校验优惠券码的合法性（防伪、防盲猜）。</p>
 *
 * <p><b>字符集设计：</b>排除了易混淆字符（0, 1, o, I, l 等），共 54 个字符，极大地提升了用户手动输入的体验。</p>
 * @since 3.0.24
 */
public class CouponCodeEngine {
    private CouponCodeEngine(){}
    /**
     * 排除易混淆字符的字母表（共 54 个字符）
     * 避免用户将 1 与 l/I 混淆，将 0 与 O/o 混淆。
     */
    private static final String ALPHABET = "23456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";

    /**
     * 默认的随机载荷部分长度（生成的最终券码长度 = 默认载荷长度 10 + 2位校验码 = 12位）
     */
    private static final int DEFAULT_PAYLOAD_LIMIT = 10;

    /**
     * 校验码固定长度（CRC16 转换为自定义长度字符后占 2 位）
     */
    private static final int VERIFY_CODE_LENGTH = 2;

    /**
     * 生成默认长度（12位）的带 CRC16 校验位的优惠券码
     *
     * @return 12 位优惠券码（10位随机载荷 + 2位校验码）
     */
    public static String generate() {
        return generate(DEFAULT_PAYLOAD_LIMIT);
    }

    /**
     * 生成自定义长度的带 CRC16 校验位的优惠券码
     *
     * @param payloadLength 随机载荷部分的长度（必须大于 0）
     * @return 最终的优惠券码（长度 = payloadLength + 2）
     * @throws IllegalArgumentException 如果传入的长度小于或等于 0
     */
    public static String generate(int payloadLength) {
        if (payloadLength <= 0) {
            throw new IllegalArgumentException("载荷长度必须大于 0");
        }
        // 生成指定长度的随机 Nano ID 载荷
        String payload = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, ALPHABET, payloadLength);
        // 将载荷转换为 Hex 16 进制字符串（CRC 工具类入参要求）
        String hexPayload = HexFormat.of().formatHex(payload.getBytes(StandardCharsets.UTF_8));
        // 计算载荷的 CRC16 校验值，生成 2 位校验码
        String verCode = Crc16Utils.getVerCode(hexPayload, VERIFY_CODE_LENGTH);
        // 拼接返回最终的优惠券码
        return payload + verCode;
    }

    /**
     * 验证默认长度（12位）的优惠券码是否合法（不查询数据库）
     *
     * @param code 待验证的 12 位优惠券码
     * @return {@code true} 代表校验通过，该券码格式合法；{@code false} 代表券码伪造或输入错误
     */
    public static boolean verify(String code) {
        return verify(code, DEFAULT_PAYLOAD_LIMIT);
    }

    /**
     * 验证自定义长度的优惠券码是否合法（不查询数据库）
     *
     * @param code 待验证的优惠券码
     * @param payloadLength 生成该券码时指定的随机载荷长度
     * @return {@code true} 代表校验通过；{@code false} 校验失败
     */
    public static boolean verify(String code, int payloadLength) {
        // 基础防御：空指针校验及长度校验（总长度必须等于 载荷长度 + 2位校验码）
        int expectedTotalLength = payloadLength + VERIFY_CODE_LENGTH;
        if (code == null || code.length() != expectedTotalLength) {
            return false;
        }
        // 拆分出随机载荷部分和末尾的 2 位校验码
        String payload = code.substring(0, payloadLength);
        String inputVerCode = code.substring(payloadLength);
        // 将载荷转换为 Hex 字符串，重新计算期望的校验码
        String hexPayload = HexFormat.of().formatHex(payload.getBytes(StandardCharsets.UTF_8));
        String expectedVerCode = Crc16Utils.getVerCode(hexPayload, VERIFY_CODE_LENGTH);
        // 比较输入的校验码和计算出的校验码（大小写敏感）
        return expectedVerCode.equals(inputVerCode);
    }
}
