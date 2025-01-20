package com.github.hugh.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * md5 加密工具测试
 *
 * @author hugh
 * @version 1.0.0
 */
class Md5Test {

    @Test
    void testStringMd5() {
        String password1 = "1234";
        String str1 = "";
        // 1234
        String str4 = "81DC9BDB52D04DC20036DBD8313ED055";
        assertEquals(str1 ,Md5Utils.lowerCase(str1) );
        assertEquals(str4.toLowerCase(), Md5Utils.lowerCase(password1));
        assertEquals(str4, Md5Utils.upperCase(password1));
        // 八个8
        String str2 = "8ddcff3a80f4189ca1c9d4d902c3c909";
        assertEquals(str2, Md5Utils.lowerCase("88888888"));
        assertEquals("19954ff325e4a00662a5521d055c26e1", Md5Utils.lowerCase("8566889"));
        assertEquals("f854b68c6f8b2195704f76e05aaa65a1", Md5Utils.lowerCase("huahua"));
        //f854b68c6f8b2195704f76e05aaa65a1
        assertEquals(32, Md5Utils.upperCase("1234").length());
    }

    @Test
    void testFile() {
        String ip2DbPath = Md5Test.class.getResource("/logo.png").getFile();
        Assertions.assertNotNull(ip2DbPath);
        File file = new File(ip2DbPath);
        String md5Result = "559e0c4bd56eb91202e60b398b8c556d";
        assertEquals(md5Result, Md5Utils.encryptFile(file));
        assertEquals(md5Result, Md5Utils.encryptFile(ip2DbPath));
    }

    @Test
    void testFileBin() {
        String ip2DbPath = Md5Test.class.getResource("/host_display_gd32.bin").getFile();
        Assertions.assertNotNull(ip2DbPath);
        File file = new File(ip2DbPath);
        String md5Result = "018f36293f3c4ed46674e19e87f731f4";
        assertEquals(md5Result, Md5Utils.encryptFile(file));
    }
    @Test
    void testEncryptBytes_emptyInput() {
        byte[] input = new byte[0];
        String expected = "d41d8cd98f00b204e9800998ecf8427e";
        String actual = Md5Utils.encryptBytes(input);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptBytes_simpleString() {
        String inputString = "hello";
        byte[] input = inputString.getBytes(StandardCharsets.UTF_8);
        String expected = "5d41402abc4b2a76b9719d911017c592";
        String actual = Md5Utils.encryptBytes(input);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptBytes_differentString() {
        String inputString = "world123";
        byte[] input = inputString.getBytes(StandardCharsets.UTF_8);
        String expected = "9a4cd921287bec14b5626c20d6f821bc";
        String actual = Md5Utils.encryptBytes(input);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptBytes_withSpecialCharacters() {
        String inputString = "!@#$%^&*()_+=-`~[]\\{}|;':\",./<>?";
        byte[] input = inputString.getBytes(StandardCharsets.UTF_8);
        String expected = "6772da2cc1d35686091f17b0ccd17481";
        String actual = Md5Utils.encryptBytes(input);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptBytes_longerString() {
        String inputString = "This is a longer string to test the MD5 encryption.";
        byte[] input = inputString.getBytes(StandardCharsets.UTF_8);
        String expected = "22a1e66f16a2c56175b30b9ef0f1388b";
        String actual = Md5Utils.encryptBytes(input);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptBytes_binaryData() {
        byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a};
        String expected = "70903e79b7575e3f4e7ffa15c2608ac7";
        String actual = Md5Utils.encryptBytes(input);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptBytes_differentByteValues() {
        byte[] input = {-128, 0, 127, -1, 1}; // Example with negative, zero, and positive values
        String expected = "a57805737be37f3023eb0209e5d483d6";
        String actual = Md5Utils.encryptBytes(input);
        assertEquals(expected, actual);
    }

    @Test
    void testEncryptBytes_sameInputDifferentEncodings() throws UnsupportedEncodingException {
        String inputString = "你好";
        byte[] utf8Bytes = inputString.getBytes(StandardCharsets.UTF_8);
        byte[] gbkBytes = inputString.getBytes("GBK"); // Assuming GBK is supported

        String md5Utf8 =Md5Utils.encryptBytes(utf8Bytes);
        String md5Gbk = Md5Utils.encryptBytes(gbkBytes);

        assertNotEquals(md5Utf8, md5Gbk, "MD5 should be different for different encodings");
    }
}
