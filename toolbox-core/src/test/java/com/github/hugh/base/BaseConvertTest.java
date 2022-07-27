package com.github.hugh.base;

import com.github.hugh.util.base.BaseConvertUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 进制转换测试类
 *
 * @author AS
 * @date 2020/9/14 14:58
 */
public class BaseConvertTest {

    // 十进制转 十六进制字符串
    @Test
    void decToHexTest() {
        String str1 = "256";
        assertEquals("100", BaseConvertUtils.decToHex(str1));
        String str2 = "255";
        String hexStr = BaseConvertUtils.decToHex(str2);
        assertEquals("FF", hexStr.toUpperCase());
    }

    // 十进制转十六进制数组
    @Test
    void decToHexBytesTest() {
        String str2 = "255";
        // 十进制字符串转十六进制数组
        byte[] hexToBytes = BaseConvertUtils.decToHexBytes(str2);
        System.out.println("---1----><" + Arrays.toString(hexToBytes));
        // 十六进制数组转 十六进制字符串
        String s = BaseConvertUtils.hexBytesToString(hexToBytes);
        assertEquals("FF", s);
        String str = "6162";
        assertEquals(BaseConvertUtils.hexToAscii(str), "ab");
        // 十进制的数组
        byte[] dec = {84, 48, 1, 0, 4, 49, 50, 51, 52};
        // 十六进制的字符串
        String result = "543001000431323334";
        assertEquals(result, BaseConvertUtils.hexBytesToString(dec));
        String result2 = "54 30 01 00 04 31 32 33 34";
        assertEquals(result2, BaseConvertUtils.hexBytesToString(dec, " "));
    }

    // 十进制转二进制
    @Test
    void testDecToBinary() {
        assertEquals(BaseConvertUtils.decToBinary(10, 12), "000000001010");
        assertEquals(BaseConvertUtils.decToBinary(10), "1010");
        String s = BaseConvertUtils.decToBinary("4");
        assertEquals(s, "100");
        String s1 = BaseConvertUtils.decToBinary("4", 4);
        System.out.println(s1);
    }

    // 二进制转十进制
    @Test
    void testBinaryToDec() {
        String s = BaseConvertUtils.binaryToDec("100");
        assertEquals(s, "4");
    }

    // 十六进制转二进制
    @Test
    void testHexToBinary() {
        String str = "043F";
        assertEquals(BaseConvertUtils.hexToBinary(str, 15), "000010000111111");
        assertEquals(BaseConvertUtils.hexToBinary(str), "10000111111");
    }

    // 十六进制转10进制字符串
    @Test
    void testHexToDecString() {
        assertEquals(BaseConvertUtils.hexToDecString("8C"), "140");
    }

    @Test
    void testTex() {
        String asciiStr = "xiaozhi10201";
        String str = "043F";
        String str2 = "4A3F7F40";
        String str3 = "7869616f7a68693130323031";
        byte[] bytes = BaseConvertUtils.hexToBytes(str);
        System.out.println("--0->>>" + Arrays.toString(bytes));
        byte bytes2 = BaseConvertUtils.hexToByte(str2);
        System.out.println(String.valueOf(bytes2));
        String s = BaseConvertUtils.hexToAscii(str2);
        System.out.println("---1->>>" + s);
        String s1 = BaseConvertUtils.asciiToHex(s);
        System.out.println("==2=>>" + s1);
        String s2 = BaseConvertUtils.asciiToHex(s, "_");
        System.out.println("==3=>>" + s2);
        String s3 = BaseConvertUtils.asciiToHex(s, " ");
        System.out.println("==3=>>" + s3);
        assertEquals(asciiStr, BaseConvertUtils.hexToAscii(str3));
    }

    @Test
    void testTex02() {
        // 十六进制的 zhou597881
        String tex = "7a686f75353937383831";
        String name = "zhou597881";
//        System.out.println(BaseConvertUtils.hexToAscii(tex));
        assertEquals(BaseConvertUtils.hexToAscii(tex), name);

        String zhouHex = BaseConvertUtils.asciiToHex(name);
        assertEquals(zhouHex, tex.toUpperCase());

        String s = BaseConvertUtils.asciiToHex("4");
        assertEquals(s, "34");

        String str1 = "7e 00 18 20 20 10 26 02 88 87 00 10 00 0b 7a 68 6f 75 35 39 37 38 38 31 c6 7e";
        String[] strings = str1.split(" ");
        byte[] bytes = BaseConvertUtils.hexArrToBytes(strings);
        String replace = str1.replace(" ", "");
        String s1 = BaseConvertUtils.hexToAscii(replace);
        assertEquals(new String(bytes), s1);

        byte[] hexToBytes = BaseConvertUtils.hexToBytes(replace);
        assertArrayEquals(bytes, hexToBytes);
    }

    @Test
    void testIntToByte() {
        String str1 = "FC";
        int a = 252;
        byte[] bytes1 = BaseConvertUtils.decToHexBytes(a);
        String s = BaseConvertUtils.decToHex(a);
        assertEquals(str1, BaseConvertUtils.hexBytesToString(bytes1));
        assertEquals(str1, s.toUpperCase());
        String str = "252";
        // 字符串获取getBytes为十进制数组
        System.out.println(Arrays.toString(str.getBytes()));
        System.out.println("--->>" + BaseConvertUtils.hexBytesToString(str.getBytes()));
    }

    @Test
    void testHexToDecReInt() {
        int a = 256;
        byte[] bytes1 = BaseConvertUtils.decToHexBytes(a);
        assertEquals(BaseConvertUtils.hexToDecReInt(bytes1), a);
        byte[] totalHexBytes = BaseConvertUtils.decToHexBytes(a);
        System.out.println(Arrays.toString(totalHexBytes));

        System.out.println(BaseConvertUtils.hexBytesToString(totalHexBytes));
    }


}
