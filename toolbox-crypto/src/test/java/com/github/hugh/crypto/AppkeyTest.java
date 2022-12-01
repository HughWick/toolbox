package com.github.hugh.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * uuid app key 生成器
 *
 * @author AS
 * @date 2020/8/31 15:18
 */
class AppkeyTest {

    @Test
    void testKey() {
        Assertions.assertEquals(32 , AppkeyUtils.generate().length());
        Assertions.assertEquals(64 , AppkeyUtils.generateSecret().length());
    }
}
