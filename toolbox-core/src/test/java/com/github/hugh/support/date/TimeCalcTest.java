package com.github.hugh.support.date;

import com.github.hugh.constant.DateCode;
import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 日期计算工具类
 *
 * @author hugh
 */
class TimeCalcTest {

    @Test
    void testFormat() {
        String start1 = "2023-07-18";
        String end1 = "2023-07-17";
        final String message = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TimeCalc.on(start1, end1, DateCode.YEAR_MONTH_DAY).toHours();
        }).getMessage();
        assertEquals("结束日期不能小于开始日期", message);
        String start2 = "2023-07-11";
        String end2 = "2023-07-17";
        final String s1 = TimeCalc.on(start2, end2, DateCode.YEAR_MONTH_DAY).setEnablePrecision(false).toHours();
        assertEquals("144小时", s1);
        final String s2 = TimeCalc.on(start2, end2, DateCode.YEAR_MONTH_DAY)
                .toHours();
        assertEquals("144.0小时", s2);
    }

    @Test
    void testToMinutes() {
        String start1 = "2023-07-18 10:00:00";
        String end1 = "2023-07-18 10:50:00";
        final TimeCalc on = TimeCalc.on(start1, end1);
        final String s1 = on.toMinutes();
        assertEquals("50分", s1);
        assertEquals("50", on.setChineseFormat(false).toMinutes());
    }

    @Test
    void testToHours() {
        String start1 = "2023-07-18 10:00:00";
        String end1 = "2023-07-18 11:50:00";
        final TimeCalc on1 = TimeCalc.on(start1, end1);
//        final String s1 = on.toHours();
        assertEquals("1.83小时", on1.toHours());
        on1.setChineseFormat(false);
        assertEquals("1.83", on1.toHours());

        final TimeCalc on2 = TimeCalc.on(start1, end1);
        on2.setEnablePrecision(false);
        assertEquals("1小时", on2.toHours());
        on2.setChineseFormat(false);
        assertEquals("1", on2.toHours());

        String start2 = "2023-07-18 09:00:00";
        String end2 = "2023-07-18 11:43:00";
        final TimeCalc on3 = TimeCalc.on(start2, end2);
        assertEquals("2.72小时", on3.toHours());
        on3.setEnablePrecision(false);
        assertEquals("2小时", on3.toHours());
    }

    @Test
    void testToDays() {
        String start1 = "2023-07-17 08:00:00";
        String end1 = "2023-07-18 11:50:00";
        final TimeCalc on = TimeCalc.on(start1, end1);
        // 测试精准
        assertEquals("1.16天", on.toDays());
        on.setChineseFormat(false);
        assertEquals("1.16", on.toDays());
        // 测试整数
        on.setEnablePrecision(false);
        on.setChineseFormat(true);
        assertEquals("1天", on.toDays());
        on.setChineseFormat(false);
        assertEquals("1", on.toDays());
    }


    @Test
    void testToCompleteness() {
        String start1 = "2023-07-17 08:00:00";
        String end1 = "2023-07-18 11:50:10";
        final TimeCalc on1 = TimeCalc.on(start1, end1);
        assertEquals("1天3小时50分10秒", on1.toCompleteness());
        String start2 = "2023-07-18 08:00:00";
        String end2 = "2023-07-18 11:50:10";
        final TimeCalc on2 = TimeCalc.on(start2, end2);
        assertEquals("3小时50分10秒", on2.toCompleteness());
        String start3 = "2023-07-18 11:00:00";
        String end3 = "2023-07-18 11:21:10";
        final TimeCalc on3 = TimeCalc.on(start3, end3);
        assertEquals("21分10秒", on3.toCompleteness());
        String start4 = "2023-07-18 11:00:00";
        String end4 = "2023-07-18 11:00:18";
        final TimeCalc on4 = TimeCalc.on(start4, end4);
        assertEquals("18秒", on4.toCompleteness());

        String start5 = "2023-03-12 08:00:00";
        String end5 = "2023-07-18 02:50:10";
        final TimeCalc on5 = TimeCalc.on(start5, end5);
        assertEquals("127天18小时50分", on5.toCompleteness(false));
    }


    @Test
    void testSecondDiff() {
        String start1 = "2023-07-18 11:40:00";
        String end1 = "2023-07-18 11:50:10";
        final TimeCalc on1 = TimeCalc.on(start1, end1);
        assertEquals(610, on1.secondsDiff());
    }

    @Test
    void testMinutesDiff() {
        String start1 = "2023-07-18 11:05:00";
        String end1 = "2023-07-18 11:50:10";
        final TimeCalc on1 = TimeCalc.on(start1, end1);
        assertEquals(45, on1.minutesDiff());

        final String message = Assertions.assertThrows(ToolboxException.class, () -> {
            TimeCalc.on(null, end1, DateCode.YEAR_MONTH_DAY).minutesDiff();
        }).getMessage();
        assertEquals(" start date is null ", message);
        final String message2 = Assertions.assertThrows(ToolboxException.class, () -> {
            TimeCalc.on(start1, null, DateCode.YEAR_MONTH_DAY).minutesDiff();
        }).getMessage();
        assertEquals(" end date is null ", message2);
    }

}
