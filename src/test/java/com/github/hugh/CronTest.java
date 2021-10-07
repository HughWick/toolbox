package com.github.hugh;

import com.github.hugh.util.regex.CronRegex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author AS
 * @date 2020/9/14 10:22
 */
public class CronTest {

    @Test
    void test01() {
        assertTrue(CronRegex.isCron("20 * * * * ? "));
        assertFalse(CronRegex.isCron("10 ** * * ? "));
        assertFalse(CronRegex.isTooShort("0/29 * * * * ? "), "错误");
        System.out.println("===4=>>" + CronRegex.isTooShort("1-30 * * * * ? "));
        System.out.println("===5=>>" + CronRegex.isTooShort("0 1 * * * ? "));
    }
}
