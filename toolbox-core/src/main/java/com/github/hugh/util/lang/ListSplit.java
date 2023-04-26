package com.github.hugh.util.lang;

import com.github.hugh.constant.StrPool;
import com.github.hugh.util.ListUtils;
import com.google.common.base.CharMatcher;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用于将字符串按照指定分隔符拆分并转换成列表的工具类。
 *
 * @param <R> 列表中元素的泛型类型
 * @since 2.5.8
 */
@Getter
@AllArgsConstructor
public class ListSplit<R> {

    // 要拆分成列表的字符串
    private String value;
    // 分隔符，默认使用逗号
    private String separator;
    // 用于忽略字符串中某些字符的字符匹配器，默认使用 Guava 中的 LIST_CHAR_MATCHER，即忽略空格符、制表符和换行符
    private CharMatcher charMatcher;
    // 用于将拆分出来的字符串元素转换为另一种类型的函数。默认为 null，表示不进行转换
    private Function<String, R> mapper;

    /**
     * 创建一个默认使用逗号作为分隔符、空格作为忽略字符、不进行转换的 ListSplit 实例。
     *
     * @param value 要拆分成列表的字符串
     */
    public ListSplit(String value) {
        this(value, StrPool.COMMA, ListUtils.LIST_CHAR_MATCHER, null);
    }

    /**
     * 以指定字符串为参数创建 ListSplit 实例，并返回该实例。
     *
     * @param value 要拆分成列表的字符串
     * @param <R>   列表元素的泛型类型
     * @return 新的 ListSplit 实例
     */
    public static <R> ListSplit<R> on(String value) {
        return new ListSplit<>(value);
    }

    /**
     * 指定一个转换函数，将列表中的元素转换为另一种类型。
     *
     * @param mapper 转换函数
     * @return 返回当前 ListSplit 实例，方便进行其他操作
     */
    public ListSplit<R> mapper(Function<String, R> mapper) {
        this.mapper = mapper;
        return this;
    }

    /**
     * 指定一个分隔符，将要拆分的字符串按照该分隔符拆分成元素。
     *
     * @param separator 分隔符
     * @return 返回当前 ListSplit 实例，方便进行其他操作
     */
    public ListSplit<R> separator(String separator) {
        this.separator = separator;
        return this;
    }

    /**
     * 指定一个字符匹配器，用于忽略字符串中的某些字符。
     *
     * @param charMatcher 字符匹配器
     * @return 返回当前 ListSplit 实例，方便进行其他操作
     */
    public ListSplit<R> charMatcher(CharMatcher charMatcher) {
        this.charMatcher = charMatcher;
        return this;
    }

    /**
     * 将经过拆分和转换后的元素列表以 List<R> 的形式返回。
     *
     * @return 经过拆分和转换后的元素列表
     */
    public List<R> toList() {
        final List<String> objects = ListUtils.guavaStringToList(this.value, this.separator, this.charMatcher);
        if (this.mapper == null) {// 判断泛型类型是否是 String 类型
            return (List<R>) objects;
        }
        return objects.stream().map(this.mapper).collect(Collectors.toList());
    }
}
