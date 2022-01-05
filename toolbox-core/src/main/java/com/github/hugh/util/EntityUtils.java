package com.github.hugh.util;

import com.esotericsoftware.kryo.Kryo;
import com.github.hugh.support.instance.Instance;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体操作工具类
 *
 * @author hugh
 * @since 1.2.0
 */
public class EntityUtils {

    /**
     * 实现两个实体类属性之间的复制
     *
     * @param <T>    实体类型
     * @param source 源文
     * @param dest   复制目标
     * @throws IntrospectionException    无法将字符串类名称映射到 Class 对象、无法解析字符串方法名，或者指定对其用途而言具有错误类型签名的方法名称
     * @throws InvocationTargetException 如果底层方法抛出异常
     * @throws IllegalAccessException    如果这个{@code Method}对象正在执行Java语言访问控制，并且底层方法不可访问。
     */
    public static <T> void copy(T source, T dest) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), java.lang.Object.class); // 获取属性
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();
        BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(), java.lang.Object.class);
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : sourceProperty) {
            for (PropertyDescriptor descriptor : destProperty) {
                if (propertyDescriptor.getName().equals(descriptor.getName())) {
                    // 调用source的getter方法和dest的setter方法
                    descriptor.getWriteMethod().invoke(dest, propertyDescriptor.getReadMethod().invoke(source));
                    break;
                }
            }
        }
    }

    /**
     * 通过Kryo框架深拷贝
     *
     * @param <T>    实体类型
     * @param source 拷贝对象
     * @return T 实体对象
     */
    public static <T> T deepClone(T source) {
        return Instance.getInstance().singleton(Kryo.class).copy(source);
    }

    /**
     * 复制list源 中的所有对象到新的list集合中并返回
     *
     * @param sourceList 源
     * @param clazz      目标类
     * @param <E>        类型
     * @param <T>        返回类型
     * @return List
     * @throws IllegalAccessException    如果这个{@code Method}对象正在执行Java语言访问控制，并且底层方法不可访问。
     * @throws IntrospectionException    无法将字符串类名称映射到 Class 对象、无法解析字符串方法名，或者指定对其用途而言具有错误类型签名的方法名称
     * @throws InvocationTargetException 如果底层方法抛出异常
     * @throws InstantiationException    当试图通过newInstance()方法创建某个类的实例,而该类是一个抽象类或接口时,抛出该异常。
     * @throws NoSuchMethodException     没有找到该方法
     * @since 2.1.10
     */
    public static <E, T> List<T> copy(List<E> sourceList, Class<T> clazz) throws IllegalAccessException, IntrospectionException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (ListUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>();
        for (E object : sourceList) {
            T o = clazz.getDeclaredConstructor().newInstance();
            copy(object, o);
            list.add(o);
        }
        return list;
    }
}
