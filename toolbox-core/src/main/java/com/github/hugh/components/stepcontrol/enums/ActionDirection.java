package com.github.hugh.components.stepcontrol.enums;

public enum ActionDirection {
    /**
     * 反作用（误差 = SP - PV）：PV 越低，输出越大
     * 典型场景：照明补光、冬天加热、供水增压
     */
    REVERSE,

    /**
     * 正作用（误差 = PV - SP）：PV 越高，输出越大
     * 典型场景：夏天制冷、排气排烟、大棚降温
     */
    DIRECT
}
