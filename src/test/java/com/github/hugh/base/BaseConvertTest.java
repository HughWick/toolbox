package com.github.hugh.base;

import com.github.hugh.util.base.BaseConvertUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/9/14 14:58
 */
public class BaseConvertTest {

    @Test
    public void test01() {
        System.out.println("=1==>>>>" + BaseConvertUtils.tenToSixteen(84213840));
        String binary = BaseConvertUtils.tenToBinary(84213840);
        System.out.println("==2=>>>>" + binary);
        System.out.println("==3=>>>>" + BaseConvertUtils.toBinary(1024,12));
        System.out.println("--4-===>>" + BaseConvertUtils.binaryToTen(binary));
        System.out.println("--4-===>>" + BaseConvertUtils.binaryToTen(binary));
    }
}
