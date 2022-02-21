package com.github.hugh.util;

import com.esotericsoftware.kryo.Kryo;
import com.github.hugh.support.instance.Instance;
import org.springframework.beans.BeanUtils;

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
     */
    public static <T> void copy(T source, T dest) {
        BeanUtils.copyProperties(source, dest);
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
