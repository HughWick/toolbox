package com.github.hugh.lang;

import com.github.hugh.bean.dto.ResponseDTO;
import com.github.hugh.util.TimeUtils;
import org.junit.jupiter.api.Test;

/**
 * @author AS
 * @date 2022/2/15 19:52
 */
public class ResponseTest {

    @Test
    void test01() {
        ResponseDTO dto1 = new ResponseDTO("0000", "ssss" , "/" , TimeUtils.now() );
        System.out.println(dto1);
//        System.out.println("--1->" + JsonObjectUtils.toJson(dto1));
        System.out.println("===2=>>>" + dto1.equalCode("0000"));
        System.out.println("==3==>>>" + dto1.notEqualCode("0000"));
    }
}
