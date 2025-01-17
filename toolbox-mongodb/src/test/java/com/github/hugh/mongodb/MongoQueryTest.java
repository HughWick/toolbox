package com.github.hugh.mongodb;

import com.github.hugh.IdSequence;
import com.github.hugh.constant.DateCode;
import com.github.hugh.mongodb.exception.ToolboxMongoException;
import com.github.hugh.mongodb.model.CollectionDto;
import com.github.hugh.util.DateUtils;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author AS
 * @date 2023/12/13 17:13
 */
class MongoQueryTest {
    private static final String databaseName = "test_01";
    public static final String CONNECTION_STRING = "mongodb://hugh:8225586@47.79.38.215:7070/" + databaseName + "?authSource=admin";

    private static final String SERIAL_NUMBER = "serial_number";
    private static final String CREATE_DATE = "createDate";
//    private static final String CONNECTION_STRING = "mongodb://cmmop:cmmop#8225586@192.168.10.29:7070/cmmop?authSource=admin";

    private static final String collection_name = "collection_test";

    private MongoTemplate mongoTemplate;

    private static boolean INIT_DATA_FLAG = true;

    private CollectionDto createCollectionDto(String serialNumber, int flag, int status, String createBy, String createDateString, int vintage, int deleteFlag) {
        CollectionDto collectionDto = new CollectionDto();
        collectionDto.setSerialNo(IdSequence.snowflake());
        collectionDto.setSerialNumber(serialNumber);
        collectionDto.setFlag(flag); // 设置默认值或从外部传入
        collectionDto.setStatus(status);
        collectionDto.setDeleteFlag(deleteFlag);
        collectionDto.setDataVersion(0);
        collectionDto.setCreateBy(createBy);
        collectionDto.setCreateDate(DateUtils.parse(createDateString));
        collectionDto.setVintage(vintage);
        return collectionDto;
    }

    @AfterEach
    void clean() {
//        mongodExecutable.stop();
    }

    List<CollectionDto> items = Arrays.asList(
            createCollectionDto("123", 0, 0, null, "2023-11-04 02:24:32.123", 2011, 0),
            createCollectionDto("test1", 0, 1, null, "2023-12-05 02:25:29.456", 2012, 0),
            createCollectionDto("test2", 0, 0, null, "2023-12-06 02:47:47.333", 2013, 0),
            createCollectionDto("D901", 1, 1, null, "2023-12-07 07:08:24.321", 2014, 0),
            createCollectionDto("D101", 1, 1, "null", "2023-12-08 07:29:48.369", 2015, 0),
            createCollectionDto("test03", 1, 0, "", "2023-12-09 07:54:54.741", 2016, 1),

            createCollectionDto("ym_0001", 1, 0, "盘满", "2023-12-10 00:49:38", 2017, 0),
            createCollectionDto("ym_0002", 0, 0, "熊锦明", "2023-12-11 00:50:45", 2018, 0),
            createCollectionDto("YM_0003", 0, 0, "叶文洁", "2023-12-12 01:09:50", 2019, 0),
            createCollectionDto("YM_0004", 0, 0, "何德之", "2023-12-13 02:45:47", 2020, 0),
            createCollectionDto("Ym_0005", 0, 0, "李金玲", "2023-12-14 02:48:03", 2021, 0),
            createCollectionDto("Ym_0006", 0, 0, "周冬键", "2023-12-15 02:51:23", 2022, 0),
            createCollectionDto("Ym_0007", 0, 0, "爱寺米", "2024-01-19 00:47:15", 2023, 0)
    );

    @BeforeEach
    void setup() {
        if (mongoTemplate == null) {
            MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
            mongoTemplate = new MongoTemplate(mongoClient, databaseName);
        }
        if (INIT_DATA_FLAG) {
            for (CollectionDto collectionDto : items) {
                // 检查集合中是否已经有数据，这里假设至少有一个你想要初始化的数据存在就认为已经初始化过了
                Map<String, Object> params = new HashMap<>();
                params.put("serial_number", collectionDto.getSerialNumber());
                Query query = RequestQuery.create(params).query();
                boolean alreadyInitialized = mongoTemplate.exists(query, collection_name);
                if (alreadyInitialized) {
//                System.out.println("数据已存在，跳过初始化。");
                } else {
                    mongoTemplate.save(collectionDto, collection_name);
                }
            }
        }
        INIT_DATA_FLAG = false;
    }

