package com.github.hugh.util;

import com.github.hugh.bean.dto.EntityCompare;
import com.github.hugh.exception.ToolboxException;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 实体属性
 *
 * @author hugh
 * @since 2.5.6
 */
public class EntityCompareUtils {

    /**
     * 比较两个对象是否不同，忽略指定属性。
     *
     * @param obj1             对象1
     * @param obj2             对象2
     * @param ignoreProperties 需要忽略比较的属性列表
     * @return 如果两个对象不同，返回 true；否则，返回 false。
     */
    public static boolean isObjDiff(Object obj1, Object obj2, String... ignoreProperties) {
        return ListUtils.isNotEmpty(compare(obj1, obj2, ignoreProperties));
    }

    /**
     * 判断两个对象是否相同，并忽略指定属性。
     *
     * @param obj1             对象1
     * @param obj2             对象2
     * @param ignoreProperties 需要忽略比较的属性列表
     * @return 如果两个对象相同，返回 true；否则，返回 false。
     */
    public static boolean isObjSame(Object obj1, Object obj2, String... ignoreProperties) {
        return ListUtils.isEmpty(compare(obj1, obj2, ignoreProperties));
    }

    /**
     * 比较两个对象的属性，返回它们之间的差异信息。
     *
     * @param obj1             对象1
     * @param obj2             对象2
     * @param ignoreProperties 忽略比较的属性
     * @return 两个对象之间的差异信息列表
     */
    public static List<EntityCompare> compare(Object obj1, Object obj2, String... ignoreProperties) {
        try {
            // 初始化 ignoreList 和 item
            List<String> ignoreList = buildIgnoreList(ignoreProperties);
            List<EntityCompare> item = new ArrayList<>();
            // 比较两个对象的属性
            compareProperties(obj1, obj2, ignoreList, item);
            return item;
        } catch (Exception exception) {
            throw new ToolboxException(exception);
        }
    }

    /**
     * 构建忽略列表
     *
     * @param ignoreProperties 需要忽略的属性
     * @return 如果有需要忽略的属性，则返回忽略列表，否则返回null
     */
    private static List<String> buildIgnoreList(String... ignoreProperties) {
        if (ignoreProperties != null && ignoreProperties.length > 0) {
            return Arrays.asList(ignoreProperties);
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 比较两个对象的属性值并记录差异结果
     *
     * @param obj1       对象1
     * @param obj2       对象2
     * @param ignoreList 需要忽略比对的属性列表
     * @param item       记录差异结果的集合
     * @throws IntrospectionException 如果获取属性描述器失败，则抛出此异常
     */
    private static void compareProperties(Object obj1, Object obj2, List<String> ignoreList, List<EntityCompare> item) throws IntrospectionException {
        if (obj1.getClass() != obj2.getClass()) {
            return;
        }
        PropertyDescriptor[] pds = Introspector.getBeanInfo(obj1.getClass(), Object.class).getPropertyDescriptors();
        Arrays.stream(pds)
//                .filter(pd -> ignoreList != null && ignoreList.contains(pd.getName()))
                .forEach(pd -> compareProperty(pd, obj1, obj2, ignoreList, item));
    }

    /**
     * 比较两个对象的某一个属性值
     *
     * @param pd         属性描述器
     * @param obj1       对象1
     * @param obj2       对象2
     * @param ignoreList 需要忽略比对的属性列表
     * @param item       记录差异结果的集合
     */
    private static void compareProperty(PropertyDescriptor pd, Object obj1, Object obj2, List<String> ignoreList, List<EntityCompare> item) {
        String name = pd.getName();
        Method readMethod = pd.getReadMethod();
        Object o1;
        Object o2;
        try {
            o1 = readMethod.invoke(obj1);
            o2 = readMethod.invoke(obj2);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            throw new ToolboxException(exception.getMessage(), exception);
        }
        boolean needCompare;
        if (ignoreList != null && ignoreList.contains(pd.getName())) {
            needCompare = false;
        } else {
            needCompare = compareObjects(o1, o2);
        }
        if (needCompare) {
            addEntityCompare(name, o1, o2, item);
        }
    }

    /**
     * 比较两个对象的属性值是否相等，支持特殊数据类型转换
     *
     * @param o1 对象1的属性值
     * @param o2 对象2的属性值
     * @return 如果属性值不相等，则返回true；否则返回false；
     */
    private static boolean compareObjects(Object o1, Object o2) {
//        if (o1 instanceof Timestamp) {
//            o1 = new Date(((Timestamp) o1).getTime());
//        }
//        if (o2 instanceof Timestamp) {
//            o2 = new Date(((Timestamp) o2).getTime());
//        }
        if (o1 == null && o2 == null) {
            return false;
        }
        if (o1 == null) {
            return true;
        }
        return !o1.equals(o2);
    }

    /**
     * 将比对结果添加到集合中
     *
     * @param fieldName 属性名称
     * @param oldValue  对象1的属性值
     * @param newValue  对象2的属性值
     * @param item      记录比对结果的集合
     */
    private static void addEntityCompare(String fieldName, Object oldValue, Object newValue, List<EntityCompare> item) {
        EntityCompare entityCompare = new EntityCompare();
        entityCompare.setFieldName(fieldName);
        entityCompare.setOldValue(oldValue);
        entityCompare.setNewValue(newValue);
        item.add(entityCompare);
    }
}
