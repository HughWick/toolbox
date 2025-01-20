package com.github.hugh.util.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * 姓名工具类
 *
 * @author AS
 * @date 2020/10/10 17:52
 */
class NamesTest {



    // 姓名脱敏测试
    @Test
    void testEncrypt() {
        assertNull(NamesUtils.encrypt(null));
        assertEquals("荒" , NamesUtils.encrypt("荒"));
        assertEquals("*三", NamesUtils.encrypt("刘三"));
        assertEquals("王*如", NamesUtils.encrypt("王月如"));
        assertEquals("欧**华", NamesUtils.encrypt("欧阳震华"));
        assertEquals("麦麦提·***", NamesUtils.encrypt("麦麦提·赛帕克"));
    }

    @Test
    void test02() {
        assertNull(NamesUtils.desensitized(null));
        assertEquals("刘*", NamesUtils.desensitized("刘三"));
        assertEquals("王**", NamesUtils.desensitized("王月如"));
        assertEquals("欧阳**", NamesUtils.desensitized("欧阳震华"));
        assertEquals("麦******", NamesUtils.desensitized("麦麦提·赛帕克"));
    }

}
