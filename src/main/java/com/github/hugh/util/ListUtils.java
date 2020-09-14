package com.github.hugh.util;

import com.github.hugh.exception.ToolBoxException;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * List工具类
 *
 * @author hugh
 * @version 1.0.0
 */
public class ListUtils {

    /**
     * 判断集合是否为null或者集合内元素空
     *
     * @param collection 集合
     * @return boolean {@code true} 空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合不是空集合
     *
     * @param collection 集合
     * @return boolean {@code true} 有元素
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Google guava list数据分页
     * <li>注:切割后是根据总集合内的下标获取值、所以page是从0开始</li>
     *
     * @param originList 数据
     * @param page       页数
     * @param size       条数
     * @return Object
     * @since 1.0.7
     */
    public static Object guavaPartitionList(List<?> originList, int page, int size) {
        if (isEmpty(originList)) {
            throw new ToolBoxException("数据不能为空!");
        }
        List pagedList = Lists.partition(originList, size);//根据条数切割成多个list
        return pagedList.get(page);//根据下标(分页)对应的数据
    }

    /**
     * Google guava 数组转 List
     *
     * @param array 数组
     * @return List
     * @since 1.0.7
     */
    public static List guavaArrToList(String[] array) {
        return Lists.newArrayList(array);
    }

    /**
     * Google guava 字符串转 List
     *
     * @param string 字符串
     * @return List
     * @since 1.0.7
     */
    public static List guavaStringToList(String string) {
        return Lists.newArrayList(string);
    }
}
