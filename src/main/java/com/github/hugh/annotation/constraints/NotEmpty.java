package com.github.hugh.annotation.constraints;

import com.github.hugh.annotation.constraints.validator.NotEmptyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 *  自定义的不为空验证注解
 *
 * @author hugh
 * @since  1.0.5 
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NotEmptyValidator.class)//指向自定义验证类
public @interface NotEmpty {

    /**
     * 失败消息
     *
     * @return String
     */
    String message() default "";

    /**
     * 值
     *
     * @return String
     */
    String value() default "";

    /**
     * 默认参数
     *
     * @return Class
     */
    Class<?>[] groups() default {};

    /**
     * 默认参数
     *
     * @return Class
     */
    Class<? extends Payload>[] payload() default {};
}
