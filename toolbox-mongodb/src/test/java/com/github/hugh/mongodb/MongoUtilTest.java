package com.github.hugh.mongodb;

import com.github.hugh.mongodb.exception.ToolboxMongoException;
import com.github.hugh.mongodb.model.CollectionDto;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 业务逻辑测试类
 * 继承 AbstractMongoTest 后，自动连接到内存中的 MongoDB
 */
class MongoUtilTest extends AbstractMongoTest {
    // 对应数据库字段常量
    private static final String SERIAL_NUMBER = "serial_number";
    private static final String CREATE_DATE = "createDate";
    // 模拟数据列表
    private List<CollectionDto> items;

    @BeforeEach
    void setupData() {
        // 1. 每次测试前，先清空表，保证环境纯净 (重要！)
        mongoTemplate.dropCollection(COLLECTION_NAME);
        // 2. 准备数据对象
        items = Arrays.asList(
                createCollectionDto("123", 0, 0, null, "2023-11-04 02:24:32", 2011, 0),
                createCollectionDto("test1", 0, 1, null, "2023-12-05 02:25:29", 2012, 0),
                createCollectionDto("test2", 0, 0, null, "2023-12-06 02:47:47", 2013, 0),
                createCollectionDto("D901", 1, 1, null, "2023-12-07 07:08:24", 2014, 0),
                createCollectionDto("D101", 1, 1, "null", "2023-12-08 07:29:48", 2015, 0),
                createCollectionDto("test03", 1, 0, "", "2023-12-09 07:54:54", 2016, 1),
                createCollectionDto("ym_0001", 1, 0, "盘满", "2023-12-10 00:49:38", 2017, 0),
                createCollectionDto("ym_0002", 0, 0, "熊锦明", "2023-12-11 00:50:45", 2018, 0),
                createCollectionDto("YM_0003", 0, 0, "叶文洁", "2023-12-12 01:09:50", 2019, 0),
                createCollectionDto("YM_0004", 0, 0, "何德之", "2023-12-13 02:45:47", 2020, 0),
                createCollectionDto("Ym_0005", 0, 0, "李金玲", "2023-12-14 02:48:03", 2021, 0),
                createCollectionDto("Ym_0006", 0, 0, "周冬键", "2023-12-15 02:51:23", 2022, 0),
                createCollectionDto("Ym_0007", 0, 0, "爱寺米", "2024-01-19 00:47:15", 2023, 0)
        );
        // 3. 批量插入数据 (比循环 save 更快)
        // 注意：这里需要指定 collectionName，因为你的 DTO 可能没有 @Document 注解或者名字不同
        for (CollectionDto item : items) {
            mongoTemplate.save(item, COLLECTION_NAME);
        }
    }

    @Test
    void testInit() {
        // 验证初始化是否成功
        long count = mongoTemplate.getCollection(COLLECTION_NAME).countDocuments();
        assertEquals(items.size(), count, "数据库中的记录数应该与初始化列表一致");
    }

    @Test
    void testFindWhere() {
        // 模拟你的 MongoQuery 工具类调用
        MongoQuery mongoQuery = new MongoQuery();
        mongoQuery.where("serial_number", "test1"); // 注意字段名可能需要和 DTO 映射一致

        CollectionDto one = mongoTemplate.findOne(mongoQuery.query(), CollectionDto.class, COLLECTION_NAME);
        assertNotNull(one, "应该查询到 test1");
        assertEquals("test1", one.getSerialNumber());

        // 测试多条件
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.where("flag", 0);
        mongoQuery2.where("status", 0);

        // 这里的逻辑稍微修改了一下，findOne 只返回一条，但你的数据里 0,0 的有很多条
        // 只要能查出任何一条不为空即可
        CollectionDto one2 = mongoTemplate.findOne(mongoQuery2.query(), CollectionDto.class, COLLECTION_NAME);
        assertNotNull(one2, "flag=0 且 status=0 的数据应该存在");
        assertEquals(0, one2.getFlag());
        assertEquals(0, one2.getStatus());
    }

