package com.github.hugh.base;

import com.github.hugh.constant.enums.ControlEnum;
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
        System.out.println("==3=>>>>" + BaseConvertUtils.toBinary(10, 12));
        System.out.println("--4-===>>" + BaseConvertUtils.binaryToTen(binary));
        System.out.println("--4-===>>" + BaseConvertUtils.binaryToTen(binary));
    }

    @Test
    public void test02() {
        String str = "61:62";
        System.out.println("--->>" + BaseConvertUtils.hexToString(str, ":"));
    }

    @Test
    public void test03() {
        String str = "043F";
        System.out.println("-1-->>" + BaseConvertUtils.sixteenToBinary(str, 15));
        System.out.println("-2-->>" + BaseConvertUtils.sixteenToBinary(str));
        System.out.println("==3=>>>>" + BaseConvertUtils.toBinary(10, 12));
    }

    public static void main(String[] args) {
        System.out.println("---1>>" + ControlEnum.ENABLE.getCode());
        System.out.println("--2->>" + ControlEnum.ENABLE.getDesc());
        System.out.println("--3->>" + ControlEnum.DISABLE.getDesc());
        System.out.println("--4->>" + ControlEnum.DISABLE.getDesc());
        System.out.println(ControlEnum.ENABLE);
        System.out.println(ControlEnum.DISABLE);
    }
}
