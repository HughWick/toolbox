package com.github.hugh.id;

import com.github.hugh.IdSequence;
import org.junit.jupiter.api.Test;

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
        System.out.println("-->" + unique.length());
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

    public static void main(String[] args) {
//        Instance.getInstance().singleton(null);
        long id = 1359463339870322690L;
        System.out.println("-1-->" + IdSequence.snowflake());
        System.out.println("-2-->" + IdSequence.snowflake());
        System.out.println("-3-->" + IdSequence.snowflake());
        System.out.println("------->>" + Snowflake.decompile(id));
    }
}
