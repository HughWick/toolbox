package com.github.hugh.model.dto;

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
}
