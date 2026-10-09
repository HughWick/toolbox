package com.github.hugh.components.stepcontrol.enums;

/**
 * 控制动作方向（正作用 / 反作用）
 * <p>
 * 用于定义控制系统（如 PID 控制器）中，过程测量值（PV, Process Variable）
 * 与设定值（SP, Setpoint）产生偏差时，控制器输出（Output）的调节趋势。
 */
public enum ActionDirection {
    /**
     * 反作用控制（Reverse Action）
     * <p>
     * <b>控制逻辑：</b>测量值（PV）低于设定值（SP）时增大输出，测量值升高时降低输出（输出与 PV 的变化方向相反）。<br>
     * <b>偏差计算：</b> Error = SP - PV<br>
     * <b>典型应用场景：</b>
     * <ul>
     *   <li>冬天加热 / 升温控制（温度越低，加热功率越大）</li>
     *   <li>植物补光 / 室内照明（光照越弱，补光灯亮度越高）</li>
     *   <li>恒压供水增压（管道水压越低，水泵转速/输出越高）</li>
     * </ul>
     */
    REVERSE,

    /**
     * 正作用控制（Direct Action）
     * <p>
     * <b>控制逻辑：</b>测量值（PV）高于设定值（SP）时增大输出，测量值降低时降低输出（输出与 PV 的变化方向相同）。<br>
     * <b>偏差计算：</b> Error = PV - SP<br>
     * <b>典型应用场景：</b>
     * <ul>
     *   <li>夏天制冷 / 空调降温（温度越高，制冷功率越大）</li>
     *   <li>排气排烟 / 气体浓度控制（有害气体/烟雾浓度越高，风机排风量越大）</li>
     *   <li>水箱排水 / 防洪排水（水位越高，排水泵输出越大）</li>
     * </ul>
     */
    DIRECT
}