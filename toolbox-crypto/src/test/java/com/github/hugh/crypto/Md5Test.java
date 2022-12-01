package com.github.hugh.crypto;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        // 1234
        String str4 = "81DC9BDB52D04DC20036DBD8313ED055";
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
        String path = "D:\\public\\newFile.bin";
        File file = new File(path);
        assertEquals("4a61626dce2f490c1462acd8afa5e052", Md5Utils.encryptFile(file));
        assertEquals("4a61626dce2f490c1462acd8afa5e052", Md5Utils.encryptFile(path));

    }
}
