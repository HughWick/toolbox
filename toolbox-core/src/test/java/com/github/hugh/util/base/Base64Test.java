package com.github.hugh.util.base;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * base 64 加密工具
 *
 * @author AS
 * @date 2020/8/31 15:43
 */
class Base64Test {

    @Test
    void testBase64() throws UnsupportedEncodingException {
        String str = "123zvb@!";
        String s1 = Base64.encode(str);
        assertEquals("MTIzenZiQCE=", s1);
        assertEquals("", Base64.encode(""));
        assertEquals(s1, Base64.encodeToString(str.getBytes()));
        String decode = Base64.decode(s1);
        assertEquals(str, decode);
        assertEquals("", Base64.decode(""));
        assertEquals(decode, Base64.decodeToString(s1.getBytes()));
        String str3 = "中";
        assertEquals(2, str3.getBytes("gb2312").length);
    }
    // 测试正常字符串
    @Test
    void testNormalString() {
        String text = "Hello";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->Base64.decode(text.getBytes()));
        assertEquals("Last unit does not have enough valid bits", illegalArgumentException.getMessage());
    }

    // 测试空字符串
    @Test
    void testEmptyString() {
        String text = "";
        byte[] expected = Base64.decode(text.getBytes());
        byte[] actual = Base64.decodeToByte(text);
        assertArrayEquals(expected, actual, "Test failed for empty string");
    }

    // 测试包含特殊字符的字符串
    @Test
    void testSpecialCharacters() {
        String text = "!@#$%^&*()";
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->Base64.decode(text.getBytes()));
        assertEquals("Illegal base64 character 21", illegalArgumentException.getMessage());
    }

    // 测试 null 字符串
    @Test
    void testNullString() {
        String text = null;
//        byte[] expected = new byte[0]; // 假设 decodeToByte 处理 null 并返回空字节数组
        assertNull(Base64.decodeToByte(text));
//        byte[] actual = Base64.decodeToByte(text);
//        assertArrayEquals(expected, actual, "Test failed for null string");
    }
}
