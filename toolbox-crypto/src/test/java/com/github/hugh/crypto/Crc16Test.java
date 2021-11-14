package com.github.hugh.crypto;

import org.junit.jupiter.api.Test;

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

}