package com.github.hugh.constraints;

import com.github.hugh.constraints.validator.NotEmptyValidator;

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
 * @see 1.0.5 
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NotEmptyValidator.class)//指向自定义验证类
public @interface NotEmpty {

    String message() default ""; //这边可以标注默认的验证失败消息

    String value() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
