package com.github.hugh.util;

import com.github.hugh.exception.ToolboxException;
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
     * <ul>
     * <li>注:切割后是根据总集合内的下标获取值、所以page是从0开始</li>
     * </ul>
     *
     * @param originList 数据
     * @param page       页数
     * @param size       条数
     * @return Object
     * @since 1.1.0
     */
    public static Object guavaPartitionList(List<?> originList, int page, int size) {
        if (isEmpty(originList)) {
            throw new ToolboxException("数据不能为空!");
        }
        List pagedList = Lists.partition(originList, size);//根据条数切割成多个list
        return pagedList.get(page);//根据下标(分页)对应的数据
    }

    /**
     * Google guava 数组转 List
     *
     * @param array 数组
     * @return List
     * @since 1.1.0
     */
    public static List guavaArrToList(String[] array) {
        return Lists.newArrayList(array);
    }

    /**
     * Google guava 字符串转 List
     *
     * @param string 字符串
     * @return List
     * @since 1.1.0
     */
    public static List guavaStringToList(String string) {
        return Lists.newArrayList(string);
    }

//    public static <T> Comparator<List<T>> listComparator(Map<Integer, Boolean> colSortMaps) {
//        Ordering ordering = Ordering.natural();
//        return (list1, list2) -> {
//            ComparisonChain compareChain = ComparisonChain.start();
//            for (Integer index : colSortMaps.keySet()) {
//                T currObj = Optional.ofNullable(list1.get(index)).orElse(null);//如果 Optional 中有值则将其返回，否则返回 orElse 方法传入的参数。
//                T compObj = Optional.ofNullable(list2.get(index)).orElse(null);
//                Comparator<Object> objComparator = objComparator(ordering, colSortMaps.getOrDefault(index, true));
//                compareChain = compareChain.compare(currObj, compObj, objComparator);
//            }
//            return compareChain.result();
//        };
//    }

//    private static Comparator<Object> objComparator(Ordering ordering, boolean asc) {
//        if (asc) {
//            return ordering::compare;
//        } else {
//            return ordering.reverse()::compare;
//        }
//    }
}
