package com.github.hugh.support.gson;

import com.github.hugh.util.gson.JsonObjectUtils;
import com.google.gson.JsonArray;
import org.junit.Test;

/**
 * {@link JsonObjectUtils} 测试类
 */
public class JsonObjectUtilsTest {


    @Test
    public void test01() {
        String str2 = "[{\"serialNo\":\"1339497989051277312\",\"createBy\":1,\"createDate\":1608196182000,\"updateBy\":\"xxxx\",\"updateDate\":1615444156000}]";
        JsonArray jsonElements = JsonObjectUtils.parseArray(str2);
        System.out.println("-====>>>" + JsonObjectUtils.toArrayList(jsonElements));
    }
}
