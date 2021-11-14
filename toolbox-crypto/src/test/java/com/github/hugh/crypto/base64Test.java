package com.github.hugh.crypto;


import com.github.hugh.util.base.Base64;
import org.junit.jupiter.api.Test;


/**
 * @author AS
 * @date 2020/8/31 15:43
 */
public class base64Test {

//    @Test
//    public void test01() {
//        String str = "123";
//        String s1 = Base64.encode(str);
//        System.out.println("-1-->>" + s1);
//        System.out.println("--2->>" + Base64.encode(""));
//        System.out.println("--3->>" + Base64.decode(""));
//        String decode = Base64.decode(s1);
//        System.out.println("--->>" + decode);
//    }


    @Test
    public void test02() {
        String str = "123";
        String s1 = Base64.encode(str);
        System.out.println("-1-->>" + s1);
        String decode = Base64.decode(s1);
        System.out.println("--2->>" + decode);
    }


}
