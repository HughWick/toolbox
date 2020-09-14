package com.github.hugh;

import com.github.hugh.util.regex.RegexUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/9/11 16:42
 */
public class IpTest {

    @Test
    public void test001(){
        String str = "113.218.2.1223";
        System.out.println("--1->>"+ RegexUtils.isIp(str));
        System.out.println("--2->>"+ RegexUtils.isNotIp(str));
    }
}
