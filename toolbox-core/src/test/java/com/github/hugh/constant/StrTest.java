package com.github.hugh.constant;

import com.github.hugh.constant.enums.StringEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 字符串常量测试类
 * User: AS
 * Date: 2023/1/6 16:25
 */
class StrTest {

    @Test
    void test01() {
        assertEquals(Str.COMMA, StringEnum.COMMA.getValue());
        assertEquals(Str.POINT, StringEnum.POINT.getValue());
        assertEquals(Str.BACKSLASH, StringEnum.BACKSLASH.getValue());
        assertEquals(Str.EMPTY, StringEnum.EMPTY.getValue());
    }
}
