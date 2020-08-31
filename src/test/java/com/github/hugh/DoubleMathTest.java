package com.github.hugh;

import com.github.hugh.util.DoubleMathUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/8/31 15:34
 */
public class DoubleMathTest {

    @Test
    public void test01() {
        System.out.println("--->>" + DoubleMathUtils.add(1, 2));
        System.out.println("--->>" + DoubleMathUtils.mul(1, 2));
        System.out.println("--->>" + DoubleMathUtils.div(1, 2));
        System.out.println("--->>" + DoubleMathUtils.div(1, 3, 5));
        System.out.println("--->>" + DoubleMathUtils.round(1.000, 2));
        System.out.println("--->>" + DoubleMathUtils.getString(1.00011));
        System.out.println("--->>" + DoubleMathUtils.numberFormat.format(1.1234566123));
        System.out.println("--->>" + DoubleMathUtils.numberGiveUp.format(1.1234566123));
    }
}
