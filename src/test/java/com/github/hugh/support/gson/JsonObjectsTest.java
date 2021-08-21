package com.github.hugh.support.gson;

import com.alibaba.fastjson.JSON;
import com.github.hugh.model.Student;
import com.github.hugh.model.dto.ResultDTO;
import com.github.hugh.util.gson.JsonObjects;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import org.junit.Test;

/**
 * gson JsonObjects测试类
 *
 * @author AS
 */
public class JsonObjectsTest {
    @Test
    public void test01() {
        String str = "{\"age\":1,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1888,\"name\":\"张三\",\"create\":\"2019-04-06\"}";
        String str2 = "{\"age\":2,\"amount\":15.14,\"birthday\":null,\"create\":null,\"id\":null,\"name\":\"张三\",\"create\":\"2019-04-06\"}";
        JsonObjects json = new JsonObjects(str);
        JsonObjects json2 = new JsonObjects(str2);
        System.out.println(json);
        System.out.println(json.toJson());
        System.out.println(json.formJson(Student.class));
        System.out.println("--1-->>>" + json.getString("age"));
        System.out.println("---2->>>" + json.getInt("id"));
        System.out.println("---3->>>" + json.getDoubleValue("amount"));
        System.out.println("--4-->>>" + json2.getString("age"));
        System.out.println("--5-->>>" + json2.getInteger("id"));
        System.out.println("--6-->>>" + json2.getDouble("amount"));
        System.out.println("--7-->>>" + json2.getLong("age"));
        System.out.println("--8-->>>" + json2.getLongValue("age"));
        System.out.println("--8-->>>" + json2);
        System.out.println("----NED----");
//        String str2 = "{\"serialNo\":null,\"createBy\":null,\"createDate\":null,\"updateBy\":null,\"updateDate\":null,\"deleteBy\":null,\"deleteDate\":null,\"deleteFlag\":0," +
//                "\"id\":81,\"ip\":null,\"zclxmc2\":\"网络交换传输\",\"cjmc\":null,\"sxxsj\":null,\"zxzts\":null,\"ywzjbdsl\":0,\"tdmc\":null,\"szs\":null,\"laniotzt\":null,\"ywzjjcsj\":null,\"gdzt\":null,\"tbzt\":0,\"txmm\":null,\"htmc\":null,\"kkmc\":null,\"fxmc\":null,\"bdzcjh\":null,\"dzjcmc\":null,\"ywzjmc\":null,\"ppmc\":\"海康\",\"zclxmc\":\"防火墙\",\"zclxmc1\":null,\"ywzjzt\":null,\"ssq_NO\":\"43010407\",\"sfmc\":\"\",\"csmc\":\"\",\"qxmc\":\"\",\"xzjdmc\":\"\",\"sbxlh\":\"123123123\",\"iccid\":null,\"zzzp\":null,\"rkdjr_ID\":null,\"bdzt\":0,\"ywzjbd_ID\":0,\"zcmc\":\"测试资产\",\"kkbd_ID\":null,\"dzjcbd_ID\":null,\"zclx\":26,\"cds\":null,\"xh\":\"0021\",\"sf\":\"430000\",\"cs\":\"430100\",\"qx\":\"430104\",\"cj\":\"1\",\"pp_ID\":1,\"sydw\":1,\"xqbm\":\"43010407\",\"xzjd\":\"43010407\",\"gldk\":\"\",\"rksj\":1591175764000,\"zxzt\":0,\"gbbm\":\"\",\"fx_ID\":0,\"zczt\":\"1\",\"xxdz\":\"\"" +
//                ",\"jd\":\"\",\"wd\":\"\",\"dlzh\":\"\",\"dlmm\":\"\",\"ccrq\":null,\"bxq\":1,\"sbbm\":\"\",\"imei\":null,\"ht_ID\":0,\"cjdw\":0,\"ywdw\":0,\"azrq\":null}";
    }

    @Test
    public void test02() {
        String str = "{}";
        JsonObjects json = new JsonObjects(str);
        System.out.println("---->>" + json.isNull());
        String str2 = null;
        JsonObjects json2 = new JsonObjects(str2);
        System.out.println("---->>" + json2.isNull());
        JsonObject jsopn = new JsonObject();
        System.out.println("---->>" + new JsonObjects(jsopn).toString());
        System.out.println("---->>" + new JsonObjects(jsopn));
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

        JsonObjects data = json2.jsonObjects("data");
        System.out.println("---1>>" + data.getString("id"));
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


    @Data
    class testCommand {
        private int type;
    }
}
