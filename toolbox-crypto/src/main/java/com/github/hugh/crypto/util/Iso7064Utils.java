package com.github.hugh.crypto.util;

public class Iso7064Utils {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 计算 ISO 7064 Mod 37-2 校验位
     * 会自动忽略非字母/数字字符（如 '-', '/'）
     */
    public static char computeCheckDigit(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        int p = 36; // 初始值 (P0)
        String upperInput = input.toUpperCase();
        for (int i = 0; i < upperInput.length(); i++) {
            char c = upperInput.charAt(i);
            int val = ALPHABET.indexOf(c);
            // 如果是符号（不在ALPHABET中），直接跳过
            if (val == -1) {
                continue;
            }
            // 核心算法: S = (P + D) mod 37
            int s = (p + val) % 37;
            // 如果 S=0，调整为 37 (ISO 7064 规定) -> 实际计算中可简化逻辑
            // 实际上: P_new = (S * 2) mod 37
            // 注意：当 s=0 时，ISO标准处理比较特殊，但在Mod 37-2中，
            // 只要保证 sum 不为 0 即可，或按照公式严格执行。
            // 简便公式：result = ((result + val) * 2) % 37
            p = (s == 0 ? 37 : s) * 2 % 37;
        }
        // 计算校验字符: (38 - P) mod 37
        int checkVal = (38 - p) % 37;
        // 映射回字符 (如果是0-35直接映射，理论上Mod 37-2的结果必定在0-35之间)
        // 极少数情况如果算出36 (即'*')，在严格Mod 37-2中是不作为校验位输出的，
        // 但如果出现，通常意味着该序列无法生成有效校验位(碰撞)，不过这种情况极罕见。
        if (checkVal == 36) {
            // 这种情况在 ISO 7064 Mod 37-2 规范中通常定义为无效或需填充
            // 但作为工程实现，可以 fallback 到 '0' 或者报错，这里按标准通常是不会产生的。
            throw new RuntimeException("Algorithm result yielded 36, which is not in 0-Z set");
        }
        return ALPHABET.charAt(checkVal);
    }

    // 校验方法
    public static boolean isValid(String inputWithCheckDigit) {
        if (inputWithCheckDigit == null || inputWithCheckDigit.length() < 2) return false;
        // 分离数据和校验位
        char inputCheck = inputWithCheckDigit.charAt(inputWithCheckDigit.length() - 1);
        String data = inputWithCheckDigit.substring(0, inputWithCheckDigit.length() - 1);
        try {
            return computeCheckDigit(data) == inputCheck;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String serial = "ABC-12345";
        char check = computeCheckDigit(serial);
        System.out.println("Serial: " + serial + check);
        // 假设算法输出 'Y'，结果为 ABC-12345Y
    }
}
