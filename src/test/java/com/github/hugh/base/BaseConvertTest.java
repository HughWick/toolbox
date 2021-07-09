package com.github.hugh.base;

import com.github.hugh.constant.enums.ControlEnum;
import com.github.hugh.util.base.BaseConvertUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/9/14 14:58
 */
public class BaseConvertTest {

    @Test
    public void test01() {
        System.out.println("=1==>>>>" + BaseConvertUtils.decToHex(84213840));
        String binary = BaseConvertUtils.decToBinary(84213840);
        System.out.println("==2=>>>>" + binary);
        System.out.println("==3=>>>>" + BaseConvertUtils.toBinary(10, 12));
        System.out.println("--4-===>>" + BaseConvertUtils.binaryToDec(binary));
        System.out.println("--4-===>>" + BaseConvertUtils.binaryToDec(binary));
    }

    @Test
    public void test02() {
        String str = "61:62";
        System.out.println("--->>" + BaseConvertUtils.hexToString(str, ":"));
    }

    @Test
    public void test03() {
        String str = "043F";
        System.out.println("-1-->>" + BaseConvertUtils.hexToBinary(str, 15));
        System.out.println("-2-->>" + BaseConvertUtils.hexToBinary(str));
        System.out.println("==3=>>>>" + BaseConvertUtils.toBinary(10, 12));
        System.out.println("==4=>>>>" + BaseConvertUtils.toBinary("10", 12));
        System.out.println("==5=>>>>" + BaseConvertUtils.toBinary("10"));
        System.out.println("==6=>>>>" + BaseConvertUtils.hexToDecString("8C"));
    }

    @Test
    public void testTex() {
        String str = "043F";
        String str2 = "4A3F7F40";
        byte[] bytes = BaseConvertUtils.hexToBytes(str);
        System.out.println(new String(bytes));
        byte bytes2 = BaseConvertUtils.hexToByte(str2);
        System.out.println(new String(String.valueOf(bytes2)));
        String s = BaseConvertUtils.hexToAscii(str2);
        System.out.println("---->>>" + s);
        String s1 = BaseConvertUtils.asciiToHex(s);
        System.out.println("===>>"+s1);
        System.out.println(BaseConvertUtils.bytesToHexString(bytes));
    }


    public static void main(String[] args) {
        System.out.println("---1>>" + ControlEnum.ENABLE.getCode());
        System.out.println("--2->>" + ControlEnum.ENABLE.getDesc());
        System.out.println("--3->>" + ControlEnum.DISABLE.getDesc());
        System.out.println("--4->>" + ControlEnum.DISABLE.getDesc());
        System.out.println(ControlEnum.ENABLE);
        System.out.println(ControlEnum.DISABLE);
    }
}
