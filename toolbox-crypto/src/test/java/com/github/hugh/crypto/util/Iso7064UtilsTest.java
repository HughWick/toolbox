package com.github.hugh.crypto.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class Iso7064UtilsTest {

    @ParameterizedTest
    @DisplayName("标准数据计算校验位测试 (最终修正)")
    @CsvSource({
            "ABC, 1",
            "123, G",
            "MA123, 3",
            "ISO7064, P",
            "ISO7065, N",
            "A, I",
            "G123489654321, Y",
            "G12348965432100, M"
    })
    void testComputeCheckDigit(String input, char expectedCheckDigit) {
        assertEquals(expectedCheckDigit, Iso7064Utils.computeCheckDigit(input));
    }

    @Test
    @DisplayName("测试特殊碰撞情况 (Input '1')")
    void testCollisionWithInput1() {
        // 在 Pure System (P0=37) 算法下，输入 "1" 会导致
        // 最终 P=2，校验位计算结果为 36 ('*')。
        // 代码应当拦截这种情况并抛出异常。
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Iso7064Utils.computeCheckDigit("1");
        });
        assertTrue(exception.getMessage().contains("36"));
    }

    @Test
    @DisplayName("测试忽略特殊符号和大小写")
    void testIgnoreSymbolsAndCase() {
        assertEquals('1', Iso7064Utils.computeCheckDigit("A-B.C"));
        assertEquals('1', Iso7064Utils.computeCheckDigit("a-b.c")); // 小写测试
        // "123" -> 'O'
        char simple = Iso7064Utils.computeCheckDigit("123");
        char complex = Iso7064Utils.computeCheckDigit("1-2/3");
        assertEquals(simple, complex);
    }

    @ParameterizedTest
    @DisplayName("完整字符串校验测试 (isValid)")
    @CsvSource({
            "ABC1, true",       // 正确
            "ABC8, false",      // 校验位错误
            "123G, true",       // 正确 (末尾是字母O)
            "1230, false",      // 错误 (末尾是数字0)
            "MA1233, true",     // 正确
            "ISO7065N, true",   // 正确
            "iso7064p, true"    // 小写输入，代码内部应转大写处理
    })
    void testIsValid(String input, boolean expected) {
        assertEquals(expected, Iso7064Utils.isValid(input));
    }

    @Test
    @DisplayName("isValid 边界与异常测试")
    void testIsValidEdgeCases() {
        assertFalse(Iso7064Utils.isValid(null));
        assertFalse(Iso7064Utils.isValid(""));
        assertFalse(Iso7064Utils.isValid("A")); // 长度不足，无法区分数据和校验位

        // G8S -> 数据部分 G8 会抛异常，isValid 应捕获异常并返回 false
        assertFalse(Iso7064Utils.isValid("G8S"));
    }

    @Test
    @DisplayName("输入无效时的异常处理")
    void testExceptions() {
        assertThrows(IllegalArgumentException.class, () -> Iso7064Utils.computeCheckDigit(""));
        assertThrows(IllegalArgumentException.class, () -> Iso7064Utils.computeCheckDigit(null));
        // 只有符号没有有效字符
        assertThrows(IllegalArgumentException.class, () -> Iso7064Utils.computeCheckDigit("-/-"));
    }
}