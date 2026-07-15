package com.github.hugh.crypto.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponCodeEngineTest {

    @Test
    @DisplayName("验证生成的券码格式与自检功能")
    void testCouponWorkflow() {
        // 生成优惠券
        String couponCode = CouponCodeEngine.generate();
        System.out.println("生成的优惠券码: " + couponCode);
        Assertions.assertEquals(12, couponCode.length());
        // 正常校验，必须通过
        boolean isValid = CouponCodeEngine.verify(couponCode);
        Assertions.assertTrue(isValid, "正常的券码校验应该通过");
    }

    @Test
    @DisplayName("验证伪造、输入错误的券码能被快速拦截")
    void testInvalidCouponInterception() {
        String couponCode = CouponCodeEngine.generate(); // 假设是 "abcde56789XY"

        // 1. 篡改其中一个字符（模拟用户打错字）
        char[] chars = couponCode.toCharArray();
        chars[3] = chars[3] == 'A' ? 'B' : 'A'; // 强行改变第四位
        String tamperedCode = new String(chars);

        // 2. 校验篡改后的券，必须返回 false（不查库直接拒绝）
        boolean isValid = CouponCodeEngine.verify(tamperedCode);
        Assertions.assertFalse(isValid, "被篡改过的券码应该校验失败");
        System.out.println("篡改码校验结果: " + isValid + " (已被安全拦截)");
    }
}