    @Test
    void testInit() {
        // 添加断言，验证所有 items 中的数据都已存在于数据库中
        for (CollectionDto collectionDto : items) {
            Map<String, Object> params = new HashMap<>();
            params.put("serial_number", collectionDto.getSerialNumber());
            Query query = RequestQuery.create(params).query();
            boolean existsAfterOperation = mongoTemplate.exists(query, collection_name);
            assertTrue(existsAfterOperation, "断言失败: 数据初始化后，serialNumber为 " + collectionDto.getSerialNumber() + " 的数据应该存在。");
        }
    }

    @Test
    void testFindWhere() {
        MongoQuery mongoQuery = new MongoQuery();
        mongoQuery.where(SERIAL_NUMBER, "test1");
        CollectionDto one = mongoTemplate.findOne(mongoQuery.query(), CollectionDto.class);
        assertNotNull(one);
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.where("flag", 0);
        mongoQuery2.where("status", 0);
        CollectionDto one2 = mongoTemplate.findOne(mongoQuery2.query(), CollectionDto.class);
//        System.out.println("---->" + one2);
        assertNotNull(one2);
        MongoQuery mongoQuery3 = new MongoQuery();
//        mongoQuery3.where("flag", 0);
        mongoQuery3.where("status", "0");
        CollectionDto one3 = mongoTemplate.findOne(mongoQuery3.query(), CollectionDto.class);
//        System.out.println("---->" + one2);
    }

    @Test
    void testFindLike() {
        MongoQuery mongoQuery = new MongoQuery();
        mongoQuery.like(SERIAL_NUMBER, "ym");
        List<CollectionDto> collectionTests = mongoTemplate.find(mongoQuery.query(), CollectionDto.class);
        assertEquals(2, collectionTests.size());
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.likeIgnoreCase(SERIAL_NUMBER, "ym");
        List<CollectionDto> collectionTests2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class);
        assertEquals(7, collectionTests2.size());
    }

    @Test
    void testFindOr() {
        MongoQuery mongoQuery = new MongoQuery();
        mongoQuery.where("status", 1)
                .or("flag", 1);
        List<CollectionDto> collectionTests = mongoTemplate.find(mongoQuery.query(), CollectionDto.class);
        assertEquals(2, collectionTests.size());
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.or(Lists.newArrayList("flag", "status"), Lists.newArrayList(1, 1));
        List<CollectionDto> collectionTests2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class);
        assertEquals(5, collectionTests2.size());
        final ToolboxMongoException mongoQueryException1 = assertThrowsExactly(ToolboxMongoException.class, () -> MongoQuery.on().or(new ArrayList<>(), Lists.newArrayList(11)));
        assertEquals("keys和values不能为空", mongoQueryException1.getMessage());
        MongoQuery.on().or(Lists.newArrayList("flag", "status"), Lists.newArrayList(11));
//        final ToolboxMongoException mongoQueryException2 = assertThrowsExactly(
//                ToolboxMongoException.class, () ->
//                MongoQuery.on().or(Lists.newArrayList("flag", "status"), Lists.newArrayList(11)));
//        assertEquals("keys和values的数量不匹配", mongoQueryException2.getMessage());
    }

    // 降序
    @Test
    void testFindOrderByDesc() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.where("flag", 0)
                .like(SERIAL_NUMBER, "test")
                .orderBy(CREATE_DATE, "desc");
        List<CollectionDto> collection = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(2, collection.size());
        // 验证集合不为空
        assertNotNull(collection, "返回的集合不能为空");
        // 验证集合按照指定属性降序排列
        for (int i = 0; i < collection.size() - 1; i++) {
            Date currentDate = collection.get(i).getCreateDate();
            Date nextDate = collection.get(i + 1).getCreateDate();
            assertTrue(currentDate.compareTo(nextDate) >= 0,
                    "集合中的元素未按照 create_date 属性降序排列");
        }
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.where("flag", 0)
                .like(SERIAL_NUMBER, "test")
                .orderByDesc(CREATE_DATE);
        List<CollectionDto> collection2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class);
        assertEquals(2, collection2.size());
        // 验证集合不为空
        assertNotNull(collection2, "返回的集合不能为空");
        // 验证集合按照指定属性降序排列
        for (int i = 0; i < collection2.size() - 1; i++) {
            Date currentDate = collection2.get(i).getCreateDate();
            Date nextDate = collection2.get(i + 1).getCreateDate();
            assertTrue(currentDate.compareTo(nextDate) >= 0,
                    "集合中的元素未按照 create_date 属性降序排列");
        }
    }

    // 升序
    @Test
    void testOrderByAsc() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.orderBy(CREATE_DATE, "asc");
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        // 验证集合不为空
        assertNotNull(collection1, "返回的集合不能为空");
        // 验证集合按照指定属性升序排列
        for (int i = 0; i < collection1.size() - 1; i++) {
            Date currentDate = collection1.get(i).getCreateDate();
            Date nextDate = collection1.get(i + 1).getCreateDate();
            assertTrue(currentDate.compareTo(nextDate) <= 0,
                    "集合中的元素未按照 create_date 属性升序排列");
        }
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.orderByAsc(CREATE_DATE);
        List<CollectionDto> collection2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class);
        // 验证集合不为空
        assertNotNull(collection2, "返回的集合不能为空");
        // 验证集合按照指定属性升序排列
        for (int i = 0; i < collection2.size() - 1; i++) {
            Date currentDate = collection2.get(i).getCreateDate();
            Date nextDate = collection2.get(i + 1).getCreateDate();
            assertTrue(currentDate.compareTo(nextDate) <= 0,
                    "集合中的元素未按照 create_date 属性升序排列");
        }
    }

    @Test
    void testPage() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.like(SERIAL_NUMBER, "test").page(1, 1);
        CollectionDto one = mongoTemplate.findOne(mongoQuery1.query(), CollectionDto.class);
        assertNotNull(one, "返回的实体不能为空");
        assertEquals("test1", one.getSerialNumber());
        MongoQuery mongoQuery2 = new MongoQuery();
