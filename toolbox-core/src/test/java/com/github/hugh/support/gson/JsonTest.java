package com.github.hugh.support.gson;

import com.github.hugh.constant.DateCode;
import com.github.hugh.model.Student;
import com.github.hugh.util.OkHttpUtils;
import com.github.hugh.util.gson.JsonObjectUtils;
import com.github.hugh.util.gson.JsonObjects;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

/**
 * @author AS
 * @date 2020/9/28 11:38
 */
public class JsonTest {

    @Test
    public void toJsonObject() {
        String jsonString = "{\"name\":\"xiaolin\",\"value\":\"\",\"port\":\"\"}";
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("port").getAsInt());
        String arrStr = "[1,2,3,4]";
        JsonArray jsonArray = JsonParser.parseString(arrStr).getAsJsonArray();
        System.out.println("--->>" + jsonArray);
    }

    @Test
    public void test() {
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\"}";
        Gson gson = new Gson();
        Student student = gson.fromJson(str, Student.class);
//        System.out.println("--->>" + DateUtils.format(student.getCreate(), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(gson.toJson(student));
        JsonObject jsonObject = JsonParser.parseString(str).getAsJsonObject();
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("name").getAsString());
        System.out.println("====================");
        // json to map
        String mapStr = "{\"m1\":{\"username\":\"张三\",\"password\":\"123\",\"money\":null,\"book\":{\"id\":0,\"name\":\"三国演义\",\"price\":0,\"author\":\"罗贯中\"}},\"m2\":{\"username\":\"李四\",\"password\":\"123\",\"money\":500,\"book\":{\"id\":0,\"name\":\"红楼梦\",\"price\":0,\"author\":\"曹雪芹\"}}}";
        JsonObject asJsonObject = JsonParser.parseString(mapStr).getAsJsonObject();
        System.out.println(asJsonObject);
