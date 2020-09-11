package com.github.hugh;

import com.github.hugh.util.StringUtils;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/9/11 16:05
 */
public class StringTest {

    @Test
    public void test01(){
        String str = "https://github.com/HughWick/toolbox";
        System.out.println("--->>>"+ StringUtils.before(str,"/"));
    }
}
