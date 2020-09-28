package com.github.hugh.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import java.util.Map;

/**
 * @author AS
 * @date 2020/9/28 11:38
 */
public class JsonDemo {

    @Test
    public void toJsonObject(){
        String jsonString = "{\"name\":\"xiaolin\",\"value\":\"xxx\"}";
//        JsonObject jsonObject = JsonParser.parseString​(jsonString).getAsJsonObject();
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        
        System.out.println(jsonObject.get("name").getAsString());
    }

    public static void main(String[] args) {
        JsonObject msgObj = new JsonObject();
        msgObj.addProperty("test", "123");
        msgObj.addProperty("test1", "1233");
        msgObj.addProperty("test2", "1234");
        msgObj.addProperty("test3", "1235");
        System.out.println(msgObj.toString());
        String msgStr = msgObj.toString();

        Gson g = new Gson();
        JsonObject obj = g.fromJson(msgStr, JsonObject.class);
        System.out.println(obj.get("test"));
        for (Map.Entry<String, JsonElement> set : obj.entrySet()) {//通过遍历获取key和value
            System.out.println(set.getKey() + "_" + set.getValue().getAsInt());
        }
    }

}
