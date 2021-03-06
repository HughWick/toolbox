package com.github.hugh.regex;

import com.github.hugh.util.regex.RegexUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/9/11 15:42
 */
public class RegexTest {

    @Test
    public void test01() {
        String str = "escapeWord[]";
        String str2 = "<>";
        String str3 = "1select>1";
        System.out.println("-1-->>" + RegexUtils.escapeWord(str));
        System.out.println("-2-->>" + RegexUtils.isPunctuation(str2));
        System.out.println("-3-->>" + RegexUtils.isSql(str3));
        System.out.println("-3-->>" + RegexUtils.isNotIp(str3));
    }


    @Test
    public void test02() {
        String str1 = "123sf";
        String str2 = "_123sf";
        String str3 = "123sf@";
        String str4 = "123sf()";
        String str5 = "123sf-";
        String str6 = "123sf（）";
        String str7 = "skjh上课就会让她";
        String str8 = "skjh上 课就会让她 ";
        System.out.println("---->>>" + RegexUtils.isPunctuation(str1));
        System.out.println("---->>>" + RegexUtils.isPunctuation(str2));
        System.out.println("---->>>" + RegexUtils.isPunctuation(str3));
        System.out.println("---->>>" + RegexUtils.isPunctuation(str4));
        System.out.println("---->>>" + RegexUtils.isPunctuation(str5));
        System.out.println("---->>>" + RegexUtils.isPunctuation(str6));
        System.out.println("---->>>" + RegexUtils.isPunctuation(str7));
        System.out.println("---->>>" + RegexUtils.isPunctuation(str8));
    }

    @Test
    public void test03() {
        String longitude = "112.94662109375";
        String longitude2 = "1019.48427455";
        String latitude = "28.219310709636";
        String latitude2 = "218.59640742";
        System.out.println("---1->" + RegexUtils.isLonLat(longitude, latitude));
        System.out.println("--2->" + RegexUtils.isNotLonLat(longitude, latitude));
        System.out.println("--3->" + RegexUtils.isLongitude(longitude));
        System.out.println("--4->" + RegexUtils.isLongitude(longitude2));
        System.out.println("--5->" + RegexUtils.isLatitude(latitude));
        System.out.println("--6->" + RegexUtils.isLatitude(latitude2));
    }

    @Test
    public void test04() {
        String str = "136438455@qq.com";
        String str2 = "136438455@qq";
        String str3 = null;
        System.out.println("-1-->>" + RegexUtils.isEmail(str));
        System.out.println("--2->>" + RegexUtils.isEmail(str));
        System.out.println("-3-->>" + RegexUtils.isEmail(str2));
        System.out.println("-4-->>" + RegexUtils.isNotEmail(str2));
        System.out.println("-5-->>" + RegexUtils.isNotEmail(str3));
    }

    @Test
    public void test05() {
        String url = "www.baidu.com";
        String url1 = "http://www.baidu.com";
        String url2 = "http://www.baidu.com";
        System.out.println("-1-->" + RegexUtils.isWebSite(url));
        System.out.println("-2-->" + RegexUtils.isWebSite(url1));
        System.out.println("-3-->" + RegexUtils.isWebSite(url2));
    }
}
