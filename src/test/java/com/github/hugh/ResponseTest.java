package com.github.hugh;

import com.github.hugh.constant.ResponseCode;
import com.github.hugh.support.Responses;
import net.sf.json.JSONObject;
import org.junit.Test;

/**
 * @author AS
 * @date 2020/11/19 10:40
 */
public class ResponseTest {

    @Test
    public void test01() {
        Responses r = new Responses(ResponseCode.CODE, "1", ResponseCode.MESSAGE, "操作成功");
        System.out.println("--1->>" + r.info());
        Responses r2 = new Responses<>(ResponseCode.CODE, "1", "msg", "操作成功", ResponseCode.DATA, "object");
        System.out.println("--2->>" + r2.data());
        JSONObject json = new JSONObject();
        json.put("token", ",");
        Responses r3 = new Responses<>(ResponseCode.CODE, "1", "msg", "操作成功", ResponseCode.DATA, json, ResponseCode.TOTAL_COUNT, 100);
        System.out.println("--3->>" + r3.complete());
        Responses r4 = new Responses(ResponseCode.CODE, "0013", "msg", "暂无数据");
        System.out.println("-4-->>" + r4.info());
    }
}
