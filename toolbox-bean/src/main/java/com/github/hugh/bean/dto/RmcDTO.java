package com.github.hugh.bean.dto;

import lombok.Data;

/**
 * gprmc 解析实体类
 * User: AS
 * Date: 2022/10/20 14:56
 */
@Data
public class RmcDTO {
    private String name; // $GPRMC，语句ID，表明该语句为Recommended Minimum Specific GPS/TRANSIT Data(RMC)推荐最小定位信息
    private String time;// UTC时间，hhmmss.sss格式
    private String status;// 状态，A=定位，V=未定位
    private String latitude;//纬度 ddmm.mmmm，度分格式（前导位数不足则补0）
    private String latitudeBearing; // 纬度方位：纬度N（北纬）或S（南纬）
    private String longitude;// 经度dddmm.mmmm，度分格式（前导位数不足则补0）
    private String longitudeBearing;// 经度E（东经）或W（西经）
    private String speed;// 速度，节，Knots(一节也是1.852千米／小时)
    private String azimuth; //方位角，度(二维方向指向，相当于二维罗盘)
    private String date;// UTC日期，ddmmyy格式
    private String magneticDeclination;//磁偏角，(000 - 180)度(前导位数不足则补0)
    private String directionOfMagneticDeclination;//磁偏角方向，E=东，W=西
    private String mode;// 模式，A=自动，D=差分，E=估测，N=数据无效(3.0协议内容)
    private String calibrationValue;//校验值
    private String readingDate;// cst（北京）时间
}
