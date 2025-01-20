package com.github.hugh.components.entity;

import com.github.hugh.bean.dto.EntityCompareResult;
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
 * 实体比较类
 *
 * @since 2.7.4
 */
public class EntityCompare<E> {

    private E objects1;
    private E objects2;
    private List<String> ignoreProperties;

    public EntityCompare() {

    }

    /**
     * 创建一个 EntityCompare 实例，用于比较两个对象，并指定需要忽略比较的属性。
     *
     * @param objects1         第一个对象
     * @param objects2         第二个对象
     * @param ignoreProperties 需要忽略比较的属性列表
     */
    public EntityCompare(E objects1, E objects2, List<String> ignoreProperties) {
        this.objects1 = objects1;
        this.objects2 = objects2;
        this.ignoreProperties = ignoreProperties;
    }

    /**
     * 创建一个 EntityCompare 实例，用于比较两个对象。
     *
     * @param objects1 第一个对象
     * @param objects2 第二个对象
     */
    public EntityCompare(E objects1, E objects2) {
        this.objects1 = objects1;
        this.objects2 = objects2;
    }

    /**
     * 设置 objectsList1 列表
     *
     * @param objects 要设置的 objectsList1 列表
     */
    public void setObjectsOne(E objects) {
        this.objects1 = objects;
    }

    /**
     * 设置 objectsList2 列表
     *
     * @param objects 要设置的 objectsList2 列表
     */
    public void setObjectsTwo(E objects) {
        this.objects2 = objects;
    }

    /**
     * 设置 ignoreProperties 列表
     *
     * @param ignoreProperties 要设置的 ignoreProperties 列表
     */
    public void setIgnoreProperties(List<String> ignoreProperties) {
        this.ignoreProperties = ignoreProperties;
    }

    /**
     * 创建一个 EntityCompare 实例，用于比较两个对象
     *
     * @param objects1 第一个对象
     * @param objects2 第二个对象
     * @param <E>      对象的类型
     * @return EntityCompare 实例
     */
    public static <E> EntityCompare<E> on(E objects1, E objects2) {
        return on(objects1, objects2, new ArrayList<>());
    }

    /**
     * 创建一个 EntityCompare 实例，用于比较两个对象，并指定需要忽略比较的属性
     *
     * @param objects1         第一个对象
     * @param objects2         第二个对象
     * @param ignoreProperties 需要忽略比较的属性数组
     * @param <E>              对象的类型
     * @return EntityCompare 实例
     */
    public static <E> EntityCompare<E> on(E objects1, E objects2, String... ignoreProperties) {
        return on(objects1, objects2, buildIgnoreList(ignoreProperties));
    }

    /**
     * 创建一个 EntityCompare 实例，用于比较两个对象，并指定需要忽略比较的属性
     *
     * @param objects1         第一个对象
     * @param objects2         第二个对象
     * @param ignoreProperties 需要忽略比较的属性列表
     * @param <E>              对象的类型
     * @return EntityCompare 实例
     */
    public static <E> EntityCompare<E> on(E objects1, E objects2, List<String> ignoreProperties) {
        return new EntityCompare<>(objects1, objects2, ignoreProperties);
    }


    /**
     * 构建忽略列表
     *
     * @param ignoreProperties 需要忽略的属性
     * @return 如果有需要忽略的属性，则返回忽略列表，否则返回null
     */
    private static List<String> buildIgnoreList(String... ignoreProperties) {
        if (ignoreProperties == null || ignoreProperties.length == 0) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(ignoreProperties);
        }
    }

    /**
     * 比较两个对象的属性值并记录差异结果
     *
     * @return List
     * @throws IntrospectionException 如果获取属性描述器失败，则抛出此异常
     */
    public List<EntityCompareResult> compareProperties() throws IntrospectionException {
        if (objects1.getClass() != objects2.getClass()) {
            return Collections.emptyList();
        }
        List<EntityCompareResult> item = new ArrayList<>();
        PropertyDescriptor[] pds = Introspector.getBeanInfo(objects1.getClass(), Object.class).getPropertyDescriptors();
        Arrays.stream(pds)
//                .filter(pd -> ignoreList != null && ignoreList.contains(pd.getName()))
                .forEach(pd -> compareProperty(pd, objects1, objects2, ignoreProperties, item));
        return item;
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
    private static void compareProperty(PropertyDescriptor pd, Object obj1, Object obj2, List<String> ignoreList, List<EntityCompareResult> item) {
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
    public static boolean compareObjects(Object o1, Object o2) {
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
    private static void addEntityCompare(String fieldName, Object oldValue, Object newValue, List<EntityCompareResult> item) {
        EntityCompareResult entityCompare = new EntityCompareResult();
        entityCompare.setFieldName(fieldName);
        entityCompare.setOldValue(oldValue);
        entityCompare.setNewValue(newValue);
        item.add(entityCompare);
    }
}
