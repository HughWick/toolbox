package com.github.hugh.regex;

import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 字符串验证测试类
 *
 * @author AS
 * @date 2020/9/11 15:42
 */
class RegexTest {

    @Test
    void test01() {
        String str = "escapeWord[]";
        String str2 = "<>";
        String str3 = "1select>1";
        System.out.println("-1-->>" + RegexUtils.escapeWord(str));
        System.out.println("-2-->>" + RegexUtils.isPunctuation(str2));
        System.out.println("-3-->>" + RegexUtils.isSql(str3));
        System.out.println("-3-->>" + RegexUtils.isNotIp(str3));
    }

    @Test
    void testString() {
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

    // 测试经纬度
    @Test
    void testLonLat() {
        String longitude = "112.94662109375";
        String latitude = "28.219310709636";
        String longitude2 = "1019.48427455";
        String latitude2 = "218.59640742";
        assertFalse(RegexUtils.isLonLat(longitude, latitude));
        assertTrue(RegexUtils.isNotLonLat(longitude, latitude));

        assertFalse(RegexUtils.isLongitude(longitude));
        assertFalse(RegexUtils.isLongitude(longitude2));
        assertFalse(RegexUtils.isLatitude(latitude));
        assertFalse(RegexUtils.isLatitude(latitude2));
    }

    @Test
    void testEmail() {
        String str = "136438455@qq.com";
        String str2 = "136438455@qq";
        String str3 = null;
        assertTrue(RegexUtils.isEmail(str));
        assertFalse(RegexUtils.isEmail(str2));
        assertTrue(RegexUtils.isNotEmail(str2));
        assertTrue(RegexUtils.isNotEmail(str3));
    }

    @Test
    void testWebSite() {
        String url = "www.baidu.com";
        String url1 = "http://www.baidu.com";
        String url2 = "https://www.baidu.com";
        assertFalse(RegexUtils.isWebSite(url));
        assertTrue(RegexUtils.isWebSite(url1));
        assertTrue(RegexUtils.isWebSite(url2));
        assertTrue(RegexUtils.isNotWebSite(url));

        assertFalse(RegexUtils.isUrl(url));
        assertTrue(RegexUtils.isUrl(url1));
    }

    @Test
    void testNumeric() {
        String str = "12324532a";
        String str2 = "1232453";
        String str3 = "1232453.11";
        assertFalse(RegexUtils.isNumeric(str));
        assertTrue(RegexUtils.isNumeric(str2));
        assertFalse(RegexUtils.isNumeric(str3));
        assertTrue(RegexUtils.isNotNumeric(str));
    }

    // 测试验证手机号码
    @Test
    void testPhone() {
        String phoneStr = "13825004872";
        assertTrue(RegexUtils.isPhone(phoneStr));
        assertTrue(RegexUtils.isNotPhone(phoneStr + "a"));
    }
}
