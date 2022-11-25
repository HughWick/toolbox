package com.github.hugh;

import com.github.hugh.util.system.OsUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 系统验证测试类
 * User: AS
 * Date: 2022/1/20 14:58
 */
class OsTest {

    @Test
    void testSystem() {
        assertTrue(OsUtils.isWindows());
        assertFalse(OsUtils.isLinux());
        assertTrue(OsUtils.is64());
        assertFalse(OsUtils.isAarch64());
    }
}
