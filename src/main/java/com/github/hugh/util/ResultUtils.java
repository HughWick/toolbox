package com.github.hugh.util;

import com.github.hugh.model.dto.ResultDTO;

/**
 * result工具类
 *
 * @author hugh
 * @since 1.5.0
 */
public class ResultUtils {

    /**
     * 返回
     *
     * @param code    状态码
     * @param message 提示信息
     * @return ResultDTO
     */
    public static ResultDTO response(String code, String message) {
        return ResultDTO.builder().code(code).message(message).build();
    }

    /**
     * 带数据的返回
     *
     * @param code    状态码
     * @param message 提示信息
     * @param data    数据
     * @param <T>     实体泛型
     * @return ResultDTO
     */
    public static <T> ResultDTO response(String code, String message, T data) {
        return ResultDTO.builder().code(code).message(message).data(data).build();
    }

    /**
     * 判断dto 状态码是成功
     *
     * @param dto         返回信息实体类
     * @param successCode 成功状态码
     * @return boolean
     */
    public static boolean isSuccess(ResultDTO dto, String successCode) {
        if (dto == null) {
            return false;
        }
        return dto.getCode().equals(successCode);
    }

    /**
     * 判断dto 状态码时错误
     *
     * @param dto         返回信息实体类
     * @param successCode 成功状态码
     * @return boolean
     */
    public static boolean isError(ResultDTO dto, String successCode) {
        return !isSuccess(dto, successCode);
    }
}

