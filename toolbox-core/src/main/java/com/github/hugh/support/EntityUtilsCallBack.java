package com.github.hugh.support;

/**
 * 回调方法
 *
 * @param <S>
 * @param <T>
 * @since 2.1.12
 */
@FunctionalInterface
public interface EntityUtilsCallBack<S, T> {

    void callBack(S t, T s);
}
