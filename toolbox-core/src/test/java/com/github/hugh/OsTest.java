package com.github.hugh;

import com.github.hugh.util.system.OsUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 系统验证测试类
 * User: AS
 * Date: 2022/1/20 14:58
 */
class OsTest {

    @Test
    void testSystem() {
        Assertions.assertTrue(OsUtils.isWindows());
        Assertions.assertFalse(OsUtils.isLinux());
//        System.out.println(OsUtils.isWindows());
//        System.out.println(OsUtils.isLinux());
    }
}
