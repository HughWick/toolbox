package com.github.hugh.json;

import com.github.hugh.bean.dto.ResultDTO;
import com.github.hugh.json.exception.ToolboxJsonException;
import com.github.hugh.json.gson.JsonObjectUtils;
import com.github.hugh.json.gson.JsonObjects;
import com.github.hugh.json.model.ContractsDO;
import com.github.hugh.json.model.GsonTest;
import com.github.hugh.util.DateUtils;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * gson JsonObjects测试类
 *
 * @author AS
 */
public class JsonObjectsTest {

    @Test
    void testNewJsonObjects() {
//        String str = "{woman={name=dc, age=1}, name=账上的, sex_in=a,b,d}";
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map.put("name", "账上的");
        map.put("sex_in", "a,b,d");
        map.put("birthday", System.currentTimeMillis());
        map.put("create", "2022-01-10 22:33:10");
        map.put("testList", "1,2,3");
        map2.put("age", 1);
        map2.put("name", "dc");
        map.put("woman", map2);
//        System.out.println("--1-map>>" + map.toString());
        JsonObjects jsonObjects = new JsonObjects(map);
        assertEquals(JsonObjectUtils.toJson(map), jsonObjects.toJson());
//        System.out.println(jsonObjects);
//        Assertions.assertO(new JsonObjects() , jsonObjects);
//        Map<String, String> reconstructedUtilMap = Arrays.stream(str2.split(","))
//                .map(s -> s.split("="))
//                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        //        String str2 = "{birthday=1666145184398, testList=1,2,3, woman={name=dc, age=1}, name=账上的, create=2022-01-10 22:33:10, sex_in=a,b,d}";
//        JsonObjects jsonObjects2 = new JsonObjects(str2);
//        Gson gson = new Gson();
//        Map<String, Object> map3 = new HashMap<>();
//        map3 = gson.fromJson(str, map.getClass());
//        System.out.println("==2==>>" + JSON.parseObject(str2, HashMap.class).toString());
    }

    @Test
    void testStrToMap() {
        String str = "{timestamp=1493114544899, bo/dy={\"name\":\"\u6d4b\u8bd5\u5546\u6237\",\"shop_id\":\"123456\"}, cmd=order.list, source=65504, ticket=C34A0D20-45EC-9C26-CAB8-3DA309213671, encrypt=des.v1, secret=123131243245454534, fields=a|b, version=3.0}";
//        String str = "{birthday=1666145184398, testList=1,2,3, woman={name=dc, age=1}, name=账上的, create=2022-01-10 22:33:10, sex_in=a,b,d}";

//        String httpStr = "{\"birthday\":\"1666149005473\",\"testList\":\"1,2,3\",\"woman\":\"{\"name\":\"dc\",\"age\":1}\",\"name\":\"账上的\",\"create\":\"2022-01-10 22:33:10\",\"sex_in\":\"a,b,d\"}";
        String httpStr = "{birthday=1666149380771, testList=1,2,3, woman={\"name\":\"dc\",\"age\":1}, name=账上的, create=2022-01-10 22:33:10, sex_in=a,b,d}";
        cutSemiString(httpStr);
    }

//    @Test
//    void testHttpRequest() {
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.addParameter("userId", "9001");
//        request.addParameter("name", "狗蛋");
//        request.addParameter("page", "1");
//        request.addParameter("size", "20");
//        Map<String, Object> contentMap = new HashMap<>();
//        contentMap.put("hostSerialNumber", "202010260288");
//        contentMap.put("array", "1,2,3");
//        request.addParameter("content", String.valueOf(contentMap));
//        request.addParameters(contentMap);
//        Map<Object, Object> params = ServletUtils.getParams(request);
//        System.out.println("--1->>"+params);
//        System.out.println("-2-->>"+JsonObjectUtils.toJson(params));
//        TestRequestObject testRequestObject = JSON.parseObject(JSON.toJSONString(params), TestRequestObject.class);
//        TestRequestObject testRequestObject2 = JsonObjectUtils.fromJson(request, TestRequestObject.class);
//        System.out.println("====>>" + testRequestObject);
//    }

