package com.github.hugh.json;

import com.alibaba.fastjson.JSON;
import com.github.hugh.constant.DateCode;
import com.github.hugh.json.gson.JsonObjectUtils;
import com.github.hugh.json.gson.JsonObjects;
import com.github.hugh.json.model.Command;
import com.github.hugh.json.model.GsonDateDto;
import com.github.hugh.json.model.Student;
import com.github.hugh.util.DateUtils;
import com.github.hugh.util.EmptyUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * gson 工具类测试
 * User: AS
 * Date: 2021/12/29 9:44
 */
class JsonObjectUtilsTest {

    @Test
    void testFormJson() {
        String create = "1625024713000";
        Student student1 = new Student();
        student1.setId(1);
        student1.setAge(2);
        student1.setName("张三");
        student1.setAmount(10.14);
        student1.setBirthday(null);
//        student1.setSystem(0);
        student1.setCreate(DateUtils.parseTimestamp(create));
        String strJson1 = "{\"id\":1,\"age\":2,\"name\":\"张三\",\"amount\":10.14,\"birthday\":null,\"create\":\"" + create + "\"}";
        Student student = JsonObjectUtils.fromJson(strJson1, Student.class);
        assertEquals(student1.toString(), student.toString());
        String jsonStr = "{\"id\":1,\"age\":2,\"name\":\"张三\",\"amount\":10.14,\"create\":\"2021-06-30 11:45:13\",\"system\":0}";
        assertEquals(jsonStr, JsonObjectUtils.toJson(student));
        String jsonStr2 = "{\"id\":1,\"age\":2,\"name\":\"张三\",\"amount\":10.14,\"create\":" + create + ",\"system\":0}";
        assertEquals(jsonStr2, JsonObjectUtils.toJsonTimestamp(student));
    }

    @Test
    void testAddProperty() {
        String str1 = "{\"country\":\"国家\"}";
        JsonObject item = new JsonObject();
        item.addProperty("country", "国家");// 国家
        assertEquals(str1, item.toString());
    }

    // json转对象时，日期格式测试
    @Test
    void testFormJsonDate() {
        String dateStr = "2019-04-06 12:11:20";
        String jsonStr = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"" + dateStr + "\"}";
        Student student2 = JsonObjectUtils.fromJson(jsonStr, Student.class);
        assertEquals("Sat Apr 06 12:11:20 CST 2019", student2.getCreate().toString());
        assertEquals(dateStr, DateUtils.ofPattern(student2.getCreate()));
        // 测试时间戳
        String timeStampStr1 = "1625024713000";
        String cstDateStr = "Tue Sep 08 23:58:09 CST 2020";
        Date date = DateUtils.parseDate(cstDateStr, DateCode.CST_FORM);
        GsonDateDto gsonDateDto1 = new GsonDateDto();
        gsonDateDto1.setId(1);
        gsonDateDto1.setAge(2);
        gsonDateDto1.setName("张三");
        gsonDateDto1.setAmount(10.14);
        gsonDateDto1.setSystemDate(Long.parseLong(timeStampStr1));
        gsonDateDto1.setCreateDate(date);
        gsonDateDto1.setBirthday(DateUtils.parse(dateStr));
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":\"" + dateStr + "\",\"systemDate\":" + timeStampStr1 + ",\"id\":1,\"name\":\"张三\",\"createDate\":\"" + cstDateStr + "\"}";
        GsonDateDto gsonDateDto2 = JsonObjectUtils.fromJson(str, GsonDateDto.class);
        assertEquals(gsonDateDto1.toString(), gsonDateDto2.toString());
        assertEquals(JsonObjectUtils.toJson(gsonDateDto2), JsonObjectUtils.toJson(gsonDateDto1));
        // 再转换回去 验证
        JsonObject parse = JsonObjectUtils.parse(str);
        assertEquals(gsonDateDto1.toString(), JsonObjectUtils.fromJson(parse, GsonDateDto.class).toString());
//        var jsonStrSimple = "{age:2,amount:10.14,birthday:" + dateStr + ",systemDate:" + timeStampStr1 + ",id:1,name:张三,createDate:" + cstDateStr + "}";
//        GsonDateDto gsonDateDto3 = JsonObjectUtils.fromJson(jsonStrSimple, GsonDateDto.class);

    }

