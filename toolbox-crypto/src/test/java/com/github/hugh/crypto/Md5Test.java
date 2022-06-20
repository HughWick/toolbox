package com.github.hugh.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author hugh
 * @version 1.0.0
 */
public class Md5Test {

    @Test
    void testStringMd5() {
        System.out.println("--1->" + Md5Utils.lowerCase("1234"));
        System.out.println("-2-->" + Md5Utils.lowerCase("88888888"));
        System.out.println("-3-->" + Md5Utils.lowerCase("8566889"));
        //f854b68c6f8b2195704f76e05aaa65a1
        System.out.println("-4-->" + Md5Utils.lowerCase("huahua"));
        System.out.println("-5-->" + Md5Utils.upperCase("1234"));
        System.out.println("-5-->" + Md5Utils.upperCase("1234").length());
    }

    @Test
    void testFile() {
        File file = new File("D:\\public\\newFile.bin");
        Assertions.assertEquals(Md5Utils.encryptFile(file), "4a61626dce2f490c1462acd8afa5e052");
    }
}
