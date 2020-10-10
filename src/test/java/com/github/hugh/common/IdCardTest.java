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
        String idcard15 = "420923198611046";
//        String idcard18 = "110101199003071751";//
        String idcard18 = "430104199003076214";//
        String hkIdCard = "H0905613601";//
        // 15位身份证
        System.out.println(IdCardUtils.isIdCard(idcard15));
        // 18位身份证
        System.out.println(IdCardUtils.is18Place(idcard18));
        // 15位身份证转18位身份证
        System.out.println(IdCardUtils.encrypt(idcard18));
    }
}
