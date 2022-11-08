package com.github.hugh;

import com.github.hugh.util.regex.CronRegex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * quartz 定时器字符串验证类
 *
 * @author AS
 * @date 2020/9/14 10:22
 */
class CronTest {

    @Test
    void testIsCron() {
        assertTrue(CronRegex.isCron("20 * * * * ? "));
        assertFalse(CronRegex.isCron("10 ** * * ? "));
        assertTrue(CronRegex.isTooShort("0/29 * * * * ? "), "错误");
        assertFalse(CronRegex.isTooShort("1-30 * * * * ? "));
        assertFalse(CronRegex.isTooShort("0 1 * * * ? "));
    }
}
