package com.github.hugh;

import com.github.hugh.util.DoubleMathUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author AS
 * @date 2020/8/31 15:34
 */
public class DoubleMathTest {

    @Test
    void test01() {
        double add = DoubleMathUtils.add(1, 2);
        assertEquals(3, add);
        double sub = DoubleMathUtils.sub(5, 4);
        assertEquals(1, sub);
        System.out.println("---2>>" + DoubleMathUtils.mul(1, 2));
        System.out.println("--3->>" + DoubleMathUtils.div(1, 2));
        System.out.println("--4->>" + DoubleMathUtils.div(1, 3, 5));
        System.out.println("--5->>" + DoubleMathUtils.round(1.000, 2));
        System.out.println("--6->>" + DoubleMathUtils.getString(1.00011));
        System.out.println("--7->>" + DoubleMathUtils.numberFormat.format(1.1234566123));
        System.out.println("--8->>" + DoubleMathUtils.numberGiveUp.format(1.1234566123));
    }

    @Test
    public void test02() {
        var i1 = -1;
        int i2 = -2;
        double pow = Math.pow(10, -i1);
        System.out.println("10的负一次方等于：" + pow);
        System.out.println("10的负二次方等于：" + Math.pow(10, -i2));
        System.out.println("10的负三次方等于：" + Math.pow(10, -(-3)));
        System.out.println("-1等于：" + (-1));
        System.out.println("负的括号内-1等于：" + -(-1));
    }
}
