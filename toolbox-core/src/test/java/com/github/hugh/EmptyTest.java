package com.github.hugh;

import com.github.hugh.util.EmptyUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试内容是否未空工具类
 */
class EmptyTest {

    @Test
    void isEmpty() {
        String[] arr = {};
        int[] intArray = {};
        assertTrue(EmptyUtils.isEmpty("  "));
        assertTrue(EmptyUtils.isEmpty("null"));
        assertTrue(EmptyUtils.isEmpty(new ArrayList<>()));
        assertTrue(EmptyUtils.isEmpty(arr));
        assertTrue(EmptyUtils.isEmpty(new HashMap<>()));
        assertFalse(EmptyUtils.isEmpty(12));
        assertFalse(EmptyUtils.isEmpty("[]"));
        assertTrue(EmptyUtils.isEmpty(intArray));
    }

    // 不为空
    @Test
    void isNotEmpty() {
        assertTrue(EmptyUtils.isNotEmpty("b"));
        assertTrue(EmptyUtils.isNotEmpty("[]"));
        assertFalse(EmptyUtils.isNotEmpty(""));
    }

    @Test
    void testStringArrayEmpty() {
        String[] strArr = {"1"};
        assertTrue(EmptyUtils.isNotEmpty(strArr));
        assertFalse(EmptyUtils.isEmpty(strArr));
        assertEquals(1, strArr.length);
    }
}
