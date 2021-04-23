package com.github.hugh.model.dto;

import lombok.Data;

/**
 * ping命令的结果接收
 *
 * @author hugh
 * @version 1.6.4
 */
@Data
public class PingDTO {
    private int loss;//丢包
    private double delay;//延迟 单位:ms
    private int status;//状态
}
