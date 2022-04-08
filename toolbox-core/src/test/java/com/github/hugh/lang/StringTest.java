package com.github.hugh.lang;

import com.github.hugh.util.StringUtils;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//import static org.junit.jupiter.api.

/**
 * @author AS
 * @date 2020/9/11 16:05
 */
public class StringTest {

    @Test
    void test01() {
//        String str = "https://github.com/HughWick/toolbox";
        String fName = "C:\\Users\\Lenovo\\Desktop\\0e9f4beeb6a5423585c6eabda21a63ee.jpg";
        var str2 = "\t0.12 \n";
        var str3 = "\t10.12 \n";
        //或者
//        String fileName = fName.substring(fName.lastIndexOf("\\") + 1);
        String value = "startDate";
//        System.out.println("fileName = " + fileName);
        System.out.println("---1>>>" + StringUtils.after(fName, "\\"));
        System.out.println("--2->>>" + StringUtils.before(fName, "\\"));
        System.out.println("--3->>>" + StringUtils.isContainChinese(fName));
        System.out.println("--4->>>" + StringUtils.getNumber(str2));
        System.out.println("--4->>>" + StringUtils.getDouble(str3));
        System.out.println("--5>>" + StringUtils.varcharSize(value));
    }

    @Test
    void test02() {
        System.out.println("--1->" + StringUtils.leftPadding("1", 8, 'a'));
        System.out.println("-2-->" + StringUtils.leftPadding("1", 8));
    }

    @Test
    void test03() {
        String value = "startDate";
        String str1 = "getCamelCaseString";
        String camel = StringUtils.camelToUnderline(str1);
        System.out.println("--1-->>" + camel);
        System.out.println("--2-->>" + StringUtils.camelToUnderlineUppercase(camel));
        System.out.println("--2-->>" + StringUtils.getCamelCase(camel, false));
    }

    @Test
    void test04() {
        System.out.println("--1-->>" + StringUtils.replaceAnyBlank(" entity is error !"));
        System.out.println("--2-->>" + StringUtils.replaceAnyBlank("entity is error !", "_"));
    }

    @Test
    void testChinese() {
        var str = "[a, b, [c]]]";
        System.out.println("==2=>>>" + StringUtils.trim(str, "]"));
        System.out.println("==2=>>>" + StringUtils.trim(str, "["));
        System.out.println("--4-->>" + StringUtils.isContainChinese("；"));
        System.out.println("--5-->>" + StringUtils.isContainChinese("中文"));
        System.out.println("--6-->>" + StringUtils.isNotContainChinese("中文2"));
        System.out.println("--7-->>" + StringUtils.isNotContainChinese("中文"));
    }

    @Test
    void testAfter() {
        String string = "http://hyga.hnlot.com.cn:8000/capture/DaHua/capture/6G0BEB9GA12F70A/2021/1/17/9946090cb09b4986af8615174e862b9e.jpg";
        String string2 = "https://www.hnlot.com.cn/DaHua/capture/4M061F3PAA33D78/2020/10/27/e242edb022494a12a778c4cddecf2a48.jpg";
        System.out.println("--1-->>" + StringUtils.after(string, "/", 4));
        System.out.println("--2-->>" + StringUtils.after(string2, "/", 4));
    }

    @Test
    void test07() {
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

    @Test
    void trimResults() {
        var str = "\t0.12 \n";
        assertNull(null);
        String string = null;
//        assertNotNull(string,"字符串不能为空");
        assertEquals(StringUtils.trim(str), str.trim());
        System.out.println("--------------->>");

    }


    public static void main(String[] args) {
        String init = "Bob is a Bird... Bob is a Plane... Bob is Superman!";
        String changed = init.replace("Bob is", "It's"); // Noncompliant  
        System.out.println(changed);
//        CharMatcher.javaIsoControl()
        // changed = changed.replaceAll("\\.\\.\\.", ";"); // Noncompliant
//        double threadcount = DoubleMathUtils.mul(16, 3);
//        System.out.println("-=-threadcount ->>" + threadcount);
    }
}
