package com.github.hugh.aop.constraints.validator;

import com.github.hugh.aop.constraints.NotEmpty;
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

    private String message;

    @Override
    public void initialize(NotEmpty notEmpty) {
        this.message = notEmpty.message();
    }

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
        // 如果值为null或空字符串，则禁用默认约束违规提示，并使用指定消息构建自定义约束违规提示
        if (value == null || value.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation(); // 禁用默认约束违规提示
            constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation(); // 使用指定消息构建自定义约束违规提示
        }
        return EmptyUtils.isNotEmpty(value);
    }
}
