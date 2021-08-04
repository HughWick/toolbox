package com.github.hugh.annotation.constraints;

import com.github.hugh.annotation.constraints.validator.StringLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字符串长度验证类
 *
 * @author Hugh
 * @since 1.7.3
 */
@Documented
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
@Constraint(validatedBy = StringLengthValidator.class)//指向自定义验证类
public @interface StringLength {

    /**
     * 当验证不通过时的提示信息
     *
     * @return String
     */
    String message() default "字符串长度错误";

    /**
     * 最小长度
     *
     * @return int
     */
    int min() default 0;

    /**
     * @return size the element must be lower or equal to
     */
    int max() default Integer.MAX_VALUE;

    /**
     * 是否过滤中文计算
     * <p>默认不过滤，也就是将中文作为一个字符串计算</p>
     * <p>true时则按照一个中文占两个字符计算长度</p>
     *
     * @return boolean
     */
    boolean filterChinese() default false;

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
