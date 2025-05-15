package com.github.hugh.constant;

import com.github.hugh.constant.enums.StringEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 字符串常量测试类
 * User: AS
 * Date: 2023/1/6 16:25
 */
class StrPoolTest {

    @Test
    void test01() {
        assertEquals(StrPool.COMMA, StringEnum.COMMA.getValue());
        assertEquals(StrPool.POINT, StringEnum.POINT.getValue());
        assertEquals(StrPool.BACKSLASH, StringEnum.BACKSLASH.getValue());
        assertEquals(StrPool.EMPTY, StringEnum.EMPTY.getValue());
        assertEquals("\t", StrPool.TAB);
        assertEquals("/", StrPool.SLASH);
        assertEquals("-", StrPool.DASHED);
        assertEquals("{", StrPool.CURLY_BRACKETS_START);
        assertEquals("}", StrPool.CURLY_BRACKETS_END);
        assertEquals("[", StrPool.BRACKET_START);
        assertEquals("]", StrPool.BRACKET_END);
    }
    @Test
    void testCommaConstant() {
        // 验证 COMMA 常量是否是 ","
        assertEquals(",", StrPool.COMMA, "COMMA constant should be \",\"");
    }

    @Test
    void testPointConstant() {
        // 验证 POINT 常量是否是 "."
        assertEquals(".", StrPool.POINT, "POINT constant should be \".\"");
    }

    @Test
    void testBackslashConstant() {
        // 验证 BACKSLASH 常量是否是 "\"
        // 在 Java 字符串字面量中，一个反斜杠需要写成两个 "\\"
        assertEquals("\\", StrPool.BACKSLASH, "BACKSLASH constant should be \"\\\"");
    }

    @Test
    void testSlashConstant() {
        // 验证 SLASH 常量是否是 "/"
        assertEquals("/", StrPool.SLASH, "SLASH constant should be \"/\"");
    }

    @Test
    void testEmptyConstant() {
        // 验证 EMPTY 常量是否是 ""
        assertEquals("", StrPool.EMPTY, "EMPTY constant should be \"\"");
    }

    @Test
    void testSpaceConstant() {
        // 验证 SPACE 常量是否是 " "
        assertEquals(" ", StrPool.SPACE, "SPACE constant should be \" \"");
    }

    @Test
    void testTabConstant() {
        // 验证 TAB 常量是否是 "\t" (制表符)
        // 在 Java 字符串字面量中，制表符是 "\t"
        assertEquals("\t", StrPool.TAB, "TAB constant should be \"\\t\"");
    }

    @Test
    void testDashedConstant() {
        // 验证 DASHED 常量是否是 "-"
        assertEquals("-", StrPool.DASHED, "DASHED constant should be \"-\"");
    }

    @Test
    void testUnderlineConstant() {
        // 验证 UNDERLINE 常量是否是 "_"
        assertEquals("_", StrPool.UNDERLINE, "UNDERLINE constant should be \"_\"");
    }

    @Test
    void testCurlyBracketsStartConstant() {
        // 验证 CURLY_BRACKETS_START 常量是否是 "{"
        assertEquals("{", StrPool.CURLY_BRACKETS_START, "CURLY_BRACKETS_START constant should be \"{\"");
    }

    @Test
    void testCurlyBracketsEndConstant() {
        // 验证 CURLY_BRACKETS_END 常量是否是 "}"
        assertEquals("}", StrPool.CURLY_BRACKETS_END, "CURLY_BRACKETS_END constant should be \"}\"");
    }

    @Test
    void testBracketStartConstant() {
        // 验证 BRACKET_START 常量是否是 "["
        assertEquals("[", StrPool.BRACKET_START, "BRACKET_START constant should be \"[\"");
    }

    @Test
    void testBracketEndConstant() {
        // 验证 BRACKET_END 常量是否是 "]"
        assertEquals("]", StrPool.BRACKET_END, "BRACKET_END constant should be \"]\"");
    }

    @Test
    void testEmptyJsonConstant() {
        // 验证 EMPTY_JSON 常量是否是 "{}"
        assertEquals("{}", StrPool.EMPTY_JSON, "EMPTY_JSON constant should be \"{}\"");
    }

    @Test
    void testColonConstant() {
        // 验证 COLON 常量是否是 ":"
        assertEquals(":", StrPool.COLON, "COLON constant should be \":\"");
    }

    @Test
    void testSemicolonConstant() {
        // 验证 SEMICOLON 常量是否是 ";"
        assertEquals(";", StrPool.SEMICOLON, "SEMICOLON constant should be \";\"");
    }

    @Test
    void testPipeConstant() {
        // 验证 PIPE 常量是否是 "|"
        assertEquals("|", StrPool.PIPE, "PIPE constant should be \"|\"");
    }
}
