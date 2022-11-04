package com.github.hugh;

import com.github.hugh.util.RandomUtils;
import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 随机工具类
 *
 * @author AS
 * @date 2020/9/9 11:01
 */
class RandomTest {

    @Test
    void testRandom() {
        assertInstanceOf(ThreadLocalRandom.class, RandomUtils.getRandom());
        assertInstanceOf(SecureRandom.class, RandomUtils.getSecureRandom());
        assertEquals(3, RandomUtils.randomString(3).length());
        assertEquals(2, RandomUtils.randomChar(2).length());
        assertTrue(RegexUtils.isNumeric(RandomUtils.randomNumber(3)));
        int i = RandomUtils.randomInt(600, 900);
        assertTrue(i > 600 && i <= 900);
    }

    @Test
    void test02() {
        for (int i = 0; i < 100; i++) {
            System.out.println("---->" + RandomUtils.number(4));
        }
    }

    @Test
    void testRandomList() {
        var list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        System.out.println(RandomUtils.random(list));
    }

//    public static int number(int length) {
//        int num = 1;
//        double random = Math.random();
//        if (random < 0.1D) {
//            random += 0.1D;
//        }
//        for (int i = 0; i < length; i++) {
//            num *= 10;
//        }
//        return (int) (random * num);
//    }
}
