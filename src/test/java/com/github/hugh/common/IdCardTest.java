package com.github.hugh.common;

import com.github.hugh.util.common.IdCardUtils;
import org.junit.Test;

/**
 * @author hugh
 * @version 1.0.0
 */
public class IdCardTest {

    @Test
    public void test01(){
//        String idcard15 = "632123820927051";
        String idcard15 = "631111110927332";
        String idcard18 = "430104199003076214";//
        String hkIdCard = "H0905613601";//
        // 15位身份证
        System.out.println(IdCardUtils.isIdCard(idcard15));
        System.out.println(IdCardUtils.isNotIdCard(idcard15));
        // 18位身份证
        System.out.println(IdCardUtils.is18Place(idcard18));
        System.out.println(IdCardUtils.isNot18Place(idcard18));
        // 15位身份证转18位身份证
        System.out.println(IdCardUtils.encrypt(idcard18));

        System.out.println("---->>"+IdCardUtils.encrypt(idcard18,6,4));
        System.out.println("---->>"+IdCardUtils.encrypt("idcard18",7,1));
    }
}
