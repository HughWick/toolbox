package com.github.hugh.annotation.constraints.validator;

import com.github.hugh.annotation.constraints.IpV4;
import com.github.hugh.util.regex.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * IpV4 校验实现
 *
 * @author hugh
 * @since 1.4.9
 */
public class IpV4Validator implements ConstraintValidator<IpV4, String> {

    private boolean nullable;

    @Override
    public void initialize(IpV4 ipV4) {
        nullable = ipV4.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // 当可空标识为true、则判断字符串是否为空或者为null都不进行ip验证
        if (nullable && ("".equals(value) || null == value)) {
            return true;
        }
        return RegexUtils.isIp(value);
    }
}
