package com.github.hugh;

import com.github.hugh.util.regex.RegexUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/9/11 15:42
 */
public class RegexTest {

    @Test
    public void test01(){
        String str = "escapeWord[]";
        String str2 = "<>";
        String str3 = "1select>1";
        System.out.println("-1-->>"+ RegexUtils.escapeWord(str));
        System.out.println("-2-->>"+ RegexUtils.isPunctuation(str2));
        System.out.println("-3-->>"+ RegexUtils.isSql(str3));
        System.out.println("-3-->>"+ RegexUtils.isNotIp(str3));

    }

}
