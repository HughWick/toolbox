package com.github.hugh.crypto.dec;

import com.github.hugh.crypto.DesEncDecUtils;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * des enc 加密测试
 *
 * @author AS
 * @date 2020/9/18 15:10
 */
class DesEncDecTest {

    // 先获取加密实体进行加密解密
    @Test
    void testEncryptDecode() {
        String keys = "cmmop_app";
        DesEncDecUtils desEnc = DesEncDecUtils.getInstance(keys);
        try {
            String str1 = "0ec4dbfdfb7945f0a6ca61fd14065a77";
            String t1 = desEnc.encrypt(str1);
            assertEquals("cGPyInMT06IHCpWrhsMVyRfyT687mSqIQFruNjmAxeRkuYJR6xka0Q==", t1);
            String encodeURL1 = URLEncoder.encode(t1, StandardCharsets.UTF_8);
            assertEquals("cGPyInMT06IHCpWrhsMVyRfyT687mSqIQFruNjmAxeRkuYJR6xka0Q%3D%3D", encodeURL1);
            String decode = URLDecoder.decode(encodeURL1, StandardCharsets.UTF_8);
            assertEquals(t1, decode);
            assertEquals(str1, desEnc.decrypt(decode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // URL 加密解密测试
    @Test
    void urlEncryptTest() {
        DesEncDecUtils desEnc = DesEncDecUtils.getInstance("box_security");
        try {
            String urlStr1 = "http://qr.hnlot.com.cn/box/openDoor?boxCode=19FE0E30CF";
            String encrypt1 = desEnc.encrypt(urlStr1);
            assertEquals("vpYJRBSOYCmNanjH0Oe2Ul1BiSuZ6VQG7QvqCQMDqftMBe2YnvzJfxg8zAwMKKk4gDmmriGzjt0=", encrypt1);
            // URL加密
            String encodeURL1 = URLEncoder.encode(encrypt1, StandardCharsets.UTF_8);
            assertEquals("vpYJRBSOYCmNanjH0Oe2Ul1BiSuZ6VQG7QvqCQMDqftMBe2YnvzJfxg8zAwMKKk4gDmmriGzjt0%3D", encodeURL1);
            String decode = URLDecoder.decode(encodeURL1, StandardCharsets.UTF_8);
            // URL 反编译
            assertEquals(encrypt1, decode);
            // 最终密文解密
            assertEquals(urlStr1, desEnc.decrypt(decode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDecode() {
        String key = "box_security";
        DesEncDecUtils desEnc = DesEncDecUtils.getInstance(key);
        try {
            String str = "vpYJRBSOYCmNanjH0Oe2Ul1BiSuZ6VQG7QvqCQMDqftMBe2YnvzJfx7Es2rvl0kAzxCzD9Pd5os%3D";
            String result1 = "http://qr.hnlot.com.cn/box/openDoor?boxCode=8EBFA26E46";
            String decode = URLDecoder.decode(str, StandardCharsets.UTF_8);
            assertEquals("vpYJRBSOYCmNanjH0Oe2Ul1BiSuZ6VQG7QvqCQMDqftMBe2YnvzJfx7Es2rvl0kAzxCzD9Pd5os=", decode);
            assertEquals(result1, desEnc.decrypt(decode));
//            System.out.println("-decode--->>" + decode);
//            System.out.println("--解密->>" + desEnc.decrypt(decode));
//            assertEquals("http://qr.hnlot.com.cn/box/openDoor?boxCode=8EBFA26E46", desEnc.decrypt(decode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 校验
    @Test
    void testCheck() throws BadPaddingException, IllegalBlockSizeException {
        String key = "yinmeng0000w";
        DesEncDecUtils Des = DesEncDecUtils.getInstance(key);
        assertEquals("JDOq2XbTaBBv0cTG+csoDQ==", Des.encrypt("13825004872"));
        String string1 = "md5pass";
        String md5pass = Des.encrypt(string1);
        String result = "4LJigdM+uWM=";
        assertEquals(md5pass, result);
        assertEquals(Des.decrypt(result), string1);
        assertTrue(DesEncDecUtils.check(key, string1, result));
//        System.out.println(Des.decrypt(Des.encrypt("46010319821218091X")));
    }
}
