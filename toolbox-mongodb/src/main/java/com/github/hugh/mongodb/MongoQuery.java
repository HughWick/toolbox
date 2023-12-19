package com.github.hugh.mongodb;

import com.github.hugh.constant.StrPool;
import com.github.hugh.mongodb.exception.ToolboxMongoException;
import com.github.hugh.util.ListUtils;
import com.google.common.collect.Lists;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * mongo db 查询构造器，用于构建 MongoDB 查询对象
 *
 * @author hugh
 * @since 2.8.0
 */
public class MongoQuery {

    private final Query query;

    /**
     * 构造方法，初始化一个新的 MongoDB 查询对象
     */
    public MongoQuery() {
        this.query = new Query();
    }

    /**
     * 创建一个新的 MongoQuery 实例，用于构建 MongoDB 查询
     *
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public static MongoQuery on() {
        return new MongoQuery();
    }

    /**
     * 添加精确匹配查询条件
     *
     * @param key   字段名
     * @param value 匹配值
     * @return 返回 MongoDB 查询对象
     */
    public MongoQuery where(String key, Object value) {
        query.addCriteria(Criteria.where(key).is(value));
        return this;
    }

    /**
     * 添加或查询条件
     *
     * @param key   字段名
     * @param value 匹配值
     * @return 返回 MongoDB 查询对象
     */
    public MongoQuery or(String key, Object value) {
        or(Lists.newArrayList(key), Lists.newArrayList(value));
        return this;
    }

    public MongoQuery or(List<String> keys, Object value) {
        // 构造或查询条件
        List<Criteria> criterias = new ArrayList<>();
        for (String key : keys) {
            criterias.add(Criteria.where(key).is(value));
        }
        Criteria orCriteria = new Criteria().orOperator(criterias.toArray(new Criteria[0]));
        // 向查询对象中添加或查询条件
        query.addCriteria(orCriteria.type(JsonSchemaObject.Type.objectType()));
        return this;
    }

    /**
     * 添加或查询条件，其中 keys 和 values 分别表示匹配字段和匹配值
     *
     * @param keys   字段名列表
     * @param values 匹配值列表
     * @return 返回 MongoDB 查询对象
     */
    public MongoQuery or(List<String> keys, List<Object> values) {
        if (ListUtils.isEmpty(keys) || ListUtils.isEmpty(values)) {
            throw new ToolboxMongoException("keys和values不能为空");
        }
//        if (keys.size() != values.size()) {
//            throw new ToolboxMongoException("keys和values的数量不匹配");
//        }
        // 构造或查询条件
        List<Criteria> criterias = new ArrayList<>();
        for (int i = 0; i < Math.min(keys.size(), values.size()); i++) {
            String k = keys.get(i);
            Object v = values.get(i);
            criterias.add(Criteria.where(k).is(v));
        }
        Criteria orCriteria = new Criteria().orOperator(criterias.toArray(new Criteria[0]));
        // 向查询对象中添加或查询条件
        query.addCriteria(orCriteria);
        return this;
    }

    /**
     * 使用不区分大小写的正则表达式匹配方式查询指定字段值中包含给定字符串的文档
     *
     * @param key   要匹配的字段名
     * @param value 要匹配的字符串
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery likeIgnoreCase(String key, String value) {
        query.addCriteria(Criteria.where(key).regex(".*" + value + ".*", "i"));
        return this;
    }

    /**
     * 使用区分大小写的正则表达式匹配方式查询指定字段值中包含给定字符串的文档
     *
     * @param key   要匹配的字段名
     * @param value 要匹配的字符串
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery like(String key, String value) {
        query.addCriteria(Criteria.where(key).regex(".*" + value + ".*"));
        return this;
    }

    /**
     * 使用正则表达式进行查询
     *
     * @param key   要匹配的字段名
     * @param regex 正则表达式字符串
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery regex(String key, String regex) {
        query.addCriteria(Criteria.where(key).regex(regex));
        return this;
    }

    /**
     * 根据指定字段进行排序
     *
     * @param key   要排序的字段名
     * @param value 排序顺序，可以是 "ASC" 表示升序，也可以是 "DESC" 表示降序
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery orderBy(String key, String value) {
        // 根据排序字段和排序顺序进行排序
        Sort.Direction direction = value.equalsIgnoreCase(Sort.Direction.ASC.toString())
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        query.with(Sort.by(new Sort.Order(direction, key)));
        return this;
    }

    /**
     * 获取当前的查询对象
     *
     * @return 返回当前的查询对象
     */
    public Query query() {
        return query;
    }

