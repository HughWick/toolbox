package com.github.hugh.time;

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
        System.out.println("-4-->>" + TimeUtils.checkTimestamp("1528858736043", 1800000));
        System.out.println("--->>" + TimeUtils.getTime());
        System.out.println("--->>" + TimeUtils.endOfLastMonth());
    }

    @Test
    public void test02() {
        String start = "2020-06-08 00:00:00";
        String end = "2020-06-10 00:00:00";
        System.out.println("--->>" + TimeUtils.isCrossDay(start, end));
        System.out.println("--->>" + TimeUtils.isCrossDay(start, end,2));
    }

    @Test
    public void test03(){
        String timeStart = "2019-12-11";
        String timeEnd = "2020-12-20";
        TimeUtils.collectLocalDates(timeStart, timeEnd).forEach(System.out::println);
    }

}
