package com.github.hugh;

import com.github.hugh.util.regex.RegexUtils;
import org.junit.Test;

import java.io.BufferedReader;

/**
 * @author AS
 * @date 2020/9/11 16:42
 */
public class IpTest {

    @Test
    public void test001() {
        String str = "113.218.2.1223";
        System.out.println("--1->>" + RegexUtils.isIp(str));
        System.out.println("--2->>" + RegexUtils.isNotIp(str));
    }

    public static void main(String[] args) {
        try {
            String s = "123";
            try (BufferedReader in = new BufferedReader(null)) {
                int i = 1 / 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("====");
    }
}