//        System.out.println("--->>" + asJsonObject.get("m1").getAsJsonObject().get("money").getAsDouble());
        JsonObjects jsonObjects = new JsonObjects(JsonObjectUtils.toJson(student));
        System.out.println("--->>" + jsonObjects);
    }

    @Test
    public void test03() {
        JSONObject json = new JSONObject();
        json.put("ZH", "admin");
        json.put("MM", "88888888");
        try {
            JsonObject jsonObject = OkHttpUtils.postFormReJsonObject("https://www.hnlot.com.cn/ptpz/yonghu/login", json);
            assert jsonObject != null;
            System.out.println("--getJsonObject->>" + JsonObjectUtils.getJsonObject(jsonObject, "data"));
            JsonObject data = JsonObjectUtils.getJsonObject(jsonObject, "data");
            System.out.println("--getString->>" + JsonObjectUtils.getString(data, "access_token"));
            JsonArray menus = JsonObjectUtils.getJsonArray(data, "menus");
            for (int i = 0; i < menus.size(); i++) {
                System.out.println("--JsonArray遍历->" + menus.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test04() throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("token", UUID.randomUUID().toString());
        JSONObject json = new JSONObject();
        json.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        String str = OkHttpUtils.get("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json, header);
        System.out.println("--->" + JsonParser.parseString(str).getAsJsonObject());
    }


    @Test
    public void test05() {
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\",\"id\":null}";
        String str2 = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\",\"id\":\"null\"}";

//        String str2 = "{\"serialNo\":null,\"createBy\":null,\"createDate\":null,\"updateBy\":null,\"updateDate\":null,\"deleteBy\":null,\"deleteDate\":null,\"deleteFlag\":0," +
//                "\"id\":81,\"ip\":null,\"zclxmc2\":\"网络交换传输\",\"cjmc\":null,\"sxxsj\":null,\"zxzts\":null,\"ywzjbdsl\":0,\"tdmc\":null,\"szs\":null,\"laniotzt\":null,\"ywzjjcsj\":null,\"gdzt\":null,\"tbzt\":0,\"txmm\":null,\"htmc\":null,\"kkmc\":null,\"fxmc\":null,\"bdzcjh\":null,\"dzjcmc\":null,\"ywzjmc\":null,\"ppmc\":\"海康\",\"zclxmc\":\"防火墙\",\"zclxmc1\":null,\"ywzjzt\":null,\"ssq_NO\":\"43010407\",\"sfmc\":\"\",\"csmc\":\"\",\"qxmc\":\"\",\"xzjdmc\":\"\",\"sbxlh\":\"123123123\",\"iccid\":null,\"zzzp\":null,\"rkdjr_ID\":null,\"bdzt\":0,\"ywzjbd_ID\":0,\"zcmc\":\"测试资产\",\"kkbd_ID\":null,\"dzjcbd_ID\":null,\"zclx\":26,\"cds\":null,\"xh\":\"0021\",\"sf\":\"430000\",\"cs\":\"430100\",\"qx\":\"430104\",\"cj\":\"1\",\"pp_ID\":1,\"sydw\":1,\"xqbm\":\"43010407\",\"xzjd\":\"43010407\",\"gldk\":\"\",\"rksj\":1591175764000,\"zxzt\":0,\"gbbm\":\"\",\"fx_ID\":0,\"zczt\":\"1\",\"xxdz\":\"\"" +
//                ",\"jd\":\"\",\"wd\":\"\",\"dlzh\":\"\",\"dlmm\":\"\",\"ccrq\":null,\"bxq\":1,\"sbbm\":\"\",\"imei\":null,\"ht_ID\":0,\"cjdw\":0,\"ywdw\":0,\"azrq\":null}";
        JsonObject json = JsonParser.parseString(str).getAsJsonObject();
        JsonObject json2 = JsonParser.parseString(str2).getAsJsonObject();

        System.out.println("--->>" + json.get("id"));
//        System.out.println("--->>"+json.get("id").getAsString());
        System.out.println("--getString->>" + JsonObjectUtils.getString(json, "id"));
        System.out.println("--getString->>" + JsonObjectUtils.getString(json2, "id"));
    }


    @Test
    public void test06() {
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06\",\"id\":null}";
        JsonObject parse = JsonObjectUtils.parse(str);
        System.out.println("--->>" + parse);
        String arr = "[1,2,3,4,5]";
        JsonArray jsonArray = JsonObjectUtils.parseArray(arr);
        System.out.println("--->>" + jsonArray);
        String arr2 = null;
        System.out.println("--->>" + JsonObjectUtils.parseArray(arr2));
    }

    @Test
    public void test07() {
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

        String tmee = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"2019-04-06 12:11:20\"}";
        Student student = JsonObjectUtils.fromJson(tmee, Student.class);
        System.out.println("=--7->>" + student.getCreate());
        System.out.println("-8-->>" + JsonObjectUtils.toJson(student));
        Map<Object, Object> objectObjectMap = JsonObjectUtils.toMap(json2);
        System.out.println("-9-->>" + objectObjectMap);
        Student student2 = JsonObjectUtils.fromJson(tmee, Student.class, DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC);
        System.out.println("=--10->>" + student2.getCreate());
    }

    @Test
    void test08() {
        String str2 = "[{\"serialNo\":\"1339497989051277312\",\"createBy\":1,\"createDate\":1608196182000,\"updateBy\":\"xxxx\",\"updateDate\":1615444156000}]";
        JsonArray jsonElements = JsonObjectUtils.parseArray(str2);
        List<JsonObject> objects = JsonObjectUtils.toArrayList(jsonElements);
        System.out.println("-====>>>" + objects);
    }

    @Test
    void testTime() {
        String str = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create2\":null,\"id\":1,\"name\":\"张三\",\"create\":\"1625024713000\"}";
        Student student1 = JsonObjectUtils.fromJsonTimeStamp(str, Student.class);
        System.out.println(student1.toString());

        JsonObjects jsonObjects = new JsonObjects(str);
        System.out.println(jsonObjects.fromJsonTimeStamp(Student.class));
        Student student = jsonObjects.fromJsonTimeStamp(Student.class);
        System.out.println(JsonObjectUtils.toJson(student));
        Date date = jsonObjects.getDate("create");
        System.out.println("---1-->>" + date);
        String date2 = jsonObjects.getDateStr("create2");
        System.out.println("---2-->>" + date2);
        String date3 = jsonObjects.getDateStr("create");
        System.out.println("---3-->>" + date3);
        String date4 = jsonObjects.getDateStr("create", DateCode.YEAR_MONTH_DAY);
        System.out.println("--3--?>" + date4);

//        String str2 = "{\"age\":2,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1,\"name\":\"张三\",\"create\":\"1625024713000\",\"test\":}";
//        Student student2 = JsonObjectUtils.fromJsonTimeStamp(str2, Student.class);
//        System.out.println(student1.toString());
    }


    public static void main(String[] args) {
        JsonObject msgObj = new JsonObject();
        msgObj.addProperty("test", "null");
        msgObj.addProperty("test1", "1233");
        msgObj.addProperty("test2", "1234");
        msgObj.addProperty("test3", "1235");
        System.out.println(msgObj.toString());
//        String msgStr = msgObj.toString();
//        Gson g = new Gson();
//        JsonObject obj = g.fromJson(msgStr, JsonObject.class);
//        System.out.println(obj.get("test"));
//        for (Map.Entry<String, JsonElement> set : obj.entrySet()) {//通过遍历获取key和value
//            System.out.println(set.getKey() + "_" + set.getValue().getAsInt());
//        }
    }
}