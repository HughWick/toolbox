package com.github.hugh;

import com.github.hugh.util.CoordinatesUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

/**
 * User: AS
 * Date: 2021/4/20 16:55
 */
public class CoordinatesTest {

    @Test
    public void test01() {
        double[] doubles = CoordinatesUtils.gcj02ToBd09(112.94671143, 28.21967801);
        double[] doubles2 = CoordinatesUtils.gcj02ToBd09(0, 0);
        System.out.println(ArrayUtils.toString(doubles, ","));
        System.out.println("-1-->>" + doubles[0]);
        System.out.println("-2-->>" + doubles[1]);
    }
}
