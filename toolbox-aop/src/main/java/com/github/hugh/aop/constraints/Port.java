package com.github.hugh.aop.constraints;

import com.github.hugh.aop.constraints.validator.PortValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 端口验证
 *
 * @author Hugh
 * @since 1.7.2
 */
@Documented
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
@Constraint(validatedBy = PortValidator.class)//指向自定义验证类
public @interface Port {

    /**
     * 返回信息
     *
     * @return String
     */
    String message() default "端口错误";

    /**
     * 值
     *
     * @return String
     */
    String value() default "";

    /**
     * 是否允许为空
     * <p>默认不允许，也就是false时，不管value是空字符串或者null都进行Ip规则校验</p>
     *
     * @return boolean
     */
    boolean nullable() default false;

    /**
     * 约束注解在验证时所属的组别
     *
     * @return Class
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     *
     * @return Class
     */
    Class<? extends Payload>[] payload() default {};
}
