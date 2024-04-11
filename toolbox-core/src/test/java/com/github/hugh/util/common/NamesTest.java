package com.github.hugh.util.common;

import com.github.hugh.util.common.NamesUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals("*三", NamesUtils.encrypt("刘三"));
        assertEquals("王*如", NamesUtils.encrypt("王月如"));
        assertEquals("欧**华", NamesUtils.encrypt("欧阳震华"));
        assertEquals("麦麦提·***", NamesUtils.encrypt("麦麦提·赛帕克"));
//        System.out.println("--1->>" + NamesUtils.encrypt("刘三"));
//        System.out.println("--2->>" + NamesUtils.encrypt("王月如"));
//        System.out.println("--3->>" + NamesUtils.encrypt("欧阳震华"));
//        System.out.println("--4->>" + NamesUtils.encrypt("麦麦提·赛帕克"));
    }

    @Test
    void test02() {
        assertEquals("刘*", NamesUtils.desensitized("刘三"));
        assertEquals("王**", NamesUtils.desensitized("王月如"));
        assertEquals("欧阳**", NamesUtils.desensitized("欧阳震华"));
        assertEquals("麦******", NamesUtils.desensitized("麦麦提·赛帕克"));
//        System.out.println("---1->>>" +);
//        System.out.println("---2->>>" + NamesUtils.desensitized("王月如"));
//        System.out.println("---3->>>" + NamesUtils.desensitized("欧阳震华"));
//        System.out.println("---4->>>" + NamesUtils.desensitized("麦麦提·赛帕克"));
    }

}
