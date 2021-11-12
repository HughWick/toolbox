package com.github.hugh.aop.constraints;


import com.github.hugh.aop.constraints.validator.DateFormatValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 日期格式验证注解类
 *
 * @author hugh
 * @since  1.0.5
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateFormatValidator.class)//指向自定义验证类
public @interface DateFormat {

    /**
     * 说明
     *
     * @return String
     */
    String message() default "日期格式不正确！"; //这边可以标注默认的验证失败消息

    /**
     * 日期格式
     *
     * @return String
     */
    String pattern() default "yyyy-MM-dd HH:mm:ss";

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
