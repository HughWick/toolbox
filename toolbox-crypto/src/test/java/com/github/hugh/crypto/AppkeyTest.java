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
        String appkey1 = AppkeyUtils.generate();
        String appSecret1 = AppkeyUtils.generateSecret();
        System.out.println("-appkey1-->"+ appkey1);
        System.out.println("-appSecret1-->"+ appSecret1);
        assertEquals(32 , appkey1.length());
        assertEquals(64 , appSecret1.length());
    }
}
