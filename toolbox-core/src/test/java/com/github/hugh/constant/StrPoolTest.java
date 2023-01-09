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
}
