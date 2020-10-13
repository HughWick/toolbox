package com.github.hugh.gson;

import com.github.hugh.model.Student;
import com.github.hugh.util.DateUtils;
import com.github.hugh.util.OkHttpUtils;
import com.github.hugh.util.gson.JsonObjectUtils;
import com.google.gson.*;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

/**
 * @author AS
 * @date 2020/9/28 11:38
 */
public class JsonDemo {

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
        System.out.println("--->>" + DateUtils.format(student.getCreate(), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(gson.toJson(student));
        JsonObject jsonObject = JsonParser.parseString(str).getAsJsonObject();
        System.out.println(jsonObject);
        System.out.println(jsonObject.get("name").getAsString());
        System.out.println("====================");
        // json to map
        String mapStr = "{\"m1\":{\"username\":\"张三\",\"password\":\"123\",\"money\":null,\"book\":{\"id\":0,\"name\":\"三国演义\",\"price\":0,\"author\":\"罗贯中\"}},\"m2\":{\"username\":\"李四\",\"password\":\"123\",\"money\":500,\"book\":{\"id\":0,\"name\":\"红楼梦\",\"price\":0,\"author\":\"曹雪芹\"}}}";
        JsonObject asJsonObject = JsonParser.parseString(mapStr).getAsJsonObject();
        System.out.println(asJsonObject);
        System.out.println("--->>" + asJsonObject.get("m1").getAsJsonObject().get("money").getAsDouble());
    }


    @Test
    public void test02() throws IOException {
        JSONObject json = new JSONObject();
        json.put("recipientAddr", "四川省成都市温江区南熏大道四段红泰翰城");
        JsonObject jsonObject = OkHttpUtils.postFormReJsonObject("https://sudo.191ec.com/silver-web-shop/manual/readInfo2", json);
//        JsonObject jsonObject = OkHttpUtils.postFormReJsonObject("https://sudo.191ec.com/silver-web-shop/manager/", json);
        assert jsonObject != null;
        System.out.println("--->>" + jsonObject.toString());
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
    public void test03() {
        JSONObject json = new JSONObject();
        json.put("ZH", "admin");
        json.put("MM", "88888888");
        try {
            JsonObject jsonObject = OkHttpUtils.postFormReJsonObject("https://www.hnlot.com.cn/ptpz/yonghu/login", json);
            assert jsonObject != null;
//            System.out.println("--->>" + jsonObject.toString());
            System.out.println("--getJsonObject->>" + JsonObjectUtils.getJsonObject(jsonObject, "data"));
            JsonObject data = JsonObjectUtils.getJsonObject(jsonObject, "data");
            System.out.println("--getString->>" + JsonObjectUtils.getString(data, "access_token"));
            JsonArray menus = JsonObjectUtils.getJsonArray(data, "menus");
            for (int i =0 ; i < menus.size() ; i++){
                System.out.println("--JsonArray遍历->"+menus.get(i));
            }
//            System.out.println("--getJsonArray->>" + JsonObjectUtils.getJsonArray(data, "menus"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