//        mongoQuery2.like(SERIAL_NUMBER, "test");
        mongoQuery2.page(1, 1);
        CollectionDto one2 = mongoTemplate.findOne(mongoQuery2.query(), CollectionDto.class);
        assertNotNull(one2, "返回的实体不能为空");
        assertEquals("123", one2.getSerialNumber());
    }

    @Test
    void testRegex() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.regex(SERIAL_NUMBER, "D");
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(2, collection1.size());
    }

    @Test
    void testIn() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.in(SERIAL_NUMBER, "test1", "test2");
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(2, collection1.size());
    }

    @Test
    void testGt() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.gt(CREATE_DATE, DateUtils.parse("2023-12-15 08:49:38.795", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(1, collection1.size());
    }

    //同时满足大于和小于
    @Test
    void testGtAndLt() {
        MongoQuery mongoQuery1 = new MongoQuery();
//        mongoQuery1.gt("createDate", DateUtils.parse("2023-12-14 15:08:24.289"));
        mongoQuery1.gtAndLt(CREATE_DATE, DateUtils.parse("2023-11-04 02:24:32.122"), DateUtils.parse("2023-12-05 02:25:29.456"));
//        mongoQuery1.gt(CREATE_DATE, DateUtils.parse("2023-12-14 15:08:24.289", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS))
//                .lt(CREATE_DATE ,DateUtils.parse("2023-12-14 15:54:54.684", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS) );
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(1, collection1.size());
    }

    @Test
    void testGte() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.gte(CREATE_DATE, DateUtils.parse("2023-12-14 15:08:24.289"));
//        mongoQuery1.gte("createDate", ("2023-12-14 15:08:24.289"));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(2, collection1.size());
    }

    @Test
    void testLt() {
        MongoQuery mongoQuery1 = new MongoQuery();
//        mongoQuery1.gt("createDate", DateUtils.parse("2023-12-14 15:08:24.289"));
        mongoQuery1.lt(CREATE_DATE, DateUtils.parse("2023-12-14 10:47:47.47", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(11, collection1.size());
    }

    @Test
    void testLte() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.lte(CREATE_DATE, DateUtils.parse("2023-12-14 15:08:24.289", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(11, collection1.size());
    }

    // 满足大于等于和小于等于
    @Test
    void testLtAndGt() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.gteAndLte(CREATE_DATE,
                DateUtils.parse("2023-12-06 02:47:47.333"),
                DateUtils.parse("2023-12-09 07:54:54.741"));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(4, collection1.size());
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.gteAndLte(CREATE_DATE,
                        DateUtils.parse("2023-12-08 07:29:48.369"),
                        DateUtils.parse("2023-12-11 00:50:45"))
                .where("delete_flag", 0);
        List<CollectionDto> collection2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class);
        assertEquals(3, collection2.size());
    }

    // 空字符串查询
    @Test
    void testIsBlank() {
        String sqlStr = "Query: { \"createBy\" : \"\"}, Fields: {}, Sort: {}";
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.isBlank("createBy");
        assertEquals(sqlStr, mongoQuery1.query().toString());
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(1, collection1.size());
    }

    @Test
    void testIsEmpty() {
        String sqlStr = "Query: { \"$or\" : [{ \"createBy\" : \"\"}, { \"createBy\" : null}]}, Fields: {}, Sort: {}";
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.isEmpty("createBy");
        assertEquals(sqlStr, mongoQuery1.query().toString());
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(5, collection1.size());
    }
}
