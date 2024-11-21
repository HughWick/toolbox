package com.github.hugh.crypto.base.sm4;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.base.Base64;
import com.github.hugh.util.base.BaseConvertUtils;
import com.lingmoyun.minilzo.MiniLZO;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SM4 国标加密算法 测试验证类
 *
 * @author AS
 * @date 2023/8/14 16:22
 */
class Sm4Test {

    private final static String key = "94f8db40a98576f249d20676776fe436";

    @Test
    void testEcb() {
        String content = "{\"name\":\"color\",\"sex\":\"man\"}";
        Sm4 on = Sm4.on().encryptKeyTypeByHex();
        String key = on.getKey();
//        System.out.println("---->" + key.length());
        assertEquals(32, key.length());
    }

    // ecb 密钥为十六进制加密方式验证
    @Test
    void testEcbKeyByHex() {
        //明文数据
//        String content = "{\"name\":\"color\",\"sex\":\"man\"}";
//        String content = "This is 一段明文内容！";
        String content = "{\"name\":\"静瑶\",\"描述\":\"测试SM4加密解密\"}";
//        String key = "94f8db40a98576f249d20676776fe436";
        Sm4 on = Sm4.on(key).encryptKeyTypeByHex();
        /*********************ECB加解密*************************/
        Sm4Result sm4Result = on.encryptEcb(content);
        String cipherText = sm4Result.toHex();
        // SM4 ECB Padding 加密结果16进制
        String hexResult = "B614335ABDE31DDB5AFB446F218A5D345A0CAED3AB74907CBA90BEA97DB49CE99402B7C55D4644AAF9E45FA1D660FE56491F4D424F55AF37DB5821CEA496B801";
        assertEquals(hexResult, cipherText);
        //SM4 ECB Padding 加密结果Base 64 :
        String result1 = "thQzWr3jHdta+0RvIYpdNFoMrtOrdJB8upC+qX20nOmUArfFXUZEqvnkX6HWYP5WSR9NQk9VrzfbWCHOpJa4AQ==";
        assertEquals(result1, sm4Result.toBase64());
        // 解密
        String decryptedData = on.decryptEcb(on.encryptEcb(content).toBase64());
//        System.out.println("SM4 ECB Padding 解密结果:\n" + decryptedData);
        assertEquals(content, decryptedData);

    }

    @Test
    void testEcbKeyByBase64() {
        String content = "This is 一段明文内容！";
//        byte[] bytes = Sm4Utils.generateKey();
//        String key = Base64.encodeToString(bytes);
//        System.out.println("==base64=>" + key);
        String base64Key = "1S2Pa1gufQxGaboTtKv+Mw==";
        Sm4 on = Sm4.on(base64Key).encryptKeyTypeByBase64();
        String cipherText = on.encryptEcb(content).toHex();
        String hexResult = "94331285E2588FD512BC901F844106C8A31760C98331D6AFF3706A4C98B70635";
        assertEquals(hexResult, cipherText);
//        System.out.println("SM4 ECB Padding 加密结果16进制:\n" + cipherText);
        String base64Result = "lDMSheJYj9USvJAfhEEGyKMXYMmDMdav83BqTJi3BjU=";
        assertEquals(base64Result, on.encryptEcb(content).toBase64());
        // 解密
        String decryptedData = on.decryptEcb(base64Result);
        assertEquals(content, decryptedData);
//        System.out.println("SM4 ECB Padding 加密结果 Base64 :\n" + on.encryptEcb(content).toBase64());
//        String key = "1234567890123456";
//        System.out.println("SM4密钥:" + key);
//        Sm4 on = Sm4.on(key).encryptKeyTypeByStr();
//        String cipherText = on.encrypt(content);
//        System.out.println("SM4 ECB Padding 加密结果16进制:\n" + cipherText);
//        String decryptedData = on.decrypt(cipherText);
//        System.out.println("SM4 ECB Padding 解密结果:\n" + decryptedData);
//        assertEquals(content, decryptedData);
    }

