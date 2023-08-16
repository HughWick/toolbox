package com.github.hugh.crypto;

import com.github.hugh.util.base.Base64;
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
        assertEquals(s1, Base64.encodeToString(str.getBytes()));
        String decode = Base64.decode(s1);
        assertEquals(str, decode);
        assertEquals(decode, Base64.decodeToString(s1.getBytes()));
        String str3 = "中";
        assertEquals(2, str3.getBytes("gb2312").length);
    }
}
