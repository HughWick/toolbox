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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author AS
 * @date 2023/12/13 17:13
 */
class MongoQueryTest {

    private static final String SERIAL_NUMBER = "serial_number";
    private static final String CREATE_DATE = "createDate";
    private static final String CONNECTION_STRING = "mongodb://cmmop:cmmop#8225586@192.168.10.29:27018/cmmop?authSource=admin";

    private static final String collection_name = "collection_test";
    private MongoTemplate mongoTemplate;


    @AfterEach
    void clean() {
//        mongodExecutable.stop();
    }

    @BeforeEach
    void setup() {
//        String ip = "localhost";
//        int port = 27017;
        System.out.println("=======init====");
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        mongoTemplate = new MongoTemplate(mongoClient, "cmmop");
    }

    @DisplayName("given object to save"
            + " when save object using MongoDB template"
            + " then object is saved")
    @Test
    void testAdd() {
        // given
//        DBObject objectToSave = BasicDBObjectBuilder.start()
//                .add("key", "value1")
//                .get();
        CollectionDto collectionTest = new CollectionDto();
        collectionTest.setSerialNo(IdSequence.snowflake());
        collectionTest.setSerialNumber("Ym_0006");
        collectionTest.setFlag(0);
        collectionTest.setStatus(0);
        collectionTest.setDeleteFlag(1);
        collectionTest.setDataVersion(0);
        collectionTest.setCreateBy("爱寺米");
        collectionTest.setCreateDate(new Date());
        collectionTest.setVintage(2023);
        // when
        mongoTemplate.save(collectionTest, collection_name);
        // then
        List<CollectionDto> collection = mongoTemplate.findAll(CollectionDto.class, collection_name);
        collection.forEach(System.out::println);
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
        System.out.println("---->" + one2);
        assertNotNull(one2);
        MongoQuery mongoQuery3 = new MongoQuery();
//        mongoQuery3.where("flag", 0);
        mongoQuery3.where("status", "0");
        CollectionDto one3 = mongoTemplate.findOne(mongoQuery3.query(), CollectionDto.class);
        System.out.println("---->" + one2);
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
        assertEquals(3, collectionTests2.size());
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
        final ToolboxMongoException mongoQueryException2 = assertThrowsExactly(ToolboxMongoException.class, () -> MongoQuery.on().or(Lists.newArrayList("flag", "status"), Lists.newArrayList(11)));
        assertEquals("keys和values的数量不匹配", mongoQueryException2.getMessage());
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
        assertEquals(2, collection1.size());
    }

    @Test
    void testGtAndLt() {
        MongoQuery mongoQuery1 = new MongoQuery();
//        mongoQuery1.gt("createDate", DateUtils.parse("2023-12-14 15:08:24.289"));
        mongoQuery1.gtAndLt(CREATE_DATE, DateUtils.parse("2023-12-14 15:08:24.289", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS),
                DateUtils.parse("2023-12-14 15:54:54.684", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS));
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
        assertEquals(6, collection1.size());
    }

    @Test
    void testLt() {
        MongoQuery mongoQuery1 = new MongoQuery();
//        mongoQuery1.gt("createDate", DateUtils.parse("2023-12-14 15:08:24.289"));
        mongoQuery1.lt(CREATE_DATE, DateUtils.parse("2023-12-14 10:47:47.47", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(2, collection1.size());
    }

    @Test
    void testLte() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.lte(CREATE_DATE, DateUtils.parse("2023-12-14 15:08:24.289", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(4, collection1.size());
    }

    @Test
    void testLtAndGt() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.gteAndLte(CREATE_DATE,
                DateUtils.parse("2023-12-14 10:25:29"),
                DateUtils.parse("2023-12-14 15:29:48.676", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class);
        assertEquals(4, collection1.size());
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.gteAndLte(CREATE_DATE,
                        DateUtils.parse("2023-12-14 10:25:29"),
                        DateUtils.parse("2023-12-14 15:29:48.676", DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC_SSS))
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