    //测试性能
    @Test
    void testFromJsonTime() {
        String dateStr = "2019-04-06 12:11:20";
        String timeStampStr1 = "1625024713000";
        String timeStampStr2 = "1625044713000";
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":\"" + dateStr + "\",\"systemDate\":" + timeStampStr1 + ",\"id\":1,\"name\":\"张三\",\"createDate\":\"" + timeStampStr2 + "\"}";
//        List<GsonDateDto> list = new ArrayList<>();
        int count = 100000;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("开始gson转换");
        for (int i = 0; i < count; i++) {
            JsonObjectUtils.fromJson(str, GsonDateDto.class);
        }
        stopWatch.stop();
        stopWatch.start("开始fastjson转换");
        for (int i = 0; i < count; i++) {
            JSON.parseObject(str, GsonDateDto.class);
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }


    @Test
    void test() {
        String ip1 = "8.8.8.8";
        String ip2 = "8.8.4.4";
        String ip3 = "156.154.70.1";
        String ip4 = "156.154.71.1";
        String str = "{\"age\":2,\"amount\":10.14,\"money\":12.3456,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\",\"id\":null,\"opType\":1}";
        String arrayStr = "{" +
                "\"client\":\"127.0.0.1\"," +
                "\"servers\":[" +
                "\"" + ip1 + "\"," +
                "\"" + ip2 + "\"," +
                "\"" + ip3 + "\"," +
                "\"" + ip4 + "\"" +
                "]}";
        ArrayList<String> arrayList = Lists.newArrayList(ip1, ip2, ip3, ip4);
        JsonObject parse = JsonObjectUtils.parse(arrayStr);
        assertEquals(arrayStr, parse.toString());
//        System.out.println("-1-->>" + parse);
        JsonObject json2 = JsonObjectUtils.parse(str);
        Map map = JsonObjectUtils.toMap(parse);
        assertEquals(arrayList.toString(), map.get("servers").toString());
//        System.out.println("-2-->>" + map.get("servers"));
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(ip1);
        jsonArray.add(ip2);
        jsonArray.add(ip3);
        jsonArray.add(ip4);
        JsonArray servers = JsonObjectUtils.getJsonArray(parse, "servers");
        assertNotNull(servers);
        assertEquals(jsonArray.toString(), servers.toString());
//        System.out.println("=--3->>" + servers);
        assertEquals(JsonObjectUtils.toArrayList(servers).toString(), arrayList.toString());
        assertEquals(JsonObjectUtils.fromJson(servers, LinkedList.class).toString(), arrayList.toString());
        assertEquals("12.3456", JsonObjectUtils.getBigDecimal(json2, "money").toString());
//        System.out.println("=--4->>" +);
//        System.out.println("=--5->>" + JsonObjectUtils.fromJson(servers, LinkedList.class));
//        System.out.println("=--6->>" + JsonObjectUtils.getBigDecimal(json2, "money"));
//        String s = new Date().toString();
//        String strDate2 = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\"}";
//        Student student = JsonObjectUtils.fromJson(strDate2, Student.class);
//        System.out.println("=--7->>" + student.getCreate());
//        System.out.println("-8-->>" + JsonObjectUtils.toJson(student));

    }

    // 测试json转map
    @Test
    void testToMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("age", 2);
        map.put("amount", 10.14);
        map.put("money", 12.3456);
        map.put("create", "2019-04-06");
        map.put("name", "张三");
        map.put("opType", 1);
        String str = "{\"age\":2,\"amount\":10.14,\"money\":12.3456,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\",\"id\":null,\"opType\":1}";
        JsonObject json2 = JsonObjectUtils.parse(str);
        Student student = JsonObjectUtils.fromJson(str, Student.class);
        Map<Object, Object> objectObjectMap = JsonObjectUtils.toMap(json2);
        assertEquals(map.toString(), objectObjectMap.toString());
//        System.out.println("1-->>" + objectObjectMap);
        assertEquals("{id=1, age=2, name=张三, amount=10.14, create=2019-04-06 00:00:00, system=0}", JsonObjectUtils.toMap(student).toString());
        // 升序验证
        assertEquals("{age=2, amount=10.14, create=2019-04-06 00:00:00, id=1, name=张三, system=0}", JsonObjectUtils.toMapSortByKeyAsc(student).toString());
        // 降序
        Map<String, Object> objectObjectMap1 = JsonObjectUtils.toMapSortByKeyDesc(student);
        assertEquals("{system=0, name=张三, id=1, create=2019-04-06 00:00:00, amount=10.14, age=2}", objectObjectMap1.toString());
        String str2 = "{\"age\":2,\"amount\":10.14,\"money\":12.3456,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"b\",\"id\":1555,\"account\":\"s\",\"accountName\":\"张三\",\"role\":\"阿三\"}";
        Student student2 = JsonObjectUtils.fromJson(str2, Student.class);
        Map<String, Object> objectObjectMap2 = JsonObjectUtils.toMapSortByValueAsc(student2);
        // 先排数字，再排字母，最后才是无序的中文
        assertEquals("{system=0, amount=10.14, id=1555, age=2, name=b, account=s, accountName=张三, role=阿三}", objectObjectMap2.toString());
        Map<String, Object> objectObjectMap3 = JsonObjectUtils.toMapSortByValueDesc(student2);
        assertEquals("{role=阿三, accountName=张三, account=s, name=b, age=2, id=1555, amount=10.14, system=0}", objectObjectMap3.toString());
    }

