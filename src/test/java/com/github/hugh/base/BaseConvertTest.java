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
        System.out.println("=1==>>>>" + BaseConvertUtils.decToHex("140"));
        String binary = BaseConvertUtils.decToBinary(84213840);
        System.out.println("==2=>>>>" + binary);
        System.out.println("==3=>>>>" + BaseConvertUtils.decToBinary(10, 12));
        System.out.println("--4-===>>" + BaseConvertUtils.binaryToDec(binary));
        System.out.println("--4-===>>" + BaseConvertUtils.binaryToDec(binary));
    }

    @Test
    public void test02() {
        String str = "61:62";
        System.out.println("--1->>" + BaseConvertUtils.hexToString(str, ":"));
//        System.out.println("--2->>" + BaseConvertUtils.hexToString(null, ":"));
    }

    @Test
    public void test03() {
        String str = "043F";
        System.out.println("-1-->>" + BaseConvertUtils.hexToBinary(str, 15));
        System.out.println("-2-->>" + BaseConvertUtils.hexToBinary(str));
        System.out.println("==3=>>>>" + BaseConvertUtils.decToBinary(10, 12));
        System.out.println("==4=>>>>" + BaseConvertUtils.decToBinary("10", 12));
        System.out.println("==5=>>>>" + BaseConvertUtils.decToBinary("10"));
        System.out.println("==6=>>>>" + BaseConvertUtils.hexToDecString("8C"));
    }

    @Test
    public void testTex() {
        String str = "043F";
        String str2 = "4A3F7F40";
        String str3 = "7869616f7a68693130323031";
        byte[] bytes = BaseConvertUtils.hexToBytes(str);
        System.out.println(new String(bytes));
        byte bytes2 = BaseConvertUtils.hexToByte(str2);
        System.out.println(String.valueOf(bytes2));
        String s = BaseConvertUtils.hexToAscii(str2);
        System.out.println("---1->>>" + s);
        String s1 = BaseConvertUtils.asciiToHex(s);
        System.out.println("==2=>>" + s1);
        String s2 = BaseConvertUtils.asciiToHex(s, "_");
        System.out.println("==3=>>" + s2);
        String s3 = BaseConvertUtils.asciiToHex(s, " ");
        System.out.println("==3=>>" + s3);
        System.out.println(BaseConvertUtils.bytesToHexString(bytes));
        System.out.println("=4==>>" + BaseConvertUtils.hexToAscii(str3));
    }

    @Test
    public void testTex02() {
        String tex = "7a686f75353937383831";
        System.out.println(BaseConvertUtils.hexToAscii(tex));
        String zhou597881 = BaseConvertUtils.asciiToHex("zhou597881");
        System.out.println("----->>" + zhou597881);
        System.out.println("----->>" + BaseConvertUtils.hexToAscii(zhou597881.replace(" ", "")));
        String s = BaseConvertUtils.asciiToHex("4" , " ");
        System.out.println("===1====>"+s);


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
