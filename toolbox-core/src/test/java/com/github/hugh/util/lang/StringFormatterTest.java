package com.github.hugh.util.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 字符串格式化工具类
 * User: AS
 * Date: 2022/12/29 10:02
 */
class StringFormatterTest {

    @Test
    void testForm() {
        String str1 = "姓名：{}，年龄：{} END";
        final String format1 = StringFormatter.format(str1, "张三", 18);
//        System.out.println(format1);
        assertEquals("姓名：张三，年龄：18 END", format1);
        byte[] bytes1 = new byte[]{1, 3};
        String byteString = "数组：{}；";
        final String format2 = StringFormatter.format(byteString, bytes1);
//        System.out.println(format2);
        assertEquals("数组：[1, 3]；", format2);
        String string2 = "字符串其他：{}，数组：{}";
        String[] stringArray = new String[]{"a", "b", "c"};
        final String format3 = StringFormatter.format(string2, "展位", stringArray);
        assertEquals("字符串其他：展位，数组：[a, b, c]", format3);
        String string3 = "字符串其他：\\{}，数组：{}";
        final String format4 = StringFormatter.format(string3, "展位");
        assertEquals("字符串其他：{}，数组：展位", format4);
    }
}
