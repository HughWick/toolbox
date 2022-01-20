package com.github.hugh.util;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.common.AssertUtils;
import com.google.common.base.Splitter;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Map 工具类
 * <ul>
 * <li>该类获取map中的值使用了{@link org.apache.commons.collections4.MapUtils}进行二次封装</li>
 * </ul>
 *
 * @author hugh
 * @since 1.0.2
 */
public class MapUtils {

    /**
     * 校验map中code状态是否为0000(成功状态)
     *
     * @param map  Map
     * @param key  map中的key
     * @param code 需要验证的code码
     * @return boolean {@code true} 一致
     */
    public static <K, V> boolean isSuccess(Map<K, V> map, K key, String code) {
        if (map == null) {
            return false;
        }
        if (EmptyUtils.isEmpty(code)) {
            return false;
        }
        return code.equals(String.valueOf(map.get(key)));
    }

    /**
     * 根据key与code码、校验与map中一致
     *
     * @param map  参数
     * @param key  键
     * @param code 值
     * @return boolean {@code true} 不一致
     */
    public static <K, V> boolean isFailure(Map<K, V> map, K key, String code) {
        return !isSuccess(map, key, code);
    }

    /**
     * 校验map是否为空
     *
     * @param map 参数
     * @return boolean {@code true} 空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    /**
     * 校验Map不为空
     *
     * @param map 参数
     * @return boolean {@code true} 不为空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 循环所有request中的参数放入至map
     *
     * @param request 客户端的请求
     * @return Map
     * @since 1.1.0
     */
    @Deprecated
    public static Map<String, Object> cyclePar(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        if (request == null) {
            return map;
        }
        Enumeration<String> isKey = request.getParameterNames();
        while (isKey.hasMoreElements()) {
            String key = isKey.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 遍历查询参数
     * <ul>
     * <li>并且移除其中的page、size键</li>
     * </ul>
     *
     * @param request 客户端的请求
     * @return Map
     * @since 1.1.0
     */
    @Deprecated
    public static Map<String, Object> cycleQueryPar(HttpServletRequest request) {
        return cycleQueryPar(request, "page", "size");
    }

    /**
     * 遍历查询参数、并且移除其中指定多余键
     *
     * @param request 客户端的请求
     * @param keys    需要删除的key
     * @return Map
     * @since 1.1.0
     */
    @Deprecated
    public static Map<String, Object> cycleQueryPar(HttpServletRequest request, String... keys) {
        Map<String, Object> map = cyclePar(request);
        for (String key : keys) {
            map.remove(key);
        }
        return map;
    }

    /**
     * 将map,转换成对应class对象的实体类
     * <ul>
     * <li>注:</li>
     * <li>1.map中的key必须与实体中的常量key一致.</li>
     * <li>2.通过class创建一个新的实例,后调用{@link #convertObjects(Object, Map, boolean)}赋值.</li>
     * <li>3.调用{@link EmptyUtils#isNotEmpty(Object)}字符串非空判断,如果字符串为空则不进行实体赋值.转换方式与{@link #convertObjects(Object, Map, boolean)}.</li>
     * </ul>
     *
     * @param cls    实体类class
     * @param params 参数
     * @param <T>    类型
     * @param <K>    键泛型
     * @param <V>    value泛型
     * @return T 返回实体
     * @throws Exception 错误异常
     * @since 1.2.3
     */
    public static <T, K, V> T toEntityNotEmpty(Class<T> cls, Map<K, V> params) throws Exception {
        AssertUtils.notNull(cls, "class");
        T obj = cls.getDeclaredConstructor().newInstance();
        return convertObjects(obj, params, true);
    }

    /**
     * map转换为实体对象,并赋值
     * <ul>
     * <li>注:</li>
     * <li>1.map中的key必须与实体中的常量key一致.</li>
     * <li>2.调用{@link EmptyUtils#isNotEmpty(Object)}字符串非空判断,如果字符串为空则不进行实体赋值.转换方式与{@link #convertObjects(Object, Map, boolean)}.</li>
     * </ul>
     *
     * @param object 实体对象
     * @param params 参数
     * @since 1.4.0
     */
    public static <T, K, V> T toEntityNotEmpty(T object, Map<K, V> params) throws Exception {
        AssertUtils.notNull(object, "object");
        return convertObjects(object, params, true);
    }

    /**
     * 将map,转换成对应class对象的实体类
     * <ul>
     * <li>注:</li>
     * <li>1.map中的key必须与实体中的常量key一致.</li>
     * <li>2.class创建一个新的实例,通过反射机制调用对象方法,获取map中的value不为{@code null}时进行赋值.</li>
     * <li>3.转换方式与{@link #convertObjects(Object, Map, boolean)}.</li>
     * </ul>
     *
     * @param cls    类
     * @param params 参数
     * @param <T>    实体泛型
     * @param <K>    map中key
     * @param <V>    map-value
     * @return T 实体
     * @throws Exception 错误信息
     * @since 1.4.0
     */
    public static <T, K, V> T convertEntity(Class<T> cls, Map<K, V> params) throws Exception {
        AssertUtils.notNull(cls, "class");
        T obj = cls.getDeclaredConstructor().newInstance();
        return convertObjects(obj, params, false);
    }

    /**
     * Map转实体类方法
     * <ul>
     * <li>注:</li>
     * <li>1.map中的key必须与实体中的常量key一致.</li>
     * <li>2.不会新创建实体对象,通过反射机制调用对象方法,获取map中的value不为{@code null}时进行赋值</li>
     * <li>3.转换方式与{@link #convertObjects(Object, Map, boolean)}</li>
     * </ul>
     *
     * @param object 实体
     * @param params 参数
     * @param <T>    实体泛型
     * @param <K>    map中key
     * @param <V>    map-value
     * @return T 实体
     * @throws Exception 错误信息
     * @since 1.4.0
     */
    public static <T, K, V> T convertEntity(T object, Map<K, V> params) throws Exception {
        AssertUtils.notNull(object, "object");
        return convertObjects(object, params, false);
    }

    /**
     * map转实体
     *
     * @param bean   实体
     * @param params 参数
     * @param empty  空判断
     * @param <T>    实体泛型
     * @return T 实体
     * @throws IntrospectionException    无法将字符串类名称映射到 Class 对象、无法解析字符串方法名，或者指定对其用途而言具有错误类型签名的方法名称
     * @throws InvocationTargetException 反射异常
     * @throws IllegalAccessException    没有访问权限的异常
     * @since 1.4.0
     */
    private static <T> T convertObjects(T bean, Map params, boolean empty) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class<?> aClass = bean.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(aClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            Object value = params.get(propertyName);
            boolean flag; // 是否空判断标识
            if (empty) { // 判断不等于空字符串
                flag = EmptyUtils.isNotEmpty(value);
            } else { // 判断不等于null
                flag = value != null;
            }
            if (flag) {
                switch (descriptor.getPropertyType().getSimpleName()) {
                    case "String":
                        break;
                    case "int":
                    case "Integer":
                        value = Integer.parseInt(String.valueOf(value).strip());
                        break;
                    case "long":
                    case "Long":
                        value = Long.parseLong(String.valueOf(value));
                        break;
                    case "double":
                    case "Double":
                        value = Double.parseDouble(String.valueOf(value));
                        break;
                    case "Date": // 当时日期类型时，进行格式校验
                        if (value instanceof Date) {
                            //日期类型不处理
                        } else if (DateUtils.isDateFormat(String.valueOf(value))) {
                            value = DateUtils.parse(String.valueOf(value));
                        }
                        break;
                    case "List":
                        String trim = trim(String.valueOf(value));
                        value = Splitter.on(",").trimResults().splitToList(trim);
                        break;
                    default:
                        throw new ToolboxException("Unknown type : " + descriptor.getPropertyType().getSimpleName());
                }
                descriptor.getWriteMethod().invoke(bean, value);
            }
        }
        return bean;
    }

    /**
     * 修剪掉前后各一位字符
     *
     * @param source 源
     * @return String
     */
    private static String trim(String source) {
        if (EmptyUtils.isEmpty(source)) {
            return "";
        }
        String s1 = source.substring(1);
        return s1.substring(0, s1.length() - 1);
    }

    /**
     * get参数后转换为String
     *
     * @param <K> key 的类型
     * @param map 参数
     * @param key 键
     * @return String
     */
    public static <K> String getString(Map<? super K, ?> map, K key) {
        return org.apache.commons.collections4.MapUtils.getString(map, key);
    }

    /**
     * get参数后转换为Long
     *
     * @param <K> key 的类型
     * @param map 参数
     * @param key 键
     * @return Long
     */
    public static <K> Long getLong(Map<? super K, ?> map, K key) {
        return org.apache.commons.collections4.MapUtils.getLong(map, key);
    }

    /**
     * get参数后转换为Int
     *
     * @param <K> key 的类型
     * @param map 参数
     * @param key 键
     * @return Integer
     */
    public static <K> Integer getInt(Map<? super K, ?> map, K key) {
        return org.apache.commons.collections4.MapUtils.getInteger(map, key);
    }

    /**
     * get参数后转换为Double
     *
     * @param <K> key 的类型
     * @param map 参数
     * @param key 键
     * @return Double
     */
    public static <K> Double getDouble(Map<? super K, ?> map, K key) {
        return org.apache.commons.collections4.MapUtils.getDouble(map, key);
    }

    /**
     * get参数后转换为Map
     *
     * @param <K> key 的类型
     * @param map 参数
     * @param key 键
     * @return Map
     */
    public static <K> Map getMap(Map<? super K, ?> map, K key) {
        return org.apache.commons.collections4.MapUtils.getMap(map, key);
    }

    /**
     * 为map put值
     * <p>如果key或value为空时则不put</p>
     *
     * @param map   map
     * @param key   key
     * @param value value
     * @param <K>   the key type
     * @param <V>   the value type
     * @since 1.7.3
     */
    public static <K, V> void setValue(final Map<? super K, V> map, final K key, final V value) {
        if (map == null) {
            return;
        }
        if (EmptyUtils.isEmpty(key) || EmptyUtils.isEmpty(value)) {
            return;
        }
        map.put(key, value);
    }

    /**
     * 移除map中对应的key
     *
     * @param map  map
     * @param keys keys
     * @since 2.1.2
     */
    public static <K, V> void removeKeys(Map<K, V> map, String... keys) {
        if (keys == null) {
            return;
        }
        for (String key : keys) {
            map.remove(key);
        }
    }

    /**
     * 遍历map中的所有value、根据value值降序排序
     *
     * @param map 需要排序的参数
     * @param <K> 键
     * @param <V> 值
     * @return map
     * @since 2.1.11
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * 遍历map中的所有value、根据value值升序排序
     *
     * @param map 需要排序的参数
     * @param <K> 键
     * @param <V> 值
     * @return map
     * @since 2.1.11
     */
    public static   <K, V extends Comparable<? super V>> Map<K, V> sortByValueAsc(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * 集合内的数据按key的字母降序排序
     *
     * @param <K> 键
     * @param <V> 值
     * @param map 需要排序的参数
     * @return map 按字母排序后的map
     * @since 2.1.11
     */
    public static  <K extends Comparable<? super K>, V> Map<K, V> sortByKeyDesc(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * 集合内的数据按key的字母升序排序
     *
     * @param <K> 键
     * @param <V> 值
     * @param map 需要排序的参数
     * @return map 按字母排序后的map
     * @since 2.1.11
     */
    public static  <K extends Comparable<? super K>, V> Map<K, V> sortByKeyAsc(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }
}
