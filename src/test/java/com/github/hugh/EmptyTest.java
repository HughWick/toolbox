package com.github.hugh;

import com.github.hugh.util.EmptyUtils;
import org.junit.Test;

public class EmptyTest {

    @Test
    public void isEmpty(){
        System.out.println("--->"+ EmptyUtils.isEmpty(""));
    }

    @Test
    public void isNotEmpty(){
        System.out.println("--->"+ EmptyUtils.isNotEmpty(""));
    }


}
