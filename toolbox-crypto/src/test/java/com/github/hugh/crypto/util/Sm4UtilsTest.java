package com.github.hugh.crypto.util;

import com.github.hugh.util.base.BaseConvertUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * sm4 加密解密测试类
 *
 * @author AS
 * @date 2023/8/14 10:03
 */
class Sm4UtilsTest {

    @Test
    void testEcb() {
        //明文数据
//        String SRC_DATA = "{\"name\":\"color\",\"sex\":\"man\"}";
//        String SRC_DATA = "This is 一段明文内容！";
        String SRC_DATA = "{\"name\":\"静瑶\",\"描述\":\"测试SM4加密解密\"}";
        byte[] sm4key = Sm4Utils.generateKey();
        assert sm4key != null;
        String key = BaseConvertUtils.hexBytesToString(sm4key).toLowerCase();
        System.out.println("SM4密钥:" + key);
        byte[] iv = Sm4Utils.generateKey();
        String ivStr = BaseConvertUtils.hexBytesToString(iv);
        System.out.println("iv偏移量密钥:" + ivStr);
        String cipherText;

        /*********************ECB加解密*************************/
        cipherText = Sm4Utils.encryptEcbPaddingToHex(key, SRC_DATA);
        System.out.println("SM4 ECB Padding 加密结果16进制:\n" + cipherText);
        String decryptedData;
        decryptedData = Sm4Utils.decryptEcbPadding(key, cipherText);
        System.out.println("SM4 ECB Padding 解密结果:\n" + decryptedData);
        assertEquals(SRC_DATA, decryptedData);
//        boolean verifyEcb = Sm4Utils.verifyEcb(key, cipherText, SRC_DATA);
//        assertTrue(verifyEcb);
    }

    /*********************CBC加解密*************************/
    @Test
    void testCbc() {
//        String SRC_DATA = "{\"name\":\"静瑶\",\"描述\":\"测试SM4加密解密\"}";
        // 文明
        String plainText = "This is 一段明文内容！";
        byte[] sm4key = Sm4Utils.generateKey();
        assert sm4key != null;
        String key = BaseConvertUtils.hexBytesToString(sm4key).toLowerCase();
        System.out.println("SM4密钥:" + key);
        byte[] iv = Sm4Utils.generateKey();
        String ivStr = BaseConvertUtils.hexBytesToString(iv);
        System.out.println("iv偏移量密钥:" + ivStr);
        String cipherText;
        cipherText = Sm4Utils.encryptCbcPadding(key, ivStr, plainText);
        System.out.println("SM4 CBC Padding 加密结果16进制:\n" + cipherText);
        String decryptedData = Sm4Utils.decryptCbcPadding(key, ivStr, cipherText);
        System.out.println("SM4 CBC Padding 解密结果:\n" + decryptedData);
//        boolean verifyCbc = Sm4Utils.verifyCbc(key, cipherText, ivStr, plainText);
//        assertTrue(verifyCbc);
        assertEquals(plainText, decryptedData);
    }
}
