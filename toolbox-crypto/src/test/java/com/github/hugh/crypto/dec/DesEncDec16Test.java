package com.github.hugh.crypto.dec;

import com.github.hugh.crypto.DesEncDecUtils;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 十六进制des加密
 */
public class DesEncDec16Test {

    @Test
    void testDec() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String str1 = "http://qr.hnlot.com.cn/box/openDoor?boxCode=19FE0E30CF";
        System.out.println("-第二种加密-" + DesEncDecUtils.encrypt(str1, "box_security"));
    }

    // 第二种加密解密方式
    @Test
    void testDecrypt() throws Exception {
        String user = "root";
        String key = "johnfnash";
        String encrypt = DesEncDecUtils.encrypt(user, key);
        System.out.println("加密后的用户名: " + encrypt);
        System.out.println("解密后的用户名: " + DesEncDecUtils.decrypt(encrypt, key));
    }
}
