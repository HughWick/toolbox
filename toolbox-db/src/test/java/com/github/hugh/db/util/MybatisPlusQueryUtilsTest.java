package com.github.hugh.db.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.hugh.db.constants.QueryCode;
import com.google.common.base.CaseFormat;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MybatisPlusQueryUtils 测试类
 * User: AS
 * Date: 2021/11/11 10:44
 */
class MybatisPlusQueryUtilsTest {

//    private MockHttpServletRequest request;

    @Test
    void testNull(){
        assertThrowsExactly(NullPointerException.class, () ->  MybatisPlusQueryUtils.create(null,true));
    }

    @Test
    void testCode() {
        assertEquals("SERIAL_NO", QueryCode.SERIAL_NO);
        assertEquals("SERIAL_NUMBER", QueryCode.SERIAL_NUMBER);
        assertEquals("STATUS", QueryCode.STATUS);
        assertEquals("FLAG", QueryCode.FLAG);
        assertEquals("CREATE_BY", QueryCode.CREATE_BY);
        assertEquals("NAME", QueryCode.NAME);
    }

    // 测试小写
    @Test
    void testCodeLowercase() {
        assertEquals("serial_no", com.github.hugh.constant.QueryCode.Lowercase.SERIAL_NO);
//        assertEquals("SERIAL_NUMBER", QueryCode.SERIAL_NUMBER);
//        assertEquals("STATUS", QueryCode.STATUS);
//        assertEquals("FLAG", QueryCode.FLAG);
//        assertEquals("CREATE_BY", QueryCode.CREATE_BY);
//        assertEquals("NAME", QueryCode.NAME);
    }

    @Test
    void testCreateLowerCase() {
        Map<String, String> map = new HashMap<>();
        map.put("model", "12V".toLowerCase());
        map.put("startDate", "2020-01-12 00:00:00");
        assertEquals("(model = ? AND create_date >= ?)", MybatisPlusQueryUtils.createLowCase(map).getTargetSql());
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
        assertEquals("(CREATE_DATE <= ? AND MODEL = ? AND NAME LIKE ? AND CREATE_DATE >= ?) ORDER BY SERIAL_NUMBER DESC", MybatisPlusQueryUtils.create(map).getTargetSql());
    }

    // 测试or语句
    @Test
    void testOr() {
        Map<String, String> map = new HashMap<>();
        map.put("serialNumber_powerCode_meteringBoardCode_boxBatch__or", "123");
        String targetSql = MybatisPlusQueryUtils.create(map).getTargetSql();
        assertEquals("((SERIAL_NUMBER LIKE ? OR POWER_CODE LIKE ? OR METERING_BOARD_CODE LIKE ? OR BOX_BATCH LIKE ?))", targetSql);
        Map<String, String> map2 = new HashMap<>();
        map2.put("name_serialNo_sex_or", "332");
        String targetSql2 = MybatisPlusQueryUtils.create(map2).getTargetSql();
        assertEquals("((NAME LIKE ? OR SERIAL_NO LIKE ? OR SEX LIKE ?))", targetSql2);
        Map<String, String> map3 = new HashMap<>();
        map3.put("name_serialNo_sex__or", "332");
        String targetSql3 = MybatisPlusQueryUtils.create(map3).getTargetSql();
        assertEquals("((NAME LIKE ? OR SERIAL_NO LIKE ? OR SEX LIKE ?))", targetSql3);
        Map<String, String> map4 = new HashMap<>();
        map4.put("name_serialNo_socketChannelId__or", "332");
        String targetSql4 = MybatisPlusQueryUtils.create(map4, false).getTargetSql();
        assertEquals("((name LIKE ? OR serial_no LIKE ? OR socket_channel_id LIKE ?))", targetSql4);
    }

