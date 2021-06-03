package com.github.hugh;

import com.github.hugh.util.StringUtils;
import org.junit.Test;

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
        System.out.println("--1-->>" + StringUtils.replaceAnyBlank("entity is error !"));
        System.out.println("--2-->>" + StringUtils.replaceAnyBlank("entity is error !", "_"));
    }

    @Test
    public void test05() {
        String str = "[a, b, [c]]]";
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

    public static void main(String[] args) {
        String init = "Bob is a Bird... Bob is a Plane... Bob is Superman!";
        String changed = init.replace("Bob is", "It's"); // Noncompliant  
        System.out.println(changed);
        // changed = changed.replaceAll("\\.\\.\\.", ";"); // Noncompliant
//        double threadcount = DoubleMathUtils.mul(16, 3);
//        System.out.println("-=-threadcount ->>" + threadcount);
    }
}
