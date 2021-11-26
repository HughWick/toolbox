package com.github.hugh.db.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.hugh.db.constants.QueryCode;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.ListUtils;
import com.github.hugh.util.ServletUtils;
import com.google.common.base.CaseFormat;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Mybatis plus 查询条件工具类
 *
 * @author hugh
 * @since 2.1.0
 */
public class MybatisPlusQueryUtils {

    /**
     * 大于等于
     */
    private static final String GE = "_GE";

    /**
     * 小于等于
     */
    private static final String LE = "_LE";

    /**
     * 模糊查询
     */
    private static final String LIKE = "_LIKE";

    /**
     * 多个等于
     */
    private static final String IN_FIELD_NAME = "_IN";

    /**
     * 空字符串
     */
    private static final String EMPTY = "";

    /**
     * 排序
     */
    private static final String SORT = "sort";

    /**
     * 创建mybatis plus查询实例
     * <p>
     * 默认添加 {@link QueryCode#DELETE_FLAG} 标识为0的条件
     * </p>
     *
     * @param params 查询条件
     * @param <K>    KEY
     * @param <T>    实体类型
     * @param <V>    VALUE
     * @return QueryWrapper
     */
    public static <T, K, V> QueryWrapper<T> createDef(Map<String, String> params, K key, V value) {
        QueryWrapper<T> queryWrapper = create(params);
        queryWrapper.eq((String) key, value);
        return queryWrapper;
    }

