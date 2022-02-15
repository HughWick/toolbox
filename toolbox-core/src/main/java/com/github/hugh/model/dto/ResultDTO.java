package com.github.hugh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 统一返回信息实体类
 *
 * @author hugh
 * @since 1.5.0
 */
@Data
@Builder
@AllArgsConstructor
public class ResultDTO<T> {

    private String code; // code
    private String message; // 提示信息
    private T data; // 数据

    /**
     * 判断code码是否一样
     * <p>当前实体类中的对应与传入的code一致</p>
     *
     * @param code code
     * @return boolean
     * @since 1.7.0
     */
    public boolean equalCode(String code) {
        return this.code.equals(code);
    }

    /**
     * 判断code码不一样
     *
     * @param code code
     * @return boolean
     * @since 1.7.0
     */
    public boolean notEqualCode(String code) {
        return !equalCode(code);
    }

    /**
     * code、提示信息
     *
     * @param code    code
     * @param message 提示信息
     */
    public ResultDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
