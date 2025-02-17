package com.github.hugh.json.gson;

import com.github.hugh.json.model.Command;
import com.github.hugh.json.model.ResponseData;
import com.github.hugh.json.model.Student;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * gson 工具类测试
 *
 * @author hugh
 * Date: 2023/1/9 10:10
 */
class GsonUtilTest {

    @Test
    void testFormJson() {
        String create = "1625024713000";
//        Student student1 = new Student();
//        student1.setId(1);
//        student1.setAge(2);
//        student1.setName("张三");
//        student1.setAmount(10.14);
//        student1.setBirthday(null);
////        student1.setSystem(0);
//        student1.setCreate(DateUtils.parseTimestamp(create));
        String strJson1 = "{\"id\":1,\"age\":2,\"name\":\"张三\",\"amount\":10.14,\"birthday\":null,\"create\":\"" + create + "\"}";
        Student student1 = GsonUtils.fromJson(strJson1, Student.class);
        assertEquals(student1.toString(), student1.toString());
        String jsonStr2 = "{\"id\":1,\"age\":2,\"name\":\"张三\",\"amount\":10.14,\"create\":\"2021-06-30 11:45:13\",\"system\":0}";
        assertEquals(jsonStr2, GsonUtils.toJson(student1));
        String jsonStr3 = "{\"id\":1,\"age\":2,\"name\":\"张三\",\"amount\":10.14,\"create\":" + create + ",\"system\":0}";
        assertEquals(jsonStr3, GsonUtils.toJsonTimestamp(student1));
        // 假设 `in` 是一个 JSON 解析器或输入流，可以模拟输入字符串
        String input = "SomeRandomText";
        String jsonStr4 = "{\"create\":" + input + "}";
        Student student4 = GsonUtils.fromJson(jsonStr4, Student.class);
        assertNull(student4.getCreate());
    }

