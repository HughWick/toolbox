package com.github.hugh.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "分页对象", description = "分页查询实体")
public class PageDto {
    @ApiModelProperty(value = "页码", required = true)
    private Integer page;
    @ApiModelProperty(value = "每页显示数量", required = true)
    private Integer size;
    @ApiModelProperty("排序字段")
    private String sortBy;
    @ApiModelProperty("排序方式")
    private String sortType;
}