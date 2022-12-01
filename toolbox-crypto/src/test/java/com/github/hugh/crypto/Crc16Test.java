package com.github.hugh.crypto;

import com.github.hugh.crypto.exception.CryptoException;
import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * crc 16 工具类测试
 *
 * @author hugh
 * @version 1.0.0
 */
class Crc16Test {

    // 测试验证
    @Test
    void testDefCrc16() {
        String code1 = Crc16Utils.generate();
        System.out.println("==1=>" + code1);
        assertEquals(10, code1.length());
        assertTrue(Crc16Utils.checkCode(code1));
        assertTrue(RegexUtils.isNotLowerCaseAndNumber(code1));

        String code2 = Crc16Utils.generate(12);
        assertEquals(14, code2.length());
        assertTrue(Crc16Utils.checkCode(code2));
//        System.out.println("==2=>" + get("48F432E3F9"));
//        System.out.println(getVerCode("E5FF01140813"));
//        System.out.println(getVerCode("F33299A548"));
//		System.out.println("F33299A548".length());
//		System.out.println("==>" + m.length());
        // System.out.println(AppUtil.generateAppKey().toUpperCase());
    }

    // 测试异常
    @Test
    void testException() {
        final CryptoException cryptoException = assertThrowsExactly(CryptoException.class, () -> Crc16Utils.generate(0));
        assertEquals("length error", cryptoException.getMessage());
        final CryptoException cryptoException2 = assertThrowsExactly(CryptoException.class, () -> Crc16Utils.generate(8, 3));
        assertEquals("is not even number", cryptoException2.getMessage());
        final CryptoException cryptoException3= assertThrowsExactly(CryptoException.class, () -> Crc16Utils.generate(4, 4));
        assertEquals("code length less than verify length error", cryptoException3.getMessage());
    }

    // 磐石致维8位编码
    @Test
    void boxCode() {
        String code1 = Crc16Utils.generate(8);
        assertEquals(10, code1.length());
        assertTrue(Crc16Utils.checkCode(code1));
    }

    @Test
    void testCrc16Modbus() {
//        System.out.println(Crc16Utils.getCRC("01 03 20 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF"));
        assertEquals("4494", Crc16Utils.getModbusChecksum("14 01 01 01"));
        assertEquals("9444", Crc16Utils.getModbusChecksum("14 01 01 01", true));
        String str = "01 03 00 00 00 08".replace(" ", "");
        int num = str.length() / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(str.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
        assertEquals("0C44", Crc16Utils.getModbusChecksum(para));
        assertEquals("440C", Crc16Utils.getModbusChecksum(para, true));
//        System.out.println(Crc16Utils.getModbusChecksum("01 03 10 00 8F 02 4E 00 91 02 44 00 92 02 5A 00 8B 02 47"));
    }

    @Test
    void testCrc16ModbusVer() {
        String str = "14 01 01 01";
        String str2 = "54 30 01 00 04 31 32 33 34";
        String modbusChecksum = Crc16Utils.getModbusChecksum(str2);
        assertEquals("339C", modbusChecksum);
//        System.out.println(modbusChecksum);
        assertTrue(Crc16Utils.verifyModbus(str, "4494"));
        assertTrue(Crc16Utils.verifyModbus(str, "44 94"));
        assertTrue(Crc16Utils.verifyModbus(str, "44 94 "));
        assertTrue(Crc16Utils.verifyModbus(str, "9444", true));
        assertThrowsExactly(CryptoException.class, () -> Crc16Utils.verifyModbus(null, "4494"), "data is empty");
        assertThrowsExactly(CryptoException.class, () -> Crc16Utils.verifyModbus(str, null), "data is 2empty");
    }

    @Test
    void testWeXin() {
        // 微信 open id
        String str1 = "00f18fb4f65ab436";
        assertEquals(16, str1.length());
//        System.out.println(str1.length());
        final String generate = Crc16Utils.generate(14);
        System.out.println(generate.toLowerCase(Locale.ROOT));
        assertEquals(16, generate.length());
        assertTrue(Crc16Utils.checkCode(generate));
    }

    @Test
    void testNew() {
        int codeLength = 12;
        int verifyCodeLength = 4;
        final String generate = Crc16Utils.generate(codeLength, verifyCodeLength);
        assertEquals(codeLength + verifyCodeLength, generate.length());
        assertTrue(RegexUtils.isUpperCaseAndNumber(generate));
        assertTrue(Crc16Utils.verifyCode(generate, 4));
        final CryptoException toolboxException = assertThrowsExactly(CryptoException.class, () -> Crc16Utils.verifyCode(generate, 3));
        assertEquals("is not even number", toolboxException.getMessage());
    }

    // 测试小写
    @Test
    void testLower() {
        final String str1 = Crc16Utils.generateLowerCase(8);
        assertEquals(10, str1.length());
        assertTrue(RegexUtils.isLowerCaseAndNumber(str1));
        assertFalse(RegexUtils.isNotLowerCaseAndNumber(str1));
        final String str2 = Crc16Utils.generateLowerCase(12, 4);
        assertEquals(16, str2.length());
        String str3 = "8a566f5d68953414";
        assertTrue(RegexUtils.isLowerCaseAndNumber(str3));
        assertTrue(Crc16Utils.verifyCode(str3, 4));
    }
}