    @Test
    void testEcbKeyByStr() {
        String content = "This is 一段明文内容！";
//        String content = "{\"name\":\"color\",\"sex\":\"man\"}";
        String strKey = "1234567890123456";
//        String strKey = "123456789012345612345678901234561234567890123456";
        Sm4 on = Sm4.on(strKey).encryptKeyTypeByStr();
        Sm4Result sm4Result = on.encryptEcb(content);
        String hexResult = "833002FB69305CB039C0E4E7E60A876C551876530B5AD488FC45B320F592384B";
//        System.out.println("--->"+sm4Result.toHex());
        assertEquals(hexResult, sm4Result.toHex());
        String base64Result = "gzAC+2kwXLA5wOTn5gqHbFUYdlMLWtSI/EWzIPWSOEs=";
        assertEquals(base64Result, sm4Result.toBase64());
        // 解密
        assertEquals(content, on.decryptEcb(sm4Result.toBase64()));
//        final ToolboxException toolboxException = assertThrowsExactly(ToolboxException.class,
//                () -> Sm4.on("123").encryptKeyTypeByStr().encryptEcb(content));
//        assertEquals("The key length cannot be less than 16 bytes!", toolboxException.getMessage());
    }

    @Test
    void testCbcKeyByHex() {
        String content = "{\"name\":\"color\",\"sex\":\"man\"}";
        String key = "75581857C52426E1FC2723E89D59AECB";
        String ivHex = "94f8db40a98576f249d20676776fe436";
        Sm4 on = Sm4.on(key).encryptKeyTypeByHex();
        Sm4Result sm4Result = on.encryptCbc(content, ivHex);
        String base64Result = "9ACSa8qs72yOMs0mCfd9ZiXeU/X/VHXfYyRJoyte4P4=";
        assertEquals(base64Result, sm4Result.toBase64());
        assertEquals(base64Result, BaseConvertUtils.hexToBase64(sm4Result.toHex()));
        // 解密
        assertEquals(content, on.decryptCbc(base64Result, ivHex));
        // 设置结果集为16进制
        Sm4 sm4Two = Sm4.on(key).encryptKeyTypeByHex().encryptResultTypeByHex();
//        on.encryptResultTypeByHex();
        assertEquals(content, sm4Two.decryptCbc(sm4Result.toHex(), ivHex));

    }

    @Test
    void testCbcKeyByBase64() {
//        String content = "{\"name\":\"color\",\"sex\":\"man\"}";
        String content = "This is 一段明文内容！";
        String base64Key = "le7F0gaN61cBRgaSehdpsw==";
//        byte[] bytes = Sm4Utils.generateKey();
//        String base64Key = Base64.encodeToString(bytes);
//        System.out.println("-base64Key--->" + base64Key);
//        byte[] bytesIv = Sm4Utils.generateKey();
//        String base64Iv = Base64.encodeToString(bytesIv);
        String base64Iv = "y6NTpgV8F1OWMpUvwRRj8w==";
//        System.out.println("-base64Iv--->" + base64Iv);
//        String ivHex = "94f8db40a98576f249d20676776fe436";
        Sm4 on = Sm4.on(base64Key).encryptKeyTypeByBase64();
        Sm4Result sm4Result = on.encryptCbc(content, base64Iv);
        String base64Result = "H8tzQIp9nMpC5N8LLE/ecjoC/gtMEax5qqPzZ8P9P5Q=";
        assertEquals(base64Result, sm4Result.toBase64());
        // 解密
        assertEquals(content, on.decryptCbc(base64Result, base64Iv));
//        System.out.println("-toBase64--->" + sm4Result.toBase64());
    }

    @Test
    void testCbcKeyByStr() {
        String content = "This is 一段明文内容！";
//        String content = "{\"name\":\"color\",\"sex\":\"man\"}";
        String strKey = "1234567890123456";
//        String strKey = "123456789012345612345678901234561234567890123456";
//        byte[] bytesIv = Sm4Utils.generateKey();
        String base64Iv = "6543210987654321";
        Sm4 on = Sm4.on(strKey)
                .encryptKeyTypeByStr();//
        Sm4Result sm4Result = on.encryptCbc(content, base64Iv);
        String base64Result = "69eeDN6DQ0Fchukwvlr0UzRZ8+N6VzUi4iYUicFAlq4=";
        assertEquals(base64Result, sm4Result.toBase64());
        assertEquals(content, on.decryptCbc(sm4Result.toBase64(), base64Iv));
        final ToolboxException toolboxException = assertThrowsExactly(ToolboxException.class,
                () -> Sm4.on("123").encryptKeyTypeByStr().decryptCbc(content, base64Iv));
        assertEquals("密钥长度不能少于 16 字节！", toolboxException.getMessage());
        final ToolboxException toolboxException2 = assertThrowsExactly(ToolboxException.class,
                () -> Sm4.on("123").encryptKeyTypeByStr().decryptCbc(content, null));
        assertEquals("iv is null", toolboxException2.getMessage());
        // 验证
        boolean verifyCbc = on.verifyCbc(base64Result, base64Iv, content);
        assertTrue(verifyCbc);
    }

