package com.github.hugh.secret;

import com.github.hugh.util.secrect.AppkeyUtils;
import org.junit.jupiter.api.Test;

/**
 * @author AS
 * @date 2020/8/31 15:18
 */
public class AppkeyTest {

    @Test
    public void test01() {
        System.out.println("-1-->>" + AppkeyUtils.generate());
        System.out.println("-2-->>" + AppkeyUtils.generateSecret());
    }
}