    /**
     * 设置分页查询参数
     *
     * @param page 当前页码（从1开始）
     * @param size 每页的记录数
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery page(int page, int size) {
        int currentPage = page > 0 ? page - 1 : 0;
        // 创建分页请求
        query.with(PageRequest.of(currentPage, size));
        return this;
    }

    /**
     * 使用 in 运算符查询指定字段值在给定数组中的文档
     *
     * @param key    要匹配的字段名
     * @param values 匹配的值数组
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery in(String key, Object... values) {
        in(key, Arrays.asList(values));
        return this;
    }

    public MongoQuery in(String key, Collection<?> values) {
        query.addCriteria(Criteria.where(key).in(values));
        return this;
    }

    /**
     * 使用大于（{@code >}）运算符查询指定字段值大于给定值的文档
     *
     * @param key   要匹配的字段名
     * @param value 给定的比较值
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery gt(String key, Object value) {
        query.addCriteria(Criteria.where(key).gt(value));
        return this;
    }

    /**
     * 使用大于等于（{@code >=}）运算符查询指定字段值大于等于给定值的文档
     *
     * @param key   要匹配的字段名
     * @param value 给定的比较值
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery gte(String key, Object value) {
        query.addCriteria(Criteria.where(key).gte(value));
        return this;
    }

    /**
     * 使用小于（{@code <}）运算符查询指定字段值小于给定值的文档
     *
     * @param key   要匹配的字段名
     * @param value 给定的比较值
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery lt(String key, Object value) {
        query.addCriteria(Criteria.where(key).lt(value));
        return this;
    }

    /**
     * 使用小于等于（{@code <=}）运算符查询指定字段值小于等于给定值的文档
     *
     * @param key   要匹配的字段名
     * @param value 给定的比较值
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery lte(String key, Object value) {
        query.addCriteria(Criteria.where(key).lte(value));
        return this;
    }

    /**
     * 使用同时满足大于等于和小于等于运算符的方式查询指定字段值在给定范围内的文档
     *
     * @param key      要匹配的字段名
     * @param gteValue 给定的比较下限值（包含）
     * @param lteValue 给定的比较上限值（包含）
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery gteAndLte(String key, Object gteValue, Object lteValue) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where(key).lte(lteValue),
                Criteria.where(key).gte(gteValue)
        );
        query.addCriteria(criteria);
        return this;
    }

    /**
     * 使用同时满足大于和小于运算符的方式查询指定字段值在给定范围内（不包含边界值）的文档
     *
     * @param key      要匹配的字段名
     * @param gteValue 给定的比较下限值（不包含）
     * @param lteValue 给定的比较上限值（不包含）
     * @return 返回 MongoQuery 对象，便于进行链式调用
     */
    public MongoQuery gtAndLt(String key, Object gteValue, Object lteValue) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where(key).lt(lteValue),
                Criteria.where(key).gt(gteValue)
        );
        query.addCriteria(criteria);
        return this;
    }

    /**
     * 添加一个条件，检查指定的键是否为空（包括空字符串或仅包含空格）。
     *
     * @param key 要检查的键
     * @return 更新后的 MongoQuery 对象
     */
    public MongoQuery isBlank(String key) {
        query.addCriteria(Criteria.where(key).is(StrPool.EMPTY));
        return this;
    }

    /**
     * 添加一个条件，检查指定的键是否为空（包括空字符串或 null）。
     *
     * @param key 要检查的键
     * @return 更新后的 MongoQuery 对象
     */
    public MongoQuery isEmpty(String key) {
        query.addCriteria(new Criteria().orOperator(
                Criteria.where(key).is(StrPool.EMPTY),
                Criteria.where(key).isNull()
        ));
        return this;
    }
}
