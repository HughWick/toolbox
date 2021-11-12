package com.github.hugh.util;

import com.esotericsoftware.kryo.Kryo;
import com.github.hugh.support.instance.Instance;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

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
        var sourceBean = Introspector.getBeanInfo(source.getClass(), java.lang.Object.class); // 获取属性
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();
        var destBean = Introspector.getBeanInfo(dest.getClass(), java.lang.Object.class);
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
}
