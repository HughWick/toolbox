package com.github.hugh.mongodb;

import com.github.hugh.mongodb.model.CollectionDto;
import com.github.hugh.util.StringUtils;
import com.github.hugh.util.TimeUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 请求查询 mongodb
 *
 * @author hugh
 */
class RequestQueryTest {
    private static final String CONNECTION_STRING = "mongodb://cmmop:cmmop#8225586@192.168.10.29:7070/cmmop?authSource=admin";

    private static final String collection_name = "collection_test";
    private MongoTemplate mongoTemplate;

    private static final String CREATE_DATE_GE_KEY = "createDate_ge";
    private static final String CREATE_DATE_LE_KEY = "createDate_le";

    private static final String delete_flag = "deleteFlag";

    @AfterEach
    void clean() {
//        mongodExecutable.stop();
    }

    @BeforeEach
    void setup() {
//        String ip = "localhost";
//        int port = 27017;
        if (mongoTemplate == null) {
            System.out.println("=======init====");
            MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
            mongoTemplate = new MongoTemplate(mongoClient, "cmmop");
        }
    }

    @Test
    void testEmpty() {
        NullPointerException nullPointerException = Assertions.assertThrowsExactly(NullPointerException.class, () -> {
            RequestQuery.createPage(null).query();
        });
        assertEquals(null, nullPointerException.getMessage());
        Query query1 = RequestQuery.createPage(new HashMap<>()).query();
        System.out.println("--->" + query1.toString());
        String sqlStr1 = "Query: {}, Fields: {}, Sort: {}";
        assertEquals(sqlStr1, query1.toString());
    }

    @Test
    void testWhere() {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNumber", "123");
        Query query1 = RequestQuery.createPage(map).query();
        String sqlStr1 = "Query: { \"serialNumber\" : \"123\"}, Fields: {}, Sort: {}";
        assertEquals(sqlStr1, query1.toString());
        map.put("flag", 0);
        Query query2 = RequestQuery.createPage(map).query();
        String queryStr2 = "Query: { \"serialNumber\" : \"123\", \"flag\" : 0}, Fields: {}, Sort: {}";
        assertEquals(queryStr2, query2.toString());
    }

    @Test
    void testIn() {
        String key1 = "serialNumber_in";
        Map<String, Object> map = new HashMap<>();
        map.put(key1, "ym_0001,ym_0002");
        Query query1 = RequestQuery.createPage(map).query();
        String queryStr1 = "Query: { \"serialNumber\" : { \"$in\" : [\"ym_0001\", \"ym_0002\"]}}, Fields: {}, Sort: {}";
        assertEquals(queryStr1, query1.toString());
        List<CollectionDto> collectionTests = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(2, collectionTests.size());
    }

    @Test
    void testLike() {
        String key1 = "serialNumber_like";
        Map<String, Object> map = new HashMap<>();
        map.put(key1, "ym");
        Query query1 = RequestQuery.createPage(map).query();
//        String queryStr1 = "Query: { \"serialNumber\" : { \"$in\" : [\"ym_0001\", \"ym_0002\"]}}, Fields: {}, Sort: {}";
//        assertEquals(queryStr1, query1.toString());
        List<CollectionDto> collectionTests = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(7, collectionTests.size());
    }

    @Test
    void testOr() {
        String key1 = "deleteFlag_dataVersion_or";
        Map<String, Object> map = new HashMap<>();
        map.put(key1, 1);
        Query query1 = RequestQuery.createPage(map).query();
        String queryStr1 = "Query: { \"$or\" : [{ \"deleteFlag\" : 1}, { \"dataVersion\" : 1}]}, Fields: {}, Sort: {}";
        assertEquals(queryStr1, query1.toString());
        List<CollectionDto> collectionTests = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(3, collectionTests.size());
    }

