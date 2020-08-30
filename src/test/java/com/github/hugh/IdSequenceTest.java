package com.github.hugh;

import org.junit.Test;

/**
 * @author hugh
 * @version 1.0.0
 * @since JDK 1.8
 */
public class IdSequenceTest {

    @Test
    public void test() {
        String unique = IdSequence.snowflake();
        System.out.println("-->" + unique);
    }
}
