package com.github.hugh.constraints.validator;


import com.github.hugh.constraints.NotEmpty;
import com.github.hugh.util.EmptyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 不为空注解校验实现类
 *
 * @author hugh
 * @see 1.0.5
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
//        System.out.println("-=-isValid-==" + value);
        return EmptyUtils.isNotEmpty(value);
    }
}
