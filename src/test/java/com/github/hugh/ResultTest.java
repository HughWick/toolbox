package com.github.hugh;

import com.github.hugh.model.dto.ResultDTO;
import com.github.hugh.util.ResultUtils;
import com.github.hugh.util.gson.JsonObjectUtils;
import lombok.val;

import java.util.HashMap;

/**
 * @author AS
 * @date 2021/2/1 9:52
 */
public class ResultTest {

    public static void main(String[] args) {
        ResultDTO dto = ResultUtils.response("0000", "操作成功");
        System.out.println("--1->" + JsonObjectUtils.toJson(dto));
        System.out.println("--2->" + ResultUtils.isEquals(dto, "0000"));
        System.out.println("--3->" + ResultUtils.isNotEquals(dto, "0000"));
        val map = new HashMap<>();
        System.out.println("--4->" + JsonObjectUtils.toJson(ResultUtils.response("000", "操作成功", map)));
        System.out.println("--5->>" + dto.isEquals("0000"));
        System.out.println("--6->>" + dto.isNotEquals("0000"));
        ResultDTO dto2 = ResultUtils.response("00020", "操作成功");
        System.out.println("--7->>" + dto2.isEquals("0000"));
        System.out.println("--8->>" + dto2.isNotEquals("0000"));
    }
}
