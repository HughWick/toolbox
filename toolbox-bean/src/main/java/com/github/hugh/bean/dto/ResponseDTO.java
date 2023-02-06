package com.github.hugh.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("接口统一返回信息对象v2")
public class ResponseDTO {

    @ApiModelProperty(required = true, value = "状态码", example = "0")
    private String code; // code
    @ApiModelProperty(required = true, value = "提示信息", example = "success")
    private String message; // 提示信息
    @ApiModelProperty(required = true, value = "接口路径", example = "/api/user")
    private String path;// 路径
    @ApiModelProperty(required = true, value = "时间", example = "2020-01-01 12:00:00")
    private String date;// 时间

    /**
     * 判断code码是否一样
     * <p>当前实体类中的对应与传入的code一致</p>
     *
     * @param code code
     * @return boolean  一致返回{@code true}
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
