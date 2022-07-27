package com.github.hugh.crypto;

import com.github.hugh.crypto.exception.CryptoException;
import com.github.hugh.util.base.BaseConvertUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hugh
 * @version 1.0.0
 */
public class Crc16Test {

    @Test
    void test01() {
        String m = Crc16Utils.generate();
        System.out.println("==1=>" + m);
//        System.out.println("==2=>" + get("48F432E3F9"));
//        System.out.println(getVerCode("E5FF01140813"));
//        System.out.println(getVerCode("F33299A548"));
//		System.out.println("F33299A548".length());
        // 05CF
        System.out.println("-->" + Crc16Utils.checkCode(m));
//		System.out.println("==>" + m.length());
        // System.out.println(AppUtil.generateAppKey().toUpperCase());
    }

    @Test
    void test02() {
        String m = Crc16Utils.generate(12);
        System.out.println("==1=>" + m);
        System.out.println("==2=>" + m.length());
        System.out.println("==3=>" + Crc16Utils.checkCode(m));
    }

    @Test
    void boxCode() {
        String m = Crc16Utils.generate(8);
        System.out.println("==1=>" + m);
        System.out.println("==2=>" + m.length());
        System.out.println("==3=>" + Crc16Utils.checkCode(m));
    }

    @Test
    void testCrc16Modbus() {
//        System.out.println(Crc16Utils.getCRC("01 03 20 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF 7F FF"));
        assertEquals(Crc16Utils.getModbusChecksum("14 01 01 01"), "4494");
        assertEquals(Crc16Utils.getModbusChecksum("14 01 01 01", true), "9444");
        String str = "01 03 00 00 00 08".replace(" ", "");
        int num = str.length() / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(str.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
        assertEquals(Crc16Utils.getModbusChecksum(para), "0C44");
        assertEquals(Crc16Utils.getModbusChecksum(para, true), "440C");
//        System.out.println(Crc16Utils.getModbusChecksum("01 03 10 00 8F 02 4E 00 91 02 44 00 92 02 5A 00 8B 02 47"));
    }

    @Test
    void testCrc16ModbusVer() {
        String str = "14 01 01 01";
        String str2 = "54 30 01 00 04 31 32 33 34";
        String modbusChecksum = Crc16Utils.getModbusChecksum(str2);
        System.out.println(modbusChecksum);
        assertTrue(Crc16Utils.verifyModbus(str, "4494"));
        assertTrue(Crc16Utils.verifyModbus(str, "44 94"));
        assertTrue(Crc16Utils.verifyModbus(str, "44 94 "));
        assertTrue(Crc16Utils.verifyModbus(str, "9444", true));
        assertThrowsExactly(CryptoException.class, () -> Crc16Utils.verifyModbus(null, "4494"), "data is empty");
        assertThrowsExactly(CryptoException.class, () -> Crc16Utils.verifyModbus(str, null), "data is 2empty");
    }

    @Test
    void test16() {
        int length = 100;
        byte[] bytes = BaseConvertUtils.decToHexBytes(length);
        System.out.println(Arrays.toString(bytes));
    }

}
