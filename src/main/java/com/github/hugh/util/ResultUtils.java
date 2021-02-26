package com.github.hugh.util;

import com.github.hugh.model.dto.ResultDTO;

/**
 * result工具类
 *
 * @author hugh
 * @since 1.5.0
 */
public class ResultUtils {

    private ResultUtils() {
    }

    /**
     * 校验当前对象中的code码是与传入的code码一致
     *
     * @param code code
     * @param dto  返回结果实体类
     * @param <E> 对象类型
     * @return boolean {@code true} 一样
     */
    public static <E> boolean isEquals(ResultDTO<E> dto, String code) {
        if (dto == null) {
            return false;
        }
        return dto.getCode().equals(code);
    }

    /**
     * 校验当前对象中的code码是与传入的code码不一样
     *
     * @param code code
     * @param dto  返回结果实体类
     * @param <E> 对象类型
     * @return boolean {@code true} 不一样
     */
    public static <E> boolean isNotEquals(ResultDTO<E> dto, String code) {
        return !isEquals(dto, code);
    }
}
