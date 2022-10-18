package com.github.hugh.json;

import com.alibaba.fastjson.JSON;
import com.github.hugh.bean.dto.ResultDTO;
import com.github.hugh.constant.DateCode;
import com.github.hugh.json.exception.ToolboxJsonException;
import com.github.hugh.json.gson.JsonObjectUtils;
import com.github.hugh.json.gson.JsonObjects;
import com.github.hugh.json.model.GsonTest;
import com.github.hugh.json.model.Student;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Data;
import org.junit.jupiter.api.Test;

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
        String str = "{woman={name=dc, age=1}, name=账上的, sex_in=a,b,d}";
        JsonObjects jsonObjects = new JsonObjects(str);

    }

    @Test
    void testGet() {
        String str = "{\"age\":1,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1888,\"name\":\"张三\",\"create\":\"2019-04-06 12:32:11\"}";
        String str2 = "{\"age\":2,\"amount\":15.14,\"birthday\":null,\"create\":null,\"id\":null,\"name\":\"张三\",\"create\":\"2019-04-06\"}";
        JsonObjects json = new JsonObjects(str);
        JsonObjects json2 = new JsonObjects(str2);
        System.out.println(json);
        System.out.println(json.toJson());
        System.out.println(json.formJson(Student.class));
        assertEquals(json.getString("age"), "1");
        assertEquals(json.getDateStr("create"), "2019-04-06 12:32:11");
//        System.out.println("-->"+json.getDateStr("create", "yyyy-MM-dd"));
        assertEquals(json.getDateStr("create", "yyyy-MM-dd"), "2019-04-06");
//        assertEquals(json.getDate("create") , Date.class);
        System.out.println("--->" + json.getDate("create"));
//        assertEquals(json.getDateStr("create") , "2019-04-06 12:32:11");
        System.out.println("---2->>>" + json.getInt("id"));
        System.out.println("---3->>>" + json.getDoubleValue("amount"));
        System.out.println("--4-->>>" + json2.getString("age"));
        System.out.println("--5-->>>" + json2.getInteger("id"));
        System.out.println("--6-->>>" + json2.getDouble("amount"));
        System.out.println("--7-->>>" + json2.getLong("age"));
        System.out.println("--8-->>>" + json2.getLongValue("age"));
        System.out.println("--9-->>>" + json2.containsKey("amount"));
        System.out.println("----NED----");
//        String str2 = "{\"serialNo\":null,\"createBy\":null,\"createDate\":null,\"updateBy\":null,\"updateDate\":null,\"deleteBy\":null,\"deleteDate\":null,\"deleteFlag\":0," +
//                "\"id\":81,\"ip\":null,\"zclxmc2\":\"网络交换传输\",\"cjmc\":null,\"sxxsj\":null,\"zxzts\":null,\"ywzjbdsl\":0,\"tdmc\":null,\"szs\":null,\"laniotzt\":null,\"ywzjjcsj\":null,\"gdzt\":null,\"tbzt\":0,\"txmm\":null,\"htmc\":null,\"kkmc\":null,\"fxmc\":null,\"bdzcjh\":null,\"dzjcmc\":null,\"ywzjmc\":null,\"ppmc\":\"海康\",\"zclxmc\":\"防火墙\",\"zclxmc1\":null,\"ywzjzt\":null,\"ssq_NO\":\"43010407\",\"sfmc\":\"\",\"csmc\":\"\",\"qxmc\":\"\",\"xzjdmc\":\"\",\"sbxlh\":\"123123123\",\"iccid\":null,\"zzzp\":null,\"rkdjr_ID\":null,\"bdzt\":0,\"ywzjbd_ID\":0,\"zcmc\":\"测试资产\",\"kkbd_ID\":null,\"dzjcbd_ID\":null,\"zclx\":26,\"cds\":null,\"xh\":\"0021\",\"sf\":\"430000\",\"cs\":\"430100\",\"qx\":\"430104\",\"cj\":\"1\",\"pp_ID\":1,\"sydw\":1,\"xqbm\":\"43010407\",\"xzjd\":\"43010407\",\"gldk\":\"\",\"rksj\":1591175764000,\"zxzt\":0,\"gbbm\":\"\",\"fx_ID\":0,\"zczt\":\"1\",\"xxdz\":\"\"" +
//                ",\"jd\":\"\",\"wd\":\"\",\"dlzh\":\"\",\"dlmm\":\"\",\"ccrq\":null,\"bxq\":1,\"sbbm\":\"\",\"imei\":null,\"ht_ID\":0,\"cjdw\":0,\"ywdw\":0,\"azrq\":null}";
    }

    @Test
    void testToMap() {
        String str2 = "{\"age\":2,\"amount\":15.14,\"birthday\":\"today\",\"create\":\"2022-10-15\",\"id\":12345,\"name\":\"张三\",\"create\":\"2019-04-06\",\"method\":\"wow\"}";
        JsonObjects json2 = new JsonObjects(str2);
//        System.out.println("--10-->>>" + json2.toMap());
        assertEquals("{age=2, amount=15.14, birthday=today, create=2019-04-06, id=12345, name=张三, method=wow}", json2.toMap().toString());
        String str3 = "{\"birthday\":\"today\",\"create\":\"2022-10-15\",\"amount\":15.14,\"id\":12345,\"name\":\"张三\",\"create\":\"2019-04-06\",\"method\":\"wow\",\"age\":2,\"yoh\":\"叶王\",\"valve\":\"冰娃\"}";
        String ascStr = "{age=2, amount=15.14, birthday=today, create=2019-04-06, id=12345, method=wow, name=张三, valve=冰娃, yoh=叶王}";
        assertEquals(ascStr, new JsonObjects(str3).toMapSortByKeyAsc().toString());
        String descStr = "{yoh=叶王, valve=冰娃, name=张三, method=wow, id=12345, create=2019-04-06, birthday=today, amount=15.14, age=2}";
//        System.out.println("--10-->>>" + new JsonObjects(str3).toMapSortByKeyDesc());
        assertEquals(descStr, new JsonObjects(str3).toMapSortByKeyDesc().toString());

    }

    @Test
    void testIsNull() {
        String str = "{}";
        JsonObjects json = new JsonObjects(str);
        System.out.println("--1-->>" + json.isNull());
        System.out.println("--2-->>" + json.isNotNull());
//        String str2 = null;
//        JsonObjects json2 = new JsonObjects(str2);
//        System.out.println("--3-->>" + json2.isNotNull());
    }

    @Test
    public void test03() {
        String arryStr = "{ " +
                "\"client\":\"127.0.0.1\"," +
                "\"servers\":[" +
                "    \"8.8.8.8\"," +
                "    \"8.8.4.4\"," +
                "    \"156.154.70.1\"," +
                "    \"156.154.71.1\" " +
                "    ]}";
        JsonObjects json = new JsonObjects(arryStr);
        JsonArray servers = json.getJsonArray("servers");
        System.out.println("--->>" + servers);

        String two = "{\n" +
                "    \"code\": \"0000\",\n" +
                "    \"message\": \"操作成功\",\n" +
                "    \"data\": {\n" +
                "      \"id\": 1,\n" +
                "      \"serialNo\": \"1353966399112417280\",\n" +
                "      \"original\": \"112.941375,28.223073\",\n" +
                "      \"coordinateSystem\": \"gps\",\n" +
                "      \"result\": \"112.946831597223,28.21957139757\",// 转换结果\n" +
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
        System.out.println("---<>>>" + data2);
//        System.out.println("---1>>" + data.getString("id"));
        System.out.println("--2->>" + json2.getJsonObject("data"));
        System.out.println("--3->>" + new JsonObjects(json2.getJsonObject("data")));
    }

    @Test
    public void test04() {
        String str = "{\"code\":\"0000\",\"int2\":1,\"data\":{\"hostSerialNumber\":\"202010260288\",\"networkType\":\"iot\",\"readIdList\":[\"000f0009\",\"000f0002\",\"000f0001\"]," +
                "\"resultList\":[{\"action\":\"W\",\"commandId\":\"000f0001\",\"commandKey\":\"GPRS_TIMED_SEND_DATA_TIME\",\"commandName\":\"GPRS-定时发送数据时间\",\"remake\":\"0|W||0006\",\"type\":0,\"unit\":\"S\",\"value\":\"0006\",\"longValue\":\"12387643876872367867326476\",\"doubleValue\":\"123.321\"}," +
                "{\"action\":\"W\",\"commandId\":\"000f0002\",\"commandKey\":\"GPRS_HEART_BEAT_TIME\",\"commandName\":\"GPRS-网络心跳包时间\",\"remake\":\"0|W||0006\",\"type\":0,\"unit\":\"S\",\"value\":\"0006\"}," +
                "{\"action\":\"W\",\"commandId\":\"000f0009\",\"commandKey\":\"GPRS_TIMED_READING_OF_POSITIONING_INFORMATION\",\"commandName\":\"GPRS-定时读取定位信息的时间\",\"remake\":\"0|W||0006\",\"type\":2,\"unit\":\"S\",\"value\":\"0006\"}]},\"message\":\"异步信息回调成功\"}";
        ResultDTO resultDTO1 = JSON.parseObject(str, ResultDTO.class);
        System.out.println("-fastjson->>" + resultDTO1);
//        ResultDTO resultDTO = JsonObjectUtils.fromJson(str, ResultDTO.class);
//        System.out.println("--gson直转实体->" + resultDTO);
//        JsonObjects jsonObjects = new JsonObjects(JsonObjectUtils.toJson(resultDTO.getData()));
//        System.out.println(jsonObjects.toJson());
//        JsonArray resultList = jsonObjects.getJsonArray("resultList");
//        for (JsonElement jsonElement : resultList) {
//            testCommand testCommand = JsonObjectUtils.fromJson(new JsonObjects(jsonElement).toJson(), testCommand.class);
//            System.out.println(testCommand);
//        }
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(new TypeToken<ResultDTO>() {
//        }.getType(), new MapDeserializerDoubleAsIntFix());
//        Gson gson = gsonBuilder.create();
//        ResultDTO o = gson.fromJson(str, new TypeToken<ResultDTO>() {
//        }.getType());
//        System.out.println("==gson自定义转换实体解析提=>?>>" +o);
    }

    @Test
    public void testAddProperty() {
        JsonObjects jsonObjects = new JsonObjects();
        jsonObjects.addProperty("string", "111");
        jsonObjects.addProperty("int", 22);
        jsonObjects.addProperty("double", 12.34);
        jsonObjects.addProperty("long", 12345678998123124L);
        jsonObjects.addProperty("boolean", true);
        System.out.println("--toJson->" + jsonObjects.toJson());
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
                "{\"code\":\"THXX-HTDJ-202203021\",\"createBy\":\"肖正\",\"createDate\":1646277296000,\"dateOfSigning\":\"\",\"deleteBy\":\"\",\"deleteFlag\":0,\"id\":36,\"installationAddress\":\"\",\"name\":\"广西大化产品增补合同\",\"partyA\":\"\",\"partyB\":\"\",\"projectName\":\"广西大化产品增补\",\"serialNo\":\"1499221758891266048\",\"updateBy\":\"\",\"useSide\":\"\"}]}";
        List<ContractsDO> list = new JsonObjects(str).toList("list", ContractsDO.class);
        list.forEach(e -> {
            String s1 = JsonObjectUtils.toJson(e);
            System.out.println(new JsonObjects(s1).formJson(ContractsDO.class));
            System.out.println(new JsonObjects(s1).formJson(ContractsDO.class, DateCode.YEAR_MONTH_DAY));
//            System.out.println( new JsonObjects(s1).formJson(ContractsDO.class));
        });
        List<LinkedTreeMap> jsonArray2 = new JsonObjects(str).toList("list");
        jsonArray2.forEach(System.out::println);
//        List<ContractsDO> list1 = new JsonObjects(resultDTO.getData()).toList("list", ContractsDO.class);
//        list1.forEach(System.out::println);
//        System.out.println("---2>>" + jsonArray2);
    }

    @Test
    void testFromJsonObject() {
//        var str1 = "{code:006,message:测试,age:18,created:2022-03-21 18:02:11,amount:199.88,switchs:true}";
//        System.out.println(new JsonObjects(str1).toJson());
        var str = "{code:006,message:测试,age:18,created:1625024713000,amount:199.88,switchs:true}";
        JsonObjects jsonObjects = new JsonObjects(str);
        System.out.println("-1-->>" + jsonObjects.toJson());
        System.out.println("-2-->>" + jsonObjects.fromJsonTimeStamp(GsonTest.class));
        var str2 = "{\"code\":\"006\",\"message\":\"测试\",\"age\":\"18\",\"created\":\"2022-03-21 18:02:11\",\"amount\":\"199.88\",\"switchs\":\"true\"}";
        System.out.println("=3===>>" + new JsonObjects(str2).formJson(GsonTest.class));
        System.out.println("=4===>>" + new JsonObjects(str2).formJson(GsonTest.class));
    }

    @Test
    void testEntrySet() {
        var str2 = "{code:006,message:测试,age:18,created:16250247130001,amount:199.88,switchs:true}";
        System.out.println(new JsonObjects(str2).toJson());
        var str3 = "{\"code\":\"006\",\"message\":\"测试\",\"age\":\"18\",\"created\":\"2022-03-21 18:02:11\",\"amount\":\"199.88\",\"switchs\":\"true\"}";
        JsonObjects jsonObjects = new JsonObjects(str3);
        for (Map.Entry<String, JsonElement> entrySet : jsonObjects.entrySet()) {
            System.out.println("===>>" + entrySet.getKey());
        }
    }

    @Test
    void testDate() {
        String str = "{\"age\":1,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1888,\"name\":\"张三\",\"create\":\"16250247130001\"}";
        System.out.println(new JsonObjects(str).toJson());
        String str2 = "{\"age\":2,\"amount\":15.14,\"birthday\":null,\"create\":null,\"id\":null,\"name\":\"张三\",\"create\":\"2022-03-21 18:02:11\"}";
        System.out.println(new JsonObjects(str2).toJson());
        String str3 = "{\"age\":2,\"amount\":15.14,\"birthday\":null,\"create\":null,\"id\":null,\"name\":\"张三\",\"create\":2022-03-21}";
        System.out.println(new JsonObjects(str3).toJson());
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
        System.out.println(jsonObjects);
        for (Map.Entry<String, JsonElement> entries : jsonObjects.entrySet()) {
            System.out.println(entries.getKey() + "=--------" + JsonObjectUtils.getAsString(entries.getValue()));
        }
    }
}

@Data
class ContractsDO {

    public String code;// 合同编号
    public String name;// 项目名称
    public String projectName;// 项目名称
    public String partyA;//甲方名称
    public String partyB;//乙方名称
    public Date useSide;// 使用方
    public String dateOfSigning;// 签订日期
    public String installationAddress;//安装地点
    private Date createDate;//安装地点
//    private Date Up

}
