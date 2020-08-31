package com.github.hugh.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

public class MapUtils {

    /**
     * 校验map中code状态是否为0000(成功状态)
     *
     * @param map  Map
     * @param key  map中的key
     * @param code 需要验证的code码
     * @return
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
     * 校验map中code状态不是0000(成功状态)
     *
     * @param map
     * @return boolean code值不为0000 返回true
     */
    public static boolean isFailure(Map<?, ?> map, String key, String code) {
        return !isSuccess(map, key, code);
    }

    /**
     * 校验map是否为空
     *
     * @param map
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
     * @param request
     * @return Map
     */
//    public static Map<String, Object> cyclePar(HttpServletRequest request) {
//        Map<String, Object> map = new HashMap<>();
//        if (request == null) {
//            return map;
//        }
//        Enumeration<String> isKey = request.getParameterNames();
//        while (isKey.hasMoreElements()) {
//            String key = isKey.nextElement();
//            String value = request.getParameter(key);
//            map.put(key, value);
//        }
//        return map;
//    }

    /**
     * 遍历查询参数
     * <li>并且移除其中的page、size键</li>
     *
     * @param request
     * @return Map
     */
//    public static Map<String, Object> cycleQueryPar(HttpServletRequest request) {
//        Map<String, Object> map = cyclePar(request);
//        map.remove("page");
//        map.remove("size");
//        return map;
//    }


    /**
     * 将map转换为实体对象,并赋值
     * <li>map中的key必须与实体中的常量key一致</li>
     *
     * @param bean   实体对象
     * @param params 参数
     * @return Object 实体结果
     * @throws Exception
     */
    public static Object toEntity(Object bean, Map<String, Object> params) throws Exception {
        for (Field field : bean.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String key = field.getName();
            String upperCaseKey = key.substring(0, 1).toUpperCase() + key.substring(1);// 将属性的首字符大写，方便构造get，set方法
            Method method = bean.getClass().getDeclaredMethod("set" + upperCaseKey, field.getType());
            String type = field.getType().getSimpleName();// 获取类型
            if (params.get(key) != null) {
                Object value = params.get(key);
                if (type.equals("int") || type.equals("Integer")) {
                    value = Integer.parseInt(String.valueOf(value));
                } else if (type.equals("long") || type.equals("Long")) {
                    value = Long.parseLong(String.valueOf(value));
                } else if (type.equals("double") || type.equals("Double")) {
                    value = Double.parseDouble(String.valueOf(value));
                } else if (type.equals("Date")) {// 当时日期类型时，进行格式校验
                    if (value instanceof Date) {
                        //日期类型不处理
                    } else if (com.github.hugh.util.DateUtils.isDateFormat(String.valueOf(value))) {
                        value = com.github.hugh.util.DateUtils.parseDate(String.valueOf(value));
                    }
                }
                method.invoke(bean, value);
            }
        }
        return bean;
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
