package com.github.hugh.guava;

import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.Map;

public class MapTest {

    @Test
    public void test01() {
        String str = "a=1&b=2&c=3&c=3";
        Map<String, String> targetOrderObj = Splitter.on("&")
                .trimResults()
                .omitEmptyStrings()
                .withKeyValueSeparator("=")
                .split(str);
        System.out.println(str);
    }
}
