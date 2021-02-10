package com.github.hugh.model.dto;

import com.github.hugh.util.ResultUtils;
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
public class ResultDTO<T> {
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
        return ResultUtils.isEquals(this, code);
    }

    /**
     * 校验当前对象中的code码是与传入的code码不一样
     *
     * @param code code
     * @return boolean {@code true} 不一样
     */
    public boolean isNotEquals(String code) {
        return ResultUtils.isNotEquals(this, code);
    }
}
