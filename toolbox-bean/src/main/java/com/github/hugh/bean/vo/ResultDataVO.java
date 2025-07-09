package com.github.hugh.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 统一返回结果集
 *
 * @author hugh
 * @since 2.1.13
 */
@Data
@Schema(description = "统一返回data对象")
public class ResultDataVO<T> {

    @Schema(required = true, description = "对象集合")
    private List<T> list;//
    @Schema(required = true, description = "总数")
    private long totalCount;
 
}
