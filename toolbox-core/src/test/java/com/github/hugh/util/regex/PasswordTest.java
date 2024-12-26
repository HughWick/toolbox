package com.github.hugh.util.regex;

import com.github.hugh.util.regex.PasswordRegex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 密码测试工具类
 *
 * @author AS
 * @date 2021/2/10 20:33
 */
class PasswordTest {

    @Test
    void test01() {
        String str = "11232！1";
        String str2 = "a112321";
        assertTrue(PasswordRegex.moderate(str));
        assertTrue(PasswordRegex.moderate(str2));
    }
}
