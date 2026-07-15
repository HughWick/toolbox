package com.github.hugh.crypto.util;

import com.soundicly.jnanoidenhanced.jnanoid.NanoIdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

class NanoIdTest {

    // 默认测试：标准的 21 位 URL 安全 ID
    @Test
    @DisplayName("默认生成器应生成21位符合规范的ID")
    void test01() {
        String id1 = NanoIdUtils.randomNanoId();
        Assertions.assertEquals(21, id1.length());
        System.out.println("默认 21 位 ID: " + id1);

        String id2 = NanoIdUtils.randomNanoId(16);
        Assertions.assertEquals(16, id2.length());
        System.out.println("默认 16 位 ID: " + id2);
    }

    @Test
    @DisplayName("高级用法：生成 6 位纯数字验证码")
    void testCustomAlphabetNumeric() {
        char[] digitsOnly = "0123456789".toCharArray();
        int size = 6;

        // 生成 10 个验证码进行验证
        for (int i = 0; i < 10; i++) {
            String code = NanoIdUtils.randomNanoId(
                    NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
                    digitsOnly,
                    size
            );

            Assertions.assertEquals(6, code.length());
            // 断言：每一位都必须是数字
            Assertions.assertTrue(code.matches("^[0-9]{6}$"));
            System.out.println("纯数字验证码: " + code);
        }
    }

    @Test
    @DisplayName("高级用法：生成易读、无歧义的优惠券兑换码")
    void testReadableActivationCode() {
        // 排除容易混淆的字符：1, l, I, 0, O, o, 2, Z
        char[] readableAlphabet = "3456789abcdefghijkmnpqrstuvwxyABCDEFGHJKLMNPQRSTUVWXY".toCharArray();
        int size = 12;

        String code = NanoIdUtils.randomNanoId(
                NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
                readableAlphabet,
                size
        );

        Assertions.assertEquals(12, code.length());
        // 验证生成的字符确实不包含易混淆字符
        String confusedChars = "1lI0Oo2Z";
        for (char c : code.toCharArray()) {
            Assertions.assertFalse(confusedChars.contains(String.valueOf(c)), "ID 中不应包含混淆字符: " + c);
        }
        System.out.println("易读兑换码: " + code);
    }

    @Test
    @DisplayName("高级用法：使用 ThreadLocalRandom 追求极致性能")
    void testHighPerformanceNanoId() {
        // 适用于非加密、非防预测场景（如临时的压测数据 ID 生成）
        Random fastRandom = ThreadLocalRandom.current();

        long startTime = System.nanoTime();
        Set<String> generatedIds = new HashSet<>();

        for (int i = 0; i < 10000; i++) {
            String id = NanoIdUtils.randomNanoId(
                    fastRandom,
                    NanoIdUtils.DEFAULT_ALPHABET,
                    15
            );
            generatedIds.add(id);
        }
        long endTime = System.nanoTime();

        // 确保生成的 10000 个 ID 没有发生碰撞
        Assertions.assertEquals(10000, generatedIds.size());
        System.out.println("ThreadLocalRandom 模式下，生成 10000 个 ID 耗时: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }

    @Test
    @DisplayName("边界与异常测试：非法参数拦截验证")
    void testBoundaryAndException() {
        // 1. 验证：当 Random 为 null 时抛出 IllegalArgumentException
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NanoIdUtils.randomNanoId(null, NanoIdUtils.DEFAULT_ALPHABET, 21);
        });

        // 2. 验证：当字符集为 null 时抛出 IllegalArgumentException
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            NanoIdUtils.randomNanoId(new SecureRandom(), null, 21);
//        });

        // 3. 验证：当长度小于等于 0 时抛出 IllegalArgumentException
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NanoIdUtils.randomNanoId(new SecureRandom(), NanoIdUtils.DEFAULT_ALPHABET, 0);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NanoIdUtils.randomNanoId(new SecureRandom(), NanoIdUtils.DEFAULT_ALPHABET, -5);
        });

        // 4. 验证：字符集字符数必须在 1-255 之间（大于255会报错）
        char[] tooLargeAlphabet = new char[256]; // 长度为 256 触发上限
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NanoIdUtils.randomNanoId(new SecureRandom(), tooLargeAlphabet, 10);
        });
    }

}
