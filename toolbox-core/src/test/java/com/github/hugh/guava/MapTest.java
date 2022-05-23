package com.github.hugh.guava;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * guava 字符串转map 测试类
 */
public class MapTest {

    @Test
    public void test01() {
        String str = "a=1&b=2&c=3&d=4";
        Map<String, String> targetOrderObj = Splitter.on("&")
                .trimResults()
                .omitEmptyStrings()
                .withKeyValueSeparator("=")
                .split(str);
        System.out.println(str);
        System.out.println(targetOrderObj);
    }

    @Test
    void testStringToMap() {
        String input = "<0ffff>0|Q|04<0x00100001>0|W||006<0x00100006>0|W||006";
        String result = CharMatcher.is('<').collapseFrom(input, ',');
        System.out.println(result);
        String r2 = CharMatcher.is('>').collapseFrom(result, ':');
        System.out.println(r2);
        Map<String, String> map = Splitter.on(",")
                .trimResults()
                .omitEmptyStrings()
                .withKeyValueSeparator(":")
                .split(r2);
        System.out.println(map);
    }
}
