package com.github.hugh;

import com.github.hugh.util.DoubleMathUtils;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.NumberFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * double 浮点小鼠测试
 *
 * @author AS
 * @date 2020/8/31 15:34
 */
class DoubleMathTest {

    //测试加法
    @Test
    void testAdd() {
        double add = DoubleMathUtils.add(1, 2);
        assertEquals(3, add);
        assertEquals(1.0, DoubleMathUtils.round(1.000, 2));
        double d2 = 1.1234566123;
        assertEquals("1.12", DoubleMathUtils.numberFormat.format(d2));
        assertEquals("1.12345", DoubleMathUtils.numberGiveUp.format(d2));
    }

    // 测试减法
    @Test
    void testSub() {
        double sub = DoubleMathUtils.sub(5, 4);
        assertEquals(1, sub);
    }

    // 测试乘法
    @Test
    void testMul() {
        double mul = DoubleMathUtils.mul(1, 2);
        assertEquals(2, mul);
    }

    // 测试除法
    @Test
    void testDiv() {
        double div = DoubleMathUtils.div(1, 2);
        assertEquals(0.5, div);
        double div2 = DoubleMathUtils.div(1, 3, 5);
        assertEquals(0.33333, div2);
    }

    // 测试获取字符串
    @Test
    void testGetString() {
        double d1 = 1.000011;
        assertEquals("1", DoubleMathUtils.getString(d1));
        assertEquals("2.01", DoubleMathUtils.getString(2.01145684));
    }

    @Test
    void testDivRound() {
        double v = DoubleMathUtils.divCeil(5, 2);
        assertEquals(3, v);
        double v1 = DoubleMathUtils.divFloor(5, 3.2);
        assertEquals(1, v1);
        // 结果为165.5 ，四舍五入后166
//        System.out.println(v2);
        assertEquals(1, v1);
        double v2 = DoubleMathUtils.divRound(331, 2);
        assertEquals(166.0, v2);
    }

    // 测试次方
    @Test
    void testPow() {
        var i1 = -1;
        int i2 = -2;
        double pow = Math.pow(10, -i1);
        //10的负一次方等于
        assertEquals(10.0, pow);
        assertEquals(100.0, Math.pow(10, -i2));
        //10的负三次方等于
        assertEquals(1000.0, Math.pow(10, -(-3)));
        //负的括号内-1等于
        assertEquals(1, -(-1));
//        System.out.println("10的负一次方等于：" + pow);
//        System.out.println("10的负二次方等于：" + Math.pow(10, -i2));
//        System.out.println("10的负三次方等于：" + Math.pow(10, -(-3)));
//        System.out.println("-1等于：" + (-1));
//        System.out.println("负的括号内-1等于：" + -(-1));
    }

    @Test
    void test010() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(8);
        numberFormat.setGroupingUsed(false);
        numberFormat.setRoundingMode(RoundingMode.DOWN);
        String longitude = "112.94670227122846";
        System.out.println("---->>" + numberFormat.format(Double.parseDouble(longitude)));

    }
}
