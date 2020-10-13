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
        System.out.println("--3->>>" + StringUtils.isChinese(fName));
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
        System.out.println("--2-->>" + StringUtils.camelToUnderlineUppercase(value));
        System.out.println("--2-->>" + StringUtils.getCamelCase(camel, false));
    }

    @Test
    public void test04() {
        System.out.println("--1-->>" + StringUtils.replaceAnyBlank("entity is error !"));
        System.out.println("--2-->>" + StringUtils.replaceAnyBlank("entity is error !", "_"));
    }
}
