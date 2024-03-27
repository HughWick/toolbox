package com.github.hugh.support;

/**
 * 实体类比较操作
 */
@FunctionalInterface
public interface EntityCompareOperation {

    /**
     * 执行超时操作。具体操作逻辑由实现该接口的类来决定。
     */
    void doOperation();
}
