package com.github.hugh.components.datetime;

import com.github.hugh.constant.DateCode;
import com.github.hugh.util.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

/**
 * @author hugh
 */

class DatesTest {

    @Test
    void testToDate() {
        String str1 = "1713152121000";
        Date date1 = Dates.on(str1).parse().toDate();
        assertEquals(str1, date1.getTime() + "");
        String str2 = "2024-04-15 11:39:00";
        Date date2 = Dates.on(str2).parse().toDate();
        assertEquals(DateUtils.parse(str2).getTime(), date2.getTime());
//        String str3 = "NaN-NaN-NaN NaN:NaN:NaN";
//        Date date3 = Dates.on(str3).setFormat("yyyy-MM-dd HH:mm:00").toDate();
//        String temp = "1779832902356332544";
//        System.out.println("====>>"+temp.length());
    }

    @Test
    void testTimestamp() {
        String str1 = "1713152121000";
        assertEquals(13, str1.length());
        String str2 = "1713152121";
        assertEquals(10, str2.length());
    }


    @Test
    void testSetFormat() {
        // 毫秒级时间戳
        String str1 = "1713747819635";
        assertEquals("2024-04-22 09:03:39", Dates.on(str1).parse().format());
        String str2 = "2024-04-15";
        String format = Dates.on(str2).setFormat(DateCode.YEAR_MONTH_DAY).parse().format();
        assertEquals("2024-04-15 00:00:00", format);
        // 秒级时间戳
        String str3 = "1713152121";
        assertEquals("2024-04-15 11:35:21", Dates.on(str3).parse().format());
    }

    @Test
    void testException() {
        String str1 = "NaN-NaN-NaN NaN:NaN:NaN";
        NumberFormatException numberFormatException1 = assertThrowsExactly(NumberFormatException.class, () -> {
            Dates.on(str1).setFormat("yyyy-MM-dd HH:mm:00").parse();
        });
        assertEquals("For input string: \"NaN-NaN-NaN NaN:NaN:NaN\"", numberFormatException1.getMessage());
    }
}
