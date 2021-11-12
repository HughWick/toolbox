package com.github.hugh.db;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * User: AS
 * Date: 2021/11/11 10:44
 */
public class QueryTest {

    @Test
    void testDate() {
        Map<String, Object> map = new HashMap<>();
        map.put("model", "12V");
        map.put("startDate", "2020-01-12 00:00:00");
        map.put("endDate", "2020-03-31 23:00:00");
        map.put("name_like", "里");
        map.put("serialNumber_powerCode_meteringBoardCode_boxBatch__or", "123");
        map.put("order", "DESC");
        map.put("sort", "serialNumber");
        map.put("serialNumber_in", "123,abc,是,发");
        String targetSql = MybatisPlusQueryUtils.create(map).getTargetSql();
        System.out.println(targetSql);
    }

    @Test
    void test01() {
        Map<String, Object> map = new HashMap<>();
        map.put("updateDate_ge", "2021-01-31 00:00:00");
        map.put("updateDate_le", "2021-03-02 00:00:00");
        String targetSql = MybatisPlusQueryUtils.create(map).getTargetSql();
        System.out.println(targetSql);
        System.out.println(MybatisPlusQueryUtils.createDef(map).getTargetSql());
    }
}
