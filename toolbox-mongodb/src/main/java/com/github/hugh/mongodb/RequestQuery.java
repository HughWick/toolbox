package com.github.hugh.mongodb;

import com.github.hugh.util.EmptyUtils;
import com.google.common.base.CaseFormat;

import java.util.Map;

/**
 * @author hugh
 * @since 2.7.0
 */
public class RequestQuery {
    /**
     * 大于等于
     */
    private static final String GE = "_ge";

    /**
     * 小于等于
     */
    private static final String LE = "_le";

    /**
     * 模糊查询
     */
    private static final String LIKE = "_like";

    /**
     * 多个等于
     */
    private static final String IN_FIELD_NAME = "_in";

    /**
     * 空字符串
     */
    private static final String EMPTY = "";

    /**
     * 排序
     */
    private static final String SORT = "sort";

    public static <K, V> MongoQuery create(Map<K, V> params) {
//        QueryObject queryObject = new QueryObject();
        MongoQuery mongoQuery = new MongoQuery();
        for (Map.Entry<K, V> entry : params.entrySet()) {
//            QueryObject.QueryDetails queryDetails = new QueryObject.QueryDetails();
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
//            String tableField = conversion(key);//将key转化为与数据库列一致的名称
            if (EmptyUtils.isEmpty(value) || SORT.equals(key)) {
                //空时不操作
//            } else if (QueryCode.START_DATE.equals(tableField)) {
////                queryWrapper.ge(QueryCode.CREATE_DATE, value);//开始日期 小于等于
//            } else if (QueryCode.END_DATE.equals(tableField)) {
////                queryWrapper.le(QueryCode.CREATE_DATE, value);//结束日期 大于等于
//            } else if (tableField.endsWith(LIKE)) {//判断结尾是否为模糊查询
////                tableField = tableField.replace(LIKE, EMPTY);//移除掉识别key
////                queryWrapper.like(tableField, value);
//            } else if ("order".equals(key)) {
////                String sortValue = String.valueOf(params.get(SORT));
////                appendOrderSql(queryWrapper, value, sortValue);
//            } else if (key.endsWith("_or")) { // 结尾是否为or
////                appendOrSql(queryWrapper, key, value);
//            } else if (tableField.endsWith(IN_FIELD_NAME)) {
////                appendInSql(queryWrapper, tableField, value);
            } else if (key.endsWith(GE)) {
                key = key.replace(GE, EMPTY);
                mongoQuery.gte(key, value);
//                tableField = tableField.replace(GE, EMPTY);
//                queryWrapper.ge(tableField, value);
//            } else if (tableField.endsWith(LE)) {
////                tableField = tableField.replace(LE, EMPTY);//移除掉识别key
////                queryWrapper.le(tableField, value);//小于等于
            } else {
                mongoQuery.where(key, value);
            }
        }
        return mongoQuery;
    }

    /**
     * 驼峰转下划线
     *
     * @param str 字符串
     * @return String
     */
    private static String conversion(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }
}


