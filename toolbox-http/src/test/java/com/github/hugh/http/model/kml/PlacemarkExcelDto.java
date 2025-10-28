package com.github.hugh.http.model.kml;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 无参数构造函数
@AllArgsConstructor // 全参数构造函数，方便转换
public class PlacemarkExcelDto {
    @ExcelProperty("点位名称")
    private String name;

    @ExcelProperty("描述")
    private String description;

    @ExcelProperty("longitude")
    private Double longitude;

    @ExcelProperty("latitude")
    private Double latitude;

    @ExcelProperty("高德经度")
    private Double amapLongitude;

    @ExcelProperty("高德纬度")
    private Double amapLatitude;

    @ExcelProperty("详细地址")
    private String address;
}