    // 测试秒级时间戳
    @Test
    void testFromJsonWithSecondTimestamp() {
        // 假设当前时间戳为秒级时间戳
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        String jsonWithSecondTimestamp = "{\"create\":" + currentTimeInSeconds + "}";
        Student student = GsonUtils.fromJson(jsonWithSecondTimestamp, Student.class);
        // 断言从JSON中解析出的Date对象是否与期望时间相符
        Date expectedDate = new Date(currentTimeInSeconds * 1000); // 转换为毫秒级时间戳
        assertEquals(expectedDate, student.getCreate());
        // 假设当前时间戳为毫秒级时间戳
        long currentTimeInMillis = System.currentTimeMillis();
        String jsonWithMillisecondTimestamp = "{\"create\":" + currentTimeInMillis + "}";
        Student student2 = GsonUtils.fromJson(jsonWithMillisecondTimestamp, Student.class);
        // 断言从JSON中解析出的Date对象是否与期望时间相符
        Date expectedDate2 = new Date(currentTimeInMillis); // 毫秒级时间戳直接使用
        assertEquals(expectedDate2, student2.getCreate());
        // 字符串
        long currentTimeInSecondsStr3 = System.currentTimeMillis() / 1000;
        String jsonWithSecondsTimestampStr3 = "{\"create\":\"" + currentTimeInSecondsStr3 + "\"}";
        Student student3 = GsonUtils.fromJson(jsonWithSecondsTimestampStr3, Student.class);
        Date expectedDate3 = new Date(currentTimeInSecondsStr3 * 1000); // 毫秒级时间戳直接使用
        assertEquals(expectedDate3.getTime(), student3.getCreate().getTime());
    }


//    @Test
//    void testEmpty() {
//        String jsonStr = "[{\"children\":[{\"children\":[{\"children\":[{\"id\":\"51342705\",\"label\":\"披砂镇\"}],\"id\":\"513427\",\"label\":\"宁南县\"}],\"id\":\"513400\",\"label\":\"凉山彝族自治州\"}],\"id\":\"510000\",\"label\":\"四川省\"},{\"children\":[{\"children\":[{\"id\":\"220112\",\"label\":\"双阳区\"}],\"id\":\"220100\",\"label\":\"长春市\"},{\"children\":[{\"id\":\"220281\",\"label\":\"蛟河市\"}],\"id\":\"220200\",\"label\":\"吉林市\"},{\"children\":[{\"children\":[{\"id\":\"22088127\",\"label\":\"万宝镇\"}],\"id\":\"220881\",\"label\":\"洮南市\"},{\"children\":[{\"id\":\"22088201\",\"label\":\"安北街道\"},{\"id\":\"22088202\",\"label\":\"联合乡\"}],\"id\":\"220882\",\"label\":\"大安市\"}],\"id\":\"220800\",\"label\":\"白城市\"}],\"id\":\"220000\",\"label\":\"吉林省\"},{\"children\":[{\"children\":[{\"children\":[{\"id\":\"43010201\",\"label\":\"文艺路街道\"},{\"id\":\"43010202\",\"label\":\"火星街道\"},{\"id\":\"43010203\",\"label\":\"韭菜园街道\"},{\"id\":\"43010204\",\"label\":\"朝阳街街道\"},{\"id\":\"43010205\",\"label\":\"东屯渡街道\"},{\"id\":\"43010206\",\"label\":\"荷花园街道\"},{\"id\":\"43010207\",\"label\":\"隆平高科技园\"},{\"id\":\"43010208\",\"label\":\"东岸街道\"},{\"id\":\"43010209\",\"label\":\"马王堆街道\"},{\"id\":\"43010210\",\"label\":\"五里牌街道\"},{\"id\":\"43010211\",\"label\":\"定王台街道\"},{\"id\":\"43010212\",\"label\":\"湘湖街道\"},{\"id\":\"43010213\",\"label\":\"马坡岭街道\"},{\"id\":\"43010214\",\"label\":\"东湖街道\"}],\"id\":\"430102\",\"label\":\"芙蓉区\"},{\"children\":[{\"id\":\"43010301\",\"label\":\"金盆岭街道\"},{\"id\":\"43010302\",\"label\":\"黑石铺街道\"},{\"id\":\"43010303\",\"label\":\"桂花坪街道\"},{\"id\":\"43010304\",\"label\":\"大托铺街道\"},{\"id\":\"43010305\",\"label\":\"城南路街道\"},{\"id\":\"43010306\",\"label\":\"裕南街街道\"},{\"id\":\"43010307\",\"label\":\"先锋街道\"},{\"id\":\"43010308\",\"label\":\"赤岭路街道\"},{\"id\":\"43010309\",\"label\":\"文源街道\"},{\"id\":\"43010310\",\"label\":\"青园街道\"},{\"id\":\"43010311\",\"label\":\"新开铺街道\"},{\"id\":\"43010312\",\"label\":\"坡子街街道\"},{\"id\":\"43010313\",\"label\":\"南托街道\"},{\"id\":\"43010314\",\"label\":\"暮云街道\"}],\"id\":\"430103\",\"label\":\"天心区\"},{\"children\":[{\"id\":\"43010401\",\"label\":\"莲花镇\"},{\"id\":\"43010402\",\"label\":\"学士街道\"},{\"id\":\"43010403\",\"label\":\"雨敞坪镇\"},{\"id\":\"43010404\",\"label\":\"桔子洲街道\"},{\"id\":\"43010405\",\"label\":\"望月湖街道\"},{\"id\":\"43010406\",\"label\":\"天顶街道\"},{\"id\":\"43010407\",\"label\":\"咸嘉湖街道\"},{\"id\":\"43010408\",\"label\":\"洋湖街道\"},{\"id\":\"43010409\",\"label\":\"观沙岭街道\"},{\"id\":\"43010410\",\"label\":\"坪塘街道\"},{\"id\":\"43010411\",\"label\":\"望岳街道\"},{\"id\":\"43010412\",\"label\":\"岳麓街道\"},{\"id\":\"43010413\",\"label\":\"银盆岭街道\"},{\"id\":\"43010414\",\"label\":\"望城坡街道\"},{\"id\":\"43010415\",\"label\":\"含浦街道\"},{\"id\":\"43010416\",\"label\":\"麓谷街道\"},{\"id\":\"43010417\",\"label\":\"梅溪湖街道\"},{\"id\":\"43010418\",\"label\":\"西湖街道\"}],\"id\":\"430104\",\"label\":\"岳麓区\"},{\"children\":[{\"id\":\"43010501\",\"label\":\"月湖街道\"},{\"id\":\"43010502\",\"label\":\"四方坪街道\"},{\"id\":\"43010503\",\"label\":\"清水塘街道\"},{\"id\":\"43010504\",\"label\":\"伍家岭街道\"},{\"id\":\"43010505\",\"label\":\"东风路街道\"},{\"id\":\"43010506\",\"label\":\"通泰街街道\"},{\"id\":\"43010507\",\"label\":\"新河街道\"},{\"id\":\"43010508\",\"label\":\"芙蓉北路街道\"},{\"id\":\"43010509\",\"label\":\"浏阳河街道\"},{\"id\":\"43010510\",\"label\":\"望麓园街道\"},{\"id\":\"43010511\",\"label\":\"捞刀河街道\"},{\"id\":\"43010512\",\"label\":\"秀峰街道\"},{\"id\":\"43010513\",\"label\":\"沙坪街道\"},{\"id\":\"43010514\",\"label\":\"洪山街道\"},{\"id\":\"43010515\",\"label\":\"湘雅路街道\"},{\"id\":\"43010516\",\"label\":\"青竹湖街道\"}],\"id\":\"430105\",\"label\":\"开福区\"},{\"children\":[{\"id\":\"43011101\",\"label\":\"长沙雨花经济开发区\"},{\"id\":\"43011102\",\"label\":\"圭塘街道\"},{\"id\":\"43011103\",\"label\":\"井湾子街道\"},{\"id\":\"43011104\",\"label\":\"跳马镇\"},{\"id\":\"43011105\",\"label\":\"左家塘街道\"},{\"id\":\"43011106\",\"label\":\"东山街道\"},{\"id\":\"43011107\",\"label\":\"东塘街道\"},{\"id\":\"43011108\",\"label\":\"砂子塘街道\"},{\"id\":\"43011109\",\"label\":\"雨花亭街道\"},{\"id\":\"43011110\",\"label\":\"同升街道\"},{\"id\":\"43011111\",\"label\":\"高桥街道\"},{\"id\":\"43011112\",\"label\":\"侯家塘街道\"},{\"id\":\"43011113\",\"label\":\"洞井街道\"},{\"id\":\"43011114\",\"label\":\"黎托街道\"}],\"id\":\"430111\",\"label\":\"雨花区\"},{\"children\":[{\"id\":\"43011201\",\"label\":\"白沙洲街道\"},{\"id\":\"43011202\",\"label\":\"黄金园街道\"},{\"id\":\"43011203\",\"label\":\"白箬铺镇\"},{\"id\":\"43011204\",\"label\":\"金山桥街道\"},{\"id\":\"43011205\",\"label\":\"大泽湖街道\"},{\"id\":\"43011206\",\"label\":\"丁字湾街道\"},{\"id\":\"43011207\",\"label\":\"桥驿镇\"},{\"id\":\"43011208\",\"label\":\"月亮岛街道\"},{\"id\":\"43011209\",\"label\":\"高塘岭街道\"},{\"id\":\"43011210\",\"label\":\"茶亭镇\"},{\"id\":\"43011211\",\"label\":\"雷锋街道\"},{\"id\":\"43011212\",\"label\":\"乌山街道\"},{\"id\":\"43011213\",\"label\":\"铜官街道\"},{\"id\":\"43011214\",\"label\":\"靖港镇\"},{\"id\":\"43011215\",\"label\":\"乔口镇\"}],\"id\":\"430112\",\"label\":\"望城区\"},{\"children\":[{\"id\":\"43012101\",\"label\":\"高桥镇\"},{\"id\":\"43012102\",\"label\":\"长龙街道\"},{\"id\":\"43012103\",\"label\":\"湘龙街道\"},{\"id\":\"43012104\",\"label\":\"安沙镇\"},{\"id\":\"43012105\",\"label\":\"黄花镇\"},{\"id\":\"43012106\",\"label\":\"春华镇\"},{\"id\":\"43012107\",\"label\":\"路口镇\"},{\"id\":\"43012108\",\"label\":\"江背镇\"},{\"id\":\"43012109\",\"label\":\"星沙街道\"},{\"id\":\"43012110\",\"label\":\"青山铺镇\"},{\"id\":\"43012111\",\"label\":\"果园镇\"},{\"id\":\"43012112\",\"label\":\"福临镇\"},{\"id\":\"43012113\",\"label\":\"北山镇\"},{\"id\":\"43012114\",\"label\":\"泉塘街道\"},{\"id\":\"43012115\",\"label\":\"榔梨街道\"},{\"id\":\"43012116\",\"label\":\"金井镇\"},{\"id\":\"43012117\",\"label\":\"黄兴镇\"},{\"id\":\"43012118\",\"label\":\"开慧镇\"}],\"id\":\"430121\",\"label\":\"长沙县\"},{\"children\":[{\"id\":\"43018101\",\"label\":\"澄潭江镇\"},{\"id\":\"43018102\",\"label\":\"沿溪镇\"},{\"id\":\"43018103\",\"label\":\"社港镇\"},{\"id\":\"43018104\",\"label\":\"大围山镇\"},{\"id\":\"43018105\",\"label\":\"文家市镇\"},{\"id\":\"43018106\",\"label\":\"官桥镇\"},{\"id\":\"43018107\",\"label\":\"葛家镇\"},{\"id\":\"43018108\",\"label\":\"沙市镇\"},{\"id\":\"43018109\",\"label\":\"北盛镇\"},{\"id\":\"43018110\",\"label\":\"永安镇\"},{\"id\":\"43018111\",\"label\":\"张坊镇\"},{\"id\":\"43018112\",\"label\":\"金刚镇\"},{\"id\":\"43018113\",\"label\":\"淮川街道\"},{\"id\":\"43018114\",\"label\":\"普迹镇\"},{\"id\":\"43018115\",\"label\":\"荷花街道\"},{\"id\":\"43018116\",\"label\":\"枨冲镇\"},{\"id\":\"43018117\",\"label\":\"洞阳镇\"},{\"id\":\"43018118\",\"label\":\"达浒镇\"},{\"id\":\"43018119\",\"label\":\"中和镇\"},{\"id\":\"43018120\",\"label\":\"官渡镇\"},{\"id\":\"43018121\",\"label\":\"龙伏镇\"},{\"id\":\"43018122\",\"label\":\"镇头镇\"},{\"id\":\"43018123\",\"label\":\"蕉溪镇\"},{\"id\":\"43018124\",\"label\":\"小河乡\"},{\"id\":\"43018125\",\"label\":\"淳口镇\"},{\"id\":\"43018126\",\"label\":\"柏加镇\"},{\"id\":\"43018127\",\"label\":\"高坪镇\"},{\"id\":\"43018128\",\"label\":\"关口街道\"},{\"id\":\"43018129\",\"label\":\"集里街道\"},{\"id\":\"43018130\",\"label\":\"古港镇\"},{\"id\":\"43018131\",\"label\":\"大瑶镇\"},{\"id\":\"43018132\",\"label\":\"永和镇\"}],\"id\":\"430181\",\"label\":\"浏阳市\"},{\"children\":[{\"id\":\"43018201\",\"label\":\"夏铎铺镇\"},{\"id\":\"43018202\",\"label\":\"花明楼镇\"},{\"id\":\"43018203\",\"label\":\"大屯营镇\"},{\"id\":\"43018204\",\"label\":\"巷子口镇\"},{\"id\":\"43018205\",\"label\":\"城郊街道\"},{\"id\":\"43018206\",\"label\":\"道林镇\"},{\"id\":\"43018207\",\"label\":\"双凫铺镇\"},{\"id\":\"43018208\",\"label\":\"白马桥乡\"},{\"id\":\"43018209\",\"label\":\"黄材镇\"},{\"id\":\"43018210\",\"label\":\"大成桥镇\"},{\"id\":\"43018211\",\"label\":\"青山桥镇\"},{\"id\":\"43018212\",\"label\":\"资福镇\"},{\"id\":\"43018213\",\"label\":\"沩山乡\"},{\"id\":\"43018214\",\"label\":\"老粮仓镇\"},{\"id\":\"43018215\",\"label\":\"沙田乡\"},{\"id\":\"43018216\",\"label\":\"回龙铺镇\"},{\"id\":\"43018217\",\"label\":\"玉潭镇\"},{\"id\":\"43018218\",\"label\":\"东湖塘镇\"},{\"id\":\"43018219\",\"label\":\"横市镇\"},{\"id\":\"43018220\",\"label\":\"金洲镇\"},{\"id\":\"43018221\",\"label\":\"煤炭坝镇\"},{\"id\":\"43018222\",\"label\":\"历经铺乡\"},{\"id\":\"43018223\",\"label\":\"龙田镇\"},{\"id\":\"43018224\",\"label\":\"流沙河镇\"},{\"id\":\"43018225\",\"label\":\"喻家坳乡\"},{\"id\":\"43018226\",\"label\":\"灰汤镇\"},{\"id\":\"43018227\",\"label\":\"坝塘镇\"},{\"id\":\"43018228\",\"label\":\"菁华铺乡\"},{\"id\":\"43018229\",\"label\":\"双江口镇\"}],\"id\":\"430182\",\"label\":\"宁乡市\"}],\"id\":\"430100\",\"label\":\"长沙市\"},{\"children\":[{\"id\":\"430902\",\"label\":\"资阳区\"},{\"children\":[{\"id\":\"43092201\",\"label\":\"灰山港镇\"}],\"id\":\"430922\",\"label\":\"桃江县\"}],\"id\":\"430900\",\"label\":\"益阳市\"}],\"id\":\"430000\",\"label\":\"湖南省\"}]";
//        final List<ElementTree> elementTrees = GsonUtils.toArrayList(jsonStr, ElementTree.class);
//        elementTrees.forEach(System.out::println);
//        assertEquals(jsonStr, GsonUtils.toJson(elementTrees));
//    }

