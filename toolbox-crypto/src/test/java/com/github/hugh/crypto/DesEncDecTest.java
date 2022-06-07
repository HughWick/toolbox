package com.github.hugh.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * des enc 加密测试
 *
 * @author AS
 * @date 2020/9/18 15:10
 */
public class DesEncDecTest {

    @Test
    void test01() {
        DesEncDecUtils desEnc = DesEncDecUtils.getInstance("cmmop_app");
        try {
            String t1 = desEnc.encrypt("0ec4dbfdfb7945f0a6ca61fd14065a77");
            System.out.println(t1);
            String encodeURL1 = URLEncoder.encode(t1, StandardCharsets.UTF_8);
            System.out.println("-encodeURL-->>" + encodeURL1);
            String decode = URLDecoder.decode(encodeURL1, StandardCharsets.UTF_8);
            System.out.println("-decode--->>" + decode);
            System.out.println(desEnc.decrypt(decode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void urlEncryptTest() {
        DesEncDecUtils desEnc = DesEncDecUtils.getInstance("box_security");
        try {
            String t1 = desEnc.encrypt("http://qr.hnlot.com.cn/box/openDoor?boxCode=19FE0E30CF");
            System.out.println("---加密后>>" + t1);
            String encodeURL1 = URLEncoder.encode(t1, StandardCharsets.UTF_8);
            System.out.println("-encodeURL-->>" + encodeURL1);
            String decode = URLDecoder.decode(encodeURL1, StandardCharsets.UTF_8);
            System.out.println("-decode--->>" + decode);
            System.out.println("--解密->>" + desEnc.decrypt(decode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void decode() {
        DesEncDecUtils desEnc = DesEncDecUtils.getInstance("box_security");
        try {
            String str = "vpYJRBSOYCmNanjH0Oe2Ul1BiSuZ6VQG7QvqCQMDqftMBe2YnvzJfx7Es2rvl0kAzxCzD9Pd5os%3D";
            String decode = URLDecoder.decode(str, StandardCharsets.UTF_8);
            System.out.println("-decode--->>" + decode);
            System.out.println("--解密->>" + desEnc.decrypt(decode));
            assertEquals("http://qr.hnlot.com.cn/box/openDoor?boxCode=8EBFA26E46", desEnc.decrypt(decode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCheck() throws BadPaddingException, IllegalBlockSizeException {
        String key = "yinmeng0000w";
        DesEncDecUtils Des = DesEncDecUtils.getInstance(key);
        System.out.println(Des.encrypt("13825004872"));
        String string1 = "md5pass";
        String md5pass = Des.encrypt(string1);
        String result = "4LJigdM+uWM=";
        assertEquals(md5pass, result);
        assertEquals(Des.decrypt(result), string1);
        assertTrue(DesEncDecUtils.check(key, string1, result));
//        System.out.println(Des.decrypt(Des.encrypt("46010319821218091X")));
    }
}
