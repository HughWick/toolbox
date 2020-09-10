package com.github.hugh.constraints.validator;

import com.github.hugh.constraints.DateFormat;
import com.github.hugh.util.DateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 日期格式验证拦截
 *
 * @author hugh
 * @see 1.0.5
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

//    public static void main(String[] args) {
//        System.out.println("--->>" + DateUtils.isDateFormat("2020-07-29", "yyyy-MM-dd"));
//    }
}