    @Test
    void testOrderByDesc() {
        String key1 = "createDate";
        Map<String, Object> map = new HashMap<>();
        map.put("order", "desc");// 降序
        map.put("sort", key1);
        map.put(delete_flag, 1);
        Query query1 = RequestQuery.createPage(map).query();
        String queryStr1 = "Query: { \"deleteFlag\" : 1}, Fields: {}, Sort: { \"createDate\" : -1}";
        assertEquals(queryStr1, query1.toString());
        List<CollectionDto> collectionTests = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(1, collectionTests.size());
        // 验证日期是否按降序排序
        LocalDateTime previousDate = null;
        for (CollectionDto collectionDto : collectionTests) {
            LocalDateTime currentDate = TimeUtils.convert(collectionDto.getCreateDate());
            if (previousDate != null) {
                assertTrue(currentDate.isBefore(previousDate) || currentDate.isEqual(previousDate));
            }
            previousDate = currentDate;
        }
    }

    @Test
    void testOrderByAsc() {
        String key1 = "createDate";
        Map<String, Object> map = new HashMap<>();
        map.put("order", "asc");// 降序
        map.put("sort", key1);
        map.put(delete_flag, 0);
        Query query1 = RequestQuery.createPage(map).query();
        String queryStr1 = "Query: { \"deleteFlag\" : 0}, Fields: {}, Sort: { \"createDate\" : 1}";
        assertEquals(queryStr1, query1.toString());
        List<CollectionDto> collectionTests = mongoTemplate.find(RequestQuery.createQuery(map), CollectionDto.class);
        assertEquals(12, collectionTests.size());
// 验证日期是否按升序排序
        LocalDateTime previousDateTime = null;
        for (CollectionDto collectionDto : collectionTests) {
            LocalDateTime currentDateTime = TimeUtils.convert(collectionDto.getCreateDate());
            if (previousDateTime != null) {
                assertTrue(currentDateTime.isAfter(previousDateTime) || currentDateTime.equals(previousDateTime));
            }
            previousDateTime = currentDateTime;
        }
    }

    @Test
    void testCreateDate() {
        String start_key1 = "startDate";
        String end_key1 = "endDate";
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(delete_flag, 0);
        map.put(start_key1, "2023-12-14 15:29:48.676");
        map.put(end_key1, "2023-12-15 10:51:24.108");
        Query query1 = RequestQuery.createPage(map).query();
//        String queryStr1 = "Query: { \"deleteFlag\" : 1, \"$and\" : [{ \"create_date\" : { \"$lte\" : { \"$date\" : \"2023-12-15T02:51:23Z\"}}}, { \"create_date\" : { \"$gte\" : { \"$date\" : \"2023-12-14T07:29:48Z\"}}}]}, Fields: {}, Sort: {}";
//        assertEquals("", query1.toString());
//        assertEquals(queryStr1, query1.getQueryObject().toJson());
        List<CollectionDto> collectionTests = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(7, collectionTests.size());
        collectionTests.forEach(System.out::println);
    }

    @Test
    void testGte() {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String key_ge_2 = "vintage_ge";
//        String key_le_2 = "vintage_le";
        map1.put(key_ge_2, 2017);
        Query query1 = RequestQuery.createPage(map1).query();
        List<CollectionDto> collectionTests = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(7, collectionTests.size());
        String key_ge_3 = "createDate_ge";
//        String key_le_3 = "createDate_le";
        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put(key_ge_3, "2023-12-15 09:09:50");
        Query query2 = RequestQuery.createPage(map2).query();
//        System.out.println("---->>" + query2.toString());
        List<CollectionDto> collectionTest2 = mongoTemplate.find(query2, CollectionDto.class);
        assertEquals(5, collectionTest2.size());
    }