    @Test
    void testValid() {
        String jsonStr = "{\"action\":\"forward485\",\"commandId\":{\"00090002\":\"1\"}\n";
        final boolean jsonValid = GsonUtils.isNotJsonValid(jsonStr);
        assertTrue(jsonValid);
    }


    @Test
    void testResponse() {
        String str1 = "{\n" +
                "  \"args\": {},\n" +
                "  \"data\": \"{\\\"foo1\\\":\\\"bar1\\\",\\\"foo2\\\":\\\"bar3\\\"}\",\n" +
                "  \"files\": {},\n" +
                "  \"form\": {},\n" +
                "  \"headers\": {\n" +
                "    \"Accept-Encoding\": \"gzip\",\n" +
                "    \"Content-Length\": \"29\",\n" +
                "    \"Content-Type\": \"application/json;charset=UTF-8\",\n" +
                "    \"Host\": \"httpbin.org\",\n" +
                "    \"User-Agent\": \"Custom User Agent\",\n" +
                "    \"X-Amzn-Trace-Id\": \"Root=1-663046e8-00aceca639b1ac3753a69a83\"\n" +
                "  },\n" +
                "  \"json\": \"{\\\"foo1\\\":\\\"bar1\\\",\\\"foo2\\\":\\\"bar3\\\"}\",\n" +
                "  \"origin\": \"222.244.144.131\",\n" +
                "  \"url\": \"https://httpbin.org/post\"\n" +
                "}";
//        Jsons jsons = Jsons.on(str1);
//        ResponseData responseData = jsons.formJson(ResponseData.class);
        ResponseData responseData1 = GsonUtils.fromJson(str1, ResponseData.class);
        // 使用 Assertions 进行断言
        // 断言 responseData1 不为 null
        assertNotNull(responseData1);
        // 断言 url 字段的值
        assertEquals("https://httpbin.org/post", responseData1.getUrl());

        // 断言 json 字段的值
        assertEquals("{\"foo1\":\"bar1\",\"foo2\":\"bar3\"}", responseData1.getJson());
    }

