package com.github.hugh.util;

import com.github.hugh.exception.ToolBoxException;

import java.util.ArrayList;
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
     * list数据分页
     *
     * @param list 数据
     * @param page 页数
     * @param size 条数
     * @return List
     */
    public static List<?> listDatas(List<?> list, int page, int size) {
        if (isEmpty(list)) {
            throw new ToolBoxException("数据不能为空!");
        }
        int totalCount = list.size();
        page = page - 1;
        int fromIndex = page * size;
        // 分页不能大于总数
        if (fromIndex > totalCount) {
            throw new ToolBoxException("页数或分页大小不正确!");
        }
        int toIndex = ((page + 1) * size);
        if (toIndex > totalCount) {
            toIndex = totalCount;
        }
        return list.subList(fromIndex, toIndex);
    }

    /**
     * 返回总页数
     *
     * @param list 总集合
     * @param size 条数
     * @return int 总页数
     */
    public static int getPages(Collection<?> list, Integer size) {
        int count = list.size() / size;
        if (list.isEmpty()) {
            return 0;
        }
        if (list.size() <= size) {
            return 1;
        } else if (count % size == 0) {
            return count;
        } else {
            return count + 1;
        }
    }

    /**
     * 根据第二集合中的值，删除源中对应的元素
     *
     * @param source 源集合
     * @param del    需删除的元素
     * @return List
     */
    public static List<?> remove(List<?> source, List<?> del) {
        List cache = new ArrayList<>();   // 将需要删除的集合合并至缓存中
        cache.addAll(del);
        for (int i = 0; i < cache.size(); i++) {// 遍历需要删除的集合缓存
            source.remove(cache.get(i)); // 移除掉对应的元素
        }
        return source; // 返回已移除的结果集
    }

    /**
     * 将一个list均分成n个list,主要通过偏移量来实现的
     *
     * @param <T>    泛型
     * @param source 数据源
     * @param n      切割条数
     * @return List
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<>();
        int remaider = source.size() % n; // (先计算出余数)
        int number = source.size() / n; // 然后是商
        int offset = 0;// 偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
}
