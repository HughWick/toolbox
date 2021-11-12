package com.github.hugh.aop.constraints.validator;

import com.github.hugh.aop.constraints.Port;
import com.github.hugh.util.regex.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 端口验证
 *
 * @author Hugh
 * @since 1.7.2
 */
public class PortValidator implements ConstraintValidator<Port, String> {

    private boolean nullable;

    @Override
    public void initialize(Port constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 当可空标识为true、则判断字符串是否为空或者为null都不进行ip验证
        if (nullable && ("".equals(value) || null == value)) {
            return true;
        }
        return RegexUtils.isPort(value);
    }
}
