package com.github.hugh.util;

import com.github.hugh.exception.ToolboxException;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
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

    private ListUtils() {
    }

    /**
     * 替换字符串中的[]、"、 "、符号表达式
     */
    private static final CharMatcher LIST_CHAR_MATCHER = CharMatcher.anyOf("[]\" \"");

    /**
     * 判断集合是否为null或者集合内元素空
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return boolean {@code true} 空
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合不为空
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return boolean {@code true} 有元素
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
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
     * @return List 切割后的list
     * @since 1.1.0
     */
    public static <T> List<T> guavaPartitionList(List<T> originList, int page, int size) {
        if (isEmpty(originList)) {
            throw new ToolboxException("数据不能为空!");
        }
        List<List<T>> pagedList = Lists.partition(originList, size);//根据条数切割成多个list
        return pagedList.get(page);//根据下标(分页)对应的数据
    }

    /**
     * Google guava 数组转 List
     *
     * @param array 数组
     * @return List
     * @since 1.1.0
     */
    public static <T> List<?> guavaArrToList(T array) {
        return Lists.newArrayList(array);
    }


    /**
     * Google guava 将字符串数组转 List
     * <p>默认通过,作为分隔符转成List,并置空[]与"符号</p>
     *
     * @param string 字符串 ["a","b"]
     * @return List
     * @since 1.6.10
     */
    public static List<String> guavaStringToList(String string) {
        return guavaStringToList(string, ",", LIST_CHAR_MATCHER);
    }

    /**
     * Google guava 将字符串数组转 List
     * <p>根据指定的分隔符进行切割,并置空[]与"符号</p>
     * <p>1.6.10 重构</p>
     *
     * @param string      字符串 ["a","b"]
     * @param separator   分隔符
     * @param charMatcher 字符匹配方式
     * @return List
     * @since 1.1.0
     */
    public static List<String> guavaStringToList(String string, String separator, CharMatcher charMatcher) {
        return Splitter.on(separator)
                .trimResults()//去除前后空格
                .trimResults(charMatcher)
                .omitEmptyStrings()//用于去除为空格的分割结果
                .splitToList(string);
    }
}