    /**
     * 遍历所有Map中的键值对、并根据对应的属性进行mybatis plus 的查询语句赋值
     * <ul>
     *  <li>
     *      默认将所有驼峰的key转换为对应下划线命名方式的key 例:{@code name}:{@code John} 结果为 name = John
     *  </li>
     *  <li>
     *     根据指定字段进行排序的sql语句 例：{@code order}:{@code name}, {@code sort}:{@code desc|asc} 结果为: order by name ASC
     *     <p>sort字段的value值大小写都支持</p>
     *  </li>
     *  <li>
     *      like查询的传值方式 例：{@code name_like}:{@code John} 转换后的sql语句为：{@code name} like '%John%'
     *  </li>
     *  <li>
     *      in查询的传值方式 例：{@code serialNumber_in}:{@code 1,2,3} 结果为 serialNumber in (1,2,3)
     *  </li>
     *  <li>
     *      or查询的传值方式 例：{@code name_account_or}:{@code John} 结果为 NAME LIKE '%John%' OR ACCOUNT LIKE '%John%'
     *  </li>
     *  <li>
     *      大于等于查询的传值方式,例:{@code age_ge}:{@code 18} 结果为:age {@code >=} 18
     *  </li>
     *  <li>
     *      小于等于查询的传值方式,例:{@code age_le}:{@code 18} 结果为:age {@code <=} 18
     *  </li>
     * </ul>
     *
     * @param params 查询条件
     * @return QueryWrapper
     */
    public static <T> QueryWrapper<T> create(Map<String, String> params) {
        if (params == null) {
            throw new NullPointerException();
        }
        QueryWrapper<T> queryWrapper = Wrappers.query();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String tableField = conversion(key);//将key转化为与数据库列一致的名称
            if (EmptyUtils.isEmpty(value) || SORT.equals(key)) {
                //空时不操作
            } else if (QueryCode.START_DATE.equals(tableField)) {
                queryWrapper.ge(QueryCode.CREATE_DATE, value);//开始日期 小于等于
            } else if (QueryCode.END_DATE.equals(tableField)) {
                queryWrapper.le(QueryCode.CREATE_DATE, value);//结束日期 大于等于
            } else if (tableField.endsWith(LIKE)) {//判断结尾是否为模糊查询
                tableField = tableField.replace(LIKE, EMPTY);//移除掉识别key
                queryWrapper.like(tableField, value);
            } else if ("order".equals(key)) {
                String sortValue = String.valueOf(params.get(SORT));
                appendOrderSql(queryWrapper, value, sortValue);
            } else if (key.endsWith("_or")) { // 结尾是否为or
                appendOrSql(queryWrapper, key, value);
            } else if (tableField.endsWith(IN_FIELD_NAME)) {
                appendInSql(queryWrapper, tableField, value);
            } else if (tableField.endsWith(GE)) {
                tableField = tableField.replace(GE, EMPTY);
                queryWrapper.ge(tableField, value);
            } else if (tableField.endsWith(LE)) {
                tableField = tableField.replace(LE, EMPTY);//移除掉识别key
                queryWrapper.le(tableField, value);//小于等于
            } else {
                queryWrapper.eq(tableField, value);
            }
        }
        return queryWrapper;
    }

    /**
     * 拼接order sql语句
     *
     * @param queryWrapper 查询条件
     * @param orderValue   order 的值：desc、asc
     * @param sortValue    排序字段名称
     * @param <T>          类型
     */
    private static <T> void appendOrderSql(QueryWrapper<T> queryWrapper, String orderValue, String sortValue) {
        if (!isAcronym(sortValue)) { // 判断判断的key不都是大写时、转换为驼峰
            sortValue = conversion(sortValue);
        }
        queryWrapper.orderBy(true, isAsc(orderValue), sortValue);
    }

    /**
     * 拼接in的sql语句
     *
     * @param queryWrapper 查询条件过滤器
     * @param tableField   字段
     * @param value        值
     * @param <T>          类型
     */
    private static <T> void appendInSql(QueryWrapper<T> queryWrapper, String tableField, Object value) {
        tableField = tableField.replace(IN_FIELD_NAME, EMPTY);
        List<?> objects = ListUtils.guavaStringToList(String.valueOf(value));
        // 转换成 in语句
        StringBuilder stringBuilder = new StringBuilder();
        for (Object no : objects) {
            stringBuilder.append("'").append(no.toString()).append("'").append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        queryWrapper.inSql(tableField, stringBuilder.toString());
    }

    /**
     * 拼接or语句以及所有条件
     *
     * @param queryWrapper 查询条件过滤器
     * @param tableField   字段
     * @param value        值
     * @param <T>          类型
     */
    private static <T> void appendOrSql(QueryWrapper<T> queryWrapper, String tableField, Object value) {
        tableField = tableField.replace("_or", EMPTY);//移除掉标识or
        List<String> strings = ListUtils.guavaStringToList(tableField, "_");
        // 遍历所有or字段，放入like查询内
        queryWrapper.and(orQueryWrapper -> {
            for (String queryKey : strings) {
                String conversion = conversion(queryKey);
                orQueryWrapper.like(conversion, value).or();
            }
        });
    }

    /**
     * 驼峰转下划线
     *
     * @param str 字符串
     * @return String
     */
    private static String conversion(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, str);
    }

    /**
     * 字符串是否为升序
     *
     * @param str 字符串
     * @return boolean true升序
     */
    public static boolean isAsc(String str) {
        if (EmptyUtils.isEmpty(str)) {
            return false;
        }
        String s = str.toUpperCase();
        return "ASC".equals(s);
    }

    /**
     * 判断字符串全是大写
     *
     * @param word 文字
     * @return boolean true全大写
     */
    public static boolean isAcronym(String word) {
        if (EmptyUtils.isEmpty(word)) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 解析请求头中所有键值对，并放入mybatis 查询对象中
     * <p>根据deleteFlag标识区分是否，加入删除标识 {@link QueryCode#DELETE_FLAG} 标识为0的条件</p>
     *
     * @param request    请求头
     * @param deleteFlag 是否增加删除标识的键值对
     * @return QueryWrapper
     * @since 2.1.2
     */
    private static <T, K> QueryWrapper<T> create(HttpServletRequest request, boolean deleteFlag, K key, Object value) {
        Map<String, String> params = ServletUtils.getParams(request);
        if (deleteFlag) {
            return createDef(params, key, value);
        } else {
            return create(params);
        }
    }

    /**
     * 解析请求头中所有键值对，并防入mybatis 查询对象中
     *
     * @param request 请求头
     * @param <T>     类型
     * @return QueryWrapper
     * @since 2.1.2
     */
    public static <T> QueryWrapper<T> create(HttpServletRequest request) {
        return create(request, false, null, null);
    }

    /**
     * 解析请求头中所有键值对，并防入mybatis 查询对象中
     * <p>
     * 加入删除标识 {@link QueryCode#DELETE_FLAG} 标识为0的条件
     * </p>
     *
     * @param request 请求头
     * @param <T>     类型
     * @return QueryWrapper
     * @since 2.1.2
     */
    public static <T> QueryWrapper<T> createDef(HttpServletRequest request) {
        return createDef(request, QueryCode.DELETE_FLAG, 0);
    }

    /**
     * 创建一个放入默认值的查询条件
     *
     * @param request 请求头
     * @param key     默认的key
     * @param value   默认值
     * @param <K>     KEY
     * @param <V>     VALUE
     * @param <T>     类型
     * @return QueryWrapper
     * @since 2.1.2
     */
    public static <T, K, V> QueryWrapper<T> createDef(HttpServletRequest request, K key, V value) {
        return create(request, true, key, value);
    }
}
