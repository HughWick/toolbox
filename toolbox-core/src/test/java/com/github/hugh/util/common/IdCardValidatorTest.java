package com.github.hugh.util.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IdCardValidatorTest {
    @Test
    void convertIdCarBy15bit_NullInput_ReturnsNull() {
        assertNull(IdCardValidator.convertIdCarBy15bit(null));
    }

    @Test
    void convertIdCarBy15bit_InvalidLength_ReturnsNull() {
        assertNull(IdCardValidator.convertIdCarBy15bit("1234567890123456")); // 16位
    }

    @Test
    void convertIdCarBy15bit_NonNumericInput_ReturnsNull() {
        assertNull(IdCardValidator.convertIdCarBy15bit("12345678901234A")); // 包含字母
    }

    @Test
    void convertIdCarBy15bit_InvalidProvinceCode_ReturnsNull() {
        assertNull(IdCardValidator.convertIdCarBy15bit("003456789012345")); // 无效的省份代码
    }

    @Test
    void convertIdCarBy15bit_InvalidDate_ReturnsNull() {
        assertNull(IdCardValidator.convertIdCarBy15bit("113456789012345")); // 无效的日期
    }

    @Test
    void convertIdCarBy15bit_ValidInput_ReturnsValid18BitIdCard() {
        String idCard15 = "110105670401001";
        String expectedIdCard18 = "110105196704010013";
        assertEquals(expectedIdCard18, IdCardValidator.convertIdCarBy15bit(idCard15));
    }

    @Test
    void validateIdCard10_TaiwanUnknownGender_ReturnsCorrectInfo() {
        String[] result = IdCardValidator.validateIdCard10("A323456789");
        assertEquals("台湾", result[0]);
        assertEquals("N", result[1]);
        assertEquals("false", result[2]);
    }


    @Test
    void validateIdCard10_HongKong_Invalid_ReturnsFalse() {
        String[] result = IdCardValidator.validateIdCard10("AB123456(8)");
        assertEquals("香港", result[0]);
        assertEquals("N", result[1]);
        assertEquals("false", result[2]);
    }

    @Test
    void validateIdCard10_InvalidPattern_ReturnsNull() {
        String[] result = IdCardValidator.validateIdCard10("12345678901");
        assertNull(result);
    }
}
