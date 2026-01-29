package com.github.hugh.crypto.util;

/**
 * ISO 7064 Mod 37-2 校验算法工具类
 * <p>
 * 该算法用于生成和校验混合字母与数字的字符串校验位。
 * <br>
 * 特点：
 * <ul>
 *     <li>字符集：0-9, A-Z (共36个字符)</li>
 *     <li>能够检测出所有的单字符错误和相邻字符交换错误</li>
 *     <li>计算时会自动忽略非字母数字字符（如 '-', ' '）</li>
 * </ul>
 * 常见应用：ISBT 128 (医疗), 某些集装箱编号或自定义编码系统。
 * </p>
 */
public class Iso7064Utils {
    private Iso7064Utils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 字符集：0-9 (0-9), A-Z (10-35)
     */
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 计算 ISO 7064 Mod 37-2 校验位
     *
     * @param input 待计算的原始字符串 (可以包含分隔符，计算时会被忽略)
     * @return 计算出的校验字符 ('0'-'9', 'A'-'Z')
     */
    public static char computeCheckDigit(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        int p = 37;
        String upperInput = input.toUpperCase();
        boolean hasValidChar = false;
        for (int i = 0; i < upperInput.length(); i++) {
            char c = upperInput.charAt(i);
            int val = ALPHABET.indexOf(c);
            // 忽略非字母数字字符
            if (val == -1) {
                continue;
            }
            hasValidChar = true;
            // 核心公式: S = (P + val) mod 37
            int s = (p + val) % 37;
            // 如果 S=0，按 ISO 规范需视为 37 进行下一步运算
            // P_new = (S * 2) mod 37
            p = (s == 0 ? 37 : s) * 2 % 37;
        }
        if (!hasValidChar) {
            throw new IllegalArgumentException("Input does not contain any valid alphanumeric characters");
        }
        // 计算校验值: (38 - P) mod 37
        int checkVal = (38 - p) % 37;
        // 理论上 Mod 37-2 产生的校验位必定在 0-35 之间 (对应的字符是 0-Z)
        // 值为 36 ('*') 在此模式下通常不作为输出字符
        if (checkVal == 36) {
            throw new IllegalStateException("Algorithm yielded 36 ('*'), which is invalid for Mod 37-2 check digit");
        }
        return ALPHABET.charAt(checkVal);
    }

    /**
     * 校验字符串是否符合 ISO 7064 Mod 37-2 标准
     * <p>
     * 该方法假设输入的<b>最后一个有效字母/数字</b>是校验位，前面的有效字符是数据。
     * 例如："G8-S" -> 数据是 "G8"，校验位是 'S'
     * </p>
     *
     * @param inputWithCheckDigit 包含校验位的完整字符串
     * @return true 校验通过, false 校验失败或输入无效
     */
    public static boolean isValid(String inputWithCheckDigit) {
        if (inputWithCheckDigit == null || inputWithCheckDigit.length() < 2) {
            return false;
        }
        // 提取所有有效字符
        StringBuilder validChars = new StringBuilder();
        String upperInput = inputWithCheckDigit.toUpperCase();
        for (int i = 0; i < upperInput.length(); i++) {
            char c = upperInput.charAt(i);
            if (ALPHABET.indexOf(c) != -1) {
                validChars.append(c);
            }
        }
        // 有效字符长度必须至少为2 (1个数据 + 1个校验位)
        if (validChars.length() < 2) {
            return false;
        }
        // 分离数据和校验位
        // 最后一个字符是校验位
        char providedCheckDigit = validChars.charAt(validChars.length() - 1);
        // 前面的是数据
        String data = validChars.substring(0, validChars.length() - 1);
        try {
            // 重新计算校验位并对比
            char calculatedCheckDigit = computeCheckDigit(data);
            return calculatedCheckDigit == providedCheckDigit;
        } catch (Exception e) {
            return false;
        }
    }
}
