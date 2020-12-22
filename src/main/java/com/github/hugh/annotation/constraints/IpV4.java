package com.github.hugh.annotation.constraints;

import com.github.hugh.annotation.constraints.validator.IpV4Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * IpV4 校验注解
 *
 * @author hugh
 * @since 1.4.9
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = IpV4Validator.class)//指向自定义验证类
public @interface IpV4 {
    String message() default "IP格式错误"; //这边可以标注默认的验证失败消息

    String value() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