    // 测试主机上传内容
    @Test
    void testHost() {
        // NoPadding 加密
        String str1 = "c7d8aa976d299095b88947af76b1fc783c6583bff5bf660f97e86774edab9ae202ebdca8703300e9ee6cc0e5c0e56535b1af6b5eca328d86c3a0b4d049684508";
        // PKCS5Padding
        String str2 = "9513E70F9BB51256B209581E1384E07D9DC17FC547EF4D03B92A2C9366AD5CC796E18B7328BFF08A2FE30B439F531B06D6B68C4C990B8A5CF2B2771F95BBB38E0B1BC78C727E4687AF099811D0F29157F0280659C9CE240928B7B0B63EA0D06D";
        String strKey = "1234567890123456";
//        String oriStr = "{\"action\":\"heartbeat\",\"count\":\"0\",\"00030009\":\"lite_01\"}";
        Sm4 sm4 = Sm4.on(strKey).encryptKeyTypeByStr();
//        Sm4Result sm4Result = on.encryptEcb(oriStr);
//        System.out.println("====>"+sm4Result.toBase64());
//        String s1 = on.decryptEcb(sm4Result.toBase64());
//        System.out.println("=1===>>" + s1);
        sm4.encryptResultTypeByHex();
//        String s3 = on.decryptEcb(sm4Result.toHex());
//        System.out.println("===3=>>" + s3);
//        String s2 = on.decryptEcb("x9iql20pkJW4iUevdrH8eDxlg7/1v2YPl+hndO2rmuIC69yocDMA6e5swOXA5WU1sa9rXsoyjYbDoLTQSWhFCA==");
        String s1 = sm4.decryptEcb(str1);
        String result1 = "{\"action\":\"heartbeat\",\"count\":\"0\",\"00030009\":\"lite_01\"}";
        assertEquals(result1, s1.strip().trim());
//        Jsons jsons = new Jsons(s1.strip().trim());
//        System.out.println("--json====>" + jsons.toJson());
        String s2 = sm4.decryptEcb(str2);
        String result2 = "{\"action\":\"xxxxxx\",\"count\":\"xxxxxx\",\"00030009\":\"xxxxxx\",\"server_result\":\"result\"}";
        assertEquals(result2, s2);
    }

    @Test
    void testVerifyEcb() {
        String content = "{\"name\":\"静瑶\",\"描述\":\"测试SM4加密解密\"}";
        String key = "94f8db40a98576f249d20676776fe436";
        Sm4 on = Sm4.on(key).encryptKeyTypeByHex();
        //SM4 ECB Padding 加密结果Base 64 :
        String result1 = "thQzWr3jHdta+0RvIYpdNFoMrtOrdJB8upC+qX20nOmUArfFXUZEqvnkX6HWYP5WSR9NQk9VrzfbWCHOpJa4AQ==";
        // 验证
        boolean verifyEcb1 = on.verifyEcb(result1, content);
        assertTrue(verifyEcb1);
        // SM4 ECB Padding 加密结果16进制
        String hexResult = "B614335ABDE31DDB5AFB446F218A5D345A0CAED3AB74907CBA90BEA97DB49CE99402B7C55D4644AAF9E45FA1D660FE56491F4D424F55AF37DB5821CEA496B801";
        on.encryptResultTypeByHex();
        boolean verifyEcb2 = on.verifyEcb(hexResult, content);
        assertTrue(verifyEcb2);
    }

