package com.github.hugh;

import com.github.hugh.util.regex.CronRegex;
import org.junit.jupiter.api.Test;

/**
 * @author AS
 * @date 2020/9/14 10:22
 */
public class CronTest {

    @Test
    public void test01(){
        System.out.println("===1=>>" + CronRegex.isCron("10 ** * * ? "));
        System.out.println("===2=>>" + CronRegex.isCron("20 * * * * ? "));
        System.out.println("==3==>>" + CronRegex.isTooShort("0/29 * * * * ? "));
        System.out.println("===4=>>" + CronRegex.isTooShort("1-30 * * * * ? "));
        System.out.println("===5=>>" + CronRegex.isTooShort("0 1 * * * ? "));
    }
}
