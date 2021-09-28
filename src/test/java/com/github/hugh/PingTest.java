package com.github.hugh;

import com.github.hugh.util.PingUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/10/14 9:42
 */
public class PingTest {

    @Test
    public void test() {
        System.out.println("-1-->" + PingUtils.send("192.168.1.45", 3, 1000));
        System.out.println("-2-->" + PingUtils.batch("192.168.1.45", 4, 1000));
    }

    @Test
    public void test02() {
        System.out.println("--->>" + PingUtils.ping("192.168.1.45"));
    }

    @Test
    public void test03() {
        PingUtils.ping("");
        System.out.println("---1>>" + PingUtils.ping("192.168.1.25"));
        System.out.println("-2-->>" + PingUtils.ping("192.168.1.236", 9, 1000));
    }
}
