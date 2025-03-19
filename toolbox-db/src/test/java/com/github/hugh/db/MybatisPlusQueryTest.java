package com.github.hugh.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.hugh.db.util.MybatisPlusQueryUtils;
import com.github.hugh.util.ServletUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void testV1Control() {
        String order = "desc";
        String sort = "id";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("age", "1");
        request.addParameter("page", "1");
        request.addParameter("size", "20");
        Map<String, Object> params = ServletUtils.getParamsDeleteLimit(request);
        params.put("order", order);
        params.put("sort", sort);
        assertEquals("(AGE = ? AND DELETE_FLAG = ?) ORDER BY ID DESC", MybatisPlusQueryUtils.createDef(params).getTargetSql());

    }

    @Test
    void testV2() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("age", "1");
        request.addParameter("page", "1");
        request.addParameter("size", "20");
        MybatisPlusQuery mybatisPlusQuery = new MybatisPlusQuery(request);
        mybatisPlusQuery.
                setSortOrder("ID", "desc").
                defaultDeleteFlag()
                .removePageSize();
        assertEquals("(delete_flag = ? AND age = ?) ORDER BY ID DESC", mybatisPlusQuery.create().getTargetSql());
        mybatisPlusQuery.keyUpper();
        assertEquals("(DELETE_FLAG = ? AND AGE = ?) ORDER BY ID DESC", mybatisPlusQuery.create().getTargetSql());
    }

    @Test
    void testUnion() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("age", "1");
        Map<String, Object> params = new HashMap<>();
        params.put("name", "order");
        MybatisPlusQuery mybatisPlusQuery = new MybatisPlusQuery(request);
        mybatisPlusQuery.addParams(params)
                .defaultDeleteFlag()
        ;
        assertEquals("(name = ? AND delete_flag = ? AND age = ?)", mybatisPlusQuery.create().getTargetSql());
    }

    @Test
    void testUnionSigle () {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("age", "1");
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", "order");
        MybatisPlusQuery mybatisPlusQuery = new MybatisPlusQuery(request);
        mybatisPlusQuery.addParams("name", "order")
                .defaultDeleteFlag()
        ;
        assertEquals("(name = ? AND delete_flag = ? AND age = ?)", mybatisPlusQuery.create().getTargetSql());
    }

    // 测试无效的排序类型时，setSortOrder 是否抛出异常
    @Test
    void testSetSortOrder_InvalidSortType() {
        MybatisPlusQuery query = new MybatisPlusQuery();
        // 测试无效的排序类型 "invalid"
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            query.setSortOrder("name", "invalid");
        });
        // 验证抛出的异常消息是否包含正确的提示
        assertEquals("Invalid sortType. Only 'asc' or 'desc' are allowed.", exception.getMessage());
    }
}
