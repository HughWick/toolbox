package com.github.hugh.util;

import com.github.hugh.exception.ToolboxException;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
     * list默认分隔符 {@code ,}
     */
    private static final String LIST_SEPARATOR = ",";

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
            throw new NullPointerException();
        }
        List<List<T>> pagedList = Lists.partition(originList, size);//根据条数切割成多个list
        return pagedList.get(page);//根据下标(分页)对应的数据
    }

    /**
     * Google guava 将字符串数组转 List
     * <p>默认通过,作为分隔符转成List,并置空[]与"符号</p>
     * <p>不支持多级嵌套list转换</p>
     *
     * @param string 字符串 ["a","b"]
     * @return List
     * @since 1.6.10
     */
    public static List<String> guavaStringToList(String string) {
        return guavaStringToList(string, ",");
    }

    /**
     * Google guava 将字符串数组转 List
     * <p>并置空[]与"符号</p>
     *
     * @param string    字符串 ["a","b"]
     * @param separator 分隔符
     * @return List
     * @since 1.6.11
     */
    public static List<String> guavaStringToList(String string, String separator) {
        return guavaStringToList(string, separator, LIST_CHAR_MATCHER);
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
        List<String> strings = Splitter.on(separator)
                .trimResults()//去除前后空格
                .trimResults(charMatcher)
                .omitEmptyStrings()//用于去除为空格的分割结果
                .splitToList(string);
        return new ArrayList<>(strings);
    }

    /**
     * list转字符串
     * <p>
     * 拼接格式为：A,B,C
     * </p>
     *
     * @param list 源
     * @param <T>  类型
     * @return String
     * @since 2.1.11
     */
    public static <T> String listToString(List<T> list) {
        return listToString(list, LIST_SEPARATOR);
    }

    /**
     * list转字符串
     * <p>
     * 拼接格式为：A,B,C
     * </p>
     *
     * @param list 源
     * @param <T>  类型
     * @return String
     * @since 2.1.10
     */
    public static <T> String listToString(List<T> list, String separator) {
        return listObjectToString(list, null, separator, false);
    }

    /**
     * list转 in sql 语句字符串
     *
     * @param list 源集合
     * @param <T>  类型
     * @return String
     * @since 2.3.4
     */
    public static <T> String listToInSql(List<T> list) {
        return listObjectToString(list, null, null, true);
    }

    /**
     * list对象转换对应的字符串
     *
     * @param list 对象集合
     * @param name 对象get方法名称
     * @param <T>  实体类型
     * @return String
     * @since 2.3.7
     */
    public static <T> String listObjectToString(List<T> list, String name) {
        return listObjectToString(list, name, LIST_SEPARATOR, false);
    }

    /**
     * list对象转换对应的字符串
     *
     * @param list      对象集合
     * @param name      对象get方法名称
     * @param separator 分隔符
     * @param <T>       实体类型
     * @param inSql     是否拼接为in sql语句
     * @return String
     * @since 2.3.7
     */
    public static <T> String listObjectToString(List<T> list, String name, String separator, boolean inSql) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (T obj : list) {
                Object value;
                if (EmptyUtils.isEmpty(name)) {
                    value = obj;
                } else {
                    if (obj instanceof Map) {
                        value = ((Map<?, ?>) obj).get(name);
                    } else {
                        Method m = obj.getClass().getMethod(("get" + org.springframework.util.StringUtils.capitalize(name)));
                        value = m.invoke(obj);
                    }
                }
                if (EmptyUtils.isEmpty(value)) {
                    continue;
                }
                if (inSql) {
                    stringBuilder.append("'").append(value).append("'").append(LIST_SEPARATOR);
                } else {
                    stringBuilder.append(value).append(separator);
                }
            }
            return StringUtils.trimLastPlace(stringBuilder);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ToolboxException(e);
        }
    }
}
