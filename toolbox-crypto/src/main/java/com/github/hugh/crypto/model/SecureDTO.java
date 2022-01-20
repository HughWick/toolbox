package com.github.hugh.crypto.model;

import lombok.Data;

/**
 * 密钥实体信息类
 * User: AS
 * Date: 2022/1/20 9:02
 */
@Data
public class SecureDTO {

    private String serialNo; // 请求唯一序列号
    private String data;// 数据
    private String appkey;// app 唯一ID
    private String appSecret;// app 密钥
    private String token;// token
    private String sign;// 签名
    private String timestamp;//时间戳
    private String notifyUrl;//回调URL
    private String version;// 版本号
}
