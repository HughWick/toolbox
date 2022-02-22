package com.github.hugh.util;

import com.esotericsoftware.kryo.Kryo;
import com.github.hugh.support.EntityUtilsCallBack;
import com.github.hugh.support.instance.Instance;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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

    /**
     * 复制list 集合属性
     *
     * @param <T>     目标对象类型
     * @param <S>     源-对象类型
     * @param sources 源
     * @param target  目标类
     * @return List
     * @since 2.1.12
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 复制list集合
     * <p>
     * 使用场景：Entity、Bo、Vo层数据的复制，因为BeanUtils.copyProperties只能给目标对象的属性赋值，却不能在List集合下循环赋值，因此添加该方法
     * 如：{@code List<AdminEntity> }赋值到 {@code List<AdminVo>} ，{@code List<AdminVo>}中的 AdminVo 属性都会被赋予到值
     * S: 数据源类 ，T: 目标类::new(eg: AdminVo::new)
     *
     * @param <T>      目标对象类型
     * @param <S>      源-对象类型
     * @param sources  源
     * @param target   目标类
     * @param callBack 回调方法，属性复制完成后进行调用
     * @since 2.1.12
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, EntityUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            BeanUtils.copyProperties(source, t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }
}