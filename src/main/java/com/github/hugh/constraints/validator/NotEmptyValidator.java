package com.github.hugh.constraints.validator;


import com.github.hugh.constraints.NotEmpty;
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

//    private List<String> valueList;

    @Override
    public void initialize(NotEmpty notEmpty) {
//        valueList = new ArrayList<>();
//        for (String val : notEmpty.value()) {
//            valueList.add(notEmpty.value());
//        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return EmptyUtils.isNotEmpty(value);
    }
}
