package com.github.hugh.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统一返回接口相应实体类
 *
 * @author hugh
 * @since 2.1.11
 */
@Data
@AllArgsConstructor
public class ResponseDTO {

    private String code; // code
    private String message; // 提示信息
    private String path;// 路径
    private String date;// 时间

    /**
     * 判断code码是否一样
     * <p>当前实体类中的对应与传入的code一致</p>
     *
     * @param code code
     */
    public boolean equalCode(String code) {
        return this.code.equals(code);
    }

    /**
     * 判断code码不一样
     *
     * @param code code
     * @return boolean
     */
    public boolean notEqualCode(String code) {
        return !equalCode(code);
    }
}
