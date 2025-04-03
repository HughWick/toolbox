package com.github.hugh.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 省市区四级信息
 *
 * @author hugh
 * @since 2.8.6
 */
@Data
@ApiModel(value = "省市区四级信息")
public class RegionDto {
    @ApiModelProperty(value = "省份编码")
    private String provinceCode;
    @ApiModelProperty(value = "省份名称")
    private String provinceName;
    @ApiModelProperty(value = "城市编码")
    private String cityCode;
    @ApiModelProperty(value = "城市名称")
    private String cityName;
    @ApiModelProperty(value = "区域编码")
    private String areaCode;
    @ApiModelProperty(value = "区域名称")
    private String areaName;
    @ApiModelProperty(value = "街道编码")
    private String streetCode;
    @ApiModelProperty(value = "街道名称")
    private String streetName;
    @ApiModelProperty(value = "街道编码扩展为8位")
    private String streetCodeEx;
}