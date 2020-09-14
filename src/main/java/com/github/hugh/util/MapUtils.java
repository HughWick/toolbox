package com.github.hugh.util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Map 工具类
 * <ul>
 * <li>该类获取map中的值使用了apache MapUtil进行二次封装</li>
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
     * @return boolean @code true} 一致
     */
    public static boolean isSuccess(Map<?, ?> map, String key, String code) {
        if (map == null) {
            return false;
        }
        if (com.github.hugh.util.EmptyUtils.isEmpty(code)) {
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
    public static boolean isFailure(Map<?, ?> map, String key, String code) {
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
    public static Map<String, Object> cycleQueryPar(HttpServletRequest request) {
        return cycleQueryPar(request, "page", "size");
    }

    /**
     * 遍历查询参数、并且移除其中指定多余键
     *
     * @param request 客户端的请求
     * @param keys  需要删除的key
     * @return Map
     * @since 1.1.0
     */
    public static Map<String, Object> cycleQueryPar(HttpServletRequest request, String... keys) {
        Map<String, Object> map = cyclePar(request);
        for (String key : keys) {
            map.remove(key);
        }
        return map;
    }


    /**
     * 将map转换为实体对象,并赋值
     * <ul>
     * <li>map中的key必须与实体中的常量key一致,且命名规范为驼峰</li>
     * </ul>
     *
     * @param bean   实体对象
     * @param params 参数
     * @throws Exception
     */
    public static void toEntity(Object bean, Map<String, Object> params) throws Exception {
        if (bean == null) {
            throw new RuntimeException("bean is null");
        }
        for (Field field : bean.getClass().getDeclaredFields()) {
            field.setAccessible(true);// 不会去检查Java语言权限控制（private之类的）
            String key = field.getName();
            String upperCaseKey = key.substring(0, 1).toUpperCase() + key.substring(1);// 将属性的首字符大写，方便构造get，set方法
            Method method = bean.getClass().getDeclaredMethod("set" + upperCaseKey, field.getType());
            String type = field.getType().getSimpleName();// 获取类型
            Object value = params.get(key);
            if (EmptyUtils.isNotEmpty(value)) {
                switch (type) {
                    case "int":
                    case "Integer":
                        value = Integer.parseInt(String.valueOf(value));
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
                            value = DateUtils.parseDate(String.valueOf(value));
                        }
                        break;
                }
                method.invoke(bean, value);
            }
        }
    }

    /**
     * get参数后转换为String
     *
     * @param map 参数
     * @param key 键
     * @return String
     */
    public static String getString(Map map, String key) {
        return org.apache.commons.collections4.MapUtils.getString(map, key);
    }

    /**
     * get参数后转换为Long
     *
     * @param map 参数
     * @param key 键
     * @return String
     */
    public static Long getLong(Map map, String key) {
        return org.apache.commons.collections4.MapUtils.getLong(map, key);
    }

    /**
     * get参数后转换为Int
     *
     * @param map 参数
     * @param key 键
     * @return String
     */
    public static Integer getInt(Map map, String key) {
        return org.apache.commons.collections4.MapUtils.getInteger(map, key);
    }

    /**
     * get参数后转换为Double
     *
     * @param map 参数
     * @param key 键
     * @return String
     */
    public static Double getDouble(Map map, String key) {
        return org.apache.commons.collections4.MapUtils.getDouble(map, key);
    }

    /**
     * get参数后转换为Map
     *
     * @param map 参数
     * @param key 键
     * @return String
     */
    public static Map getMap(Map map, String key) {
        return org.apache.commons.collections4.MapUtils.getMap(map, key);
    }

}
