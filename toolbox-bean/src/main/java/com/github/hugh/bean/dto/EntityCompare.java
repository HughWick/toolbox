package com.github.hugh.bean.dto;

import lombok.Data;

/**
 * 实体比较结果类
 *
 * @author hugh
 * @since 2.5.6
 */
@Data
public class EntityCompare {

    private String fieldName; // 属性名
    private Object oldValue; // 原值
    private Object newValue; // 改变后的值
}
