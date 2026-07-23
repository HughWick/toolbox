package com.github.hugh.util.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("测试 Double 基础格式化与去除末尾0")
    void testDoubleFormatTrimZeros() {
        assertNull(NumberFormatUtils.formatTrimZeros((Double) null));
        assertEquals("12", NumberFormatUtils.formatTrimZeros(12.00));
        assertEquals("12.3", NumberFormatUtils.formatTrimZeros(12.30));
        assertEquals("12.34", NumberFormatUtils.formatTrimZeros(12.34));
        assertEquals("0", NumberFormatUtils.formatTrimZeros(0.000));
    }

    @Test
    @DisplayName("测试 Double 四舍五入临界值（验证旧方法的 bug 是否解决）")
    void testDoubleRoundingBoundary() {
        // 关键点：1.005 在旧方法中会被误算为 "1"，新方法必须正确输出 "1.01"
        assertEquals("1.01", NumberFormatUtils.formatTrimZeros(1.005));
        assertEquals("3.56", NumberFormatUtils.formatTrimZeros(3.555));
        assertEquals("0.36", NumberFormatUtils.formatTrimZeros(0.355));
    }

    @Test
    @DisplayName("测试 Double 特殊异常值 (NaN 和 Infinity)")
    void testDoubleSpecialValues() {
        assertEquals("NaN", NumberFormatUtils.formatTrimZeros(Double.NaN));
        assertEquals("Infinity", NumberFormatUtils.formatTrimZeros(Double.POSITIVE_INFINITY));
        assertEquals("-Infinity", NumberFormatUtils.formatTrimZeros(Double.NEGATIVE_INFINITY));
    }

    @Test
    @DisplayName("测试 Float 基础格式化与精度防膨胀")
    void testFloatFormatTrimZeros() {
        assertNull(NumberFormatUtils.formatTrimZeros((Float) null));

        // 关键点：2.005f 强转 double 会变成 2.0049999...，测试新方法能否准确识别为 2.01
        assertEquals("2.01", NumberFormatUtils.formatTrimZeros(2.005f));
        assertEquals("12.3", NumberFormatUtils.formatTrimZeros(12.30f));
        assertEquals("0", NumberFormatUtils.formatTrimZeros(0.0f));
    }

    @Test
    @DisplayName("测试自定义保留位数 maxScale")
    void testCustomMaxScale() {
        assertEquals("1.235", NumberFormatUtils.formatTrimZeros(1.23456, 3));
        assertEquals("1.2", NumberFormatUtils.formatTrimZeros(1.23456, 1));
        assertEquals("1", NumberFormatUtils.formatTrimZeros(1.23456, 0));
    }

    @Test
    @DisplayName("测试 Double 零值转为整数 0")
    void testDoubleZeroValues() {
        assertEquals("0", NumberFormatUtils.formatZeroAsInt(0.0));
        assertEquals("0", NumberFormatUtils.formatZeroAsInt(0.00));
        assertEquals("0", NumberFormatUtils.formatZeroAsInt(-0.00));

        // 四舍五入后变成 0.00 的微小值，也应返回 "0"
        assertEquals("0", NumberFormatUtils.formatZeroAsInt(0.001, 2));
    }

    @Test
    @DisplayName("测试 Double 非零值保留末尾 0")
    void testDoubleNonZeroValues() {
        // 1.2 格式化为 2 位保留应为 "1.20"
        assertEquals("1.20", NumberFormatUtils.formatZeroAsInt(1.2));

        // 1.0 非零整数，格式化为 2 位应为 "1.00"
        assertEquals("1.00", NumberFormatUtils.formatZeroAsInt(1.0));

        // 正常小数保留
        assertEquals("12.34", NumberFormatUtils.formatZeroAsInt(12.34));
        assertEquals("1.01", NumberFormatUtils.formatZeroAsInt(1.005));
    }

    @Test
    @DisplayName("测试 Float 类型的零值与非零值")
    void testFloatValues() {
        assertEquals("0", NumberFormatUtils.formatZeroAsInt(0.0f));
        assertEquals("1.20", NumberFormatUtils.formatZeroAsInt(1.2f));
        assertEquals("2.01", NumberFormatUtils.formatZeroAsInt(2.005f));
    }

    @Test
    @DisplayName("测试 null 及特殊异常值")
    void testSpecialValues() {
        assertNull(NumberFormatUtils.formatZeroAsInt((Double) null));
        assertEquals("NaN", NumberFormatUtils.formatZeroAsInt(Double.NaN));
        assertEquals("Infinity", NumberFormatUtils.formatZeroAsInt(Double.POSITIVE_INFINITY));
    }

    @Test
    @DisplayName("测试 BigDecimal 零值转为整数 0")
    void testBigDecimalZeroValues() {
        assertEquals("0", NumberFormatUtils.formatZeroAsInt(BigDecimal.ZERO));
        assertEquals("0", NumberFormatUtils.formatZeroAsInt(new BigDecimal("0.00")));
        assertEquals("0", NumberFormatUtils.formatZeroAsInt(new BigDecimal("-0.00")));

        // 四舍五入后变成 0.00 的微小值，也应返回 "0"
        assertEquals("0", NumberFormatUtils.formatZeroAsInt(new BigDecimal("0.001"), 2));
    }

    @Test
    @DisplayName("测试 BigDecimal 非零值保留末尾 0")
    void testBigDecimalNonZeroValues() {
        // 1.2 格式化为 2 位保留应为 "1.20"
        assertEquals("1.20", NumberFormatUtils.formatZeroAsInt(new BigDecimal("1.2")));

        // 1.0 非零整数，格式化为 2 位应为 "1.00"
        assertEquals("1.00", NumberFormatUtils.formatZeroAsInt(new BigDecimal("1.0")));

        // 正常小数保留
        assertEquals("12.34", NumberFormatUtils.formatZeroAsInt(new BigDecimal("12.34")));
        assertEquals("1.01", NumberFormatUtils.formatZeroAsInt(new BigDecimal("1.005")));
    }

    @Test
    @DisplayName("测试 BigDecimal null 值")
    void testBigDecimalNullValue() {
        assertNull(NumberFormatUtils.formatZeroAsInt((BigDecimal) null));
    }
    @Test
    @DisplayName("测试其他 Number 类型（Integer、Long、Short、Byte、BigInteger 等）转换为 BigDecimal")
    void testOtherNumberTypesToBigDecimal() {
        // 1. Integer 类型
        assertEquals(new BigDecimal("100"), NumberFormatUtils.toBigDecimal(100));
        assertEquals(new BigDecimal("-50"), NumberFormatUtils.toBigDecimal(-50));

        // 2. Long 类型
        assertEquals(new BigDecimal("10000000000"), NumberFormatUtils.toBigDecimal(10000000000L));

        // 3. Short 与 Byte 类型
        assertEquals(new BigDecimal("10"), NumberFormatUtils.toBigDecimal((short) 10));
        assertEquals(new BigDecimal("5"), NumberFormatUtils.toBigDecimal((byte) 5));

        // 4. BigInteger 类型
        BigInteger bigInt = new BigInteger("999999999999999999");
        assertEquals(new BigDecimal("999999999999999999"), NumberFormatUtils.toBigDecimal(bigInt));

        // 5. 其他 Number 子类（如 AtomicInteger / AtomicLong）
        assertEquals(new BigDecimal("123"), NumberFormatUtils.toBigDecimal(new java.util.concurrent.atomic.AtomicInteger(123)));
    }
    @Test
    @DisplayName("字符串重载方法能正确解析并去除末尾零")
    void testStringOverload() {
        assertEquals("1.2", NumberFormatUtils.formatTrimZeros("1.200"));
        assertEquals("12", NumberFormatUtils.formatTrimZeros("12.00"));
        assertEquals("0", NumberFormatUtils.formatTrimZeros("0.000"));
        assertEquals("3.14", NumberFormatUtils.formatTrimZeros("  3.1400  ")); // 前后空格
    }

    @Test
    @DisplayName("原 Number 方法不受影响，保持正常工作")
    void testNumberOriginal() {
        assertEquals("1.2", NumberFormatUtils.formatTrimZeros(1.200d));
        assertEquals("12", NumberFormatUtils.formatTrimZeros(12));
        assertEquals("100", NumberFormatUtils.formatTrimZeros(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("字符串输入与保留位数及舍入模式")
    void testStringWithScaleAndRounding() {
        assertEquals("1.23", NumberFormatUtils.formatTrimZeros("1.2345", 2));
        assertEquals("1.24", NumberFormatUtils.formatTrimZeros("1.2355", 2)); // HALF_UP
        assertEquals("1.23", NumberFormatUtils.formatTrimZeros("1.2355", 2, RoundingMode.DOWN));
    }

    @Test
    @DisplayName("科学计数法字符串支持")
    void testScientificNotationString() {
        assertEquals("1000", NumberFormatUtils.formatTrimZeros("1e3"));
        assertEquals("0.0123", NumberFormatUtils.formatTrimZeros("1.23e-2", 4));
    }
    @Test
    @DisplayName("Null 与空字符串返回 null")
    void testNullAndEmptyInputs() {
        assertNull(NumberFormatUtils.formatTrimZeros((String) null));
        assertNull(NumberFormatUtils.formatTrimZeros(""));
        assertNull(NumberFormatUtils.formatTrimZeros("   "));
        assertNull(NumberFormatUtils.formatZeroAsInt((String) null));
        assertNull(NumberFormatUtils.formatZeroAsInt(""));
        assertNull(NumberFormatUtils.formatZeroAsInt("   "));
    }

    @Test
    @DisplayName("非法字符串解析抛出 IllegalArgumentException")
    void testInvalidStringThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> NumberFormatUtils.formatTrimZeros("abc"));
        assertThrows(IllegalArgumentException.class, () -> NumberFormatUtils.formatTrimZeros("1.2.3"));
        assertThrows(IllegalArgumentException.class, () -> NumberFormatUtils.formatZeroAsInt("12a"));
    }
    @ParameterizedTest
    @CsvSource({
            "'0', '0'",
            "'0.00', '0'",
            "'-0.00', '0'",
            "'0.0000', '0'"
    })
    @DisplayName("字符串形式的零值均转换为整数 '0'")
    void testZeroStringValues(String input, String expected) {
        assertEquals(expected, NumberFormatUtils.formatZeroAsInt(input));
    }

    @Test
    @DisplayName("非零字符串保持指定小数位数（不去除末尾零）")
    void testNonZeroStringValues() {
        assertEquals("1.20", NumberFormatUtils.formatZeroAsInt("1.2"));
        assertEquals("1.23", NumberFormatUtils.formatZeroAsInt("1.234", 2));
    }
}
