package com.github.hugh.base;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.base.BaseConvertUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 进制转换测试类
 *
 * @author AS
 * @date 2020/9/14 14:58
 */
public class BaseConvertTest {

    // 十进制转换
    @Test
    void decToTexTest() {
        String str1 = "256";
        System.out.println("=1==>>>>" + BaseConvertUtils.decToHex(84213840));
        assertEquals("100", BaseConvertUtils.decToHex(str1));
        String str2 = "255";
        // 十进制转十六
        String hexStr = BaseConvertUtils.decToHex(str2);
//        // 十进制字符串转十六进制数组
        byte[] hexToBytes = BaseConvertUtils.decToHexBytes(str2);
        System.out.println(Arrays.toString(hexToBytes));
        // 十六进制数组转 十六进制字符串
        String s = BaseConvertUtils.bytesToHexString(hexToBytes);
        assertEquals(hexStr.toUpperCase(), s);
//        assertEquals(hexStr.toUpperCase() , BaseConvertUtils.decToHexBytes(str2));
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

    // 16进制转字符串
    @Test
    void testHexToString() {
        String str = "61:62";
        assertEquals(BaseConvertUtils.hexToString(str, ":"), "ab");
        Assertions.assertThrowsExactly(ToolboxException.class, () -> {
            BaseConvertUtils.hexToString(str, null);
        });
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
        String str = "043F";
        String str2 = "4A3F7F40";
        String str3 = "7869616f7a68693130323031";
        byte[] bytes = BaseConvertUtils.hexToBytes(str);
        System.out.println("--0->>>" + Arrays.toString(bytes));
        System.out.println("十六进制数组转字符串-》" + BaseConvertUtils.bytesToHexString(bytes));
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
        System.out.println("=4==>>" + BaseConvertUtils.hexToAscii(str3));
    }

    @Test
    void testTex02() {
        String tex = "7a686f75353937383831";
        System.out.println(BaseConvertUtils.hexToAscii(tex));
        String zhou597881 = BaseConvertUtils.asciiToHex("zhou597881");
        System.out.println("----->>" + zhou597881);
        System.out.println("----->>" + BaseConvertUtils.hexToAscii(zhou597881.replace(" ", "")));
        String s = BaseConvertUtils.asciiToHex("4", " ");
        System.out.println("===1====>" + s);
        String str1 = "7e 00 18 20 20 10 26 02 88 87 00 10 00 0b 7a 68 6f 75 35 39 37 38 38 31 c6 7e";
        String[] strings = str1.split(" ");
        byte[] bytes = BaseConvertUtils.hexArrToBytes(strings);
        System.out.println(new String(bytes));
        byte[] bytes2 = BaseConvertUtils.hexArrToBytes(str1, " ");
        System.out.println(new String(bytes2));
    }
}
