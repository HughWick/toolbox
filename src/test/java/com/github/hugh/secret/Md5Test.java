package com.github.hugh.secret;

import com.github.hugh.util.secrect.Md5Utils;
import org.junit.jupiter.api.Test;

/**
 * @author hugh
 * @version 1.0.0
 */
public class Md5Test {

    @Test
    public void md5(){
        System.out.println("--1->"+ Md5Utils.lowerCase("1234"));
        System.out.println("-2-->"+ Md5Utils.lowerCase("88888888"));
        System.out.println("-2-->"+ Md5Utils.lowerCase("8566889"));
        //f854b68c6f8b2195704f76e05aaa65a1
        System.out.println("-2-->"+ Md5Utils.lowerCase("huahua"));
        System.out.println("-3-->"+ Md5Utils.upperCase("1234"));
    }

}
