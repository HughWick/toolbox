package com.github.hugh.util.regex;

import com.github.hugh.util.base.Base64;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 字符串验证测试类
 *
 * @author AS
 * @date 2020/9/11 15:42
 */
class RegexUtilsTest {

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

        assertFalse(RegexUtils.isLongitude(""));
        assertFalse(RegexUtils.isLongitude(longitude));
        assertFalse(RegexUtils.isLongitude(longitude2));
        assertTrue(RegexUtils.isLongitude(str3));
        assertFalse(RegexUtils.isLatitude(""));
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
        String url_1 = "www.baidu.com";
        String url_2 = "http://www.baidu.com";
        String url_3 = "https://www.baidu.com";
        assertFalse(RegexUtils.isWebSite(url_1));
        assertTrue(RegexUtils.isWebSite(url_2));
        assertTrue(RegexUtils.isWebSite(url_3));
        assertTrue(RegexUtils.isNotWebSite(url_1));

        assertFalse(RegexUtils.isUrl(url_1));
        assertTrue(RegexUtils.isUrl(url_2));
    }

    @Test
    void testNumeric() {
        String str = "12324532a";
        String str2 = "1232453";
        String str3 = "1232453.11";
        assertFalse(RegexUtils.isNumeric(""));
        assertFalse(RegexUtils.isNumeric(str));
        assertTrue(RegexUtils.isNumeric(str2));
        assertFalse(RegexUtils.isNumeric(str3));
        assertTrue(RegexUtils.isNotNumeric(str));
    }

    String[] testValidPhoneNumbers = {
            "18648293237",
            "18978050839",
            "18787430100",
            "17687283010",
            "18015323726",
            "17507831217",
            "14776385170",
            "13794021755",
            "18152222513",
            "13561724388",
            "15095695809",
            "16218772914",
            "13789542824",
            "17873204222",
            "19570151891",
            "17322698512",
            "15725030667",
            "13528479148",
            "15913849737",
            "16625868750",
            "17538341249",
            "17218208007",
            "17857352575",
            "13371420258",
            "19571200572",
            "13542749223",
            "14580334830",
            "17807315276",
            "18211443794",
            "15731186549",
            "18167881156",
            "14887382331",
            "19200565267",
            "16253375056",
            "16519418674",
            "13524187207",
            "15934860076",
            "18468056147",
            "16758283682",
            "19907709806",
            "17305024476",
            "14591570201",
            "15914845836",
            "13125582747",
            "18021352978",
            "18906954897",
            "17635847941",
            "13066411104",
            "18572542046",
            "15256844239",
            "17648090966",
            "18601794303",
            "15740056165",
            "13903467346",
            "13848257417",
            "18142331794",
            "17216406367",
            "17676404179",
            "13165293382",
            "13205814038",
            "13193617678",
            "18124082219",
            "17718937458",
            "17377886528",
            "14927370356",
            "13392763775",
            "13072743741",
            "13538339308",
            "13222558648",
            "19945822353",
            "14743145972",
            "13379947431",
            "14799591588",
            "19987414164",
            "13596661322",
            "18808974423",
            "17754207374",
            "19910638316",
            "13001900726",
            "17557109371",
            "19029141881",
            "13870269805",
            "15060051944",
            "19972125638",
            "18709954512",
            "13026171765",
            "19511705078",
            "19704939579",
            "13623482594",
            "13383577652",
            "19207977521",
            "14873557486",
            "15748407983",
            "15635045752",
            "14670746258",
            "18800022105",
            "16655678504",
            "18488752689",
            "14738548550",
            "18270590366"
    };

    // 测试验证手机号码
    @Test
    void testPhone() {
        String phoneStr = "13825004872";
        assertTrue(RegexUtils.isPhone(phoneStr));
        assertTrue(RegexUtils.isNotPhone(phoneStr + "a"));
        // 输出测试数据
        for (String phoneNumber : testValidPhoneNumbers) {
            assertTrue(RegexUtils.isPhone(phoneNumber), phoneNumber);
        }
    }

    @Test
    void testPhoneNumber2() {
        List<String> phoneNumbers = Arrays.asList(
                "18584296492",
                "18411016599",
                "15334958988",
                "15178232858",
                "17579092938",
                "18095656006",
                "16632232052",
                "13405266116",
                "15208081544",
                "16662549950",
                "13376377683",
                "16691484889",
                "13993924277",
                "13554187768",
                "13472207495",
                "13241933390",
                "13991504514",
                "19379307734",
                "17276295148",
                "19899597699",
                "18387814597",
                "15371991507",
                "17810994163",
                "18532774004",
                "18119080757",
                "13599776445",
                "19105315812",
                "19557999768",
                "15666191794",
                "18040869374",
                "15151617536",
                "13469207333",
                "16564585311",
                "18299631744",
                "15318108732",
                "15001229235",
                "18017118914",
                "17650599656",
                "14819394016",
                "18783507002",
                "14559683770",
                "15834609507",
                "19873936606",
                "13443083489",
                "14820621513",
                "16670697736",
                "14701492328",
                "18696502445",
                "15852349389",
                "17808644423",
                "13796963487",
                "15006839943",
                "15983796485",
                "17224270621",
                "17289158294",
                "16554469328",
                "19827156993",
                "14654500529",
                "15760046356",
                "19906768813",
                "16720138713",
                "17407817739",
                "19728608168",
                "13150063987",
                "13433671511",
                "18665805303",
                "18716165459",
                "17680181754",
                "19026570773",
                "13633416745",
                "15768854178",
                "18074418721",
                "13841157143",
                "16534634925",
                "13136376837",
                "14834327020",
                "13682227175",
                "18752844113",
                "15678336819",
                "18816812288",
                "17643454668",
                "13279280607",
                "14925851938",
                "19832176320",
                "15866154088",
                "19117608406",
                "13014091090",
                "18092550027",
                "15994550662",
                "17726675569",
                "16751637222",
                "19033198309",
                "13357833430",
                "15656181973",
                "14834153389",
                "18810739639",
                "19303120147",
                "15583556563",
                "14987672197",
                "17895797218"
        );
        for (String phoneNumber : phoneNumbers) {
            assertTrue(RegexUtils.isPhone(phoneNumber), phoneNumber);
        }
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
        assertFalse(RegexUtils.isFullChinese(""));
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
        assertFalse(RegexUtils.isLowerCaseAndNumber(""));
        assertFalse(RegexUtils.isLowerCaseAndNumber(string1));
        String string2 = "980bb8126e4046e3";
        assertTrue(RegexUtils.isLowerCaseAndNumber(string2));
    }

    // 验证字符串为大写+数字
    @Test
    void testUpper() {
        String string1 = "4fceb2d22F";
        assertFalse(RegexUtils.isUpperCaseAndNumber(""));
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
//        String base64_2 = "rcPTxbQ4HK38+Lwu5duH6ZboGexw3tWW5xgqNJYO90mn2ELuW6slLkpNBTi1kAbQ ";
//        assertFalse(RegexUtils.isBase64(base64_2));
        String base64_3 = "rcPTxbQ4HK38+Lwu5duH6ZboGexw3tWW5xgqNJYO90mn2ELuW6slLkpNBTi1kAbQ";
        assertTrue(RegexUtils.isBase64(base64_3));
    }

    @Test
    void testIsHexadecimal() {
        String str1 = "7e 00 4a 20 22 08 05 00 75 84 01 03 00 01 00 00 5d 48 00 00 00 17 00 00 00 ae 00 00 48 2c 00 00 01 c2 01 9b 04 5d 04 5e 04 5f 04 5d 00 18 00 18 01 4d 01 b8 01 9d 00 00 00 00 00 00 00 3f 00 00 00 00 00 00 00 00 00 00 00 00 6f 7e";
        assertTrue(RegexUtils.isHexadecimal(str1.replace(" ", "")));
        String str2 = "7e 00 4a 20 22 08 05 00 75 84 mm";
        assertFalse(RegexUtils.isHexadecimal(str2.replace(" ", "")));
        assertFalse(RegexUtils.isHexadecimal(""));
        assertTrue(RegexUtils.isNotHexadecimal(str2.replace(" ", "")));
    }

    @Test
    void testIsDomain() {
        String domain1 = "blog.51cto.com";
        String domain2 = "dev.hnlot.com.cn";
        String domain3 = "223.5.5.5";
        String domain4 = "2235fdg";
        assertFalse(RegexUtils.isDomain(""));
        assertTrue(RegexUtils.isDomain(domain1));
        assertTrue(RegexUtils.isDomain(domain2));
        assertTrue(RegexUtils.isDomain(domain3));
        assertFalse(RegexUtils.isDomain(domain4));
        String[] valid_urls = {
                // 常见的 HTTP 和 HTTPS URL
                "http://example.com",
                "https://example.com",
                "http://www.example.com",
                "https://www.example.com",
                // FTP URLs
                "ftp://example.com",
                "ftp://user:pass@example.com",
//                "ftp://192.168.1.1/resource",
                // 不带协议的域名
                "example.com",
                "www.example.com",
                "example.co.uk",
                "subdomain.example.com",
                // 带端口的 URL
                "example.com:8080",
                "example.com:80/path/to/resource",
                // 包含路径、查询参数和片段标识符的 URL
                "example.com/path/to/resource",
                "example.com/path/to/resource?query=1#fragment",
                // 带国际化字符的域名（IDN）
                // 仅域名部分包含特殊字符
                "example.com/path/to/resource%20with%20spaces",
                "example.com/#hash",
                // 不常见的协议
                "mailto:user@example.com",
                "http://example.com:99999", // Invalid port
                "http://222.244.144.131",
                "http://a.b-c.de",
                "http://cmmop.nmg-ds.hnlot.com.cn",
                "cmmop.nmg-ds.hnlot.com.cn:10110",
                // 下面无法验证的url
                "http://a.b--c.de/",
                "http://www.foo.bar./",
        };
        for (String url : valid_urls) {
            assertTrue(RegexUtils.isDomain(url), url);
        }
    }

    @Test
    void isNotDomain() {
        // 无效的 URL 和 IP 地址
        String[] invalid_urls = {
                "256.256.256.256",     // Invalid IP
                "http://",              // Invalid URL
                "https://",             // Invalid URL
                "ftp://",               // Invalid URL
                "example..com",         // Invalid domain
                "http://-example.com",  // Invalid domain
                "http://example-.com",  // Invalid domain
                "http://.example.com",   // Invalid domain
                "http://example.com:abc", // Invalid port
                "just_a_string",        // Invalid format
                "https://xn--fsq@p1ai", // Russian IDN example (пример.рф)
                "http://-a.b.co",
                "http://foo.bar/foo(bar)baz quux",
                "http://10.1.1.1",
                "ftp://192.168.1.1/resource",
        };
        for (String url : invalid_urls) {
            assertTrue(RegexUtils.isNotDomain(url), url);
        }
    }

    @Test
    void testIsBase64Image() {
        // 真实图片
        String str1 = "data:image/jpeg;base64,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";
        assertTrue(RegexUtils.isBase64(str1));
        String base64WithHeader = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/yQALCAABAAEBAREA/8wABgAQEAX/2gAIAQEAAD8A0sUpQKBSqVf/2Q==";
        String validBase64 = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/yQALCAABAAEBAREA/8wABgAQEAX/2gAIAQEAAD8A0sUpQKBSqVf/2Q==";
        String invalidBase64 = "This is not Base64!";
        String invalidBase64WithHeader = "data:text/plain;base64,This is not Base64!";
        String base64WithWhitespace = " /9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/yQALCAABAAEBAREA/8wABgAQEAX/2gAIAQEAAD8A0sUpQKBSqVf/2Q== ";
        assertTrue(RegexUtils.isBase64(base64WithHeader));
        assertTrue(RegexUtils.isBase64(validBase64));
        assertFalse(RegexUtils.isBase64(invalidBase64));
        assertFalse(RegexUtils.isBase64(invalidBase64WithHeader));
        assertTrue(RegexUtils.isBase64(base64WithWhitespace));
        assertFalse(RegexUtils.isBase64(null));
    }

    @Test
    void testValidBase64() {
        assertTrue(RegexUtils.isBase64("SGVsbG8gV29ybGQh")); // "Hello World!" 的 Base64 编码
        assertTrue(RegexUtils.isBase64("data:text/plain;base64,SGVsbG8gV29ybGQh"));//带头的
    }

    @Test
    void testIsOtherChars() {
        // 测试用例 1：普通字符串，不含任何控制字符
        assertTrue(!RegexUtils.isOtherChars("Hello World!"), "Test Case 1 Failed");

        // 测试用例 2：包含换行符
        assertTrue(RegexUtils.isOtherChars("Hello\nWorld!"), "Test Case 2 Failed");

        // 测试用例 3：包含回车符
        assertTrue(RegexUtils.isOtherChars("Hello\rWorld!"), "Test Case 3 Failed");

        // 测试用例 4：包含制表符
        assertTrue(RegexUtils.isOtherChars("Hello\tWorld!"), "Test Case 4 Failed");

        // 测试用例 5：包含多个控制字符
//        assertTrue(RegexUtils.isOtherChars("Hello\\x00World!"), "Test Case 5 Failed");

        // 测试用例 6：包含多个换行符
        assertTrue(RegexUtils.isOtherChars("Hello\nWorld\n!"), "Test Case 6 Failed");

        // 测试用例 7：空字符串
        assertTrue(!RegexUtils.isOtherChars(""), "Test Case 7 Failed");

        // 测试用例 8：仅包含空格
        assertTrue(!RegexUtils.isOtherChars("     "), "Test Case 8 Failed");
    }
}
