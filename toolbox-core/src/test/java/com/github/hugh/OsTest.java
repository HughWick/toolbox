package com.github.hugh;

import com.github.hugh.util.system.OsUtils;
import org.junit.jupiter.api.Test;

/**
 * User: AS
 * Date: 2022/1/20 14:58
 */
public class OsTest {

    @Test
    void test01() {
        System.out.println(OsUtils.isWindows());
        System.out.println(OsUtils.isLinux());
    }
}
