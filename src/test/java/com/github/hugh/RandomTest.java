package com.github.hugh;

import com.github.hugh.util.RandomUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/9/9 11:01
 */
public class RandomTest {

    @Test
    public void test001() {
        System.out.println("--1->>" + RandomUtils.getRandom());
        System.out.println("--2->>" + RandomUtils.getSecureRandom());
        System.out.println("--4->>" + RandomUtils.randomChar(2));
        for (int i = 0; i < 10; i++) {
            System.out.println("--5->>" + RandomUtils.randomNumber(2));
        }
        System.out.println("--6->>" + RandomUtils.number(2));
    }
}