    // 多段json解析
    @Test
    void testReal() {
        // 前半截json
        String str1 = "{\"action\":\"time\",\"00010001\":\"12.57\",\"00010007\":\"0.00\",\"00010008\":\"0.00\",\"00020001\":\"26.0\",\"00020002\":\"31.0\",\"00020003\":\"0\",\"00020004\":\"0\",\"00020009\":\"C_0019\",\"0002000a\":\"1\",\"00030003\":\"爱饭海鲜(世界钰园店)世界钰园\",\"00030004\":\"湖南省/邵阳市/北塔区/田江街道\",\"00050001\":\"31\",\"00050006\":\"TDD LTE\",\"00060001\":\"$GNGGA,010228.00,2716.15366,N,11127.12657,E,1,04,2.55,183.3,M,,M,,*5C\\r\\n\\r\\nOK\\r\\n\",\"00060002\":\"$GNRMC,01022";
        boolean notJsonValid1 = GsonUtils.isNotJsonValid(str1);
        assertTrue(notJsonValid1);
        List<Command> commands1 = GsonUtils.parseMultipleJson(str1, Command.class);
        assertEquals(0, commands1.size());
        // 中间
        String str2 = "\"02000003\":\"NULL\",\"00030009\":\"D010202407090";
        boolean notJsonValid2 = GsonUtils.isNotJsonValid(str2);
        assertTrue(notJsonValid2);
        List<Command> commands2 = GsonUtils.parseMultipleJson(str2, Command.class);
        assertEquals(0, commands2.size());
        // 后半截
        String str3 = "02\"}";
        boolean notJsonValid3 = GsonUtils.isNotJsonValid(str3);
        assertTrue(notJsonValid3);
        List<Command> commands3 = GsonUtils.parseMultipleJson(str3, Command.class);
        assertEquals(0, commands3.size());
    }

