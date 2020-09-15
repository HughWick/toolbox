package com.github.hugh.annotation.constraints;

import com.github.hugh.annotation.constraints.validator.OrderValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * order 校验注解
 *
 * @author hugh
 * @since 1.1.0
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = OrderValidator.class)
public @interface Order {

    /**
     * 错误信息
     * @return String
     */
    String message() default "排序类型不支持";

    /**
     * 接收字段
     * @return String[]
     */
    String[] accepts() default {"desc", "asc"};

    /**
     * 默认值
     * @return Class
     */
    Class<?>[] groups() default {};

    /**
     * 默认值
     * @return Class
     */
    Class<? extends Payload>[] payload() default {};
}
