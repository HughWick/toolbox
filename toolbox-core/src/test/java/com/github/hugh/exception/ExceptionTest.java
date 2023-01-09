package com.github.hugh.exception;

import com.github.hugh.util.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 异常工具测试类
 * User: AS
 * Date: 2022/12/29 11:00
 */
class ExceptionTest {

    @Test
    void testException() {
        ToolboxException toolboxException = new ToolboxException("s圣诞节卡");
        final String message = ExceptionUtils.getMessage(toolboxException);
        assertEquals("ToolboxException: s圣诞节卡", message);
        try {
            int i = 1 / 0;
        } catch (ArithmeticException arithmeticException) {
            final String rootCauseMessage = ExceptionUtils.getRootCauseMessage(arithmeticException);
            assertEquals("ArithmeticException: / by zero", rootCauseMessage);
        }
//        ExceptionUtil.getRootCauseMessage()
    }
}