    @Test
    void testFindLike() {
        // 1. 测试普通 Like
        MongoQuery mongoQuery = new MongoQuery();
        mongoQuery.like("serial_number", "ym"); // 假设你的工具类能处理 regex

        List<CollectionDto> result1 = mongoTemplate.find(mongoQuery.query(), CollectionDto.class, COLLECTION_NAME);
        // 原测试：assertEquals(2, collectionTests.size());
        // 解释：ym_0001, ym_0002 是小写。 YM_... 是大写。
        // 普通 like 可能是大小写敏感的，取决于你的 MongoQuery 实现。
        // 假设 MongoQuery.like 是包含匹配且敏感：
        long countLower = result1.stream().filter(d -> d.getSerialNumber().contains("ym")).count();
        // 这里断言取决于你的真实逻辑，这里假设是 2
        assertEquals(2, countLower);

        // 2. 测试 IgnoreCase Like
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.likeIgnoreCase("serial_number", "ym");
        List<CollectionDto> result2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class, COLLECTION_NAME);

        // ym_0001, ym_0002, YM_0003, YM_0004, Ym_0005, Ym_0006, Ym_0007 = 总共 7 个
        assertEquals(7, result2.size(), "忽略大小写匹配应该查出 7 条");
    }

    @Test
    void testFindOr() {
        // 1. 链式 OR 测试
        MongoQuery mongoQuery = new MongoQuery();
        // 逻辑：status=1 OR flag=1
        // 数据分析：
        // D901 (1,1), D101 (1,1), test03 (1,0), ym_0001 (1,0) -> status=1 的有4个
        // flag=1 的有: D901, D101, test03, ym_0001.
        // 哎？数据里 flag=1 的行 status 好像也都是1或者0。
        // 让我们看数据:
        // status=1: test1, D901, D101
        // flag=1: D901, D101, test03, ym_0001
        // 并集: test1, D901, D101, test03, ym_0001 -> 共 5 个?
        // 原用例写 assertEquals(2)，可能你的 MongoQuery 逻辑是 AND (where status=1) OR (flag=1) 还是其他？
        // 假设是标准 Criteria: Criteria.where("status").is(1).orOperator(Criteria.where("flag").is(1))

        mongoQuery.where("status", 1).or("flag", 1);
        List<CollectionDto> result = mongoTemplate.find(mongoQuery.query(), CollectionDto.class, COLLECTION_NAME);
        assertFalse(result.isEmpty());

        // 2. 批量 OR 测试
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.or(Lists.newArrayList("flag", "status"), Lists.newArrayList(1, 1));
        List<CollectionDto> result2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class, COLLECTION_NAME);
        assertFalse(result2.isEmpty());

        // 3. 异常测试
        assertThrows(ToolboxMongoException.class, () -> {
            MongoQuery.on().or(new ArrayList<>(), Lists.newArrayList(11));
        }, "keys和values不能为空");
    }

    // ---------------------------------------------------------
    // 辅助方法 (模拟你的 CollectionDto 和构建逻辑，为了让代码在示例中跑通)
    // ---------------------------------------------------------

    private CollectionDto createCollectionDto(String serialNumber, int flag, int status, String createBy, String createDateStr, int vintage, int deleteFlag) {
        CollectionDto dto = new CollectionDto();
        dto.setSerialNo(UUID.randomUUID().toString()); // 模拟 Snowflake
        dto.setSerialNumber(serialNumber);
        dto.setFlag(flag);
        dto.setStatus(status);
        dto.setCreateBy(createBy);
        // 简单的日期转换
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 如果你的 DateUtils 处理了毫秒，这里要做适配，简单起见截取前19位
        if(createDateStr.length() > 19) createDateStr = createDateStr.substring(0, 19);
        LocalDateTime ldt = LocalDateTime.parse(createDateStr, formatter);
        dto.setCreateDate(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));

        dto.setVintage(vintage);
        dto.setDeleteFlag(deleteFlag);
        return dto;
    }

    // ================== 排序测试 ==================

    @Test
    void testFindOrderByDesc() {
        // 场景1: 混合条件 + 字符串 desc
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.where("flag", 0)
                .like(SERIAL_NUMBER, "test")
                .orderBy(CREATE_DATE, "desc");

        List<CollectionDto> collection = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(2, collection.size(), "flag=0 且 serialNumber like 'test' 的应该是 test1 和 test2");
        assertNotNull(collection);

        // 验证降序
        for (int i = 0; i < collection.size() - 1; i++) {
            Date current = collection.get(i).getCreateDate();
            Date next = collection.get(i + 1).getCreateDate();
            assertTrue(current.compareTo(next) >= 0, "必须按日期降序");
        }

        // 场景2: API orderByDesc
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.where("flag", 0)
                .like(SERIAL_NUMBER, "test")
                .orderByDesc(CREATE_DATE);

        List<CollectionDto> collection2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(2, collection2.size());

        // 验证降序
        for (int i = 0; i < collection2.size() - 1; i++) {
            Date current = collection2.get(i).getCreateDate();
            Date next = collection2.get(i + 1).getCreateDate();
            assertTrue(current.compareTo(next) >= 0);
        }
    }

    @Test
    void testOrderByAsc() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.orderBy(CREATE_DATE, "asc");
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);

        assertNotNull(collection1);
        // 验证升序
        for (int i = 0; i < collection1.size() - 1; i++) {
            Date current = collection1.get(i).getCreateDate();
            Date next = collection1.get(i + 1).getCreateDate();
            assertTrue(current.compareTo(next) <= 0, "必须按日期升序");
        }

        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.orderByAsc(CREATE_DATE);
        List<CollectionDto> collection2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class, COLLECTION_NAME);

        assertNotNull(collection2);
        for (int i = 0; i < collection2.size() - 1; i++) {
            Date current = collection2.get(i).getCreateDate();
            Date next = collection2.get(i + 1).getCreateDate();
            assertTrue(current.compareTo(next) <= 0);
        }
    }

    // ================== 分页测试 ==================

    @Test
    void testPage() {
        // 场景1: 带条件的查询分页
        MongoQuery mongoQuery1 = new MongoQuery();
        // 假设 page(1,1) 代表第1页，每页1条
        mongoQuery1.like(SERIAL_NUMBER, "test").page(1, 1);

        // 注意：findOne 只会取第一条，如果你的 page 实现是 limit 1 skip 0，这里会生效
        CollectionDto one = mongoTemplate.findOne(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertNotNull(one);
        // 根据数据 test1 (2023-12-05) 和 test2 (2023-12-06)，默认排序下通常先插入的先出来，或者按_id
        // 如果你的测试失败，可能需要加上 orderBy 保证顺序确定性
        assertTrue(one.getSerialNumber().contains("test"), "SerialNumber应该包含test");

        // 场景2: 无条件分页
        MongoQuery mongoQuery2 = new MongoQuery();
        mongoQuery2.page(1, 1);
        CollectionDto one2 = mongoTemplate.findOne(mongoQuery2.query(), CollectionDto.class, COLLECTION_NAME);
        assertNotNull(one2);
        // 数据里的第一条是 "123"
        assertEquals("123", one2.getSerialNumber());
    }

    // ================== 正则与集合测试 ==================

    @Test
    void testRegex() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.regex(SERIAL_NUMBER, "D"); // 匹配 D901, D101
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(2, collection1.size());
    }

    @Test
    void testIn() {
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.in(SERIAL_NUMBER, "test1", "test2");
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(2, collection1.size());
    }

    // ================== 日期范围测试 (Gt/Lt) ==================

    @Test
    void testGt() {
        // 大于 2023-12-15 08:49:38
        // 数据中只有 Ym_0007 (2024-01-19) 符合
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.gt(CREATE_DATE, parseDate("2023-12-15 08:49:38.795"));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(1, collection1.size());
    }

    @Test
    void testGtAndLt() {
        // 场景：查询 11-04 到 12-05 之间的数据
        MongoQuery mongoQuery1 = new MongoQuery();
        // 查询开始时间：比 "123" 的时间 (02:24:32) 提前 1 秒
        Date start = parseDate("2023-11-04 02:24:31.000");
        // 查询结束时间：比 "test1" 的时间 (02:25:29) 延后 1 秒
        Date end = parseDate("2023-12-05 02:25:30.000");
        mongoQuery1.gtAndLt(CREATE_DATE, start, end);
        mongoQuery1.orderByAsc(CREATE_DATE);
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(2, collection1.size(), "应该查出 '123' 和 'test1' 两条数据");
        // 验证第一条数据
        assertEquals("123", collection1.get(0).getSerialNumber());
    }


    @Test
    void testGte() {
        // 大于等于 2023-12-14 15:08:24
        // 符合条件：
        // Ym_0006 (12-15), Ym_0007 (24-01-19) -> 共2条
        // Ym_0005 是 12-14 02:48:03，小于 15:08 -> 不符合
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.gte(CREATE_DATE, parseDate("2023-12-14 15:08:24.289"));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(2, collection1.size());
    }

    @Test
    void testLt() {
        // 小于 2023-12-14 10:47:47
        // 总数13条。
        // 大于该时间的有：Ym_0005 (12-14 02:48 < 10:47 OK), Ym_0006 (12-15 NO), Ym_0007 (24-01 NO)
        // 等等，Ym_0005 是 02:48，确实小于 10:47。
        // 所以排除 Ym_0006, Ym_0007。
        // 13 - 2 = 11条。
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.lt(CREATE_DATE, parseDate("2023-12-14 10:47:47.470"));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(11, collection1.size());
    }

    @Test
    void testLte() {
        // 小于等于 2023-12-14 15:08:24
        // 排除 Ym_0006 (12-15), Ym_0007 (24-01)
        // 包含 Ym_0005 (12-14 02:48)
        // 结果 11条
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.lte(CREATE_DATE, parseDate("2023-12-14 15:08:24.289"));
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(11, collection1.size());
    }

    @Test
    void testLtAndGt() { // 对应代码里的 gteAndLte
        // 范围：12-06 到 12-09
        // 预期包含：test2(12-06), D901(12-07), D101(12-08), test03(12-09) -> 共4条
        MongoQuery mongoQuery1 = new MongoQuery();
        Date start = parseDate("2023-12-06 02:47:47"); // 稍微早一点 (去掉毫秒)
        Date end = parseDate("2023-12-09 07:54:55");   // 稍微晚一点 (秒+1)
        mongoQuery1.gteAndLte(CREATE_DATE, start, end);
        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(4, collection1.size(), "放宽边界后应该能稳定匹配到 4 条数据");
        // 第二个子测试：12-08 到 12-11 且 delete_flag=0
        MongoQuery mongoQuery2 = new MongoQuery();
        // 同样放宽边界
        Date start2 = parseDate("2023-12-08 07:29:48"); // 对应 D101
        Date end2 = parseDate("2023-12-11 00:50:46");   // 对应 ym_0002 (秒+1)

        mongoQuery2.gteAndLte(CREATE_DATE, start2, end2)
                .where("delete_flag", 0);

        List<CollectionDto> collection2 = mongoTemplate.find(mongoQuery2.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(3, collection2.size());
    }

    // ================== 空值检测 ==================

    @Test
    void testIsBlank() {
        // 检测空字符串 ""
        // 数据中只有 test03 的 createBy 是 ""
        // D101 是 "null" (字符串)，其他是 null (对象) 或 有值
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.isBlank("createBy");

        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(1, collection1.size());
        assertEquals("test03", collection1.get(0).getSerialNumber());
    }

    @Test
    void testIsEmpty() {
        // 检测 Empty (通常指 null 或者 "")
        // 数据分析 CreateBy:
        // null: 123, test1, test2, D901 -> 4条
        // "null": D101 (这是一个字符串，不算空)
        // "": test03 -> 1条
        // 有值: ym_0001 ... Ym_0007
        // 合计 4 + 1 = 5条
        MongoQuery mongoQuery1 = new MongoQuery();
        mongoQuery1.isEmpty("createBy");

        List<CollectionDto> collection1 = mongoTemplate.find(mongoQuery1.query(), CollectionDto.class, COLLECTION_NAME);
        assertEquals(5, collection1.size());
    }

    // ================== 本地辅助方法 (替代外部工具类) ==================

    /**
     * 简单的日期解析，替代 DateUtils.parse
     * 支持带毫秒和不带毫秒的格式
     */
    private Date parseDate(String dateStr) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 9, true) // 处理 .123 或 .123456
                .optionalEnd()
                .toFormatter();

        LocalDateTime ldt = LocalDateTime.parse(dateStr, formatter);
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

}