package com.github.hugh.aop.constraints.validator;

import com.github.hugh.aop.constraints.DateFormat;
import com.github.hugh.util.DateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 日期格式验证拦截
 *
 * @author hugh
 * @since  1.0.5
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

    private String pattern;

    @Override
    public void initialize(DateFormat dateFormat) {
        pattern = dateFormat.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return DateUtils.isDateFormat(value, pattern);
    }

}