    // 测试获取json中的日期对象
    @Test
    void testGetDate() {
//        Date date = new Date();
//        System.out.println("===>>" + date);
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":\"Wed Dec 21 09:58:33 CST 2021\",\"create2\":\"2021-12-29 09:44:12\",\"id\":1,\"name\":\"张三\",\"create\":\"1625024713000\",\"createDate\":\"null\"}";
        JsonObject parse = JsonObjectUtils.parse(str);
        assertEquals(str, parse.toString());
//        System.out.println(parse);
        assertEquals("2021-06-30 11:45:13", JsonObjectUtils.getDateStr(parse, "create"));
        assertEquals("2021-12-29 09:44:12", JsonObjectUtils.getDateStr(parse, "create2"));
        assertEquals("2021-12-21 09:58:33", JsonObjectUtils.getDateStr(parse, "birthday"));
        assertNull(JsonObjectUtils.getDateStr(parse, "createDate"));
        assertInstanceOf(Date.class, JsonObjectUtils.getDate(parse, "create"));
    }

    // 测试json转list集合
    @Test
    void testArray() {
//        JsonObjects jsonObjects = new JsonObjects();
//        jsonObjects.addProperty("age", 2);
//        jsonObjects.addProperty("amount", 10.14);
//        jsonObjects.addProperty("birthday", "null");
//        jsonObjects.addProperty("create", "2019-04-06");
//        jsonObjects.addProperty("id", "null");
//        jsonObjects.addProperty("name", "张三");
//        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\",\"id\":\"null\"}";
//        JsonObject parse = JsonObjectUtils.parse(str);
//        assertEquals(jsonObjects.toJson(), parse.toString());
//        System.out.println("--1->>" + parse);
        String arr = "[1,2,3,4,5]";
        JsonArray jsonArray1 = new JsonArray();
        jsonArray1.add(1);
        jsonArray1.add(2);
        jsonArray1.add(3);
        jsonArray1.add(4);
        jsonArray1.add(5);
        assertEquals(jsonArray1.toString(), JsonObjectUtils.parseArray(arr).toString());
        assertNull(JsonObjectUtils.parseArray(null));

        String str2 = "[{\"serialNo\":\"1339497989051277312\",\"createBy\":1,\"createDate\":1608196182000,\"updateBy\":\"xxxx\",\"updateDate\":1615444156000,\"name\":\"张三\"}]";
        JsonArray jsonElements = JsonObjectUtils.parseArray(str2);
//        System.out.println("-=4==>>>" + objects);
        List<Student> students = JsonObjectUtils.toArrayList(jsonElements, Student.class);
//        System.out.println("-1->"+students);
//        List<JsonObject> objects = JsonObjectUtils.toArrayList(jsonElements);
//        System.out.println("-1->"+objects);
        List<Object> objects1 = JsonObjectUtils.toArrayList(str2);
        assertEquals(students.size(), objects1.size());
        objects1.forEach(e -> {
            assertInstanceOf(JsonObjects.class, new JsonObjects(e));
            assertEquals(new JsonObjects(e).getString("name"), "张三");
        });
    }

    @Test
    void testMapToJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        JsonObject parse = JsonObjectUtils.parse(map);
        assert parse != null;
        assertEquals("{\"a\":1}", JsonObjectUtils.toJson(parse));
//        System.out.println("---->>" + JsonObjectUtils.toJson(parse));
    }

