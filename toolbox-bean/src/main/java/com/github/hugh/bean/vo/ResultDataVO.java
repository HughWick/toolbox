package com.github.hugh.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.util.List;

/**
 * 统一返回结果集
 *
 * @author hugh
 * @since 2.1.13
 */
@Data
@Tag(name = "统一返回data对象", description = "返回data对象")
public class ResultDataVO<T> {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "对象集合")
    private List<T> list;//
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "总数")
    private long totalCount;

}