    // 同字符串多个json解析
    @Test
    void test02() {
        // 第一种有一个完整的json，但是后面的第二个不完整
        String str1 = "{\"action\":\"heartbeat\",\"count\":\"0\",\"00030009\":\"D01020240605114\",\"00050001\":\"31\"}";
        String str2 = "{\"action\":\"time\",\"00010001\":\"12.26\",\"00010007\":\"0.00\",\"00010008\":\"0.00\",\"00020001\":\"22.0\",\"00020002\":\"36.0\",\"00020003\":\"0\",\"00020004\":\"0\",\"00020009\":\"C_0019\",\"0002000a\":\"1\",\"00030003\":\"(未知)\",\"00030004\":\"湖南省/邵阳市/大祥区/城北路街道\",\"00050001\":\"31\",\"00050006\":\"FDD LTE\",\"00060001\":\"$GNGGA,010910.00,2714.58249,N,11127.59198,E,1,23,0.62,226.5,M,,M,,*59\\r\\n\\r\\nOK\\r\\";
        String later = str1 + str2;
        assertTrue(GsonUtils.isNotJsonValid(later));
        List<Jsons> commands = GsonUtils.parseMultipleJson(later);
        assertEquals(1, commands.size());
        Jsons next = commands.iterator().next();
//        for (Jsons jsonStr1 : commands) {
        String replace = later.replace(next.toJson(), "");
        assertEquals(replace, str2);
//        }
        // 替换掉完整的
//        System.out.println("=====>>" + replace);
    }

