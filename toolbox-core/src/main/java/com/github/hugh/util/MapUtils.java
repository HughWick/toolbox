package com.github.hugh.util;

import com.github.hugh.constant.StrPool;
import com.github.hugh.exception.ToolboxException;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Map 工具类
 *
 * @author hugh
 * @since 1.0.2
 */
public class MapUtils {
    private MapUtils() {
    }

    /**
     * 判断指定的Map中，key对应的value是否与所传入的code参数相等。
     *
     * @param map  要进行判断的 Map 对象
     * @param key  需要判断的键值 key
     * @param code 要比较的值
     * @return 如果 Map 中 key 对应的 value 与 code 相等则返回 true，否则返回 false
     * @since 2.5.11
     */
    public static <K, V> boolean isEquals(Map<K, V> map, K key, String code) {
        // 如果 map 为空，则返回 false
        if (map == null) {
            return false;
        }
        // 如果 code 为空，则返回 false
        if (EmptyUtils.isEmpty(code)) {
            return false;
        }
        // 获取 Map 中 key 对应的 value，并将其转换成字符串进行比较
        return code.equals(String.valueOf(map.get(key)));
    }

    /**
     * 判断指定的Map中，key对应的value是否与所传入的code参数不相等。
     *
     * @param map  要进行判断的 Map 对象
     * @param key  需要判断的键值 key
     * @param code 要比较的值
     * @return 如果 Map 中 key 对应的 value 与 code 不相等则返回 true，否则返回 false
     * @since 2.5.11
     */
    public static <K, V> boolean isNotEquals(Map<K, V> map, K key, String code) {
        // 调用 isEquals 方法进行比较，然后取反结果
        return !isEquals(map, key, code);
    }

