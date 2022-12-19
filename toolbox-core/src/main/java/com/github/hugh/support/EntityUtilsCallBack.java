package com.github.hugh.support;

/**
 * 回调方法
 *
 * @param <S> 源参数类型
 * @param <T> 目标参数类型
 * @since 2.1.12
 */
@FunctionalInterface
public interface EntityUtilsCallBack<S, T> {

    /**
     * 实体属性复制回调方法
     *
     * @param sources 源-对象类型
     * @param target  目标对象类型
     */
    void callBack(S sources, T target);
}
