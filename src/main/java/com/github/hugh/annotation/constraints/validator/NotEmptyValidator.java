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

    /**
     * 校验value是否不为空、
     * <p>{@code false} 则抛出校验异常</p>
     *
     * @param value                      参数
     * @param constraintValidatorContext 源
     * @return boolean {@code true}不为空
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return EmptyUtils.isNotEmpty(value);
    }
}
