package com.github.hugh.crypto.dec;

import com.github.hugh.crypto.DesEncDecUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 十六进制des加密
 */
class DesEncDec16Test {

    // 第二种加密解密方式
    @Test
    void testDecrypt() {
        try {
            String user = "root";
            String key = "johnfnash";
            String encrypt = DesEncDecUtils.encrypt(user, key);
            assertEquals("8B524C34459FA1BF", encrypt);
            assertEquals(user, DesEncDecUtils.decrypt(encrypt, key));
            String str1 = "http://qr.hnlot.com.cn/box/openDoor?boxCode=19FE0E30CF";
            assertEquals("BE960944148E60298D6A78C7D0E7B6525D41892B99E95406ED0BEA090303A9FB4C05ED989EFCC97F183CCC0C0C28A9388039A6AE21B38EDD", DesEncDecUtils.encrypt(str1, "box_security"));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
//        System.out.println("加密后的用户名: " + encrypt);
//        System.out.println("解密后的用户名: " + DesEncDecUtils.decrypt(encrypt, key));
    }
}
