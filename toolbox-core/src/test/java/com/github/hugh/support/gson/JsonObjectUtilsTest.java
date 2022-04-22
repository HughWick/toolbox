package com.github.hugh.support.gson;

import com.github.hugh.bean.dto.Ip2regionDTO;
import com.github.hugh.constant.DateCode;
import com.github.hugh.model.Student;
import com.github.hugh.util.OkHttpUtils;
import com.github.hugh.util.gson.JsonObjectUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

/**
 * User: AS
 * Date: 2021/12/29 9:44
 */
class JsonObjectUtilsTest {

    @Test
    void testFormJson() {
        String strDate2 = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\"}";
        Student student = JsonObjectUtils.fromJson(strDate2, Student.class);
//        JsonObject parse = JsonObjectUtils.parse(student);
//        System.out.println("=--1->>" + parse);
        System.out.println("=--2->>" + JsonObjectUtils.fromJson(student, Student.class));
        System.out.println("-2-->>" + JsonObjectUtils.toJson(student));
        JsonObject item = new JsonObject();
        item.addProperty("country", "国家");// 国家
        System.out.println("-3-->>" + JsonObjectUtils.fromJson(item, Ip2regionDTO.class));
    }

    @Test
    void testFormJsonDate() {
        String tmee = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06 12:11:20\"}";
        Student student2 = JsonObjectUtils.fromJson(tmee, Student.class, DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC);
        System.out.println("=--1>>" + student2.getCreate());
    }

    @Test
    void test() {
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\",\"id\":null,\"opType\":1}";
        String arryStr = "{ " +
                "\"client\":\"127.0.0.1\"," +
                "\"servers\":[" +
                "    \"8.8.8.8\"," +
                "    \"8.8.4.4\"," +
                "    \"156.154.70.1\"," +
                "    \"156.154.71.1\" " +
                "    ]}";
        JsonObject parse = JsonObjectUtils.parse(arryStr);
        JsonObject json2 = JsonObjectUtils.parse(str);
        System.out.println("-1-->>" + parse);
        Map map = JsonObjectUtils.toMap(parse);
        System.out.println("-2-->>" + map.get("servers"));
        JsonArray servers = JsonObjectUtils.getJsonArray(parse, "servers");
        System.out.println("=--3->>" + servers);
        System.out.println("=--4->>" + JsonObjectUtils.toArrayList(servers));
        System.out.println("=--5->>" + JsonObjectUtils.fromJson(servers, LinkedList.class));
//        String s = new Date().toString();
//        String strDate2 = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\"}";
//        Student student = JsonObjectUtils.fromJson(strDate2, Student.class);
//        System.out.println("=--7->>" + student.getCreate());
//        System.out.println("-8-->>" + JsonObjectUtils.toJson(student));
        Map<Object, Object> objectObjectMap = JsonObjectUtils.toMap(json2);
        System.out.println("-9-->>" + objectObjectMap);
    }

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

    @Test
    void testArray() {
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\",\"id\":null}";
        JsonObject parse = JsonObjectUtils.parse(str);
        System.out.println("--1->>" + parse);
        String arr = "[1,2,3,4,5]";
        JsonArray jsonArray = JsonObjectUtils.parseArray(arr);
//        jsonArray.forEach(System.out::println);
        System.out.println("-2-->>" + jsonArray);
        String arr2 = null;
        System.out.println("-3-->>" + JsonObjectUtils.parseArray(arr2));

        String str2 = "[{\"serialNo\":\"1339497989051277312\",\"createBy\":1,\"createDate\":1608196182000,\"updateBy\":\"xxxx\",\"updateDate\":1615444156000}]";
        JsonArray jsonElements = JsonObjectUtils.parseArray(str2);
        List<JsonObject> objects = JsonObjectUtils.toArrayList(jsonElements);
        System.out.println("-=4==>>>" + objects);
    }

    @Test
    void testDate() {
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create2\":null,\"id\":1,\"name\":\"张三\",\"create\":\"1625024713000\"}";
//        JsonObjects jsonObjects = new JsonObjects(str);
        Student student1 = JsonObjectUtils.fromJsonTimeStamp(str, Student.class);
        System.out.println(student1.toString());
        System.out.println("--->>" + JsonObjectUtils.toJson(student1));
//        Student student = jsonObjects.fromJsonTimeStamp(Student.class);
//        System.out.println(JsonObjectUtils.toJson(student));
    }
}