    // 测试in 语句
    @Test
    void testIn() {
        Map<String, String> map = new HashMap<>();
        map.put("serialNumber_in", "123,abc,是,发");
        String key = "name_IN";
        map.put(key, "大写,ABC,@!#,{}");
        //
        String to = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, key);
        assertEquals("NAME__I_N", to);
        String targetSql = MybatisPlusQueryUtils.create(map).getTargetSql();
        assertEquals("(SERIAL_NUMBER IN ('123','abc','是','发') AND NAME__I_N = ?)", targetSql);
        Map<String, String> map2 = new HashMap<>();
        map2.put("NetworkFlag_in", "123,abc,是,发");
        String targetSql2 = MybatisPlusQueryUtils.create(map2).getTargetSql();
        assertEquals("(NETWORK_FLAG IN ('123','abc','是','发'))", targetSql2);
        Map<String, String> map3 = new HashMap<>();
        map3.put("heartbeatFlag_in", "1,100,4");
        map3.put("deleteFlag", "1");
        String targetSql3 = MybatisPlusQueryUtils.create(map3).getTargetSql();
        assertEquals("(DELETE_FLAG = ? AND HEARTBEAT_FLAG IN ('1','100','4'))", targetSql3);
        String targetSql4 = MybatisPlusQueryUtils.create(map3, false).getTargetSql();
        assertEquals("(delete_flag = ? AND heartbeat_flag IN ('1','100','4'))", targetSql4);
    }

    // 测试大于、小于
    @Test
    void testGeAndLe() {
        Map<String, String> map = new HashMap<>();
        map.put("updateDate_ge", "2021-01-31 00:00:00");
        map.put("updateDate_le", "2021-03-02 00:00:00");
        String targetSql = MybatisPlusQueryUtils.create(map).getTargetSql();
//        System.out.println(MybatisPlusQueryUtils.create(map).getCustomSqlSegment());
        assertEquals("(UPDATE_DATE >= ? AND UPDATE_DATE <= ?)", targetSql);
    }

    @Test
    void testRequestCreate() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("userId", "9001"); //直接添加request参数，相当简单
        QueryWrapper<Object> objectQueryWrapper = MybatisPlusQueryUtils.create(request);
        assertEquals("(USER_ID = ?)", objectQueryWrapper.getTargetSql());
//        System.out.println(objectQueryWrapper.getTargetSql());
        QueryWrapper<Object> objectQueryWrapper2 = MybatisPlusQueryUtils.createDef(request);
        assertEquals("(USER_ID = ? AND DELETE_FLAG = ?)", objectQueryWrapper2.getTargetSql());
//        System.out.println(objectQueryWrapper2.getTargetSql());
        Map<String, String> map = new HashMap<>();
        QueryWrapper<Object> defQueryWrapper = MybatisPlusQueryUtils.createDef(map);
        assertEquals("(DELETE_FLAG = ?)", defQueryWrapper.getTargetSql());
//        System.out.println("--->>" + defQueryWrapper.getTargetSql());
    }

    @Test
    void testRequestCreateLowerCase() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("serialNo", "9001"); //直接添加request参数，相当简单
        QueryWrapper<Object> objectQueryWrapper = MybatisPlusQueryUtils.createLowerCase(request);
//        System.out.println(objectQueryWrapper.getSqlSelect());
        assertEquals("(serial_no = ?)", objectQueryWrapper.getTargetSql());
    }

    @Test
    void testSortType() {
        Map<String, String> map = new HashMap<>();
        map.put("model", "12V");
        map.put(MybatisPlusQueryUtils.SORT_TYPE, "DESC");
        map.put(MybatisPlusQueryUtils.SORT_BY, "serialNumber");
        assertEquals("(MODEL = ?) ORDER BY SERIAL_NUMBER DESC", MybatisPlusQueryUtils.create(map).getTargetSql());
    }

    @Test
    void testIsAcronym() {
        assertTrue(MybatisPlusQueryUtils.isAcronym("ABC"));
        assertFalse(MybatisPlusQueryUtils.isAcronym("Abc"));
        assertFalse(MybatisPlusQueryUtils.isAcronym("Abc"));
        assertFalse(MybatisPlusQueryUtils.isAcronym(""));
    }

    @Test
    void testIsAsc(){
        assertTrue(MybatisPlusQueryUtils.isAsc("asc"));
        assertFalse(MybatisPlusQueryUtils.isAsc(""));
    }
}
