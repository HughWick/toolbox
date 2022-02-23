package com.github.hugh.bean.dto;

import lombok.Data;

/**
 * Ip2region 解析结果对象
 *
 * @author hugh
 * @version 1.5.4
 */
@Data
public class Ip2regionDTO {
    private String country;// 国家
    private String region;// 大区：华中、华东、华北、华南、华西
    private String province;// 省份
    private String city;// 城市
    private String isp;// 运营商
}
