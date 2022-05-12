package com.github.hugh.db.sql;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.hugh.db.constants.QueryCode;
import com.github.hugh.db.model.QueryVO;
import com.github.hugh.db.util.MybatisPlusQueryUtils;
import com.github.hugh.util.gson.JsonObjectUtils;
import com.github.hugh.util.gson.JsonObjects;
import com.google.common.base.CaseFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * User: AS
 * Date: 2021/11/11 10:44
 */
class QueryTest {

    private MockHttpServletRequest request;

    @Test
    void testCode() {
        Assertions.assertEquals("SERIAL_NO", QueryCode.SERIAL_NO);
        Assertions.assertEquals("SERIAL_NUMBER", QueryCode.SERIAL_NUMBER);
    }

    @Test
    void testDate() {
        Map<String, String> map = new HashMap<>();
        map.put("model", "12V");
        map.put("startDate", "2020-01-12 00:00:00");
        map.put("endDate", "2020-03-31 23:00:00");
        map.put("name_like", "里");

        map.put("order", "DESC");
        map.put("sort", "serialNumber");
        System.out.println(map);
        String targetSql = MybatisPlusQueryUtils.create(map).getTargetSql();
        System.out.println(targetSql);
    }

    // 测试or语句
    @Test
    void testOr() {
        Map<String, String> map = new HashMap<>();
        map.put("serialNumber_powerCode_meteringBoardCode_boxBatch__or", "123");
        String targetSql = MybatisPlusQueryUtils.create(map).getTargetSql();
        System.out.println(targetSql);
    }

    // 测试in 语句
    @Test
    void testIn() {
        Map<String, String> map = new HashMap<>();
        map.put("serialNumber_in", "123,abc,是,发");
        String key = "name_IN";
        map.put(key, "大写,ABC,@!#,{}");
        String to = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, key);
        System.out.println("====LOWER_CAMEL===>>>" + to);
        String targetSql = MybatisPlusQueryUtils.create(map).getTargetSql();
        System.out.println(targetSql);
    }

    // 测试大于、小于
    @Test
    void test01() {
        Map<String, String> map = new HashMap<>();
        map.put("updateDate_ge", "2021-01-31 00:00:00");
        map.put("updateDate_le", "2021-03-02 00:00:00");
        String targetSql = MybatisPlusQueryUtils.create(map).getTargetSql();
        System.out.println(targetSql);
//        System.out.println(MybatisPlusQueryUtils.createDef(map).getTargetSql());
    }

    @Test
    void test02() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "9001"); //直接添加request参数，相当简单
        QueryWrapper<Object> objectQueryWrapper = MybatisPlusQueryUtils.create(request);
        System.out.println(objectQueryWrapper.getTargetSql());
        QueryWrapper<Object> objectQueryWrapper2 = MybatisPlusQueryUtils.createDef(request);
        System.out.println(objectQueryWrapper2.getTargetSql());
        Map<String, String> map = new HashMap<>();
        QueryWrapper<Object> defQueryWrapper = MybatisPlusQueryUtils.createDef(map);
        System.out.println("--->>" + defQueryWrapper.getTargetSql());
    }

    @Test
    void testObject() {
        var queryVO = new QueryVO();
//        Function<QueryVO, Double> getAmount = QueryVO::getAmount;
//        System.out.println("===>>" +getAmount.toString() );
        queryVO.setAge(18);
        System.out.println("--json->>" + JsonObjectUtils.toJson(queryVO));
        System.out.println("-fast-json->>" + JSON.toJSONString(queryVO));
        var s = new JsonObjects(queryVO).toMap();
        System.out.println("--1->" + s);
        QueryWrapper<Object> defQueryWrapper = MybatisPlusQueryUtils.createDef(s);
        System.out.println("--2->>" + defQueryWrapper.getTargetSql());
    }
}
