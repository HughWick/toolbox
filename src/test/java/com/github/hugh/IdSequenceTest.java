package com.github.hugh;

import com.github.hugh.id.Snowflake;
import org.junit.Test;

import java.util.Map;

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

    @Test
    public void test02() {
        Snowflake snowflake = new Snowflake(1, 2);
        for (int i = 0; i < 1 << 12; i++) {
            System.out.println(snowflake.nextId());
            Map dec = Snowflake.decompile(snowflake.nextId());
            System.out.println("--->" + dec);

        }
    }
}
