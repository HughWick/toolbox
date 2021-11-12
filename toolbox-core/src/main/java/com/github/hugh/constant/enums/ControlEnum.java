package com.github.hugh.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 控制状态枚举类
 *
 * @author hugh
 * @since 1.1.0
 */
@ToString
@Getter
@AllArgsConstructor
public enum ControlEnum {

    ENABLE("enable", "启用"),
    DISABLE("disable", "禁用");

    private final String code;// 编码
    private final String desc;// 中文描述
}
