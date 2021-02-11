package com.github.hugh.model.dto;

import lombok.Data;

/**
 * 统一返回信息实体类
 *
 * @author hugh
 * @since 1.5.0
 */
@Data
public class ResultDTO<T> {

    public ResultDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultDTO(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private String code;
    private String message;
    private T data;

    /**
     * 校验当前对象中的code码是与传入的code码一致
     *
     * @param code code
     * @return boolean {@code true} 一样
     */
    public boolean isEquals(String code) {
        return this.getCode().equals(code);
    }

    /**
     * 校验当前对象中的code码是与传入的code码不一样
     *
     * @param code code
     * @return boolean {@code true} 不一样
     */
    public boolean isNotEquals(String code) {
        return !isEquals(code);
    }
}
