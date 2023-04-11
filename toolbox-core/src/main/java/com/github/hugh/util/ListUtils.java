package com.github.hugh.util;

import com.github.hugh.constant.StrPool;
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
import java.util.stream.Collectors;

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
     * @param <T>        源数据类型
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
        return guavaStringToList(string, StrPool.COMMA);
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
     * 使用 Guava Splitter 将字符串拆分为字符串列表，然后将其转换为整数列表
     * 使用默认分隔符逗号
     *
     * @param string 待处理的字符串
     * @return 返回包含整数的 List 对象
     */
    public static List<Integer> guavaStringToListInt(String string) {
        return guavaStringToListInt(string, StrPool.COMMA);
    }

    /**
     * 使用 Guava Splitter 将字符串拆分为字符串列表，然后将其转换为整数列表
     *
     * @param string    待处理的字符串
     * @param separator 用于分割整数的分隔符
     * @return 返回包含整数的 List 对象
     * @since 2.5.5
     */
    public static List<Integer> guavaStringToListInt(String string, String separator) {
        return guavaStringToListInt(string, separator, LIST_CHAR_MATCHER);
    }

    /**
     * 使用 Guava Splitter 将字符串拆分为字符串列表，然后将其转换为整数列表
     *
     * @param string      待处理的字符串
     * @param separator   用于分割整数的分隔符
     * @param charMatcher 用于去除前后空格和指定字符的 CharMatcher
     * @return 返回包含整数的 List 对象
     * @since 2.5.5
     */
    public static List<Integer> guavaStringToListInt(String string, String separator, CharMatcher charMatcher) {
        return guavaStringToList(string, separator, charMatcher)
                .stream()
                // 使用 mapToInt() 方法将字符串流转换为整数流
                .mapToInt(Integer::parseInt)
                // 使用 boxed() 方法将整数基础类型流转换为整数流
                .boxed()
                .collect(Collectors.toList());
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
        return listToString(list, StrPool.COMMA);
    }

    /**
     * list转字符串
     * <p>
     * 拼接格式为：A,B,C
     * </p>
     *
     * @param list      源
     * @param separator 分隔符
     * @param <T>       类型
     * @return String
     * @since 2.1.10
     */
    public static <T> String listToString(List<T> list, String separator) {
        return listObjectToString(list, null, separator, false);
    }

    /**
     * 字符串数组 转 in sql 语句字符串
     *
     * @param strArray 源字符串
     * @return String
     * @since 2.4.4
     */
    public static String strArrayToInSql(String strArray) {
        return strArrayToInSql(strArray, StrPool.COMMA);
    }

    /**
     * 字符串数组 转 in sql 语句字符串
     *
     * @param strArray  源字符串
     * @param separator 分隔符
     * @return String
     * @since 2.4.4
     */
    public static String strArrayToInSql(String strArray, String separator) {
        if (EmptyUtils.isEmpty(strArray)) {
            throw new ToolboxException("string is null");
        }
        List<String> strings = guavaStringToList(strArray, separator);
        return listToInSql(strings);
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
     * list转 in sql 语句字符串
     *
     * @param list 源集合
     * @param name 对象get方法名称
     * @param <T>  类型
     * @return String
     * @since 2.3.9
     */
    public static <T> String listToInSql(List<T> list, String name) {
        return listObjectToString(list, name, null, true);
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
        return listObjectToString(list, name, StrPool.COMMA);
    }

    /**
     * list对象转换对应的字符串
     *
     * @param list      对象集合
     * @param name      对象get方法名称
     * @param separator 分隔符
     * @param <T>       实体类型
     * @return String
     * @since 2.3.12
     */
    public static <T> String listObjectToString(List<T> list, String name, String separator) {
        return listObjectToString(list, name, separator, false);
    }


    /**
     * list对象转换对应的字符串
     *
     * @param <T>       列表中的对象类型
     * @param list      要转换为字符串的列表
     * @param name      要获取的属性的名称（可为空）
     * @param separator 字符串拼接分隔符（可为空）
     * @param inSql     是否在 SQL 语句中使用
     * @return String
     * @since 2.3.7
     */
    private static <T> String listObjectToString(List<T> list, String name, String separator, boolean inSql) {
        StringBuilder stringBuilder = new StringBuilder();
        for (T obj : list) {
            Object value = getValueFromObject(name, obj);
            if (EmptyUtils.isEmpty(value)) {
                continue;
            }
            appendValueToStringBuilder(value, stringBuilder, separator, inSql);
        }
        return trimLastSeparator(stringBuilder, separator, inSql);
    }

    /**
     * 从对象中获取给定名称的值。
     * <p>
     * 如果对象是 map 类型，则返回 map 中指定名称的值。
     * </p>
     * <p>
     * 否则，将搜索与 {@code "}get{@code "} + 属性名匹配的方法，并调用该方法以获取该属性的值。
     * </p>
     *
     * @param <T>  对象类型
     * @param name 属性名称
     * @param obj  要获取属性的对象
     * @return 属性值
     */
    private static <T> Object getValueFromObject(String name, T obj) {
        // 如果属性名为空，则直接返回对象本身。
        if (EmptyUtils.isEmpty(name)) {
            return obj;
        } else {
            // 如果对象是 Map 类型，则直接通过属性名获取相应值。
            if (obj instanceof Map) {
                return ((Map<?, ?>) obj).get(name);
            } else {
                // 如果对象不是 Map 类型，则通过反射机制获取对应的 getter 方法并调用该方法获取相应属性值。
                try {
                    Method m = obj.getClass().getMethod(("get" + org.springframework.util.StringUtils.capitalize(name)));
                    return m.invoke(obj);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    // 如果获取 getter 方法抛出异常，则将其转化为 ToolboxException 并抛出。
                    throw new ToolboxException(e);
                }
            }
        }
    }

    /**
     * 将值附加到 StringBuilder 中，并添加分隔符。
     * 如果在 SQL 语句中使用，则将值括在单引号内。
     *
     * @param value         要添加的值
     * @param stringBuilder 要附加到的 StringBuilder
     * @param separator     分隔符字符串
     * @param inSql         是否在 SQL 语句中使用
     */
    private static void appendValueToStringBuilder(Object value, StringBuilder stringBuilder, String separator, boolean inSql) {
        if (inSql) {
            stringBuilder.append("'").append(value).append("'").append(StrPool.COMMA);
        } else {
            stringBuilder.append(value).append(separator);
        }
    }

    /**
     * 如果字符串的最后一个字符是给定的分隔符，则去除该字符并返回新字符串。
     * 如果分隔符为空，并且不是正在使用的 SQL 语句，则只返回输入 StringBuilder 的字符串表示形式。
     *
     * @param stringBuilder 要修剪的 StringBuilder
     * @param separator     要去除的分隔符字符串
     * @param inSql         是否在 SQL 语句中使用
     * @return 修剪后的字符串
     */
    private static String trimLastSeparator(StringBuilder stringBuilder, String separator, boolean inSql) {
        // 如果分隔符为空，并且不是正在使用的 SQL 语句，则只返回输入 StringBuilder 的字符串表示形式。
        if (EmptyUtils.isEmpty(separator) && !inSql) {
            return stringBuilder.toString();
        }
        // 否则，调用 StringUtils 工具类的方法删除 StringBuilder 中的最后一个分隔符。
        return StringUtils.trimLastPlace(stringBuilder);
    }
}
