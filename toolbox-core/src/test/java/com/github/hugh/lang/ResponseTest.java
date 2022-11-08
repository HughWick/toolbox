package com.github.hugh.lang;

import com.github.hugh.bean.dto.ResponseDTO;
import com.github.hugh.util.TimeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 返回前端响应实体类 v2
 *
 * @author AS
 * @date 2022/2/15 19:52
 */
class ResponseTest {

    @Test
    void test() {
        ResponseDTO dto1 = new ResponseDTO("0000", "ssss", "/", TimeUtils.now());
        System.out.println(dto1);
        assertTrue(dto1.equalCode("0000"));
        assertFalse(dto1.notEqualCode("0000"));
    }
}
