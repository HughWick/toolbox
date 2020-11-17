package com.github.hugh.annotation.constraints.validator;

import com.github.hugh.annotation.constraints.NotEmpty;
import com.github.hugh.util.EmptyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 不为空注解校验实现类
 *
 * @author hugh
 * @since 1.0.5
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return EmptyUtils.isNotEmpty(value);
    }
}
