package com.github.hugh.crypto;

import com.github.hugh.util.file.FileUtils;
import org.junit.jupiter.api.Assertions;
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
    void testRead(){
        String md5 = "676238ef7faa7e08d382c77a023f37f6";
        String url1 = "https://minio.hnlot.com.cn/host-os/standard/host_standrd-1.0.24_240611_RELEASE.bin";
        byte[] read = FileUtils.readContentByUrl(url1);
        String s = Md5Utils.encryptBytes(read);
        assertEquals(md5 , s);

        String url2 = "https://minio.hnlot.com.cn/host-os/traffic/host_traffic-1.3.6_240611_RELEASE.bin";
        byte[] read2 = FileUtils.readContentByUrl(url2);
        String strMd52 = Md5Utils.encryptBytes(read2);
        assertEquals("6ef8ae770a61cc0c1a22dc4740000adf" , strMd52);
    }
}
