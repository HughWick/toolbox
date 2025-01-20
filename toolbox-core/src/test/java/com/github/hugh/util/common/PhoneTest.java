package com.github.hugh.util.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 手机工具测试类
 *
 * @author hugh
 * @version 1.0.0
 */
class PhoneTest {

    @Test
    void test01() {
        assertEquals("138****4872", PhoneUtils.encrypt("13825004872"));
        String str2 = "138****";
        assertEquals(str2, PhoneUtils.encrypt(str2));
        assertEquals("", PhoneUtils.encrypt(""));
    }
}
