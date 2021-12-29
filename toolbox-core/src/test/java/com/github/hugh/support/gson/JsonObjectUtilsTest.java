package com.github.hugh.support.gson;

import com.github.hugh.util.OkHttpUtils;
import com.github.hugh.util.gson.JsonObjectUtils;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * User: AS
 * Date: 2021/12/29 9:44
 */
class JsonObjectUtilsTest {

    @Test
    void test02() throws IOException {
        JSONObject json = new JSONObject();
        json.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        JsonObject jsonObject = OkHttpUtils.postFormReJsonObject("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json);
        System.out.println("--1->>" + jsonObject.toString());
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        JsonObject jsonObject2 = OkHttpUtils.postFormReJsonObject("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", objectObjectHashMap);
        System.out.println("--2->>" + jsonObject2.toString());
        JsonObject jsonObject3 = OkHttpUtils.postFormReJsonObject("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json.toString());
        System.out.println("--3->>" + jsonObject3.toString());
        System.out.println("--getString->>" + JsonObjectUtils.getString(jsonObject, "msg1"));
        System.out.println("--getString->>" + JsonObjectUtils.getString(jsonObject, "msg"));
        System.out.println("-getInt-->>" + JsonObjectUtils.getInt(jsonObject, "status"));
        System.out.println("--getInt->>" + JsonObjectUtils.getInt(jsonObject, "status2"));
        System.out.println("--getInteger->>" + JsonObjectUtils.getInteger(jsonObject, "status"));
        System.out.println("--getInteger->>" + JsonObjectUtils.getInteger(jsonObject, "status2"));
        System.out.println("--getLong->>" + JsonObjectUtils.getLong(jsonObject, "status2"));
        System.out.println("--getLongValue->>" + JsonObjectUtils.getLongValue(jsonObject, "status2"));
        System.out.println("--getDouble->>" + JsonObjectUtils.getDouble(jsonObject, "status"));
        System.out.println("--getDoubleValue->>" + JsonObjectUtils.getDoubleValue(jsonObject, "status2"));
        System.out.println("--getBigDecimal->>" + JsonObjectUtils.getBigDecimal(jsonObject, "status2"));
    }

    @Test
    void testGetDate() {
        Date date = new Date();
        System.out.println("===>>" + date);
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":\"Wed Dec 21 09:58:33 CST 2021\",\"create2\":\"2021-12-29 09:44:12\",\"id\":1,\"name\":\"张三\",\"create\":\"1625024713000\",\"createDate\":\"null\"}";
        JsonObject parse = JsonObjectUtils.parse(str);
        System.out.println(parse);
        System.out.println("==1==-->>" + JsonObjectUtils.getDateStr(parse, "create"));
        System.out.println("==2==-->>" + JsonObjectUtils.getDateStr(parse, "create2"));
        System.out.println("==3==-->>" + JsonObjectUtils.getDateStr(parse, "birthday"));
        System.out.println("==4==-->>" + JsonObjectUtils.getDateStr(parse, "createDate"));
        System.out.println("===========================================================");
        System.out.println("==1==-date->>" + JsonObjectUtils.getDate(parse, "create"));
    }
}
