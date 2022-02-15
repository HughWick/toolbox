package com.github.hugh.lang;

import com.github.hugh.model.dto.ResultDTO;
import com.github.hugh.util.gson.JsonObjectUtils;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * @author AS
 * @date 2021/2/1 9:52
 */
public class ResultTest {

    @Test
    void test01() {
        ResultDTO dto1 = new ResultDTO("0000", "ssss");
        System.out.println("--1->" + JsonObjectUtils.toJson(dto1));
        System.out.println("===2=>>>" + dto1.equalCode("0000"));
        System.out.println("==3==>>>" + dto1.notEqualCode("0000"));
    }

    @Test
    void test02() {
//        ResultDTO dto = new ResultDTO<>("0000", "操作成功");
//        System.out.println("--->>"+ResultUtils.re());
    }

    public static void main(String[] args) {

        ResultDTO dto = new ResultDTO<>("0000", "操作成功");
        System.out.println("--1->" + JsonObjectUtils.toJson(dto));
        System.out.println("---.>>" + dto.toString());
//        System.out.println("--2->" + ResultUtils.isEquals(dto, "0000"));
//        System.out.println("--3->" + ResultUtils.isNotEquals(dto, "0000"));
        val map = new HashMap<>();

        System.out.println("--4->" + JsonObjectUtils.toJson(new ResultDTO<>("000", "操作成功", map)));
//        System.out.println("--5->>" + dto.isEquals("0000"));
//        System.out.println("--6->>" + dto.isNotEquals("0000"));
        ResultDTO dto2 = new ResultDTO<>("00200", "操作成功");
//        System.out.println("--7->>" + dto2.isEquals("0000"));
//        System.out.println("--8->>" + dto2.isNotEquals("0000"));


    }
}
