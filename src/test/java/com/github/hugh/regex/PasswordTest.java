package com.github.hugh.regex;

import com.github.hugh.util.regex.PasswordRegex;
import org.junit.Test;

/**
 * @author AS
 * @date 2021/2/10 20:33
 */
public class PasswordTest {

    @Test
    public void test01(){
        String str = "11232！1";
        String str2 = "a112321";
        System.out.println("-1-->>"+ PasswordRegex.moderate(str));
        System.out.println("--2->>"+ PasswordRegex.moderate(str2));
    }
}
