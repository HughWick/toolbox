package com.github.hugh.util.common;

import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 身份证验证工具类
 *
 * @author hugh
 * @version 1.0.0
 */
class IdCardTest {

    @Test
    void testEmpty() {
        String emptyStr = "";
        assertTrue(IdCardUtils.isNotIdCard(emptyStr));
        assertEquals(emptyStr, IdCardUtils.encrypt(emptyStr));
    }

    @Test
    void testIdCard() {
//        String idcard15 = "632123820927051";
        String idcard15 = "631111110927332";
        String idcard18 = "430104199003076214";//
        String hkIdCard = "H0905613601";//
        // 15位身份证
        assertTrue(IdCardUtils.isIdCard(idcard15));
        assertFalse(IdCardUtils.isNotIdCard(idcard15));
        // 18位身份证
        assertTrue(IdCardUtils.is18Place(idcard18));
        assertFalse(IdCardUtils.isNot18Place(idcard18));
        // 15位身份证转18位身份证
        assertEquals("430***********6214", IdCardUtils.encrypt(idcard18));
        assertEquals("430104********6214", IdCardUtils.encrypt(idcard18, 6, 4));
        ToolboxException exception1 =  assertThrows(ToolboxException.class, () -> IdCardUtils.encrypt("430104********6214", -1, 4));
        assertEquals("number before error", exception1.getMessage());
        ToolboxException exception2 =  assertThrows(ToolboxException.class, () -> IdCardUtils.encrypt("430104********6214", 4, -1));
        assertEquals("number rear error", exception2.getMessage());
        ToolboxException exception = assertThrows(ToolboxException.class, () -> IdCardUtils.encrypt("idcard18", 7, 1));
        assertEquals("id card number length error", exception.getMessage());
    }

    @Test
    void test02() {
        String idCard1 = "430426199912309526";//
        String idCard2 = "43042619991230952X";//
        assertTrue(IdCardUtils.isIdCard(idCard1));
        assertTrue(IdCardUtils.isNotIdCard(idCard2));
    }
}
