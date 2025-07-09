package com.github.hugh.aop.constraints.validator;

import com.github.hugh.aop.constraints.Order;
import com.google.common.collect.Lists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

/**
 * order 字段校验
 * <p>
 * 注: 当项目启动后，会(懒加载)创建ConstraintValidator实例，在创建实例后会初始化调
 * 用{@link ConstraintValidator#initialize}方法。
 * 所以， 只有在第一次请求时，会走initialize方法， 后面的请求是不会走initialize方法的。
 * </p>
 * <p>
 * 注: (懒加载)创建ConstraintValidator实例时， 会走缓存; 如果缓存中有，则直接使用相
 * 同的ConstraintValidator实例； 如果缓存中没有，那么会创建新的ConstraintValidator实例。
 * 由于缓存的key是能唯一定位的， 且 ConstraintValidator的实例属性只有在
 * {@link ConstraintValidator#initialize}方法中才会写；在{@link ConstraintValidator#isValid}
 * 方法中只是读。
 * 所以不用担心线程安全问题。
 * </p>
 *
 * @author hugh
 * @since 1.1.0
 */
public class OrderValidator implements ConstraintValidator<Order, String> {
    private List<String> valueList;

    @Override
    public void initialize(Order order) {
        valueList = Lists.newArrayList();
        for (String val : order.accepts()) {
            valueList.add(val.toUpperCase());
        }
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        // 不存在返回false,错误抛出
        return valueList.contains(string.toUpperCase());
    }
}
