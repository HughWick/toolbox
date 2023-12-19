package com.github.hugh.mongodb;

import com.github.hugh.constant.QueryCode;
import com.github.hugh.constant.StrPool;
import com.github.hugh.util.*;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * mongodb 请求查询封装类
 *
 * @author hugh
 * @since 2.7.0
 */
public class RequestQuery {
    private RequestQuery() {
    }

    private static Map<String, Boolean> existMap;

    /**
     * 表示大于等于（Greater than or equal to）。
     */
    private static final String GE = "_ge";

    /**
     * 表示小于等于（Less than or equal to）。
     */
    private static final String LE = "le";

    /**
     * 在LE前面添加了下划线。
     */
    private static final String UNDERLINE_LE = StrPool.UNDERLINE + LE;

    /**
     * 表示模糊搜索。
     */
    private static final String LIKE = "_like";

    /**
     * 表示查询字段的值包含在指定集合中。
     */
    private static final String IN_FIELD_NAME = "_in";

    /**
     * 表示查询字段的值满足指定条件之一。
     */
    private static final String OR_FIELD_NAME = "_or";

    /**
     * 表示日期。
     */
    private static final String DATE = "date";

    /**
     * 表示排序。
     */
    private static final String SORT = "sort";

    /**
     * 表示排序方式。
     */
    private static final String ORDER = "order";

    /**
     * 表示开始日期。
     */
    private static final String START_DATE = "startDate";

    /**
     * 表示结束日期。
     */
    private static final String END_DATE = "endDate";

    /**
     * 根据给定的参数创建一个Query对象。
     *
     * @param params 参数Map，包含查询条件和配置信息。
     * @param <K>    KEY
     * @param <V>    VALUE
     * @return 创建的Query对象。
     */
    public static <K, V> Query createQuery(Map<K, V> params) {
        // 调用create方法创建MongoQuery对象，并返回其中的query方法返回的Query对象
        return create(params).query();
    }

    /**
     * 根据给定的参数创建一个MongoQuery对象。
     *
     * @param params 参数Map，包含查询条件和配置信息。
     * @param <K>    KEY
     * @param <V>    VALUE
     * @return 创建的MongoQuery对象。
     */
    public static <K, V> MongoQuery create(Map<K, V> params) {
        if (MapUtils.isEmpty(params)) {
            return new MongoQuery();
        }
        // 使用Stream API遍历Map并修改key
        Map<String, Object> modifiedMap = params.entrySet().stream()
                .collect(Collectors.toMap(entry -> modifyKey(String.valueOf(entry.getKey())), Map.Entry::getValue));
        existMap = new HashMap<>();
        MongoQuery mongoQuery = new MongoQuery();
        for (Map.Entry<String, Object> entry : modifiedMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            if (EmptyUtils.isEmpty(entry.getValue()) || SORT.equals(key)) {
                //空时不操作
            } else if (key.toLowerCase().endsWith(LIKE)) {//判断结尾是否为模糊查询
                key = StringUtils.before(key, StrPool.UNDERLINE);
                mongoQuery.likeIgnoreCase(key, entry.getValue().toString());
            } else if (ORDER.equals(key)) {
                mongoQuery.orderBy(String.valueOf(params.get(SORT)), String.valueOf(params.get(ORDER)));
            } else if (key.toLowerCase().endsWith(OR_FIELD_NAME)) {
                key = StringUtils.before(key, StrPool.UNDERLINE);
                mongoQuery.or(ListUtils.guavaStringToList(key, StrPool.UNDERLINE), entry.getValue());
            } else if (key.toLowerCase().endsWith(IN_FIELD_NAME)) {
                key = StringUtils.before(key, StrPool.UNDERLINE);
                List<String> objects = ListUtils.guavaStringToList(entry.getValue().toString());
                mongoQuery.in(key, objects);
            } else if (key.toLowerCase().endsWith(GE)) { // 大于等于
                processGteAndLte(modifiedMap, key, entry.getValue(), mongoQuery, StringUtils.before(key, StrPool.UNDERLINE) + UNDERLINE_LE, true);
            } else if (key.toLowerCase().endsWith(UNDERLINE_LE)) {// 小于等于
                processGteAndLte(modifiedMap, key, entry.getValue(), mongoQuery, StringUtils.before(key, StrPool.UNDERLINE) + GE, false);
            } else {
                mongoQuery.where(key, entry.getValue());
            }
        }
        return mongoQuery;
    }

