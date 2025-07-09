package com.github.hugh.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分页对象
 *
 * @author hugh
 */
@Data
@Schema(description = "分页查询实体")
public class PageDto {
    @Schema(description = "页码",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer page;
    @Schema(description = "每页显示数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer size;
    @Schema(description = "排序字段")
    private String sortBy;
    @Schema(description = "排序方式")
    private String sortType;
}