package com.github.hugh.model.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 统一返回结果集
 *
 * @author hugh
 * @since 2.1.13
 */
@Data
@Api("统一返回data对象")
public class ResultDataVO<T> {

    @ApiModelProperty(required = true, value = "对象集合")
    private List<T> list;//
    @ApiModelProperty(required = true, value = "总数")
    private long totalCount;

}
