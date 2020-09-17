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
 * <ul>
 *     <li>提示:</li>
 *     <li>1、message、contains、payload是必须要写的</li>
 *     <li>2、还需要什么方法可根据自己的实际业务需求，自行添加定义即可</li>
 *     <li>注:当没有指定默认值时，那么在使用此注解时，就必须输入对应的属性值</li>
 * </ul>
 *
 * @author hugh
 * @since 1.1.0
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = OrderValidator.class)// 指定此注解的实现，即:验证器
public @interface Order {

    /**
     * 当验证不通过时的提示信息
     *
     * @return String
     */
    String message() default "排序类型不支持";

    /**
     * 接收字段
     *
     * @return String[]
     */
    String[] accepts() default {"desc", "asc"};

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
