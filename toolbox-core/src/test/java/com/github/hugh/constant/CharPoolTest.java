package com.github.hugh.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharPoolTest {

    @Test
    void testCommaConstant() {
        // 验证 COMMA 常量是否是 ','
        assertEquals(',', CharPool.COMMA, "COMMA constant should be ','");
    }

    @Test
    void testPointConstant() {
        // 验证 POINT 常量是否是 '.'
        assertEquals('.', CharPool.POINT, "POINT constant should be '.'");
    }

    @Test
    void testBackslashConstant() {
        // 验证 BACKSLASH 常量是否是 '\'
        // 在 Java 代码中，反斜杠 '\' 需要用 '\\' 表示
        assertEquals('\\', CharPool.BACKSLASH, "BACKSLASH constant should be '\\'");
    }

    @Test
    void testSlashConstant() {
        // 验证 SLASH 常量是否是 '/'
        assertEquals('/', CharPool.SLASH, "SLASH constant should be '/'");
    }

    @Test
    void testSpaceConstant() {
        // 验证 SPACE 常量是否是 ' '
        assertEquals(' ', CharPool.SPACE, "SPACE constant should be ' '");
    }

    @Test
    void testTabConstant() {
        // 验证 TAB 常量是否是 '\t' (制表符)
        assertEquals('\t', CharPool.TAB, "TAB constant should be '\\t'");
    }

    @Test
    void testDashedConstant() {
        // 验证 DASHED 常量是否是 '-'
        assertEquals('-', CharPool.DASHED, "DASHED constant should be '-'");
    }

    @Test
    void testUnderlineConstant() {
        // 验证 UNDERLINE 常量是否是 '_'
        assertEquals('_', CharPool.UNDERLINE, "UNDERLINE constant should be '_'");
    }

    @Test
    void testCurlyBracketsStartConstant() {
        // 验证 CURLY_BRACKETS_START 常量是否是 '{'
        assertEquals('{', CharPool.CURLY_BRACKETS_START, "CURLY_BRACKETS_START constant should be '{'");
    }

    @Test
    void testCurlyBracketsEndConstant() {
        // 验证 CURLY_BRACKETS_END 常量是否是 '}'
        assertEquals('}', CharPool.CURLY_BRACKETS_END, "CURLY_BRACKETS_END constant should be '}'");
    }

    @Test
    void testBracketStartConstant() {
        // 验证 BRACKET_START 常量是否是 '['
        assertEquals('[', CharPool.BRACKET_START, "BRACKET_START constant should be '['");
    }

    @Test
    void testBracketEndConstant() {
        // 验证 BRACKET_END 常量是否是 ']'
        assertEquals(']', CharPool.BRACKET_END, "BRACKET_END constant should be ']'");
    }

    @Test
    void testColonConstant() {
        // 验证 COLON 常量是否是 ':'
        assertEquals(':', CharPool.COLON, "COLON constant should be ':'");
    }

    @Test
    void testSemicolonConstant() {
        // 验证 SEMICOLON 常量是否是 ';'
        assertEquals(';', CharPool.SEMICOLON, "SEMICOLON constant should be ';'");
    }

    @Test
    void testPipeConstant() {
        // 验证 PIPE 常量是否是 '|'
        assertEquals('|', CharPool.PIPE, "PIPE constant should be '|'");
    }

}
