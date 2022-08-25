package com.github.hugh.json;

import com.github.hugh.json.gson.JsonObjectUtils;
import com.github.hugh.json.model.Student;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * User: AS
 * Date: 2021/12/29 9:44
 */
class JsonObjectUtilsTest {

    static final String YEAR_MONTH_DAY_HOUR_MIN_SEC = "yyyy-MM-dd HH:mm:ss";

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
    }

    @Test
    void testFormJsonDate() {
        String tmee = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06 12:11:20\"}";
        Student student2 = JsonObjectUtils.fromJson(tmee, Student.class, YEAR_MONTH_DAY_HOUR_MIN_SEC);
        System.out.println("=--1>>" + student2.getCreate());
    }

    @Test
    void testTimeStamp() {
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"system\":1625024713000,\"id\":1,\"name\":\"张三\",\"create\":\"1625024713000\"}";
        Student student1 = JsonObjectUtils.fromJsonTimeStamp(str, Student.class);
        System.out.println(student1.toString());
        System.out.println("--1->>" + JsonObjectUtils.toJson(student1));
        JsonObject parse = JsonObjectUtils.parse(str);
        System.out.println("---2->>" + JsonObjectUtils.fromJsonTimeStamp(parse, Student.class));
    }

    @Test
    void test() {
        String str = "{\"age\":2,\"amount\":10.14,\"money\":12.3456,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\",\"id\":null,\"opType\":1}";
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
        System.out.println("=--6->>" + JsonObjectUtils.getBigDecimal(json2, "money"));
//        String s = new Date().toString();
//        String strDate2 = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\"}";
//        Student student = JsonObjectUtils.fromJson(strDate2, Student.class);
//        System.out.println("=--7->>" + student.getCreate());
//        System.out.println("-8-->>" + JsonObjectUtils.toJson(student));
        Map<Object, Object> objectObjectMap = JsonObjectUtils.toMap(json2);
        System.out.println("-9-->>" + objectObjectMap);
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

        String str2 = "[{\"serialNo\":\"1339497989051277312\",\"createBy\":1,\"createDate\":1608196182000,\"updateBy\":\"xxxx\",\"updateDate\":1615444156000,\"name\":\"张三\"}]";
        JsonArray jsonElements = JsonObjectUtils.parseArray(str2);
        List<JsonObject> objects = JsonObjectUtils.toArrayList(jsonElements);
        System.out.println("-=4==>>>" + objects);
        List<Student> students = JsonObjectUtils.toArrayList(jsonElements, Student.class);
        students.forEach(System.out::println);
    }

    @Test
    void testMapToJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        JsonObject parse = JsonObjectUtils.parse(map);
        assert parse != null;
        System.out.println("---->>" + JsonObjectUtils.toJson(parse));
    }

    @Test
    void testForEach() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("b_2", "1223_sad");
        map.put("b", map2);
        JsonObject parse = JsonObjectUtils.parse(map);
        assert parse != null;
        for (Map.Entry<String, JsonElement> entrySet : parse.entrySet()) {
            System.out.println("key>" + entrySet.getKey());
            System.out.println("222value>" + entrySet.getValue());
            System.out.println("--------------------------");
        }
    }

    @Test
    void testSingle() throws InterruptedException {
        new Thread(() -> {
            System.out.println("---4---");
            Gson gson = JsonObjectUtils.getInstance();
            System.out.println("---4->>" + System.identityHashCode(gson));
        }).start();
        new Thread(() -> {
            System.out.println("---5---");
            Gson gson = JsonObjectUtils.getInstance();
            System.out.println("---5->>" + System.identityHashCode(gson));
        }).start();
        new Thread(() -> {
            System.out.println("---6---");
            Gson gson = JsonObjectUtils.getInstance();
            System.out.println("---6->>" + System.identityHashCode(gson));
        }).start();
        new Thread(() -> {
            System.out.println("---7---");
            Gson gson = JsonObjectUtils.getInstance();
            System.out.println("---7->>" + System.identityHashCode(gson));
        }).start();

        Thread.sleep(2000);
        System.out.println("==END==");
    }

    @Test
    void testIsJson() {
        var str = "{\"age\":1,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1888,\"name\":\"张三\",\"create\":\"16250247130001\"}";
        var str2 = "{code:006,message:测试,age:18,created:2022-03-21 18:02:11,amount:199.88,switchs:true}";
        assertTrue(JsonObjectUtils.isJsonObject(str));
        assertTrue(JsonObjectUtils.isNotJsonObject(str2));
        assertFalse(JsonObjectUtils.isJsonObject(str2));
//        var array = "[1,2,3,4,5]";
//        var array2 = "1,2,3,4,5";
//        assertTrue(JsonObjectUtils.isJsonArray(array));
//        assertFalse(JsonObjectUtils.isJsonArray(array2));
//        assertTrue(JsonObjectUtils.isNotJsonArray(array2));
        assertTrue(JsonObjectUtils.isJsonValid(str));
        assertFalse(JsonObjectUtils.isJsonValid(""));
        assertTrue(JsonObjectUtils.isNotJsonValid(""));
//        assertFalse(JsonObjectUtils.isNotJsonArray(array));
    }

    // 测试多个json解析，与统计出现次数
    @Test
    void testMultipleJsonStr() {
        String str = "{\n" +
                "\t\"action\":\t\"R\",\n" +
                "\t\"0f000001\":\t\"1.6.0-DEBUG\",\n" +
                "\t\"0f000002\":\t\"1234567890\",\n" +
                "\t\"0f000003\":\t\"35353032193936345446537F\"\n" +
                "}\n" +
                "\n" +
                "\n" +
                "{\n" +
                "\t\"action\":\t\"R2\",\n" +
                "\t\"0f000001\":\t\"1.2.0-DEBUG\",\n" +
                "\t\"0f000002\":\t\"2-1234567890\",\n" +
                "\t\"0f000003\":\t\"2-35353032193936345446537F\"\n" +
                "}";
        System.out.println("-list-->>" + JsonObjectUtils.parseMultipleJson(str));
        assertEquals(JsonObjectUtils.countJson(str), 2);
    }
}
