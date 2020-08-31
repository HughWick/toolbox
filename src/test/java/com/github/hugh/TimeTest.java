package com.github.hugh;

import com.github.hugh.util.TimeUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/8/31 15:22
 */
public class TimeTest {

    @Test
    public void test01() {
        long start = System.currentTimeMillis();
        System.out.println("-1-->>" + TimeUtils.checkTimestamp(start + "", 1800000));
        System.out.println("-2-->>" + TimeUtils.checkTimestamp(start + "1528858736043", 1800000));
//        System.out.println("-3-->>" + TimeUtils.checkTimestamp(start + "1528858736043", 1800000));
        System.out.println("-4-->>" + TimeUtils.checkTimestamp( "1528858736043", 1800000));
        System.out.println("--->>"+ TimeUtils.getTime());
        System.out.println("--->>"+ TimeUtils.endOfLastMonth());
    }
}
