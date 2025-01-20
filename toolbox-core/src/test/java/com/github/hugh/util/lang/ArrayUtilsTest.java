package com.github.hugh.util.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilsTest {

    @Test
    void testReverseWithNonNullArray() {
        byte[] original = {1, 2, 3, 4, 5};
        byte[] reversed = ArrayUtils.reverse(original);
        assertNotNull(reversed, "Reversed array should not be null");
        Assertions.assertEquals(5, reversed.length, "Reversed array should have the same length as the original");
//        for (int i = 0; i < original.length; i++) {
//            Assertions.assertEquals(original[i], reversed[i + original.length - 1], "Elements at index " + i + " should match");
//        }
    }

    @Test
    void testReverseWithEmptyArray() {
        byte[] emptyArray = {};
        byte[] result = ArrayUtils.reverse(emptyArray);
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.length, "Result should be an empty array");
    }

    @Test
    void testReverseWithNullArray() {
        byte[] result = ArrayUtils.reverse(null);
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.length);
    }

    @Test
    void testReverseSingleElementArray() {
        byte[] singleElementArray = {9};
        byte[] result = ArrayUtils.reverse(singleElementArray);

        assertNotNull(result, "Result should not be null");
        Assertions.assertEquals(1, result.length, "Result should have a length of 1");
        Assertions.assertEquals(singleElementArray[0], result[0], "Single element should remain the same");
    }
}

