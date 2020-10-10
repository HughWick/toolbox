package com.github.hugh.common;

import com.github.hugh.util.common.PhoneUtils;
import org.junit.Test;

/**
 * @author hugh
 * @version 1.0.0
 */
public class PhoneTest {

    @Test
    public void test01(){
        System.out.println("---1>>>"+ PhoneUtils.encrypt("13825004872"));
    }
}
