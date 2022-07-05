package com.github.hugh.db.constant;

import com.github.hugh.db.constants.QueryCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * User: AS
 * Date: 2022/7/5 11:00
 */
public class QueryCodeTest {

    @Test
    void testQueryCode(){
        assertEquals("STATUS" , QueryCode.STATUS);
        assertEquals("FLAG" , QueryCode.FLAG);
        assertEquals("CREATE_BY" , QueryCode.CREATE_BY);
    }
}
