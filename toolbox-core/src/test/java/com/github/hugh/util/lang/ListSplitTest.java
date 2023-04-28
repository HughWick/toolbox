package com.github.hugh.util.lang;

import com.google.common.base.CharMatcher;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 字符串切割成list
 *
 * @author AS
 * Date 2023/4/26 11:41
 */
class ListSplitTest {

    @Test
    void testList() {
        String strings = "[\"Saab\", \"Volvo\", \"BMW\" ,\"   \"]";
        assertEquals("[Saab, Volvo, BMW]", ListSplit.on(strings).toList().toString());

        String str2 = "a;b;c;d;e;f;";
        assertEquals("[a, b, c, d, e, f]", ListSplit.on(str2).separator(";").toList().toString());
        // 测试使用自定义字符匹配器忽略字符串中的空格符
        String str3 = "apple, banana,   orange";
        List<String> list3 = ListSplit.<String>on(str3).charMatcher(CharMatcher.anyOf(" ")).toList();
        assertEquals(3, list3.size());
        assertEquals("apple", list3.get(0));
        assertEquals("banana", list3.get(1));
        assertEquals("orange", list3.get(2));

        // 测试使用自定义转换函数将列表元素从 String 类型转换成 Integer 类型
        String str4 = "1,2,3,4,5";
        List<Integer> list4 = ListSplit.<Integer>on(str4).mapper(Integer::parseInt).toList();
        assertEquals(5, list4.size());
        assertEquals(Integer.valueOf(1), list4.get(0));
        assertEquals(Integer.valueOf(2), list4.get(1));
        assertEquals(Integer.valueOf(3), list4.get(2));
        assertEquals(Integer.valueOf(4), list4.get(3));
        assertEquals(Integer.valueOf(5), list4.get(4));


    }
}