    @Test
    void test04() {
        String str1 = "{\"action\":\"heartbeat\",\"count\":\"0\",\"00030009\":\"D01020240605114\",\"00050001\":\"31\"}{\"action\":\"time\",\"00010001\":\"12.27\",\"00010007\":\"0.00\",\"00010008\":\"0.00\",\"00020001\":\"23.0\",\"00020002\":\"30.0\",\"00020003\":\"0\",\"00020004\":\"0\",\"00020009\":\"C_0019\",\"0002000a\":\"1\",\"00030003\":\"(未知)\",\"00030004\":\"湖南省/邵阳市/大祥区/城北路街道\",\"00050001\":\"31\",\"00050006\":\"FDD LTE\",\"00060001\":\"$GNGGA,020952.00,2714.58325,N,11127.58547,E,1,22,0.65,229.2,M,,M,,*5E\\r\\n\\r\\nOK\\r\\n\",\"00060002\":\"$GNRMC,020951.00,A,2714.58329,N,11127.58545,E,0.254,,311024,,,A,V*15\\r\\n\\r\\nOK\\r\\n\",\"00070007\":\"192.168.75.100\",\"00070009\":\"00\",\"0007000a\":\"00\",\"0007000c\":\"0\",\"0007000d\":[0,0,0,0],\"0007000e\":[0,0,0,0,0,0,0,0,0],\"0007000f\":\"\",\"00070010\":\"0\",\"01000003\":\"NULL\",\"02000003\":\"NULL\",\"03010001\":\"230.52\",\"03010002\":\"0.21\",\"03010003\":\"25.65\",\"03010006\":\"3.55\",\"06000001\":\"1\",\"06000002\":\"12.24\",\"06000003\":\"0.259\",\"06010001\":\"1\",\"06010002\":\"12.25\",\"06010003\":\"0.000\",\"06020001\":\"1\",\"06020002\":\"12.28\",\"06020003\":\"0.000\",\"06030001\":\"1\",\"06030002\":\"12.27\",\"06030003\":\"0.000\",\"07000001\":\"1\",\"07000002\":\"230.52\",\"07010001\":\"1\",\"07010002\":\"230.52\",\"07020001\":\"1\",\"07020002\":\"230.52\",\"07030001\":\"1\",\"07030002\":\"230.52\",\"07040001\":\"1\",\"07040002\":\"230.52\",\"07050001\":\"1\",\"07050002\":\"0.00\",\"07060001\":\"1\",\"07060002\":\"0.00\",\"07070001\":\"1\",\"07070002\":\"0.00\",\"07080001\":\"1\",\"07080002\":\"0.00\",\"01000003\":\"NULL\",\"02000003\":\"NULL\",\"00030009\":\"D01020240605114\"}";
        assertTrue(GsonUtils.isNotJsonValid(str1));
    }

