package com.github.hugh.constant.enums;

/**
 * 控制状态枚举类
 *
 * @author hugh
 * @since 1.1.0
 */
public enum ControlEnum {

    ENABLE("enable", "启用"),
    DISABLE("disable", "禁用");

    private String code;
    private String desc;

    // 构造方法
    ControlEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 显示code信息
     *
     * @return String 字符串结果
     */
    public String code() {
        return this.code;
    }

    /**
     * 显示枚举描述信息
     *
     * @return String 字符串结果
     */
    public String desc() {
        return this.desc;
    }
}
