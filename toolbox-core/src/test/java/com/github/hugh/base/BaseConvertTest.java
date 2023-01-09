package com.github.hugh.base;

import com.github.hugh.constant.StrPool;
import com.github.hugh.util.base.BaseConvertUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 二、十、十六进制转换测试类
 *
 * @author AS
 * @date 2020/9/14 14:58
 */
class BaseConvertTest {

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
        assertEquals(Arrays.toString(new byte[]{-1}), Arrays.toString(hexToBytes));
//        System.out.println("---1----><" + Arrays.toString(hexToBytes));
        // 十六进制数组转 十六进制字符串
        String s = BaseConvertUtils.hexBytesToString(hexToBytes);
        assertEquals("FF", s);
        String str = "6162";
        assertEquals("ab", BaseConvertUtils.hexToAscii(str));
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
        String str = "043F";
        long l = BaseConvertUtils.hexToDec(str);
        assertEquals(1087L, l);
        assertEquals("10000111111", BaseConvertUtils.decToBinary(l));
        assertEquals("0000010000111111", BaseConvertUtils.decToBinary(l, 16));
        assertEquals("000000001010", BaseConvertUtils.decToBinary(10, 12));
        assertEquals("1010", BaseConvertUtils.decToBinary(10));
        String s = BaseConvertUtils.decToBinary("4");
        assertEquals("100", s);
        String s1 = BaseConvertUtils.decToBinary("4", 4);
        assertEquals("0100", s1);
        String hexStr1 = "31";
        assertEquals("110001", BaseConvertUtils.hexToBinaryStr(hexStr1));
        assertEquals("0000000000110001", BaseConvertUtils.hexToBinaryStr(hexStr1, 16));
    }

    // 二进制转十进制
    @Test
    void testBinaryToDec() {
        String s = BaseConvertUtils.binaryToDec("100");
        assertEquals("4", s);
    }

    // 十六进制转二进制
    @Test
    void testHexToBinary() {
        String str = "043F";
        assertEquals("000010000111111", BaseConvertUtils.hexToBinary(str, 15));
        assertEquals("10000111111", BaseConvertUtils.hexToBinary(str));
    }

    // 十六进制转10进制字符串
    @Test
    void testHexToDecString() {
        assertEquals("140", BaseConvertUtils.hexToDecString("8C"));
        String acHex = "80000000";
        assertEquals("2147483648", BaseConvertUtils.hexToDecString(acHex));
    }

    // 测试十六进制转换
    @Test
    void testTex() {
        String asciiStr = "xiaozhi10201";
        String str = "043F";
        String str2 = "4A3F7F40";
        String str3 = "7869616f7a68693130323031";
        byte[] bytes = BaseConvertUtils.hexToBytes(str);
        // 十进制的数组
        assertArrayEquals(new byte[]{4, 63}, bytes);
        byte bytes2 = BaseConvertUtils.hexToByte(str2);
        assertEquals("64", String.valueOf(bytes2));
        String s = BaseConvertUtils.hexToAscii(str2);
        String s1 = BaseConvertUtils.asciiToHex(s);
        assertEquals(str2, s1);
        String s2 = BaseConvertUtils.asciiToHex(s, "_");
        assertEquals("4A_3F_7F_40", s2);
        String s3 = BaseConvertUtils.asciiToHex(s, " ");
        assertEquals("4A 3F 7F 40", s3);
        assertEquals(asciiStr, BaseConvertUtils.hexToAscii(str3));
        // 十进制的数组
        byte[] bytes1 = new byte[]{65, 81};
        String s4 = BaseConvertUtils.hexToAscii(bytes1);
        assertEquals("AQ", s4);
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
        assertEquals("34", s);

        String str1 = "7e 00 18 20 20 10 26 02 88 87 00 10 00 0b 7a 68 6f 75 35 39 37 38 38 31 c6 7e";
        String[] strings = str1.split(StrPool.SPACE);
        byte[] bytes = BaseConvertUtils.hexArrToBytes(strings);
        String replace = str1.replace(StrPool.SPACE, StrPool.EMPTY);
        String s1 = BaseConvertUtils.hexToAscii(replace);
        assertEquals(new String(bytes), s1);

        byte[] hexToBytes = BaseConvertUtils.hexToBytes(replace);
        assertArrayEquals(bytes, hexToBytes);
    }

    // 测试int转byte数组
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
        assertArrayEquals(new byte[]{50, 53, 50}, str.getBytes());
        assertEquals("323532", BaseConvertUtils.hexBytesToString(str.getBytes()));
    }

    // 16 进制转10进制数组结果
    @Test
    void testHexToDecReInt() {
        int a = 256;
        byte[] bytes1 = BaseConvertUtils.decToHexBytes(a);
        assertEquals(BaseConvertUtils.hexToDecReInt(bytes1), a);
        byte[] totalHexBytes = BaseConvertUtils.decToHexBytes(a);
        assertEquals(Arrays.toString(new byte[]{1, 0}), Arrays.toString(totalHexBytes));
//        System.out.println(Arrays.toString(totalHexBytes));
        assertEquals("0100", BaseConvertUtils.hexBytesToString(totalHexBytes));

        int length = 100;
        byte[] bytes = BaseConvertUtils.decToHexBytes(length);
        assertArrayEquals(new byte[]{100}, bytes);
    }
}
