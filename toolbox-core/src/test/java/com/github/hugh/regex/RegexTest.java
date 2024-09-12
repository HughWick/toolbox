package com.github.hugh.regex;

import com.github.hugh.util.base.Base64;
import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        // 输出测试数据
        for (String phoneNumber : testValidPhoneNumbers) {
            assertTrue(RegexUtils.isPhone(phoneNumber) , phoneNumber);
        }
    }

    @Test
    void testPhoneNumber2(){
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
            assertTrue(RegexUtils.isPhone(phoneNumber) , phoneNumber);
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
    void testIsHexadecimal() {
        String str1 = "7e 00 4a 20 22 08 05 00 75 84 01 03 00 01 00 00 5d 48 00 00 00 17 00 00 00 ae 00 00 48 2c 00 00 01 c2 01 9b 04 5d 04 5e 04 5f 04 5d 00 18 00 18 01 4d 01 b8 01 9d 00 00 00 00 00 00 00 3f 00 00 00 00 00 00 00 00 00 00 00 00 6f 7e";
        assertTrue(RegexUtils.isHexadecimal(str1.replace(" ", "")));
        String str2 = "7e 00 4a 20 22 08 05 00 75 84 mm";
        assertFalse(RegexUtils.isHexadecimal(str2.replace(" ", "")));
        assertTrue(RegexUtils.isNotHexadecimal(str2.replace(" ", "")));
    }

    @Test
    void testIsDomain() {
        String domain1 = "blog.51cto.com";
        String domain2 = "dev.hnlot.com.cn";
        String domain3 = "223.5.5.5";
        String domain4 = "2235fdg";
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
}