//    @Test
//    void testForEach() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("a", 1);
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("b_2", "1223_sad");
//        map.put("b", map2);
//        JsonObject parse = JsonObjectUtils.parse(map);
//        assert parse != null;
//        assertEquals();
//        for (Map.Entry<String, JsonElement> entrySet : parse.entrySet()) {
//            System.out.println("key>" + entrySet.getKey());
//            System.out.println("222value>" + entrySet.getValue());
//            System.out.println("--------------------------");
//        }
//    }

    @Test
    void testSingle() throws InterruptedException {
        StringBuffer hashCode = new StringBuffer();
        new Thread(() -> {
//            System.out.println("---4---");
            Gson gson = JsonObjectUtils.getInstance();
            System.out.println("---4->>" + System.identityHashCode(gson));
            int code = System.identityHashCode(gson);
            if (EmptyUtils.isEmpty(hashCode.toString())) {
                hashCode.append(code);
            } else {
                assertEquals(String.valueOf(code), hashCode.toString());
            }
        }).start();
        new Thread(() -> {
//            System.out.println("---5---");
            Gson gson = JsonObjectUtils.getInstance();
            System.out.println("---5->>" + System.identityHashCode(gson));
            int code = System.identityHashCode(gson);
            if (EmptyUtils.isEmpty(hashCode.toString())) {
                hashCode.append(code);
            } else {
                assertEquals(String.valueOf(code), hashCode.toString());
            }
        }).start();
        new Thread(() -> {
//            System.out.println("---6---");
            Gson gson = JsonObjectUtils.getInstance();
            System.out.println("---6->>" + System.identityHashCode(gson));
            int code = System.identityHashCode(gson);
            if (EmptyUtils.isEmpty(hashCode.toString())) {
                hashCode.append(code);
            } else {
                assertEquals(String.valueOf(code), hashCode.toString());
            }
        }).start();
        new Thread(() -> {
            System.out.println("---7---");
            Gson gson = JsonObjectUtils.getInstance();
//            Gson gson = new Gson();
            int code = System.identityHashCode(gson);
            if (EmptyUtils.isEmpty(hashCode.toString())) {
                hashCode.append(code);
            } else {
                assertNotEquals(String.valueOf(code), hashCode.toString());
            }
            System.out.println("---7->>" + System.identityHashCode(gson));
        }).start();

        Thread.sleep(2000);
//        System.out.println("==END==");
    }

    // 测试字符串是否为json
    @Test
    void testIsJson() {
        var str = "{\"age\":1,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1888,\"name\":\"张三\",\"create\":\"16250247130001\"}";
        var str2 = "{code:006,message:测试,age:18,created:2022-03-21 18:02:11,amount:199.88,switchs:true}";
        assertTrue(JsonObjectUtils.isJsonObject(str));
        assertTrue(JsonObjectUtils.isNotJsonObject(str2));
        assertFalse(JsonObjectUtils.isJsonObject(str2));
        assertTrue(JsonObjectUtils.isJsonValid(str));
        assertFalse(JsonObjectUtils.isJsonValid(""));
        assertTrue(JsonObjectUtils.isNotJsonValid(""));

        var array = "[1,2,3,4,5]";
        var array2 = "1,2,3,4,5";
        assertTrue(JsonObjectUtils.isJsonArray(array));
        assertFalse(JsonObjectUtils.isJsonArray(array2));
        assertTrue(JsonObjectUtils.isNotJsonArray(array2));
//        assertFalse(JsonObjectUtils.isNotJsonArray(array));
    }

    // 测试多个json解析，与统计出现次数
    @Test
    void testMultipleJsonStr() {
        String str = "{\n" +
                "\t\"action\":\t\"R\",\n" +
                "\t\"a0f000001\":\t\"1.6.0-DEBUG\",\n" +
                "\t\"a0f000002\":\t\"1234567890\",\n" +
                "\t\"create\":\t\"2020-08-06 09:46:11\",\n" +
                "\t\"a0f000003\":\t\"35353032193936345446537F\",\n" +
                "\t\"a01000005\":\t{\n" +
                "\t\t\"id\":\t\"3759545339380F0028933930\"\n" +
                "\t},\n" +
                "\t\"testObject\":\t{\n" +
                "\t\t\"id\":\t\"3759545339380F0028933930\"\n" +
                "\t},\n" +
                "\t\"a0f000004\":\t\"1659478096000\"\n" +
                "}\n" +
                "\n" +
                "ABCSD" +
                "\n" +
                "{\n" +
                "\t\"action\":\t\"R2\",\n" +
                "\t\"a0f000001\":\t\"1.2.0-DEBUG\",\n" +
                "\t\"a0f000002\":\t\"2-1234567890\",\n" +
                "\t\"a0f000003\":\t\"2-35353032193936345446537F\",\n" +
                "\t\"a0f000004\":\t\"1659478096000\"\n" +
                "}";
        List<Command> commands = JsonObjectUtils.parseMultipleJson(str, Command.class);
//        commands.forEach(System.out::println);
        assertEquals(JsonObjectUtils.parseMultipleJson(str).size(), 2);
//        System.out.println("-list-->>" + JsonObjectUtils.parseMultipleJson(str));
//        System.out.println("-list-null->>" + JsonObjectUtils.parseMultipleJson(str, null));
        assertEquals(JsonObjectUtils.countJson(str), 2);
        assertEquals(JsonObjectUtils.parseMultipleJson(str, null).toString(), JsonObjectUtils.parseMultipleJson(str).toString());

    }
}
