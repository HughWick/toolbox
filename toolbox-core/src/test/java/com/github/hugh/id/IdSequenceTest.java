package com.github.hugh.id;

import com.github.hugh.IdSequence;
import com.github.hugh.util.DateUtils;
import com.github.hugh.util.regex.RegexUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * id生成器测试
 *
 * @author hugh
 * @version 1.0.0
 * @since JDK 1.8
 */
class IdSequenceTest {

    @Test
    void testSnowflake() {
        String unique = IdSequence.snowflake();
        assertEquals(19, unique.length());
        assertTrue(RegexUtils.isNumeric(unique));
        assertNotEquals(IdSequence.snowflake(), IdSequence.snowflake());
        String unique2 = IdSequence.snowflake(1, 1);
        assertEquals(19, unique2.length());
    }

    // 测试解析
    @Test
    void testDecompile() {
        Snowflake snowflake = new Snowflake(1, 2);
        for (int i = 0; i < 1 << 12; i++) {
            Map<String, Object> dec = Snowflake.decompile(snowflake.nextId());
            assertEquals(2, dec.get("workerId"));
            assertEquals(1, dec.get("dataCenter"));
            assertTrue(DateUtils.isDateFormat(dec.get("date").toString()));
        }
    }

//    public static void main(String[] args) {
////        Instance.getInstance().singleton(null);
//        long id = 1359463339870322690L;
//        System.out.println("-1-->" + IdSequence.snowflake());
//        System.out.println("-2-->" + IdSequence.snowflake());
//        System.out.println("-3-->" + IdSequence.snowflake());
//        System.out.println("------->>" + Snowflake.decompile(id));
//    }
}
