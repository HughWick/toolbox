package com.github.hugh.gson;

import com.github.hugh.model.Student;
import com.github.hugh.util.DateUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

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
