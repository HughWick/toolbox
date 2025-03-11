package com.github.hugh.util.lang;

import com.github.hugh.bean.dto.ResultDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 返回数据实体接口来
 *
 * @author AS
 * @date 2021/2/1 9:52
 */
class ResultTest {

    @Test
    void test01() {
        String code1 = "0000";
        String message1=  "success";
        ResultDTO dto1 = new ResultDTO(code1, message1);
        assertTrue(dto1.equalCode(code1));
        assertFalse(dto1.notEqualCode(code1));
        ResultDTO<Object> build = ResultDTO.builder().code(code1).message(message1).timestamp(System.currentTimeMillis()).build();
        System.out.println(build);

    }

    // 测试无参构造
    @Test
    void test02() {
        ResultDTO dto2 = new ResultDTO<>();
        assertNull(dto2.getMessage());
        assertNull(dto2.getCode());
        assertNull(dto2.getData());
    }

    @Test
    void defaultConstructorTest() {
        ResultDTO<String> resultDTO = new ResultDTO<>();
        // 验证 timestamp 是否已初始化 (接近当前时间)
        long currentTimeMillis = System.currentTimeMillis();
        assertTrue(resultDTO.getTimestamp() <= currentTimeMillis);
        assertTrue(resultDTO.getTimestamp() > currentTimeMillis - 100); // 允许 100ms 的误差，因为测试执行时间和获取时间戳可能存在细微差别
        // 验证 code, message, data 字段是否为默认值 (null)
        assertNull(resultDTO.getCode());
        assertNull(resultDTO.getMessage());
        assertNull(resultDTO.getData());
    }

    @Test
    void parameterizedConstructorTest() {
        String testCode = "200";
        String testMessage = "请求成功";
        ResultDTO<Integer> resultDTO = new ResultDTO<>(testCode, testMessage);
        // 验证 timestamp 是否已初始化 (接近当前时间)
        long currentTimeMillis = System.currentTimeMillis();
        assertTrue(resultDTO.getTimestamp() <= currentTimeMillis);
        assertTrue(resultDTO.getTimestamp() > currentTimeMillis - 100); // 允许 100ms 的误差
        // 验证 code 和 message 字段是否已正确赋值
        assertEquals(testCode, resultDTO.getCode());
        assertEquals(testMessage, resultDTO.getMessage());
        // 验证 data 字段是否为默认值 (null)
        assertNull(resultDTO.getData());
    }
}
