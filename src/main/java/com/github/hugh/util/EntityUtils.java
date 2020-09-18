package com.github.hugh.util;

import com.esotericsoftware.kryo.Kryo;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * 实体操作工具类
 *
 * @author hugh
 * @since 1.1.4
 */
public class EntityUtils {

    /**
     * 实现两个实体类属性之间的复制
     *
     * @param source 源文
     * @param dest   复制目标
     * @throws Exception
     */
    public static void copy(Object source, Object dest) throws Exception {
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
     * @param source 拷贝对象
     * @return T 实体对象
     */
    public static <T> T deepClone(T source) {
        return new Kryo().copy(source);
    }
}