    // 测试交通版主机定时上报加密解密
    @Test
    void test04() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("测试");
//        for (int i = 0; i < 100; i++) {
        String str1 = "{\"action\":\"time\",\"00010001\":\"12.38\",\"00010007\":\"0.00\",\"00010008\":\"0.00\",\"00020001\":\"29.0\",\"00020002\":\"50.0\",\"00020003\":\"0\",\"00020004\":\"0\",\"00020009\":\"C_0019\",\"0002000a\":\"1\",\"00030003\":\"207国道\",\"00030004\":\"湖南省/邵阳市/双清区/滨江街道\",\"00050001\":\"29\",\"00050006\":\"TDD LTE\",\"00060001\":\"$GNGGA,012106.00,2716.43061,N,11129.48964,E,1,32,0.57,197.7,M,,M,,*5C\\r\\n\\r\\nOK\\r\\n\",\"00060002\":\"$GNRMC,012111.00,A,2716.43073,N,11129.48971,E,0.073,,151124,,,A,V*18\\r\\n\\r\\nOK\\r\\n\",\"00070007\":\"192.168.75.100\",\"00070009\":\"00\",\"0007000a\":\"00\",\"0007000c\":\"0\",\"0007000d\":[0,0,0,0],\"0007000e\":[0,0,0,0,0,0,0,0,0],\"0007000f\":\"\",\"00070010\":\"0\",\"01000003\":\"NULL\",\"02000003\":\"NULL\",\"03010001\":\"209.69\",\"03010002\":\"0.23\",\"03010003\":\"26.77\",\"03010006\":\"22.36\",\"06000001\":\"1\",\"06000002\":\"12.38\",\"06000003\":\"0.286\",\"06010001\":\"1\",\"06010002\":\"12.39\",\"06010003\":\"0.000\",\"06020001\":\"1\",\"06020002\":\"12.35\",\"06020003\":\"0.000\",\"06030001\":\"1\",\"06030002\":\"12.37\",\"06030003\":\"0.000\",\"07000001\":\"1\",\"07000002\":\"209.69\",\"07010001\":\"1\",\"07010002\":\"209.69\",\"07020001\":\"1\",\"07020002\":\"209.69\",\"07030001\":\"1\",\"07030002\":\"209.69\",\"07040001\":\"1\",\"07040002\":\"209.69\",\"07050001\":\"1\",\"07050002\":\"0.00\",\"07060001\":\"1\",\"07060002\":\"0.00\",\"07070001\":\"1\",\"07070002\":\"0.00\",\"07080001\":\"1\",\"07080002\":\"0.00\",\"07080002\":\"0.00\",\"01000003\":\"NULL\",\"02000003\":\"NULL\",\"00030009\":\"D01020240605250\"}";
        System.out.println("---->>>" + str1.length());
        Sm4 sm4 = Sm4.on(key).encryptKeyTypeByHex();
        /*********************ECB加解密*************************/
        Sm4Result sm4Result = sm4.encryptEcb(str1);
        String cipherText = sm4Result.toHex();
        System.out.println("==1==>" + cipherText.getBytes(StandardCharsets.UTF_8).length);
        System.out.println("==2==>" + sm4Result.bytes().length);
        byte[] compressed = MiniLZO.compress(sm4Result.bytes());
        System.out.println("===3=====>>" + compressed.length);
        byte[] bytes = BaseConvertUtils.hexToBytes(cipherText);
        assertArrayEquals(sm4Result.bytes(), bytes);
        // 解密
        String decryptedData = sm4.decryptEcb(Base64.encodeToString(bytes));
        assertEquals(str1, decryptedData);
//        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

//        System.out.println("--->" + decryptedData);
//        // SM4 ECB Padding 加密结果16进制
//        String hexResult = "B614335ABDE31DDB5AFB446F218A5D345A0CAED3AB74907CBA90BEA97DB49CE99402B7C55D4644AAF9E45FA1D660FE56491F4D424F55AF37DB5821CEA496B801";
//        assertEquals(hexResult, cipherText);
//        //SM4 ECB Padding 加密结果Base 64 :
//        String result1 = "thQzWr3jHdta+0RvIYpdNFoMrtOrdJB8upC+qX20nOmUArfFXUZEqvnkX6HWYP5WSR9NQk9VrzfbWCHOpJa4AQ==";
//        assertEquals(result1, sm4Result.toBase64());
//        // 解密
//        String decryptedData = sm4.decryptEcb(sm4.encryptEcb(str1).toBase64());
////        System.out.println("SM4 ECB Padding 解密结果:\n" + decryptedData);
//        assertEquals(str1, decryptedData);
    }


    @Test
    void test64() {
        String str1 = "Y2oJiZAgshH/xyIyPm4D/q6T2bdAObsLR9Isvc2CvvI73T8bflodpDr/JqrHxcW8MKKy8UpQycRrL6dcIg/OP0u4/dOuXLSD9W2/Qf/o5X0pO/mlKYwu9RJsI6myBPVzCUh5t1eBI14IoFPHywTmjyv1Vz2qa3z6fPGrMGcFWQiFrIHz9IWbxII6tT4Hmv1Gk5uQWC3MMn4zfEOGbOiDx205B+A9rc6I9WKIjFwUsijhNP0pUVrfKKo+QXPsiL113m+aXN6t16YXRzM6Bbhx6g==";
        Sm4 sm4 = Sm4.on(("1234567890123456")).encryptKeyTypeByStr();
        String s = sm4.decryptEcb(str1);
        System.out.println("==>>" + s);

    }
}
