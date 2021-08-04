package com.github.hugh.annotation.constraints.validator;

import com.github.hugh.annotation.constraints.StringLength;
import com.github.hugh.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 检查一个数组的长度是否在min和max之间
 *
 * @author Hugh
 * @since 1.7.3
 **/
public class StringLengthValidator implements ConstraintValidator<StringLength, String> {

    private int min;
    private int max;
    private boolean filterChinese;

    @Override
    public void initialize(StringLength parameters) {
        min = parameters.min();
        max = parameters.max();
        filterChinese = parameters.filterChinese();
    }

    /**
     * 验证字符串长度
     *
     * @param value   要验证的字符串
     * @param context 验证条件的上下文
     * @return boolean  如果数组为空或者数组中的条目数在指定的最小值和最大值之间（包括在内），返回{@code true}，否则返回{@code false}}。
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int length;
        if (filterChinese) {
            length = StringUtils.varcharSize(value);
        } else {
            length = value.length();
        }
        return length >= min && length <= max;
    }
}
