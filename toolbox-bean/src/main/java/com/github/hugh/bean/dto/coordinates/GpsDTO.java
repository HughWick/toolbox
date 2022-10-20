package com.github.hugh.bean.dto.coordinates;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 坐标对象，由经纬度构成
 *
 * @author hugh
 * @version 1.6.4
 */
@Data
@AllArgsConstructor
public class GpsDTO {

    private double latitude; // 纬度
    private double longitude; // 经度
}
