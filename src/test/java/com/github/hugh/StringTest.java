package com.github.hugh;

import com.github.hugh.util.StringUtils;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * @author AS
 * @date 2020/9/11 16:05
 */
public class StringTest {

    @Test
    public void test01() {
//        String str = "https://github.com/HughWick/toolbox";
        String fName = "C:\\Users\\Lenovo\\Desktop\\0e9f4beeb6a5423585c6eabda21a63ee.jpg";
        //或者
//        String fileName = fName.substring(fName.lastIndexOf("\\") + 1);
        String value = "startDate";
//        System.out.println("fileName = " + fileName);
        System.out.println("---1>>>" + StringUtils.after(fName, "\\"));
        System.out.println("--2->>>" + StringUtils.before(fName, "\\"));
        System.out.println("--3->>>" + StringUtils.isContainChinese(fName));
        System.out.println("--4->>>" + StringUtils.getNumber(fName));
        System.out.println("--5>>" + StringUtils.varcharSize(value));
    }

    @Test
    public void test02() {
        System.out.println("--->" + StringUtils.leftPadding("1", 8, 'a'));
        System.out.println("--->" + StringUtils.leftPadding("1", 8));
    }

    @Test
    public void test03() {
        String value = "startDate";
        String str1 = "getCamelCaseString";
        String camel = StringUtils.camelToUnderline(str1);
        System.out.println("--1-->>" + camel);
        System.out.println("--2-->>" + StringUtils.camelToUnderlineUppercase(camel));
        System.out.println("--2-->>" + StringUtils.getCamelCase(camel, false));
    }

    @Test
    public void test04() {
        System.out.println("--1-->>" + StringUtils.replaceAnyBlank(" entity is error !"));
        System.out.println("--2-->>" + StringUtils.replaceAnyBlank("entity is error !", "_"));
    }

    @Test
    public void test05() {
        var str = "[a, b, [c]]]";
        System.out.println("==2=>>>" + StringUtils.trim(str, "]"));
        System.out.println("==2=>>>" + StringUtils.trim(str, "["));
        System.out.println("--4-->>" + StringUtils.isContainChinese("；"));
        System.out.println("--5-->>" + StringUtils.isContainChinese("中文"));
        System.out.println("--6-->>" + StringUtils.isNotContainChinese("中文2"));
        System.out.println("--7-->>" + StringUtils.isNotContainChinese("中文"));
    }

    @Test
    public void test06() {
        String string = "http://hyga.hnlot.com.cn:8000/capture/DaHua/capture/6G0BEB9GA12F70A/2021/1/17/9946090cb09b4986af8615174e862b9e.jpg";
        String string2 = "https://www.hnlot.com.cn/DaHua/capture/4M061F3PAA33D78/2020/10/27/e242edb022494a12a778c4cddecf2a48.jpg";
        System.out.println("--1-->>" + StringUtils.after(string, "/", 4));
        System.out.println("--2-->>" + StringUtils.after(string2, "/", 4));
    }

    @Test
    public void test07() {
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


    // 获取字符串中数字
    @Test
    public void getNumber() {
        String str = "abcd123和345.56jia567.23.23jian345and23or345.56";
        //先判断有没有整数，如果没有整数那就肯定就没有小数
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(str);
        String result = "";
        if (m.find()) {
            Map<Integer, String> map = new TreeMap();
            Pattern p2 = Pattern.compile("(\\d+\\.\\d+)");
            m = p2.matcher(str);
            //遍历小数部分
            while (m.find()) {
                result = m.group(1) == null ? "" : m.group(1);
                int i = str.indexOf(result);
                String s = str.substring(i, i + result.length());
                map.put(i, s);
                //排除小数的整数部分和另一个整数相同的情况下，寻找整数位置出现错误的可能，还有就是寻找重复的小数
                // 例子中是排除第二个345.56时第一个345.56产生干扰和寻找整数345的位置时，前面的小数345.56会干扰
                str = str.substring(0, i) + str.substring(i + result.length());
            }
            //遍历整数
            Pattern p3 = Pattern.compile("(\\d+)");
            m = p3.matcher(str);
            while (m.find()) {
                result = m.group(1) == null ? "" : m.group(1);
                int i = str.indexOf(result);
                //排除jia567.23.23在第一轮过滤之后留下来的jia.23对整数23产生干扰
                if (String.valueOf(str.charAt(i - 1)).equals(".")) {
                    //将这个字符串删除
                    str = str.substring(0, i - 1) + str.substring(i + result.length());
                    continue;
                }
                String s = str.substring(i, i + result.length());
                map.put(i, s);
                str = str.substring(0, i) + str.substring(i + result.length());
            }
            result = "";
            for (Map.Entry<Integer, String> e : map.entrySet()) {
                result += e.getValue() + ",";
            }
            result = result.substring(0, result.length() - 1);
        } else {
            result = "";
        }
        System.out.println(result);
    }

    @Test
    public void trimResults() {
        var str = "\t0.12 \n";
        assertEquals(str.strip(), str.trim());
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
