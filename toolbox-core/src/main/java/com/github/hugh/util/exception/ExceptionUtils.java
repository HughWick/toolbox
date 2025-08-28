package com.github.hugh.util.exception;

import com.github.hugh.util.lang.StringFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常工具类
 *
 * @see <a href="https://github.com/dromara/hutool/blob/v5-master/hutool-core/src/main/java/cn/hutool/core/exceptions/ExceptionUtil.java">ExceptionUtil</a>
 * @since 2.4.9
 */
public class ExceptionUtils {

    /**
     * 获得完整消息，包括异常名，消息格式为：{SimpleClassName}: {ThrowableMessage}
     *
     * @param throwable 异常
     * @return 完整消息
     */
    public static String getMessage(Throwable throwable) {
        if (null == throwable) {
            return null;
        }
        return StringFormatter.format("{}: {}", throwable.getClass().getSimpleName(), throwable.getMessage());
    }

    /**
     * 获取异常链上所有异常的集合，如果{@link Throwable} 对象没有cause，返回只有一个节点的List<br>
     * 如果传入null，返回空集合
     *
     * <p>
     * 此方法来自Apache-Commons-Lang3
     * </p>
     *
     * @param throwable 异常对象，可以为null
     * @return 异常链中所有异常集合
     * @since 2.4.9
     */
    public static List<Throwable> getThrowableList(Throwable throwable) {
        final List<Throwable> list = new ArrayList<>();
        while (throwable != null && !list.contains(throwable)) {
            list.add(throwable);
            throwable = throwable.getCause();
        }
        return list;
    }

    /**
     * 获取异常链中最尾端的异常，即异常最早发生的异常对象。<br>
     * 此方法通过调用{@link Throwable#getCause()} 直到没有cause为止，如果异常本身没有cause，返回异常本身<br>
     * 传入null返回也为null
     *
     * <p>
     * 此方法来自Apache-Commons-Lang3
     * </p>
     *
     * @param throwable 异常对象，可能为null
     * @return 最尾端异常，传入null参数返回也为null
     */
    public static Throwable getRootCause(final Throwable throwable) {
        final List<Throwable> list = getThrowableList(throwable);
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    /**
     * 获取异常链中最尾端的异常的消息，消息格式为：{SimpleClassName}: {ThrowableMessage}
     *
     * @param throwable 异常
     * @return 消息
     * @since 2.4.9
     */
    public static String getRootCauseMessage(final Throwable throwable) {
        return getMessage(getRootCause(throwable));
    }
}
