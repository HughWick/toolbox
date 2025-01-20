package com.github.hugh.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体比较结果类
 *
 * @author hugh
 * @since 2.5.6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityCompareResult {

    private String fieldName; // 属性名
    private Object oldValue; // 原值
    private Object newValue; // 改变后的值
}