    /**
     * 根据给定的键对键进行修改。
     *
     * @param key 要修改的键
     * @return 修改后的键
     */
    private static String modifyKey(String key) {
        if (START_DATE.equals(key)) {
            return QueryCode.Lowercase.CREATE_DATE + GE;
        } else if (END_DATE.equals(key)) {
            return QueryCode.Lowercase.CREATE_DATE + UNDERLINE_LE;
        }
        return key;
    }

    /**
     * 根据参数条件处理Mongo查询对象
     *
     * @param params     参数Map
     * @param key        第一个键
     * @param mongoQuery Mongo查询对象
     * @param entryValue 第一个值
     * @param key2       第二个键
     * @param gteFlag    大于等于标志位
     */
    public static void processGteAndLte(Map params, String key, Object entryValue, MongoQuery mongoQuery, String key2, boolean gteFlag) {
        if (Boolean.TRUE.equals(existMap.get(key))) {
            return;
        }
        Object kye2Value = params.get(key2);
        if (existAndIsNotEmpty(params, key2)) {
            handleExistParams(key, entryValue, key2, kye2Value, mongoQuery);
        } else {
            handleNonExistParams(key, entryValue, mongoQuery, gteFlag);
        }
    }

    /**
     * 处理存在参数的情况
     *
     * @param key        第一个键
     * @param keyValue   第一个值
     * @param key2       第二个键
     * @param key2Value  第二个值
     * @param mongoQuery Mongo查询对象
     */
    private static void handleExistParams(String key, Object keyValue, String key2, Object key2Value, MongoQuery mongoQuery) {
        // 提取key中下划线前的部分作为实际key
        key = StringUtils.before(key, StrPool.UNDERLINE);
        // 判断key是否包含"date"，用于处理日期类型的参数
        if (key.toLowerCase().contains(DATE)) {
            // 根据key2是否包含"le"进行不同操作
            if (key2.contains(LE)) {
                mongoQuery.gteAndLte(key, DateUtils.parse(keyValue), DateUtils.parse(key2Value));
            } else {
                mongoQuery.gteAndLte(key, DateUtils.parse(key2Value), DateUtils.parse(keyValue));
            }
        } else {
            // 根据key2是否包含"le"进行不同操作
            if (key2.contains(LE)) {
                mongoQuery.gteAndLte(key, keyValue, key2Value);
            } else {
                mongoQuery.gteAndLte(key, key2Value, keyValue);
            }
        }
        // 将key2标记为已存在，避免重复处理相同的参数
        existMap.put(key2, true);
    }

    /**
     * 处理不存在参数的情况
     *
     * @param key        键
     * @param entryValue 值
     * @param mongoQuery Mongo查询对象
     * @param gteFlag    大于等于标志位
     */
    private static void handleNonExistParams(String key, Object entryValue, MongoQuery mongoQuery, boolean gteFlag) {
        key = StringUtils.before(key, StrPool.UNDERLINE);
        if (gteFlag) {
            if (key.toLowerCase().contains(DATE)) {
                mongoQuery.gte(key, DateUtils.parse(entryValue));
            } else {
                mongoQuery.gte(key, entryValue);
            }
        } else {
            if (key.toLowerCase().contains(DATE)) {
                mongoQuery.lte(key, DateUtils.parse(entryValue));
            } else {
                mongoQuery.lte(key, entryValue);
            }
        }
    }


    /**
     * 判断给定的params中是否存在指定的key，并且该key对应的值不为空
     *
     * @param params 给定的参数Map
     * @param key    要判断的key
     * @return 如果key存在且对应的值不为空，则返回true；否则返回false
     */
    public static boolean existAndIsNotEmpty(Map params, String key) {
        return params.containsKey(key) && EmptyUtils.isNotEmpty(params.get(key));
    }
}


