package com.github.hugh.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * aop拦截方法注解
 *
 * @author hugh
 * @since 1.5.5
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspects {

    /**
     * 名称
     *
     * @return String
     */
    String name() default "";
}
