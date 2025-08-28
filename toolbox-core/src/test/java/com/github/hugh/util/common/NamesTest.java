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
    void testDesensitized() {
        assertNull(NamesUtils.desensitized(null));
        assertEquals("刘*", NamesUtils.desensitized("刘三"));
        assertEquals("王**", NamesUtils.desensitized("王月如"));
        assertEquals("欧阳**", NamesUtils.desensitized("欧阳震华"));
        assertEquals("麦******", NamesUtils.desensitized("麦麦提·赛帕克"));
        // 测试用例 1: 长度3，保留1位 -> 张**
        assertEquals("张**", NamesUtils.desensitized("张三丰"), "姓名'张三丰'脱敏结果应为'张**'");
        // 测试用例 2: 长度4，保留2位 -> 欧阳**
        assertEquals("欧阳**", NamesUtils.desensitized("欧阳娜娜"), "姓名'欧阳娜娜'脱敏结果应为'欧阳**'");
        // 测试用例 3: 长度2，保留1位 -> 王*
        assertEquals("王*", NamesUtils.desensitized("王五"), "姓名'王五'脱敏结果应为'王*'");
        // 测试用例 4: 长度2，保留1位 -> 李*
        assertEquals("李*", NamesUtils.desensitized("李四"), "姓名'李四'脱敏结果应为'李*'");
        // 测试用例 5: 长度1，保留1位 -> 赵
        assertEquals("赵", NamesUtils.desensitized("赵"), "姓名'赵'脱敏结果应为'赵'");
        // 测试用例 6: 空字符串 -> 返回空字符串
        assertEquals("", NamesUtils.desensitized(""), "空字符串脱敏结果应为空字符串");
        // 测试用例 7: null -> 返回null
        assertNull(NamesUtils.desensitized(null), "null值脱敏结果应为null");
        // 测试用例 8: 长度4，保留2位 -> AB**
        assertEquals("AB**", NamesUtils.desensitized("ABCD"), "姓名'ABCD'脱敏结果应为'AB**'");
        // 测试用例 9: 长度5，保留1位 -> A****
        assertEquals("A****", NamesUtils.desensitized("ABCDE"), "姓名'ABCDE'脱敏结果应为'A****'");
    }

    @Test
    void encrypt_NullInput_ReturnsNull() {
        assertNull(NamesUtils.encrypt(null));
    }

    @Test
    void encrypt_Length1_ReturnsSameString() {
        assertEquals("张", NamesUtils.encrypt("张"));
    }

    @Test
    void encrypt_Length2_ReturnsMaskedFirstCharacter() {
        assertEquals("*三", NamesUtils.encrypt("张三"));
    }

    @Test
    void encrypt_Length3_ReturnsMaskedMiddleCharacter() {
        assertEquals("王*如", NamesUtils.encrypt("王月如"));
    }

    @Test
    void encrypt_Length4_ReturnsMaskedMiddleTwoCharacters() {
        assertEquals("欧**华", NamesUtils.encrypt("欧阳震华"));
    }

    @Test
    void encrypt_LengthGreaterThan4WithDot_ReturnsMaskedAfterDot() {
        assertEquals("麦麦提·***", NamesUtils.encrypt("麦麦提·赛帕克"));
    }

}
