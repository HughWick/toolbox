package com.github.hugh.support;

/**
 * 超时回调
 *
 * @author hugh
 * @since 2.5.6
 */
@FunctionalInterface
public interface TimeoutOperation {

    /**
     * 执行超时操作。具体操作逻辑由实现该接口的类来决定。
     */
    void doOperation();
}