    // 测试字符串是否为json
    @Test
    void testIsJson() {
        var str = "{\"age\":1,\"amount\":10.14,\"birthday\":null,\"create\":null,\"id\":1888,\"name\":\"张三\",\"create\":\"16250247130001\"}";
        var str2 = "{code:006,message:测试,age:18,created:2022-03-21 18:02:11,amount:199.88,switchs:true}";
        assertTrue(GsonUtils.isJsonObject(str));
        assertTrue(GsonUtils.isNotJsonObject(str2));
        assertFalse(GsonUtils.isJsonObject(str2));
        assertTrue(GsonUtils.isJsonValid(str));
        assertFalse(GsonUtils.isJsonValid(""));
        assertTrue(GsonUtils.isNotJsonValid(""));

    }

    @Test
    void testJsonArray() {
        var array = "[1,2,3,4,5]";
        var array2 = "1,2,3,4,5";
        assertTrue(GsonUtils.isJsonArray(array));
        assertFalse(GsonUtils.isJsonArray(array2));
        assertTrue(GsonUtils.isNotJsonArray(array2));
//        assertFalse(JsonObjectUtils.isNotJsonArray(array));
    }

    @Test
    void isJsonObject_validJsonObject() {
        assertTrue(GsonUtils.isJsonObject("{}"));
        assertTrue(GsonUtils.isJsonObject("{\"key\":\"value\"}"));
        assertTrue(GsonUtils.isJsonObject("{\"key\":123}"));
        assertTrue(GsonUtils.isJsonObject("{\"key\":true}"));
        assertTrue(GsonUtils.isJsonObject("{\"key\":null}"));
        assertTrue(GsonUtils.isJsonObject("{\"key\":{\"nestedKey\":\"nestedValue\"}}"));
        assertTrue(GsonUtils.isJsonObject("{\"key\":[1,2,3]}"));
        assertTrue(GsonUtils.isJsonObject(" {\"key\":\"value\"} ")); // 前后有空格
        assertTrue(GsonUtils.isJsonObject("{\"key\" : \"value\"}")); // key和value之间有空格
        assertTrue(GsonUtils.isJsonObject("{\"key\":\"value\", \"anotherKey\":123}"));
        assertTrue(GsonUtils.isJsonObject("{\"key with space\":\"value\"}")); // key包含空格
        assertTrue(GsonUtils.isJsonObject("{\"key\":\"value with space\"}")); // value包含空格
        assertTrue(GsonUtils.isJsonObject("{\"key\":\"value\\\"with\\\"quotes\"}")); // value包含转义引号
    }

