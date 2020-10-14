package com.github.hugh;

import com.github.hugh.util.PingUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/10/14 9:42
 */
public class PingTest {

    @Test
    public void test(){
        System.out.println("-1-->"+ PingUtils.send("192.168.1.45", 3, 1000));
        System.out.println("-2-->"+ PingUtils.batch("192.168.1.45", 3, 1000));
    }
}
