package com.github.hugh.common;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.common.IdCardUtils;
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
        ToolboxException exception = assertThrows(ToolboxException.class, () -> IdCardUtils.encrypt("idcard18", 7, 1));
        assertEquals("id card number length error", exception.getMessage());
    }
}