    /**
     * use properties to restore the map
     *
     * @param str Input string
     * @return Map
     */
    static public Map<String, String> cutSemiString(String str) {
        //        https://stackoverflow.com/questions/3957094/convert-hashmap-tostring-back-to-hashmap-in-java
        Properties props = new Properties();
        try {
            props.load(new StringReader(str.substring(1, str.length() - 1).replace(", ", "\n")));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Map<String, String> map2 = new HashMap<>();
        for (Map.Entry<Object, Object> e : props.entrySet()) {
            map2.put((String) e.getKey(), (String) e.getValue());
            System.out.println("key:" + e.getKey() + "  value:" + e.getValue());
        }
        return map2;
    }

    // 测试jsonObject get方法
    @Test
    void testGet() {
        String str = "{\"age\":1,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1888,\"name\":\"张三\",\"create\":\"2019-04-06 12:32:11\"}";
        JsonObjects json = new JsonObjects(str);
//        System.out.println(json);
//        System.out.println(json.toJson());
//        System.out.println(json.formJson(Student.class));
        assertEquals("1", json.getString("age"));
        assertEquals("2019-04-06 12:32:11", json.getDateStr("create"));
        assertEquals("2019-04-06", json.getDateStr("create", "yyyy-MM-dd"));
        assertEquals(1888, json.getInt("id"));
        assertEquals(10.14, json.getDoubleValue("amount"));
        String str2 = "{\"age\":2,\"amount\":15.14,\"birthday\":null,\"create\":null,\"id\":null,\"name\":\"张三\",\"create\":\"2019-04-06\"}";
        JsonObjects json2 = new JsonObjects(str2);
        assertEquals("2", json2.getString("age"));
        assertNull(json2.getInteger("id"));
        assertEquals(15.14, json2.getDouble("amount"));
        assertEquals(2, json2.getLong("age"));
        assertEquals(2, json2.getLongValue("age"));
        assertTrue(json2.containsKey("amount"));
    }

    @Test
    void testToMap() {
        String str2 = "{\"age\":2,\"amount\":15.14,\"birthday\":\"today\",\"create\":\"2022-10-15\",\"id\":12345,\"name\":\"张三\",\"create\":\"2019-04-06\",\"method\":\"wow\"}";
        JsonObjects json2 = new JsonObjects(str2);
        assertEquals("{age=2, amount=15.14, birthday=today, create=2019-04-06, id=12345, name=张三, method=wow}", json2.toMap().toString());
        String str3 = "{\"birthday\":\"today\",\"create\":\"2022-10-15\",\"amount\":15.14,\"id\":12345,\"name\":\"张三\",\"create\":\"2019-04-06\",\"method\":\"wow\",\"age\":2,\"yoh\":\"叶王\",\"valve\":\"冰娃\"}";
        String ascStr = "{age=2, amount=15.14, birthday=today, create=2019-04-06, id=12345, method=wow, name=张三, valve=冰娃, yoh=叶王}";
        assertEquals(ascStr, new JsonObjects(str3).toMapSortByKeyAsc().toString());
        String descStr = "{yoh=叶王, valve=冰娃, name=张三, method=wow, id=12345, create=2019-04-06, birthday=today, amount=15.14, age=2}";
        assertEquals(descStr, new JsonObjects(str3).toMapSortByKeyDesc().toString());

    }

    // 测试json 为空
    @Test
    void testIsNull() {
        String str = "{}";
        JsonObjects json = new JsonObjects(str);
        assertTrue(json.isNull());
        assertTrue(json.isNotNull());
    }

    // 多层
    @Test
    void testMultilayer() {
        String ip1 = "8.8.8.8";
        String ip2 = "8.8.4.4";
        String ip3 = "156.154.70.1";
        String ip4 = "156.154.71.1";
        String arryStr = "{ " +
                "\"client\":\"127.0.0.1\"," +
                "\"servers\":[" +
                "    \"" + ip1 + "\"," +
                "    \"" + ip2 + "\"," +
                "    \"" + ip3 + "\"," +
                "    \"" + ip4 + "\" " +
                "    ]}";
        JsonObjects json = new JsonObjects(arryStr);
        JsonArray servers = json.getJsonArray("servers");
//        ArrayList<String> arrayList = Lists.newArrayList(ip1, ip2, ip3, ip4);
        assertEquals("[\"8.8.8.8\", \"8.8.4.4\", \"156.154.70.1\", \"156.154.71.1\"]", servers.asList().toString());

        String two = "{\n" +
                "    \"code\": \"0000\",\n" +
                "    \"message\": \"操作成功\",\n" +
                "    \"data\": {\n" +
                "      \"id\": 1,\n" +
                "      \"serialNo\": \"1353966399112417280\",\n" +
                "      \"original\": \"112.941375,28.223073\",\n" +
                "      \"coordinateSystem\": \"gps\",\n" +
                "      \"result\": \"112.946831597223,28.21957139757\",\n" +
                "      \"createBy\": null,\n" +
                "      \"createDate\": 1611645720000,\n" +
                "      \"updateBy\": null,\n" +
                "      \"updateDate\": null,\n" +
                "      \"deleteBy\": null,\n" +
                "      \"deleteDate\": null,\n" +
                "      \"deleteFlag\": 0\n" +
                "    }\n" +
                "  }";
        JsonObjects json2 = new JsonObjects(two);
//        JsonObjects data = json2.jsonObjects("data");
        JsonObjects data2 = json2.getThis("data");
        assertNull(data2.getDate("updateDate"));
        assertEquals(1, data2.getLong("id"));
//        System.out.println("---<>>>" + data2);
//        System.out.println("---1>>" + data.getString("id"));
//        System.out.println("--2->>" + json2.getJsonObject("data"));
//        System.out.println("--3->>" + new JsonObjects(json2.getJsonObject("data")));
    }

    // 解析复杂类型
    @Test
    void testReal() {
        String str = "{\"code\":\"0000\",\"int2\":1,\"data\":{\"hostSerialNumber\":\"202010260288\",\"networkType\":\"iot\",\"readIdList\":[\"000f0009\",\"000f0002\",\"000f0001\"]," +
                "\"resultList\":[{\"action\":\"W\",\"commandId\":\"000f0001\",\"commandKey\":\"GPRS_TIMED_SEND_DATA_TIME\",\"commandName\":\"GPRS-定时发送数据时间\",\"remake\":\"0|W||0006\",\"type\":0,\"unit\":\"S\",\"value\":\"0006\",\"longValue\":\"12387643876872367867326476\",\"doubleValue\":\"123.321\"}," +
                "{\"action\":\"W\",\"commandId\":\"000f0002\",\"commandKey\":\"GPRS_HEART_BEAT_TIME\",\"commandName\":\"GPRS-网络心跳包时间\",\"remake\":\"0|W||0006\",\"type\":0,\"unit\":\"S\",\"value\":\"0006\"}," +
                "{\"action\":\"W\",\"commandId\":\"000f0009\",\"commandKey\":\"GPRS_TIMED_READING_OF_POSITIONING_INFORMATION\",\"commandName\":\"GPRS-定时读取定位信息的时间\",\"remake\":\"0|W||0006\",\"type\":2,\"unit\":\"S\",\"value\":\"0006\"}]},\"message\":\"异步信息回调成功\"}";
        ResultDTO resultDTO = JsonObjectUtils.fromJson(str, ResultDTO.class);
        assertEquals("0000", resultDTO.getCode());
        JsonObjects jsonObjects = new JsonObjects(JsonObjectUtils.toJson(resultDTO.getData()));
        assertEquals(4, jsonObjects.size());
        JsonArray resultList = jsonObjects.getJsonArray("resultList");
        assertEquals(3, resultList.size());
    }

    // 测试添加属性
    @Test
    void testAddProperty() {
        String jsonStr = "{\"string\":\"111\",\"int\":22,\"double\":12.34,\"long\":12345678998123124,\"boolean\":true}";
        JsonObjects jsonObjects = new JsonObjects();
        jsonObjects.addProperty("string", "111")
                .addProperty("int", 22)
                .addProperty("double", 12.34)
                .addProperty("long", 12345678998123124L)
                .addProperty("boolean", true);
        assertEquals(jsonStr, jsonObjects.toJson());
        assertEquals(5, jsonObjects.size());
        assertFalse(jsonObjects.isEquals("a", "s"));
        assertTrue(jsonObjects.isEquals("string", "111"));
        assertTrue(jsonObjects.isNotEquals("string", "11122"));
        assertNull(jsonObjects.removeProperty("b"));
        assertThrowsExactly(ToolboxJsonException.class, () -> jsonObjects.addProperty("List", new ArrayList<>()));
        assertThrowsExactly(ToolboxJsonException.class, () -> jsonObjects.addProperty("map", new HashMap<>()));
        assertThrowsExactly(ToolboxJsonException.class, () -> jsonObjects.addProperty("JsonObject", new JsonObject()));
    }

    @Test
    void testToList() {
        String str = "{\"list\":[{\"code\":\"THXX-HTDJ-20220322-01\",\"createBy\":\"童娟\",\"createDate\":1647910685000,\"dateOfSigning\":\"2022-03-22\",\"deleteBy\":\"\",\"deleteFlag\":0,\"id\":37,\"installationAddress\":\"\",\"name\":\"研发出库（广西大化）合同\",\"partyA\":\"\",\"partyB\":\"\",\"projectName\":\"研发出库（广西大化）合同\",\"serialNo\":\"1506072686843924480\",\"updateBy\":\"\",\"useSide\":\"2022-03-21 18:02:11\"}," +
                "{\"code\":\"THXX-HTDJ-202203022\",\"createBy\":\"肖正\",\"createDate\":1646277296000,\"dateOfSigning\":\"\",\"deleteBy\":\"\",\"deleteFlag\":0,\"id\":36,\"installationAddress\":\"\",\"name\":\"广西大化产品增补合同\",\"partyA\":\"\",\"partyB\":\"\",\"projectName\":\"广西大化产品增补\",\"serialNo\":\"1499221758891266048\",\"updateBy\":\"\",\"useSide\":null}]}";
        ContractsDO contractsDO1 = new ContractsDO();
        contractsDO1.setCode("THXX-HTDJ-20220322-01");
        contractsDO1.setName("研发出库（广西大化）合同");
        contractsDO1.setProjectName("研发出库（广西大化）合同");
        contractsDO1.setPartyA("");
        contractsDO1.setPartyB("");
        contractsDO1.setUseSide(DateUtils.parse("2022-03-21 18:02:11"));
        contractsDO1.setDateOfSigning("2022-03-22");
        contractsDO1.setInstallationAddress("");
        contractsDO1.setCreateDate(DateUtils.parseTimestamp(1647910685000L));
        ContractsDO contractsDO2 = new ContractsDO();
        contractsDO2.setCode("THXX-HTDJ-202203022");
        contractsDO2.setName("广西大化产品增补合同");
        contractsDO2.setProjectName("广西大化产品增补");
        contractsDO2.setPartyA("");
        contractsDO2.setPartyB("");
        contractsDO2.setUseSide(null);
        contractsDO2.setDateOfSigning("");
        contractsDO2.setInstallationAddress("");
        contractsDO2.setCreateDate(DateUtils.parseTimestamp(1646277296000L));
        ArrayList<ContractsDO> contractsDOS = Lists.newArrayList(contractsDO1, contractsDO2);
//        Lists.newArrayList(contractsDO1, contractsDO2);
        List<ContractsDO> list = new JsonObjects(str).toList("list", ContractsDO.class);
        assertEquals(contractsDOS.size(), list.size());
//        System.out.println("--1->>"+Lists.newArrayList(contractsDO1, contractsDO2).toString());
//        System.out.println("2--->>"+list.toString());
        assertEquals(contractsDOS.toString(), list.toString());
//        assertArrayEquals(Lists.newArrayList(contractsDO1, contractsDO2), list);
//        list.forEach(e -> {
//            String s1 = JsonObjectUtils.toJson(e);
//            System.out.println(new JsonObjects(s1).formJson(ContractsDO.class));
//            System.out.println(new JsonObjects(s1).formJson(ContractsDO.class, DateCode.YEAR_MONTH_DAY));
//            System.out.println( new JsonObjects(s1).formJson(ContractsDO.class));
//        });
        List<LinkedTreeMap> jsonArray2 = new JsonObjects(str).toList("list");
        assertEquals(contractsDOS.size(), jsonArray2.size());
//        for (int i = 0; i < jsonArray2.size(); i++) {
//            assertEquals(JsonObjectUtils.toJson(jsonArray2.get(i)), JsonObjectUtils.toJson(contractsDOS.get(i)));
//        }
//        jsonArray2.forEach(e -> {
//            assertEquals(JsonObjectUtils.toJson(e) , );
//        });
//        List<ContractsDO> list1 = new JsonObjects(resultDTO.getData()).toList("list", ContractsDO.class);
//        list1.forEach(System.out::println);
//        System.out.println("---2>>" + jsonArray2);
    }

    // 测试字符串转list
    @Test
    void testToList2() {
        String str = "[{\"action\":\"84\",\"commandKey\":\"ELECTRICAL_ENERGY_DATA_VOLTAGE\",\"commandId\":\"01010001\",\"commandName\":\"电能数据_电压\",\"value\":\"235.45\",\"type\":2,\"unit\":\"V\",\"remake\":\"00005bf9\"}," +
                "{\"action\":\"84\",\"commandKey\":\"ELECTRICAL_ENERGY_DATA_POWER\",\"commandId\":\"01010002\",\"commandName\":\"电能数据_功率\",\"value\":\"1.189\",\"type\":2,\"unit\":\"kW\",\"remake\":\"000004a5\"}," +
                "{\"action\":\"84\",\"commandKey\":\"ELECTRICAL_ENERGY_DATA_CURRENT\",\"commandId\":\"01010003\",\"commandName\":\"电能数据_电流\",\"value\":\"5.083\",\"type\":2,\"unit\":\"A\",\"remake\":\"000013db\"},{\"action\":\"84\",\"commandKey\":\"ELECTRICAL_ENERGY_DATA_TOTAL_ACTIVE_ENERGY\",\"commandId\":\"01010010\",\"commandName\":\"电能数据_有功总电能\",\"value\":\"9.99\",\"type\":2,\"unit\":\"kwh\",\"remake\":\"000003e7\"},{\"action\":\"84\",\"commandKey\":\"INTERNAL_TEMPERATURE\",\"commandId\":\"01020001\",\"commandName\":\"ADC数据-内部温度\",\"value\":\"60.4\",\"type\":2,\"unit\":\"C\",\"remake\":\"0000025c\"}," +
                "{\"action\":\"84\",\"commandKey\":\"BATTERY_VOLTAGE\",\"commandId\":\"01020002\",\"commandName\":\"ADC数据-电池电压\",\"value\":\"4.15\",\"type\":2,\"unit\":\"V\",\"remake\":\"019f\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_1_VOLTAGE\",\"commandId\":\"01020003\",\"commandName\":\"ADC数据-直流通道1电压\",\"value\":\"12.08\",\"type\":2,\"unit\":\"V\",\"remake\":\"04b8\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_1_CURRENT\",\"commandId\":\"01020004\",\"commandName\":\"ADC数据-直流通道1电流\",\"value\":\"0\",\"type\":2,\"unit\":\"mA\",\"remake\":\"0000\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_2_VOLTAGE\",\"commandId\":\"01020005\",\"commandName\":\"ADC数据-直流通道2电压\",\"value\":\"12.08\",\"type\":2,\"unit\":\"V\",\"remake\":\"04b8\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_2_CURRENT\",\"commandId\":\"01020006\",\"commandName\":\"ADC数据-直流通道2电流\",\"value\":\"0\",\"type\":2,\"unit\":\"mA\",\"remake\":\"0000\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_3_VOLTAGE\",\"commandId\":\"01020007\",\"commandName\":\"ADC数据-直流通道3电压\",\"value\":\"12.09\",\"type\":2,\"unit\":\"V\",\"remake\":\"04b9\"}," +
                "{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_3_CURRENT\",\"commandId\":\"01020008\",\"commandName\":\"ADC数据-直流通道3电流\",\"value\":\"0\",\"type\":2,\"unit\":\"mA\",\"remake\":\"0000\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_4_VOLTAGE\",\"commandId\":\"01020009\",\"commandName\":\"ADC数据-直流通道4电压\",\"value\":\"0.22\",\"type\":2,\"unit\":\"V\",\"remake\":\"0016\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_4_CURRENT\",\"commandId\":\"0102000a\",\"commandName\":\"ADC数据-直流通道4电流\",\"value\":\"0\",\"type\":2,\"unit\":\"mA\",\"remake\":\"0000\"},{\"action\":\"84\",\"commandKey\":\"NET_NODE_STATUS\",\"commandId\":\"01020019\",\"commandName\":\"ADC数据-Net节点状态\",\"value\":\"0000000000111111\",\"type\":2,\"unit\":\"\",\"remake\":\"003f\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_1_RELAY_ACTUAL_STATE\",\"commandId\":\"010f0005\",\"commandName\":\"交流通道1继电器实际状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_2_RELAY_ACTUAL_STATE\",\"commandId\":\"010f0006\",\"commandName\":\"交流通道2继电器实际状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_3_RELAY_ACTUAL_STATE\",\"commandId\":\"010f0007\",\"commandName\":\"交流通道3继电器实际状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_4_RELAY_ACTUAL_STATE\",\"commandId\":\"010f0008\",\"commandName\":\"交流通道4继电器实际状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_1_STATUS\",\"commandId\":\"0102000f\",\"commandName\":\"ADC数据-直流通道1状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_2_STATUS\",\"commandId\":\"01020010\",\"commandName\":\"ADC数据-直流通道2状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_3_STATUS\",\"commandId\":\"01020011\",\"commandName\":\"ADC数据-直流通道3状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_4_STATUS\",\"commandId\":\"01020012\",\"commandName\":\"ADC数据-直流通道4状态\",\"value\":\"2\",\"type\":2,\"unit\":\"\",\"remake\":\"02\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_5_STATUS\",\"commandId\":\"01020013\",\"commandName\":\"ADC数据-直流通道5状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"DC_CHANNEL_6_STATUS\",\"commandId\":\"01020014\",\"commandName\":\"ADC数据-直流通道6状态\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"INPUT_SIGNAL_1_STATUS\",\"commandId\":\"010f0001\",\"commandName\":\"输入信号1状态（开关门状态）\",\"value\":\"0\",\"type\":2,\"unit\":\"\",\"remake\":\"00\"},{\"action\":\"84\",\"commandKey\":\"GPRS_SIGNAL_STRENGTH\",\"commandId\":\"01050001\",\"commandName\":\"GPRS数据-信号强度\",\"value\":\"24\",\"type\":2,\"unit\":\"\",\"remake\":\"24\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_1_RMS_CURRENT\",\"commandId\":\"10000001\",\"commandName\":\"交流通道1电流有效值\",\"value\":\"4.672\",\"type\":2,\"unit\":\"A\",\"remake\":\"00001240\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_2_RMS_CURRENT\",\"commandId\":\"10000002\",\"commandName\":\"交流通道2电流有效值\",\"value\":\"0.16\",\"type\":2,\"unit\":\"A\",\"remake\":\"000000a0\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_3_RMS_CURRENT\",\"commandId\":\"10000003\",\"commandName\":\"交流通道3电流有效值\",\"value\":\"0.075\",\"type\":2,\"unit\":\"A\",\"remake\":\"0000004b\"},{\"action\":\"84\",\"commandKey\":\"AC_CHANNEL_4_RMS_CURRENT\",\"commandId\":\"10000004\",\"commandName\":\"交流通道4电流有效值\",\"value\":\"0.153\",\"type\":2,\"unit\":\"A\",\"remake\":\"00000099\"}]";
//        String s = JsonObjectUtils.toJson(str);
//        assertEquals(str , s);
        JsonArray jsonArray = JsonObjectUtils.parseArray(str);
        assertNotNull(jsonArray);
        assertEquals(31, jsonArray.size());
        jsonArray.forEach(e -> {
            assertInstanceOf(JsonObjects.class, new JsonObjects(e));
        });
    }

    // 转换无双引号的字符串
    @Test
    void testFromSimpleJsonStr() {
        var str1 = "{code:006,message:测试,age:18,created:2022-03-21\t18:02:11,amount:199.88,switchs:true}";
        assertThrowsExactly(JsonSyntaxException.class, () -> {
            new JsonObjects(str1).toJson();
        });
        String jsonString = "{\"code\":\"006\",\"message\":\"测试\",\"age\":18,\"amount\":199.88,\"switchs\":true,\"created\":\"2021-06-30 11:45:13\"}";
//        System.out.println(new JsonObjects(str1).toJson());
        // 能转换成实体，但不是能转换成json
        var str = "{code:006,message:测试,age:18,created:1625024713000,amount:199.88,switchs:true}";
//        System.out.println(JsonObjectUtils.toJson(str));
        GsonTest gsonTest = new JsonObjects(str).formJson(GsonTest.class);
        assertEquals(jsonString, new JsonObjects(gsonTest).toJson());
//        System.out.println("-1-->>" + jsonObjects.toJson());
//        System.out.println("-2-->>" + jsonObjects.formJson(GsonTest.class));
//        var str2 = "{\"code\":\"006\",\"message\":\"测试\",\"age\":\"18\",\"created\":\"2022-03-21 18:02:11\",\"amount\":\"199.88\",\"switchs\":\"true\"}";
//        System.out.println("=3===>>" + new JsonObjects(str2).formJson(GsonTest.class));
//        System.out.println("=4===>>" + new JsonObjects(str2).formJson(GsonTest.class));
    }

    @Test
    void testEntrySet() {
//        var str2 = "{code:006,message:测试,age:18,created:16250247130001,amount:199.88,switchs:true}";
//        System.out.println(new JsonObjects(str2).toJson());
        var str3 = "{\"code\":\"006\",\"message\":\"测试\",\"age\":\"18\",\"created\":\"2022-03-21 18:02:11\",\"amount\":\"199.88\",\"switchs\":\"true\"}";
        JsonObjects jsonObjects = new JsonObjects(str3);
        Set<Map.Entry<String, JsonElement>> entries = JsonObjectUtils.parse(str3).entrySet();
        assertEquals(jsonObjects.entrySet(), entries);
//        for (Map.Entry<String, JsonElement> entrySet : jsonObjects.entrySet()) {
//            System.out.println("===>>" + entrySet.getKey());
//        }
    }

    // 测试解析日期对象
    @Test
    void testDate() {
        String str = "{\"create\":\"16250247130001\"}";
        JsonObjects jsonObjects1 = new JsonObjects();
        jsonObjects1.addProperty("create", "16250247130001");
        assertEquals(jsonObjects1.toJson(), new JsonObjects(str).toJson());
//        System.out.println(new JsonObjects(str).toJson());
        String str2 = "{\"age\":2,\"amount\":15.14,\"birthday\":null,\"name\":\"张三\",\"create\":\"2022-03-21 18:02:11\"}";
        JsonObjects jsonObjects2 = new JsonObjects()
                .addProperty("age", 2)
                .addProperty("amount", 15.14)
                .addProperty("name", "张三")
                .addProperty("create", "2022-03-21 18:02:11");
        assertEquals(jsonObjects2.toJson(), new JsonObjects(str2).toJson());
        String str3 = "{\"age\":2,\"amount\":15.14,\"name\":\"张三\",\"create\":2022-03-21}";
        JsonObjects jsonObjects3 = new JsonObjects()
                .addProperty("age", 2)
                .addProperty("amount", 15.14)
                .addProperty("name", "张三")
                .addProperty("create", "2022-03-21");
        assertEquals(jsonObjects3.toJson(), new JsonObjects(str3).toJson());
    }

    @Test
    void testIsEmptyValue() {
        String str = "{\"age\":1,\"amount\":10.14,\"birthday\":null,\"createBy\":null,\"id\":1888,\"name\":\"张三\",\"create\":\"16250247130001\"}";
        assertTrue(new JsonObjects(str).isEmptyValue("createBy"));
        assertFalse(new JsonObjects(str).isEmptyValue("age"));
        assertTrue(new JsonObjects(str).isNotEmptyValue("amount"));
        assertFalse(new JsonObjects(str).isNotEmptyValue("createBy"));
    }

    @Test
    void testArray() {
//        String str3 = "{\"age\":2,\"amount\":15.14,\"birthday\":null}";
        String str = "{\n" +
                "\t\"action\":\t\"R\",\n" +
                "\t\"00100001\":\t[\"00010000\"],\n" +
                "\t\"00100002\":\t[\"01010000\", \"02030000\"],\n" +
                "\t\"00100003\":\t[\"01010009\"],\n" +
                "\t\"00100004\":\t[\"0\"],\n" +
                "\t\"00100005\":\t{\"age\":2,\"amount\":15.14,\"birthday\":null}\n" +
                "}";
        JsonObjects jsonObjects = new JsonObjects(str);
        String jsonString = "{\"action\":\"R\",\"00100001\":[\"00010000\"],\"00100002\":[\"01010000\",\"02030000\"],\"00100003\":[\"01010009\"],\"00100004\":[\"0\"],\"00100005\":{\"age\":2,\"amount\":15.14}}";
        assertEquals(jsonString, jsonObjects.toJson());
//        System.out.println(jsonObjects);
        for (Map.Entry<String, JsonElement> entries : jsonObjects.entrySet()) {
//            System.out.println(entries.getKey() + "=--------" + JsonObjectUtils.getAsString(entries.getValue()));
        }
    }
}