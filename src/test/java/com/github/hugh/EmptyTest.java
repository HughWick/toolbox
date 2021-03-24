package com.github.hugh;

import com.github.hugh.util.EmptyUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmptyTest {

    @Test
    public void isEmpty() {
        String[] arr = {};
        List<String> list = new ArrayList<>();
        System.out.println("-1-->" + EmptyUtils.isEmpty("  "));
        System.out.println("-2-->" + EmptyUtils.isEmpty("null"));
        System.out.println("-3->" + EmptyUtils.isEmpty(new ArrayList<>()));
        System.out.println("-4->" + EmptyUtils.isEmpty(new HashMap<>()));
        System.out.println("-6-->" + EmptyUtils.isEmpty(12));
        System.out.println("-7-->" + EmptyUtils.isEmpty("[]"));
        System.out.println("-8-->" + EmptyUtils.isEmpty(arr));
        System.out.println("-9-->" + EmptyUtils.isEmpty(list));
    }

    @Test
    public void isNotEmpty() {
        System.out.println("---1>" + EmptyUtils.isNotEmpty("b"));
        System.out.println("2-->" + EmptyUtils.isNotEmpty(""));
        System.out.println("--3->" + EmptyUtils.isNotEmpty("[]"));
//        System.out.println("--2->" + EmptyUtils.isNotEmpty("[]"));
    }


    public static void main(String[] args) {
        String[] strArr = {"1"};
        System.out.println("---->>"+strArr.length);
        System.out.println(EmptyUtils.isEmpty(strArr));
        System.out.println(EmptyUtils.isNotEmpty(strArr));
    }
}
