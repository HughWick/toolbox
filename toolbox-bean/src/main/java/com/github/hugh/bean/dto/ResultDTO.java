package com.github.hugh.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "接口统一返回信息对象")
public class ResultDTO<T> {

    @Schema(required = true, description = "接口状态码", example = "0")
    private String code; // code
    @Schema(required = true, description = "提示信息", example = "success")
    private String message; // 提示信息
    @Schema(required = true, description = "数据")
    private T data; // 数据
    @Schema(required = true, description = "时间戳")
    private long timestamp;

    public ResultDTO() {
        this.timestamp = System.currentTimeMillis();
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
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * **新增的四参数构造器**
     *
     * @param code    code
     * @param message 提示信息
     * @param data    数据
     * @param timestamp 时间戳
     */
    public ResultDTO(String code, String message, T data, long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    /**
     * 判断code码是否一样
     * <p>当前实体类中的对应与传入的code一致</p>
     *
     * @param code code
     * @return boolean
     * @since 1.7.0
     */
    public boolean equalCode(String code) {
        if (this.code == null) {
            return false;
        }
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
}
