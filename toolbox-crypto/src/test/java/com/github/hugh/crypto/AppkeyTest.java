package com.github.hugh.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * uuid app key 生成器
 *
 * @author AS
 * @date 2020/8/31 15:18
 */
class AppkeyTest {

    @Test
    void testKey() {
        System.out.println("--->"+AppkeyUtils.generate());
        assertEquals(32 , AppkeyUtils.generate().length());
        assertEquals(64 , AppkeyUtils.generateSecret().length());
    }
}
