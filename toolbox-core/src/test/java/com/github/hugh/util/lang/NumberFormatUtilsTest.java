package com.github.hugh.util.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * NumberFormatUtils 的单元测试类
 */
class NumberFormatUtilsTest {

    @Test
    @DisplayName("测试 Double 类型：常规范围精度 (0-10位) 的四舍五入")
    void testRoundDouble_NormalScale() {
        // 基础四舍五入测试
        assertEquals(3.14, NumberFormatUtils.round(3.14159D, 2));
        assertEquals(3.15, NumberFormatUtils.round(3.145D, 2)); // 5 向上进位
        assertEquals(3.14, NumberFormatUtils.round(3.144D, 2)); // 4 舍去

        // 整数和 0 的处理
        assertEquals(3.0, NumberFormatUtils.round(3.0D, 2));
        assertEquals(0.0, NumberFormatUtils.round(0.0D, 5));

        // 最大缓存精度 (scale = 10)
        assertEquals(1.1234567891, NumberFormatUtils.round(1.123456789123D, 10));
    }

    @Test
    @DisplayName("测试 Double 类型：负数的四舍五入")
    void testRoundDouble_NegativeValues() {
        assertEquals(-3.14, NumberFormatUtils.round(-3.14159D, 2));
        assertEquals(-3.14, NumberFormatUtils.round(-3.145D, 2));
    }

    @Test
    @DisplayName("测试 Double 类型：超界精度 (降级使用 Math.pow 逻辑)")
    void testRoundDouble_ScaleOutOfBounds() {
        // scale > 10 (例如 11)
        assertEquals(3.14159265359, NumberFormatUtils.round(3.141592653589D, 11));

        // scale < 0 (例如 -1，相当于精确到十位)
        // 31.5 * 10^-1 = 3.15 -> round 后是 3 -> 3 / 10^-1 = 30.0
        assertEquals(30.0, NumberFormatUtils.round(31.4D, -1));
        assertEquals(40.0, NumberFormatUtils.round(35.5D, -1));
    }

    @Test
    @DisplayName("测试 Double 类型：Null 值输入应原样返回 Null")
    void testRoundDouble_NullInput() {
        assertNull(NumberFormatUtils.round((Double) null, 2));
    }

    @Test
    @DisplayName("测试 Float 类型：重载方法测试")
    void testRoundFloat() {
        // 常规四舍五入
        assertEquals(3.14, NumberFormatUtils.round(3.14159F, 2));
        assertEquals(3.14, NumberFormatUtils.round(3.145F, 2));

        // 负数处理
        assertEquals(-3.14, NumberFormatUtils.round(-3.14159F, 2));

        // Null 处理
        assertNull(NumberFormatUtils.round((Float) null, 2));
    }
}
