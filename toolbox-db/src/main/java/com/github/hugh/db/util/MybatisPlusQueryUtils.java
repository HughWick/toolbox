package com.github.hugh.db.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.hugh.constant.StrPool;
import com.github.hugh.db.constants.QueryCode;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.ListUtils;
import com.github.hugh.util.ServletUtils;
import com.github.hugh.util.StringUtils;
import com.google.common.base.CaseFormat;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * in 多个等于
     */
    private static final String IN_FIELD_NAME = "_id";

    /**
     * 空字符串
     */
//    private static final String EMPTY = "";

    /**
     * 排序
     */
    private static final String SORT = "sort";
    /**
     * 表示开始日期。
     */
    private static final String START_DATE = "startDate";

    /**
     * 表示结束日期。
     */
    private static final String END_DATE = "endDate";

    /**
     * 创建mybatis plus查询实例
     * <p>
     * 默认添加 {@link QueryCode#DELETE_FLAG} 标识为0的条件
     * </p>
     *
     * @param params 查询条件
     * @param <E>    实体类型
     * @param <K>    KEY
     * @param <V>    VALUE
     * @param <S>    额外参数的value
     * @param <T>    额外参数的key
     * @return QueryWrapper
     */
    public static <E, K, V, T, S> QueryWrapper<E> createDef(Map<K, V> params, T key, S value) {
        QueryWrapper<E> queryWrapper = create(params);
        queryWrapper.eq((String) key, value);
        return queryWrapper;
    }


    /**
     * 根据指定的参数创建一个 {@link QueryWrapper} 对象。
     *
     * @param params 参数Map，其中的key和value将用于创建QueryWrapper对象
     * @param <T>    QueryWrapper对象的泛型类型
     * @param <K>    Map中key的类型
     * @param <V>    Map中value的类型
     * @return 创建的QueryWrapper对象
     */
    public static <T, K, V> QueryWrapper<T> create(Map<K, V> params) {
        return create(params, true);
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
     * @param params   查询条件
     * @param <K>      KEY对象类型
     * @param <V>      VALUE象类型
     * @param keyUpper 数据库kye下划线命名方式，大小写，{@code true} 大写下划线，{@code false}小写下划线命名方式
     * @return QueryWrapper
     */
    public static <T, K, V> QueryWrapper<T> create(Map<K, V> params, boolean keyUpper) {
        if (params == null) {
            throw new NullPointerException();
        }
        // 使用Stream API遍历Map并修改，开始日期与结束日期，修改为大于等于（gte），与小于等于（lte）结尾
        Map<String, Object> modifiedMap = params.entrySet().stream()
                .collect(Collectors.toMap(entry -> modifyKey(String.valueOf(entry.getKey())), Map.Entry::getValue));
        QueryWrapper<T> queryWrapper = Wrappers.query();
        for (Map.Entry<String, Object> entry : modifiedMap.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            String tableField = conversion(key, keyUpper);//将key转化为与数据库列一致的名称
            if (EmptyUtils.isEmpty(value) || SORT.equals(key)) {
                //空时不操作
            } else if (tableField.endsWith(LIKE)) {//判断结尾是否为模糊查询
                tableField = tableField.replace(LIKE, StrPool.EMPTY);//移除掉识别key
                queryWrapper.like(tableField, value);
            } else if ("order".equals(key)) {
                String sortValue = String.valueOf(params.get(SORT));
                appendOrderSql(queryWrapper, value, sortValue);
            } else if (key.endsWith("_or")) { // 结尾是否为or
                appendOrSql(queryWrapper, key, value, keyUpper);
            } else if (key.endsWith(IN_FIELD_NAME)) {
                appendInSql(queryWrapper, tableField, value);
            } else if (tableField.endsWith(GE)) {
                tableField = tableField.replace(GE, StrPool.EMPTY);
                queryWrapper.ge(tableField, value);
            } else if (tableField.endsWith(LE)) {
                tableField = tableField.replace(LE, StrPool.EMPTY);//移除掉识别key
                queryWrapper.le(tableField, value);//小于等于
            } else {
                queryWrapper.eq(tableField, value);
            }
        }
        return queryWrapper;
    }

    /**
     * 根据指定的键进行修改，返回修改后的键。
     *
     * @param key 要修改的键
     * @return 修改后的键
     */
    private static String modifyKey(String key) {
        if (START_DATE.equals(key)) {
            return "createDate" + GE.toLowerCase();
        } else if (END_DATE.equals(key)) {
            return "createDate" + LE.toLowerCase();
        }
        return key;
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
            sortValue = conversion(sortValue, true);
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
        tableField = StringUtils.before(tableField, StrPool.UNDERLINE);
        List<String> objects = ListUtils.guavaStringToList(String.valueOf(value));
        // 转换成 in语句
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : objects) {
            stringBuilder.append("'").append(string).append("'").append(StrPool.COMMA);
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
    private static <T> void appendOrSql(QueryWrapper<T> queryWrapper, String tableField, Object value, boolean keyUpper) {
        tableField = StringUtils.before(tableField, StrPool.UNDERLINE);
        List<String> strings = ListUtils.guavaStringToList(tableField, StrPool.UNDERLINE);
        // 遍历所有or字段，放入like查询内
        queryWrapper.and(orQueryWrapper -> {
            for (String queryKey : strings) {
                String conversion = conversion(queryKey, keyUpper);
                orQueryWrapper.like(conversion, value).or();
            }
        });
    }

    /**
     * 将驼峰命名的字符串转换为下划线形式。
     *
     * @param str   驼峰命名的字符串
     * @param upper 是否转换为大写形式
     * @return 转换后的下划线形式的字符串
     */
    private static String conversion(String str, boolean upper) {
        if (upper) {
            // 将驼峰命名的字符串转换为大写下划线形式
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, str);
        } else {
            // 将驼峰命名的字符串转换为小写下划线形式
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
        }
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
     *
     * @param request 请求头
     * @param key     键
     * @param value   值
     * @param <T>     mybatisPlus查询对象类型
     * @param <K>     KEY类型
     * @param <V>     VALUE 类型
     * @return QueryWrapper
     * @since 2.1.2
     */
    private static <T, K, V> QueryWrapper<T> create(HttpServletRequest request, K key, V value) {
        Map<K, V> params = ServletUtils.getParams(request);
        if (EmptyUtils.isNotEmpty(key) && EmptyUtils.isNotEmpty(value)) {
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
        return create(request, null, null);
    }

    /**
     * 根据HttpServletRequest对象创建一个使用小写键的QueryWrapper对象。
     *
     * @param request HttpServletRequest对象，用于获取请求参数
     * @param <T>     QueryWrapper对象的泛型类型
     * @return 创建的QueryWrapper对象
     * @since 2.7.4
     */
    public static <T> QueryWrapper<T> createLowerCase(HttpServletRequest request) {
        return create(ServletUtils.getParams(request), false);
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
     * 解析请求头中所有键值对，并防入mybatis 查询对象中
     * <p>
     * 加入删除标识 {@link QueryCode#DELETE_FLAG} 标识为0的条件
     * </p>
     *
     * @param params 参数
     * @param <T>    类型
     * @param <K>    KEY
     * @param <V>    VALUE
     * @return QueryWrapper
     * @since 2.1.8
     */
    public static <T, K, V> QueryWrapper<T> createDef(Map<K, V> params) {
        return createDef(params, QueryCode.DELETE_FLAG, 0);
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
        return create(request, key, value);
    }
}
