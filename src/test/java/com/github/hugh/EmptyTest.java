package com.github.hugh;

import com.github.hugh.util.EmptyUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class EmptyTest {

    @Test
    public void isEmpty(){
//        EmptyUtils u = new EmptyUtils();
        System.out.println("-1-->"+ EmptyUtils.isEmpty(""));
        System.out.println("-2-->"+ EmptyUtils.isEmpty("null"));
        System.out.println("-3->"+ EmptyUtils.isEmpty(new ArrayList<>()));
        System.out.println("-4->"+ EmptyUtils.isEmpty(new HashMap<>()));
        System.out.println("-6-->"+ EmptyUtils.isEmpty(12));
    }

    @Test
    public void isNotEmpty(){
        System.out.println("--->"+ EmptyUtils.isNotEmpty(""));
    }


}
