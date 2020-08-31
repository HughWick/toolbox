package com.github.hugh;

import com.github.hugh.util.DateUtils;
import com.github.hugh.util.TimeUtils;
import org.junit.Test;

public class DateTest {

    public static void main(String[] args) {
        System.out.println("-----当前日期---->>" + TimeUtils.getTime());
        System.out.println("==昨日开始日期==>>" + TimeUtils.getStartedYesterday());
        System.out.println("--月的第1天--->>" + TimeUtils.firstDayOfMonth());
        System.out.println("----最后1天----->>" + TimeUtils.lastDayOfMonth());
        System.out.println("---上个月的第1天------>>" + TimeUtils.earlyLastMonth());
        System.out.println("---上个月的最后1天------>>" + TimeUtils.endOfLastMonth());
        System.out.println("--在当前系统时间之后-->>" + TimeUtils.ex("2020-04-17 13:59:59"));
    }

    @Test
    public void test01() {
        String str = "2020-06-04 13:00:21";
        System.out.println("--->" + DateUtils.isDateFormat(str));
        System.out.println("--->" + DateUtils.parseDate(str));
    }
}
