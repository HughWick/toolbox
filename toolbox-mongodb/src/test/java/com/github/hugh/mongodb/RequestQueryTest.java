package com.github.hugh.mongodb;

import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author AS
 * @date 2023/12/15 16:33
 */
class RequestQueryTest {

    @Test
    void test01() {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", "123");
        Query query1 = RequestQuery.create(map).query();
        System.out.println("===>" + query1.toString());
        String sqlStr1 = "Query: { \"serialNumber\" : \"123\"}, Fields: {}, Sort: {}";
        assertEquals(sqlStr1, query1.toString());
        map.put("flag" , 0);
        Query query2 = RequestQuery.create(map).query();
        System.out.println("2===>" + query2.toString());
    }
}