    @Test
    void isJsonObject_invalidJsonObject() {
        assertFalse(GsonUtils.isJsonObject(""));
        assertFalse(GsonUtils.isJsonObject(null));
        assertFalse(GsonUtils.isJsonObject("{\"key\":\"value\"")); // 缺少结尾大括号
        assertFalse(GsonUtils.isJsonObject("{\"key\":\"value\" ")); // 结尾有空格，但结构不完整
//        assertFalse(GsonUtils.isJsonObject("{key:\"value\"}")); // key缺少引号
//        assertFalse(GsonUtils.isJsonObject("{\"key\":value}")); // value缺少引号
        assertFalse(GsonUtils.isJsonObject("{\"key\":\"value\",}")); // 结尾有逗号
        assertFalse(GsonUtils.isJsonObject("{\"key\"\"value\"}")); // 缺少冒号
        assertFalse(GsonUtils.isJsonObject("{\"key\":\"value\"\"anotherKey\":\"anotherValue\"}")); // 缺少逗号分隔
        assertFalse(GsonUtils.isJsonObject("[1,2,3]")); // 是 JSON 数组
        assertFalse(GsonUtils.isJsonObject("123")); // 是 JSON 数字
        assertFalse(GsonUtils.isJsonObject("true")); // 是 JSON 布尔值
        assertFalse(GsonUtils.isJsonObject("null")); // 是 JSON null 值
        assertFalse(GsonUtils.isJsonObject("invalid json string"));
        assertFalse(GsonUtils.isJsonObject("{ \"key\" : \"value\" ")); // 结尾空格，且结构不完整
    }

    @Test
    void isJsonObject_emptyString() {
        assertFalse(GsonUtils.isJsonObject(""));
    }

    @Test
    void isJsonObject_nullString() {
        assertFalse(GsonUtils.isJsonObject(null));
    }

    @Test
    void isJsonObject_notJsonObjectString() {
        assertFalse(GsonUtils.isJsonObject("hello world"));
        assertFalse(GsonUtils.isJsonObject("12345"));
        assertFalse(GsonUtils.isJsonObject("true"));
        assertFalse(GsonUtils.isJsonObject("false"));
    }


    @Test
    void isJsonArray_validJsonArray() {
        assertTrue(GsonUtils.isJsonArray("[]"));
        assertTrue(GsonUtils.isJsonArray("[1, 2, 3]"));
        assertTrue(GsonUtils.isJsonArray("[\"apple\", \"banana\"]"));
        assertTrue(GsonUtils.isJsonArray("[true, false, true]"));
        assertTrue(GsonUtils.isJsonArray("[null, null]"));
        assertTrue(GsonUtils.isJsonArray("[{\"key\": \"value\"}, {\"key\": \"another\"}]")); // 数组包含对象
        assertTrue(GsonUtils.isJsonArray("[[1, 2], [3, 4]]")); // 数组包含数组
        assertTrue(GsonUtils.isJsonArray(" [1, 2] ")); // 前后有空格
    }

    @Test
    void testIsJsonArray_throwsJsonParseException_invalidSyntax_missingQuote() {
        assertFalse(GsonUtils.isJsonArray("[\"value]"));
    }
    @Test
    void isJsonArray_invalidJsonArray() {
        assertFalse(GsonUtils.isJsonArray(""));
        assertFalse(GsonUtils.isJsonArray(null));
        assertFalse(GsonUtils.isJsonArray("[1, 2")); // 缺少结尾中括号
        assertFalse(GsonUtils.isJsonArray("1, 2, 3]")); // 缺少开始中括号
        assertFalse(GsonUtils.isJsonArray("{ \"key\": \"value\" }")); // 是 JSON 对象
        assertFalse(GsonUtils.isJsonArray("123")); // 是 JSON 数字
        assertFalse(GsonUtils.isJsonArray("\"hello\"")); // 是 JSON 字符串
        assertFalse(GsonUtils.isJsonArray("true")); // 是 JSON 布尔值
        assertFalse(GsonUtils.isJsonArray("null")); // 是 JSON null
        assertFalse(GsonUtils.isJsonArray("invalid json"));
    }

    @Test
    void isJsonArray_emptyString() {
        assertFalse(GsonUtils.isJsonArray(""));
    }

    @Test
    void isJsonArray_nullString() {
        assertFalse(GsonUtils.isJsonArray(null));
    }
}