    @Test
    void testLte() {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String key_ge_2 = "vintage_ge";
        String key_le_2 = "vintage_le";
        map1.put(key_le_2, 2016);
//        map1.put(key_ge_2, 2017);
        Query query1 = RequestQuery.createPage(map1).query();
//        System.out.println("--->>" + query1.toString());
        List<CollectionDto> collectionTests = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(6, collectionTests.size());
        String key_le_3 = "createDate_le";
        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put(key_le_3, "2023-12-15 09:09:50");
        Query query2 = RequestQuery.createPage(map2).query();
//        System.out.println("---->>"+query2.toString());
        List<CollectionDto> collectionTest2 = mongoTemplate.find(query2, CollectionDto.class);
        assertEquals(8, collectionTest2.size());
    }

    @Test
    void testGteAndLteDate() {
//        String key_ge = "createDate_ge";
        String key_ge = CREATE_DATE_GE_KEY;
        String key_le = "createDate_le";
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(delete_flag, 0);
        map.put(key_ge, "2023-12-14 15:29:48.676");
        map.put(key_le, "2023-12-15 10:51:24.108");
        Query query1 = RequestQuery.createPage(map).query();
//        System.out.println("--->>" + query1.toString());
        List<CollectionDto> collectionTests = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(7, collectionTests.size());
        // 测试le在前
        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put(delete_flag, 1);
        map2.put(key_le, "2023-12-15 10:51:24.108");
        map2.put(key_ge, "2023-12-14 15:29:48.676");
        Query query2 = RequestQuery.createPage(map2).query();
//        System.out.println("--->>" + query1.toString());
        List<CollectionDto> collectionTest2 = mongoTemplate.find(query2, CollectionDto.class);
        assertEquals(1, collectionTest2.size());
    }

    @Test
    void testGteAndLte() {
        Map<String, Object> map1 = new LinkedHashMap<>();
        String key_ge_2 = "vintage_ge";
        String key_le_2 = "vintage_le";
        map1.put(key_le_2, 2023);
        map1.put(key_ge_2, 2017);
        System.out.println("--->" + map1);
        Query query1 = RequestQuery.createPage(map1).query();
        List<CollectionDto> collectionTest1 = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(7, collectionTest1.size());
        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put(key_ge_2, 2020);
        map2.put(key_le_2, 2023);
        System.out.println("--->" + map2);
        Query query2 = RequestQuery.createPage(map2).query();
        List<CollectionDto> collectionTest2 = mongoTemplate.find(query2, CollectionDto.class);
        assertEquals(4, collectionTest2.size());
    }

    @Test
    void testPage() {
        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("page", 1);
        map1.put("size", 2);
        Query query1 = RequestQuery.createPage(map1).query();
        List<CollectionDto> collectionTest1 = mongoTemplate.find(query1, CollectionDto.class);
        assertEquals(2, collectionTest1.size());
        for (CollectionDto collectionDto : collectionTest1) {
            assertTrue(collectionDto.getSerialNumber().equals("123") || collectionDto.getSerialNumber().equals("test1"));
        }
        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("page", 1);
        map2.put("size", 5);
        map2.put(delete_flag, 0);
        Query query2 = RequestQuery.createPage(map2).query();
        List<CollectionDto> collectionTest2 = mongoTemplate.find(query2, CollectionDto.class);
        assertEquals(5, collectionTest2.size());
    }

    @Test
    void testPage02() {
        int page = 1;
        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("page", page);
        map1.put("size", 5);
        Query query1 = RequestQuery.createPage(map1).query();
        assertEquals(page - 1, query1.getSkip());
        assertEquals(5, query1.getLimit());
//        System.out.println("---->>" + query1);
//        System.out.println("=====map=>>" + map1);
        Query pageQuery = RequestQuery.createPageQuery(map1);
        assertEquals(pageQuery.toString(), query1.toString());
    }

    @Test
    void test02() {
        String key1 = "serialNo_like";
        key1.toUpperCase();
        System.out.println("--->" + key1);
        String before = StringUtils.before(key1, "_");
        System.out.println("--1---->" + before);
    }
}
