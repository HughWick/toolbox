package com.github.hugh.util.exception;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.lang.StringFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    void getMessage_NullThrowable_ReturnsNull() {
        assertNull(ExceptionUtils.getMessage(null));
    }

    @Test
    void getMessage_NonNullThrowable_ReturnsFormattedString() {
        Throwable throwable = new IllegalArgumentException("Invalid argument");
        String expectedMessage = StringFormatter.format("{}: {}", throwable.getClass().getSimpleName(), throwable.getMessage());
        assertEquals(expectedMessage, ExceptionUtils.getMessage(throwable));
    }

    @Test
    void getMessage_ThrowableWithNullMessage_ReturnsClassNameOnly() {
        Throwable throwable = new IllegalArgumentException();
        String expectedMessage = StringFormatter.format("{}: {}", throwable.getClass().getSimpleName(), "");
        assertEquals(expectedMessage+"null", ExceptionUtils.getMessage(throwable));
    }
}
