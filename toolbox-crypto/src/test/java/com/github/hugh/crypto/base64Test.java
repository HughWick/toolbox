package com.github.hugh.crypto;


import com.github.hugh.util.base.Base64;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;


/**
 * @author AS
 * @date 2020/8/31 15:43
 */
public class base64Test {

    @Test
    void test01() throws UnsupportedEncodingException {
        String str = "中";
        System.out.println("--->>"+str.getBytes("gb2312").length);
//        String s1 = Base64.encode(str);
//        System.out.println("-1-->>" + s1);
//        System.out.println("--2->>" + Base64.encode(""));
//        System.out.println("--3->>" + Base64.decode(""));
//        String decode = Base64.decode(s1);
//        System.out.println("--->>" + decode);
    }


    @Test
    public void test02() {
        String str = "123zvb@!";
        String s1 = Base64.encode(str);
        System.out.println("-1-->>" + s1);
        System.out.println("-1-->>" + Base64.encodeToString(str.getBytes()));
        String decode = Base64.decode(s1);
        System.out.println("--2->>" + decode);
        System.out.println("--2->>" + Base64.decodeToString(s1.getBytes()));
//        System.out.println("--2->>" + Base64.decode(str.getBytes()));
    }


}
