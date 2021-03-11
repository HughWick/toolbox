package com.github.hugh.support.gson;

import com.github.hugh.util.gson.JsonObjects;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Test;

/**
 * @author AS
 */
public class JsonObjectsTest {
    @Test
    public void test01() {
        String str = "{\"age\":1,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1888,\"name\":\"张三\",\"create\":\"2019-04-06\"}";
        String str2 = "{\"age\":2,\"amount\":15.14,\"birthday\":null,\"create\":null,\"id\":null,\"name\":\"张三\",\"create\":\"2019-04-06\"}";
        JsonObjects json = new JsonObjects(str);
        JsonObjects json2 = new JsonObjects(str2);
        System.out.println("--1-->>>" + json.getString("age"));
        System.out.println("---2->>>" + json.getInt("id"));
        System.out.println("---3->>>" + json.getDoubleValue("amount"));
        System.out.println("--4-->>>" + json2.getString("age"));
        System.out.println("--5-->>>" + json2.getInteger("id"));
        System.out.println("--6-->>>" + json2.getDouble("amount"));
        System.out.println("--7-->>>" + json2.getLong("age"));
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
        JsonObjects json = new JsonObjects(null);
        System.out.println("---1>>" + json.isNull());
        System.out.println("----NED----");
        String tem = "{\"code\":\"0000\",\"message\":\"操作成功\",\"data\":{\"list\":[{\"serialNo\":\"1339497989051277312\",\"createBy\":\"谭运虎\",\"createDate\":1608196182000,\"updateBy\":\"系统操作\",\"updateDate\":1615444156000,\"deleteBy\":null,\"deleteDate\":null,\"deleteFlag\":0,\"identification\":0,\"counts\":null,\"tatal\":0,\"id\":489,\"ip\":\"169.1.1.1\",\"ssq_NO\":\"43010408\",\"sfmc\":\"\",\"csmc\":\"\",\"qxmc\":\"\",\"xzjdmc\":\"\",\"xqbm\":\"43010408\",\"sf\":\"430000\",\"cs\":\"430100\",\"qx\":\"430104\",\"xzjd\":\"43010408\",\"zcmc\":\"服务器1\",\"pp_ID\":3,\"ppmc\":\"前卫\",\"zxzt\":0,\"bdzt\":0,\"dzjcmc\":null,\"sbxlh\":null,\"kkmc\":null,\"rksj\":1608196182000,\"sxxsj\":1615444155000,\"tdmc\":null,\"gbbm\":null,\"gldk\":\"161\",\"ywzjbd_ID\":null,\"jd\":null,\"wd\":null,\"xxdz\":\"\",\"fx_ID\":0,\"kkbd_ID\":0,\"dzjcbd_ID\":null,\"zclxmc\":\"塔式服务器\",\"zclxmc1\":null,\"zclxmc2\":\"服务器（存储）\",\"zclx\":15,\"cj\":\"1\",\"xh\":\"cd212\",\"sydw\":1,\"zczt\":\"1\",\"dlzh\":null,\"dlmm\":null,\"ccrq\":1608134400000,\"azrq\":1606924800000,\"bxq\":2,\"sbbm\":null,\"ht_ID\":97,\"cjdw\":0,\"ywdw\":0,\"szs\":null,\"tbzt\":0,\"iccid\":null,\"ywzjzt\":null,\"mb\":3,\"cds\":null,\"zzzp\":\"[{\\\"name\\\":1609728432052,\\\"url\\\":\\\"https://cmmop.hnlot.com.cn/annex/cmmop/zczx/zcgl/ZiChanTuPian/image/2020/12/18/921313b0d11a4d038446401ee1a52754.jpg\\\",\\\"status\\\":\\\"success\\\",\\\"uid\\\":1610012972059},{\\\"name\\\":1609728432053,\\\"url\\\":\\\"https://cmmop.hnlot.com.cn/annex/cmmop/zczx/zcgl/ZiChanTuPian/image/2020/12/18/55b3c99aed424ca9973e0cf1ab12f420.jpg\\\",\\\"status\\\":\\\"success\\\",\\\"uid\\\":1610012972060},{\\\"name\\\":1609728432054,\\\"url\\\":\\\"https://cmmop.hnlot.com.cn/annex/cmmop/zczx/zcgl/ZiChanTuPian/image/2020/12/18/774507f4f08d4fd095e03c15b8018402.jpg\\\",\\\"status\\\":\\\"success\\\",\\\"uid\\\":1610012972061}]\",\"rkdjr_ID\":\"192\",\"kqjy\":0,\"kqtbjy\":0,\"fxmc\":null,\"gdzt\":null,\"zxzts\":null,\"ywzjbdsl\":0,\"laniotzt\":null,\"ywzjjcsj\":null,\"wtzhscsj\":null,\"wtscsj\":null,\"txmm\":null,\"imei\":null,\"htmc\":null,\"bdzcjh\":null,\"ywzjmc\":null,\"cjmc\":null}],\"countPage\":10,\"countList\":10}}\n";
        JsonObjects jsonObjects = new JsonObjects(tem);
        System.out.println("---->?>" + jsonObjects.isEquals("code", "0000"));
    }
}
