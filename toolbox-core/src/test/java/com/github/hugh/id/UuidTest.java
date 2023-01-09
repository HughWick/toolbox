package com.github.hugh.id;

import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * uuid 工具测试类
 *
 * @author hugh
 */
class UuidTest {

    @Test
    void test01() {
        final String genId = Uuid.genId(12);
        Assertions.assertEquals(12, genId.length());
        Assertions.assertTrue(RegexUtils.isLowerCaseAndNumber(genId.toLowerCase()));
    }
}
