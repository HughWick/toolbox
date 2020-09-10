package com.github.hugh.constraints;


import com.github.hugh.constraints.validator.DateFormatValidator;

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
 * @see 1.0.5 
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateFormatValidator.class)//指向自定义验证类
public @interface DateFormat {

    String message() default "日期格式不正确！"; //这边可以标注默认的验证失败消息

    String pattern() default "yyyy-MM-dd HH:mm:ss";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
