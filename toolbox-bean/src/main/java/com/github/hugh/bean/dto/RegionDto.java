package com.github.hugh.bean.dto;

import lombok.Data;

/**
 * 省市区四级信息
 *
 * @author hugh
 * @since 2.8.6
 */
@Data
public class RegionDto {
    private String id;
    private String provinceCode; // 省份编码
    private String provinceName; // 省份名称
    private String cityCode; // 城市编码
    private String cityName; // 城市名称
    private String areaCode; // 区域编码
    private String areaName; //区域名称
    private String streetCode; // 街道编码
    private String streetName; // 街道名称
    private String streetCodeEx; // 街道编码扩展为8位
}