    /**
     * 校验map是否为空
     *
     * @param map 参数
     * @return boolean {@code true} 空
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
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
     * <li>1、map中的key必须与实体中的常量key一致.</li>
     * <li>2、通过class创建一个新的实例,后调用{@link #convertObjects(Object, Map, boolean)}赋值.</li>
     * <li>3、调用{@link EmptyUtils#isNotEmpty(Object)}字符串非空判断，如果字符串为空则不进行实体赋值。转换方式与{@link #convertObjects(Object, Map, boolean)}一致.</li>
     * </ul>
     *
     * @param cls    实体类class
     * @param params 参数
     * @param <T>    类型
     * @param <K>    键泛型
     * @param <V>    value泛型
     * @return T 返回实体
     * @throws IntrospectionException    实体方法异常
     * @throws InvocationTargetException 目标调用异常
     * @throws IllegalAccessException    非法访问异常，注意它是检查(checked)异常，也就是需要显示捕获，此异常会在修饰符禁用访问的时候抛出，可以通过setAccessible(true)抑制修饰符检查来避免抛出此异常
     * @throws NoSuchMethodException     没有找到该方法
     * @throws InstantiationException    实例化异常
     * @since 1.2.3
     */
    public static <T, K, V> T toEntityNotEmpty(Class<T> cls, Map<K, V> params) throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        T obj = cls.getDeclaredConstructor().newInstance();
        return convertObjects(obj, params, true);
    }

    /**
     * map转换为实体对象,并赋值
     * <ul>
     * <li>注:</li>
     * <li>1、map中的key必须与实体中的常量key一致.</li>
     * <li>2、调用{@link EmptyUtils#isNotEmpty(Object)}字符串非空判断,如果字符串为空则不进行实体赋值，转换方式与{@link #convertObjects(Object, Map, boolean)}一致.</li>
     * </ul>
     *
     * @param object 实体对象
     * @param params 参数
     * @param <T>    实体对象
     * @param <K>    KEY
     * @param <V>    value
     * @return T 返回实体
     * @throws IntrospectionException    实体方法异常
     * @throws InvocationTargetException 目标调用异常
     * @throws IllegalAccessException    非法访问异常，注意它是检查(checked)异常，也就是需要显示捕获，此异常会在修饰符禁用访问的时候抛出，可以通过setAccessible(true)抑制修饰符检查来避免抛出此异常
     * @since 1.4.0
     */
    public static <T, K, V> T toEntityNotEmpty(T object, Map<K, V> params) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
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
     * @throws IntrospectionException    实体方法异常
     * @throws InvocationTargetException 目标调用异常
     * @throws IllegalAccessException    非法访问异常，注意它是检查(checked)异常，也就是需要显示捕获，此异常会在修饰符禁用访问的时候抛出，可以通过setAccessible(true)抑制修饰符检查来避免抛出此异常
     * @throws NoSuchMethodException     没有找到该方法
     * @throws InstantiationException    实例化异常
     * @since 1.4.0
     */
    public static <T, K, V> T convertEntity(Class<T> cls, Map<K, V> params) throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
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
     * @throws IntrospectionException    实体方法异常
     * @throws InvocationTargetException 目标调用异常
     * @throws IllegalAccessException    非法访问异常，注意它是检查(checked)异常，也就是需要显示捕获，此异常会在修饰符禁用访问的时候抛出，可以通过setAccessible(true)抑制修饰符检查来避免抛出此异常
     * @since 1.4.0
     */
    public static <T, K, V> T convertEntity(T object, Map<K, V> params) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
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
    private static <T, K, V> T convertObjects(T bean, Map<K, V> params, boolean empty) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Assert.notNull(bean, "bean is null");
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
                        value = Integer.parseInt(String.valueOf(value).trim().strip());
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
                        value = ListUtils.guavaStringToList(String.valueOf(value));
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
     * get参数后转换为String
     *
     * @param <K> key 的类型
     * @param map 参数
     * @param key 键
     * @return String
     */
    public static <K> String getString(Map<? super K, ?> map, K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer != null) {
                return answer.toString();
            }
        }
        return null;
    }

    /**
     * get参数后转换为Long
     *
     * @param map 参数
     * @param key 键
     * @param <K> key 的类型
     * @return Long
     */
    public static <K> Long getLong(Map<? super K, ?> map, K key) {
        final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Long) {
            return (Long) answer;
        }
        return answer.longValue();
    }

    /**
     * 获取map中对应key的 number
     *
     * @param map 参数
     * @param key 键
     * @param <K> key 的类型
     * @return number
     * @since 2.3.1
     */
    public static <K> Number getNumber(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Number) {
                    return (Number) answer;
                }
                if (answer instanceof String) {
                    try {
                        final String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);
                    } catch (final ParseException e) { // NOPMD
                        // failure means null is returned
                    }
                }
            }
        }
        return null;
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
        final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Integer) {
            return (Integer) answer;
        }
        return answer.intValue();
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
        final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Double) {
            return (Double) answer;
        }
        return answer.doubleValue();
    }

    /**
     * get参数后转换为Map
     *
     * @param <K> key 的类型
     * @param map 参数
     * @param key 键
     * @return Map
     */
    public static <K> Map<?, ?> getMap(Map<? super K, ?> map, K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer instanceof Map) {
                return (Map<?, ?>) answer;
            }
        }
        return null;
    }

    /**
     * 为map put不为空的值
     * <p>如果key或value为空时则不put</p>
     *
     * @param map   map
     * @param key   key
     * @param value value
     * @param <K>   the key type
     * @param <V>   the value type
     * @since 2.6.2
     */
    public static <K, V> void putNotEmpty(final Map<? super K, V> map, final K key, final V value) {
        if (map == null) {
            return;
        }
        if (EmptyUtils.isEmpty(key) || EmptyUtils.isEmpty(value)) {
            return;
        }
        map.put(key, value);
    }

    /**
     * 如果键和值都不为空，则将键值对存储到指定的Map中。如果值为空但有默认值，则使用默认值替代空值进行存储。
     *
     * @param map          目标Map
     * @param key          键
     * @param value        值
     * @param defaultValue 默认值
     * @param <K>          键类型
     * @param <V>          值类型
     * @since 2.6.2
     */
    public static <K, V> void putNotEmpty(final Map<? super K, V> map, final K key, final V value, final V defaultValue) {
        if (map == null) {
            return;
        }
        if (EmptyUtils.isEmpty(key)) {
            return;
        }
        if (EmptyUtils.isEmpty(value)) {
            if (EmptyUtils.isNotEmpty(defaultValue)) {
                map.put(key, defaultValue);
            }
        } else {
            map.put(key, value);
        }
    }

    /**
     * 为map put值
     * <p>如果key或value为空时则不put</p>
     * <p>2.6.2 之后修正命名规范，直接使用{@link #putNotEmpty(Map, Object, Object)}</p>
     *
     * @param map   map
     * @param key   key
     * @param value value
     * @param <K>   the key type
     * @param <V>   the value type
     * @since 1.7.3
     */
    @Deprecated
    public static <K, V> void setValue(final Map<? super K, V> map, final K key, final V value) {
        putNotEmpty(map, key, value);
    }

    /**
     * 移除map中对应的key
     *
     * @param map  map
     * @param keys keys
     * @param <K>  key 类型
     * @param <V>  value 类型
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
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAsc(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * 集合内的数据按key的字母降序排序
     *
     * @param <K>  键
     * @param <MV> 值
     * @param map  需要排序的参数
     * @return map 按字母排序后的map
     * @since 2.1.11
     */
    public static <K extends Comparable<? super K>, MV> Map<K, MV> sortByKeyDesc(Map<K, MV> map) {
        return sortByKey(map, true);
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
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKeyAsc(Map<K, V> map) {
        return sortByKey(map, false);
    }

    /**
     * 集合内的数据按key的字母升序排序
     *
     * @param <K>  键
     * @param <V>  值
     * @param map  需要排序的参数
     * @param flag 升序与降标识
     * @return map 按字母排序后的map
     * @since 2.4.4
     */
    private static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean flag) {
        Map<K, V> obj = new LinkedHashMap<>();
        final List<Map.Entry<K, V>> infos = new ArrayList<>(map.entrySet());
        if (flag) {
            infos.sort(Map.Entry.<K, V>comparingByKey().reversed());
        } else {
            infos.sort(Map.Entry.comparingByKey());// 重写集合的排序方法：按字母顺序
        }
        for (final Map.Entry<K, V> subMap : infos) {
            obj.put(subMap.getKey(), subMap.getValue());
        }
        return obj;
    }

    /**
     * cookie 中的value值转换成map
     *
     * @param value cookie中的value
     * @return Map
     * @since 2.3.10
     */
    public static Map<String, String> cookieToMap(String value) {
        if (EmptyUtils.isEmpty(value)) {
            return new HashMap<>(0);
        }
        value = value.replace(StrPool.SPACE, StrPool.EMPTY);
        Map<String, String> map = new HashMap<>();
        if (value.contains(";")) {
            String[] values = value.split(";");
            for (String val : values) {
                String[] vals = val.split("=");
                map.put(vals[0], vals[1]);
            }
        } else {
            String[] values = value.split("=");
            map.put(values[0], values[1]);
        }
        return map;
    }

    /**
     * 将一个 Java 对象转换为一个包含字段名和字段值的 Map。
     *
     * @param bean 要转换的对象
     * @param <T>  对象的类型
     * @return 包含字段名和字段值的 Map
     * @throws ToolboxException 如果访问字段时发生异常
     * @since 2.7.2
     */
    public static <T> Map<String, Object> entityToMap(T bean) throws ToolboxException {
        return entityToMap(bean, false);
    }

    /**
     * 将JavaBean对象转换为Map对象。
     *
     * @param bean       要转换的JavaBean对象
     * @param isNotEmpty 是否只添加非空字段到Map中
     * @param <T>        JavaBean对象类型
     * @return 转换后的Map对象
     * @since 2.7.2
     */
    public static <T> Map<String, Object> entityToMap(T bean, boolean isNotEmpty) {
        Map<String, Object> item = new HashMap<>();
        Class<?> clazz = bean.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                // 将字段名和字段值添加到 Map 中
                if (isNotEmpty) {
                    if (EmptyUtils.isNotEmpty(field.get(bean))) {
                        item.put(field.getName(), field.get(bean));
                    }
                } else {
                    item.put(field.getName(), field.get(bean));
                }
            } catch (IllegalAccessException illegalAccessException) {
                // 如果访问字段时发生异常，抛出自定义异常
                throw new ToolboxException(illegalAccessException);
            }
        }
        return item;
    }
}
