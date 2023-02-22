package com.github.hugh.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回信息实体类
 *
 * @author hugh
 * @since 1.5.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("接口统一返回信息对象")
public class ResultDTO<T> {

    @ApiModelProperty(required = true, value = "接口状态码", example = "0")
    private String code; // code
    @ApiModelProperty(required = true, value = "提示信息", example = "success")
    private String message; // 提示信息
    @ApiModelProperty(required = true, value = "数据")
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
