package com.github.hugh.bean.dto;

import lombok.Data;

/**
 * gga 信息实体类
 * User: AS
 * Date: 2021/8/12 14:29
 */
@Data
public class GgaDTO {
    private String name;// $GPGGA，语句ID，表明该语句为Global Positioning System Fix Data（GGA）GPS定位信息
    private String date;//UTC 时间，hhmmss.sss，时分秒格式
    private String latitude;//纬度 ddmm.mmmm，度分格式（前导位数不足则补0）
    private String latitudeBearing; // 纬度方位：纬度N（北纬）或S（南纬）
    private String longitude;// 经度dddmm.mmmm，度分格式（前导位数不足则补0）
    private String longitudeBearing;// 经度E（东经）或W（西经）
    private String gpsStatus;//GPS状态，0=未定位，1=非差分定位，2=差分定位，3=无效PPS，6=正在估算
    private String numberOfSatellites;// 正在使用的卫星数量（前导位数不足则补0）
    private String hdopHorizontalAccuracyFactor;// HDOP水平精度因子（0.5 - 99.9）
    private String altitude;//海拔高度
    private String waterSurfaceAltitude;// 地球椭球面相对大地水准面的高度
    private String differentialTime;// 差分时间（从最近一次接收到差分信号开始的秒数，如果不是差分定位将为空）
    private String differentialStationId;// 差分站ID号0000 - 1023（前导位数不足则补0，如果不是差分定位将为空）
    private String calibrationValue;//校验值
}
