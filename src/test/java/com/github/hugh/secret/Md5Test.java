package com.github.hugh.secret;

import com.github.hugh.util.secrect.Md5Utils;
import org.junit.Test;

/**
 * @author hugh
 * @version 1.0.0
 */
public class Md5Test {

    @Test
    public void md5(){
        System.out.println("--->"+ Md5Utils.lowerCase("1234"));
        System.out.println("--->"+ Md5Utils.upperCase("1234"));
    }

}
