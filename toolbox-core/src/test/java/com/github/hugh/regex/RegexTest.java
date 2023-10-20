package com.github.hugh.regex;

import com.github.hugh.util.base.Base64;
import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 字符串验证测试类
 *
 * @author AS
 * @date 2020/9/11 15:42
 */
class RegexTest {

    @Test
    void testSql() {
        String str = "escapeWord[]";
        String str3 = "1select>1";
        assertEquals("escapeWord\\[\\]", RegexUtils.escapeWord(str));
        assertFalse(RegexUtils.isSql(str3));
        assertTrue(RegexUtils.isNotIp(str3));
    }

    // 测试验证字符串中是否有特殊符号
    @Test
    void testIsPunctuation() {
        String str1 = "123sf";
        assertFalse(RegexUtils.isPunctuation(str1));
        String str2 = "_123sf";
        assertTrue(RegexUtils.isPunctuation(str2));
        String str3 = "123sf@";
        assertTrue(RegexUtils.isPunctuation(str3));
        String str4 = "123sf()";
        assertTrue(RegexUtils.isPunctuation(str4));
        String str5 = "123sf-";
        assertTrue(RegexUtils.isPunctuation(str5));
        String str6 = "123sf（）";
        assertTrue(RegexUtils.isPunctuation(str6));
        String str7 = "skjh上课就会让她";
        assertFalse(RegexUtils.isPunctuation(str7));
        String str8 = "skjh上 课就会让她 ";
        assertFalse(RegexUtils.isPunctuation(str8));
        String str9 = "<>";
        assertFalse(RegexUtils.isPunctuation(str9));
    }

    // 测试经纬度
    @Test
    void testLonLat() {
        // 纬度
        String str1 = "28.219310709636";
        String latitude = "28.219310709636";
        String latitude2 = "218.59640742";
        String lat3 = "28.21931070";
        //经度
        String str2 = "112.94662109375";
        String str3 = "112.94662109";
        String longitude = "112.94662109375";
        String longitude2 = "1019.48427455";
        assertFalse(RegexUtils.isLonLat(longitude, latitude));
        assertFalse(RegexUtils.isLonLat(longitude + "," + latitude));
        assertTrue(RegexUtils.isNotLonLat(longitude, latitude));
        assertTrue(RegexUtils.isNotLonLat(longitude + "," + latitude));

        assertFalse(RegexUtils.isLongitude(longitude));
        assertFalse(RegexUtils.isLongitude(longitude2));
        assertTrue(RegexUtils.isLongitude(str3));
        assertFalse(RegexUtils.isLatitude(latitude));
        assertFalse(RegexUtils.isLatitude(latitude2));
        assertTrue(RegexUtils.isLatitude(lat3));
        assertTrue(RegexUtils.isNotLatitude(str2));
        assertTrue(RegexUtils.isNotLongitude(str1));

        String str4 = "112.944468,28.218373";
        assertTrue(RegexUtils.isLonLat(str4));
        String str5 = "28.218373,112.944468";
        assertFalse(RegexUtils.isLonLat(str5));
    }

    @Test
    void testEmail() {
        String str = "136438455@qq.com";
        assertTrue(RegexUtils.isEmail(str));
        String str2 = "136438455@qq";
        assertFalse(RegexUtils.isEmail(str2));
        assertTrue(RegexUtils.isNotEmail(str2));
        assertTrue(RegexUtils.isNotEmail(null));
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

    // 测试验证IP
    @Test
    void testVerifyIp() {
        String ip1 = "192.168.1.25";
        assertTrue(RegexUtils.isIp(ip1));
        assertTrue(RegexUtils.isNotIp(ip1 + "23"));
        assertTrue(RegexUtils.isNotIp(""));
    }

    // 测试验证端口
    @Test
    void testVerifyPort() {
        String port1 = "25";
        assertTrue(RegexUtils.isPort(port1));
        assertTrue(RegexUtils.isNotPort(port1 + "2312"));
        assertTrue(RegexUtils.isNotPort(""));
    }

    // 验证字符串是否全是中文
    @Test
    void testVerifyFullChinese() {
        String string1 = "的撒开发和";
        assertFalse(RegexUtils.isFullChinese(string1 + "213"));
        assertTrue(RegexUtils.isFullChinese(string1));
        assertTrue(RegexUtils.isNotFullChinese(string1 + "213"));
    }

    // 测试是否为偶数
    @Test
    void testEvenNumber() {
        int c1 = 2;
        assertTrue(RegexUtils.isEvenNumber(c1));
        assertFalse(RegexUtils.isEvenNumber(3));
        assertTrue(RegexUtils.isNotEvenNumber(3));
    }

    // 字符串为小写+数字
    @Test
    void testLower() {
        String string1 = "4fceb2d22F";
        assertTrue(RegexUtils.isNotLowerCaseAndNumber(string1));
        assertFalse(RegexUtils.isLowerCaseAndNumber(string1));
        String string2 = "980bb8126e4046e3";
        assertTrue(RegexUtils.isLowerCaseAndNumber(string2));
    }

    // 验证字符串为大写+数字
    @Test
    void testUpper() {
        String string1 = "4fceb2d22F";
        assertFalse(RegexUtils.isUpperCaseAndNumber(string1));
        assertTrue(RegexUtils.isNotUpperCaseAndNumber(string1));
        String string2 = "F80C5366D9";
        assertTrue(RegexUtils.isUpperCaseAndNumber(string2));
    }

    @Test
    void testIsBase64() {
        String str = "123zvb@!";
        String s1 = Base64.encode(str);
        assertTrue(RegexUtils.isBase64(s1));
        String str2 = "MTIzenZiQCE=2";
        assertFalse(RegexUtils.isBase64(str2));
        assertTrue(RegexUtils.isNotBase64(str2));
        String base64_1 = "thQzWr3jHdta+0RvIYpdNFoMrtOrdJB8upC+qX20nOmUArfFXUZEqvnkX6HWYP5WSR9NQk9VrzfbWCHOpJa4AQ==";
        assertTrue(RegexUtils.isBase64(base64_1));
        String base64_2 = "rcPTxbQ4HK38+Lwu5duH6ZboGexw3tWW5xgqNJYO90mn2ELuW6slLkpNBTi1kAbQ ";
        assertFalse(RegexUtils.isBase64(base64_2));
        String base64_3 = "rcPTxbQ4HK38+Lwu5duH6ZboGexw3tWW5xgqNJYO90mn2ELuW6slLkpNBTi1kAbQ";
        assertTrue(RegexUtils.isBase64(base64_3));
    }

    @Test
    void testIsHexadecimal(){
        String str1 = "7e 00 4a 20 22 08 05 00 75 84 01 03 00 01 00 00 5d 48 00 00 00 17 00 00 00 ae 00 00 48 2c 00 00 01 c2 01 9b 04 5d 04 5e 04 5f 04 5d 00 18 00 18 01 4d 01 b8 01 9d 00 00 00 00 00 00 00 3f 00 00 00 00 00 00 00 00 00 00 00 00 6f 7e";
        assertTrue(RegexUtils.isHexadecimal(str1.replace(" " , "")));
        String str2 = "7e 00 4a 20 22 08 05 00 75 84 mm";
        assertFalse(RegexUtils.isHexadecimal(str2.replace(" " , "")));
        assertTrue(RegexUtils.isNotHexadecimal(str2.replace(" " , "")));

    }
}
