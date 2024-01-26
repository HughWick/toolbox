package com.github.hugh.util;

import org.junit.jupiter.api.Test;

import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 字符串工具测试
 *
 * @author AS
 * @date 2020/9/11 16:05
 */
class StringUtilTest {

    // 测试字符串长度
    @Test
    void testVarCharSize() {
        String value = "startDate";
        assertEquals(9, StringUtils.varcharSize(value));
    }

    @Test
    void testGetNumber() {
        var str2 = "\t0.12 \n";
        var str3 = "\t10.12 \n";
        assertEquals("012", StringUtils.getNumber(str2));
        assertEquals("10.12", StringUtils.getDouble(str3));
    }

    // 测试字符串左侧补位
    @Test
    void testLeftPadding() {
        assertEquals("aaaaaaa1", StringUtils.leftPadding("1", 8, 'a'));
        assertEquals("00000001", StringUtils.leftPadding("1", 8));
//        System.out.println("--1->" +);
//        System.out.println("-2-->" + StringUtils.leftPadding("1", 8));
    }

    // 字符串转驼峰
    @Test
    void testCamelToUnderlineUppercase() {
//        String value = "startDate";
        String str1 = "getCamelCaseString";
        String camel = StringUtils.camelToUnderline(str1);
        assertEquals("get_camel_case_string", camel);
        assertEquals("get_camel_case_string".toUpperCase(), StringUtils.camelToUnderlineUppercase(str1));
        assertEquals(str1, StringUtils.getCamelCase(camel, false));
    }

    // 字符串空格替换
    @Test
    void testReplaceAnyBlank() {
        assertEquals("entityiserror!", StringUtils.replaceAnyBlank(" entity is error !"));
        assertEquals("_e_n_t_i_t_y__i_s__e_r_r_o_r__!_", StringUtils.replaceAnyBlank("entity is error !", "_"));
    }

    // 测试中文字符串
    @Test
    void testChinese() {
        var str = "[a, b, [c]]]";
        assertEquals("[a, b, [c]]", StringUtils.trim(str, "]"));
        assertEquals("a, b, [c]]]", StringUtils.trim(str, "["));
//        System.out.println("==2=>>>" + StringUtils.trim(str, "]"));
//        System.out.println("==2=>>>" + StringUtils.trim(str, "["));
        assertTrue(StringUtils.isContainChinese("；"));
        assertTrue(StringUtils.isContainChinese("中文"));
        assertFalse(StringUtils.isNotContainChinese("中文2"));
        assertFalse(StringUtils.isNotContainChinese("中文"));
    }

    // 修剪指定字符之后所有
    @Test
    void testAfter() {
        String path1 = "/DaHua/capture/6G0BEB9GA12F70A/2021/1/17/9946090cb09b4986af8615174e862b9e.jpg";
        String path2 = "/capture/4M061F3PAA33D78/2020/10/27/e242edb022494a12a778c4cddecf2a48.jpg";
        String string = "http://hyga.hnlot.com.cn:8000/capture" + path1;
        String string2 = "https://www.hnlot.com.cn/DaHua" + path2;
        assertEquals(path1, StringUtils.after(string, "/", 4));
        assertEquals(path2, StringUtils.after(string2, "/", 4));
        String fileName = "0e9f4beeb6a5423585c6eabda21a63ee.jpg";
        String filePath = "C:\\Users\\Lenovo\\Desktop\\" + fileName;
        assertEquals(fileName, StringUtils.after(filePath, "\\"));


    }

    @Test
    void testBefore() {
        String path1 = "C:\\Users\\Lenovo\\Desktop";
//        String path2 = "/capture/4M061F3PAA33D78/2020/10/27/e242edb022494a12a778c4cddecf2a48.jpg";
        String fileName = path1 + "\\0e9f4beeb6a5423585c6eabda21a63ee.jpg";
//        String string2 = "https://www.hnlot.com.cn/DaHua" + path2;
        assertEquals(path1, StringUtils.before(fileName, "\\"));
        String str1 = "abc.sql";
        assertEquals("abc", StringUtils.before(str1, "."));
        String str2 = "create_date_le_le";
        assertEquals("create_date_le", StringUtils.before(str2, "_"));
    }


    @Test
    void trimResults() {
        var str = "\t0.12 \n";
        assertNull(null);
        String string = null;
//        assertNotNull(string,"字符串不能为空");
        assertEquals(StringUtils.trim(str), str.trim());
    }

    // 修剪最后一个字符
    @Test
    void testTrimLastPlace() {
        StringBuilder stringBuilder = new StringBuilder("1,2,3,");
        assertEquals("1,2,3", StringUtils.trimLastPlace(stringBuilder));
        String str = "a|b|c|";
        assertEquals("a|b|c", StringUtils.trimLastPlace(str));
        StringBuilder stringBuilder1 = null;
        assertThrowsExactly(NullPointerException.class, () -> {
            StringUtils.trimLastPlace(stringBuilder1);
        });
    }

    @Test
    void testStartWith() {
        String str = "<ss>";
        String str2 = "Ass>";
        assertTrue(StringUtils.startWithIgnoreCase(str, "<"));
        assertFalse(StringUtils.startWith(str2, "a", false));
    }

    // 测试保留小数点后指定位数
    @Test
    void testRetainDecimal() {
        String str1 = "112.94677848482011";
        final String s = StringUtils.retainDecimalDown(str1, 8);
        assertEquals(12, s.length());
        String str2 = "28.219683340569325";
        assertEquals(11, StringUtils.retainDecimalDown(str2, 8).length());
        final String s1 = StringUtils.retainDecimal(Double.parseDouble(str1), 8, false, RoundingMode.UP);
        assertEquals("112.94677849", s1);
        String str3 = "1401231485.123";
        final String s2 = StringUtils.retainDecimalDown(Double.parseDouble(str3), 2, true);
        assertEquals("1,401,231,485.12", s2);
    }

    // 测试不显示的非法字符
    @Test
    void test002() {
        String str1 = "WC-TX65望城一中新校门门口-(五期)";
        String str2 = "WC-TX65 望城一中新校门门口-(五期)";
        assertEquals(21, str1.length());
        assertEquals(22, str2.length());
//        System.out.println("--->>" + str2.length());
//        System.out.println("--->>" + "WC-TX65望城一中新校门门口-(五期)".length());
    }

    @Test
    void testCalcStr() {
        String str1 = "WC-TX65望";
        assertEquals(8, str1.length());
        assertEquals(9, StringUtils.calcGb2312Length(str1));
        assertEquals(10, StringUtils.calcUtf8Length(str1));
        assertEquals(18, StringUtils.calcUtf16Length(str1));
        assertEquals(9, StringUtils.calcGbkLength(str1));
        String str2 = "WC-TX65望&s、";
    }


    // 去重
    @Test
    void testRemoveDuplicate() {
//        String str="aabbbccccdddddeeeeeeeeefff234tttdddfffbbbggg";
        String result1 = "打开激光";
        String str1 = "打开打开激光";
        assertEquals(result1, StringUtils.removeDuplicate(str1));
        String str2 = "打打开激光";
        assertEquals(result1, StringUtils.removeDuplicate(str2));
        String str3 = "呼叫刘呼叫刘晓晓";
//        assertEquals("呼叫刘晓晓", StringUtils.removeDuplicate2(str3));
        assertEquals("呼叫刘晓晓", StringUtils.removeDuplicate(str3));


    }
}
