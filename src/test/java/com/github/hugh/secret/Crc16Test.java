package com.github.hugh.secret;

import com.github.hugh.util.secrect.Crc16Utils;
import org.junit.Test;

/**
 * @author hugh
 * @version 1.0.0
 */
public class Crc16Test {

    @Test
    public void test01() {
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
}
