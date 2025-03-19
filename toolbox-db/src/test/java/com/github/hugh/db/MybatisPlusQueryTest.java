package com.github.hugh.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MybatisPlusQueryTest {

    @Test
    void testRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("page", "1");
        request.addParameter("size", "20");
        MybatisPlusQuery mybatisPlusQuery = new MybatisPlusQuery().request(request);
        QueryWrapper<Object> objectQueryWrapper = mybatisPlusQuery.v1();
        String result1 = "(SIZE = ? AND PAGE = ? AND DELETE_FLAG = ?) ORDER BY ID DESC";

        assertEquals(result1, objectQueryWrapper.getTargetSql());
    }

    @Test
    void testCreate() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("age", "1");
        request.addParameter("page", "1");
        request.addParameter("size", "20");
        MybatisPlusQuery mybatisPlusQuery = new MybatisPlusQuery()
                .request(request)
                .removePageSize()
                .defaultDeleteFlag();
        QueryWrapper<Object> objectQueryWrapper1 = mybatisPlusQuery.create();
        String result1 = "(delete_flag = ? AND age = ?)";
        assertEquals(result1, objectQueryWrapper1.getTargetSql());
        MybatisPlusQuery mybatisPlusQuery2 = new MybatisPlusQuery()
                .request(request)
                .keyUpper()
                .defaultDeleteFlag();
        QueryWrapper<Object> objectQueryWrapper2 = mybatisPlusQuery2.create();
        String result2 = "(SIZE = ? AND PAGE = ? AND DELETE_FLAG = ? AND AGE = ?)";
        assertEquals(result2, objectQueryWrapper2.getTargetSql());
    }

}
