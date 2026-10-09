package com.github.hugh.components.stepcontrol.domain;

import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用闭环控制参数配置实体
 * <p>
 * 用于规范和约束控制回路（如PID、比例调节等）的核心控制参数，
 * 包含目标值设定、抗抖动死区、响应增益、输出限幅及频率保护机制。
 *
 * @author System
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepConfig {

    /**
     * 目标设定值 (Set Point, 简称 SP)
     * <p>
     * 受控过程变量（PV）期望维持的目标物理量数值（如目标温度、目标压力、目标流量等）。
     */
    private Double setPoint;

    /**
     * 控制死区容差 (Dead band)
     * <p>
     * 允许的误差范围 |PV - SP| <= deadBand。
     * 当测量值进入此死区区间内时，判定系统已达标，停止输出调节动作，避免执行机构因测量噪声而频繁微调和抖动磨损。
     */
    private Double deadBand;

    /**
     * 比例增益系数 (Proportional Gain, 即 Kp)
     * <p>
     * 偏差转化为调节量输出的放大倍数。
     * 系数过大可能导致系统振荡超调，系数过小则会导致响应迟缓、调节滞后。
     */
    private Double gain;

    /**
     * 单次最大调节步长限制 (Rate of Change Limit / Max Step)
     * <p>
     * 限制单次计算周期的最大变化量绝对值（|ΔOutput| <= maxStep）。
     * 用于防止执行器输出突变导致的水锤效应、机械冲击或瞬时过流。
     */
    private Double maxStep;

    /**
     * 执行机构输出下限 (Output Low Limit)
     * <p>
     * 控制输出的物理或工艺安全最小值（例如：阀门完全关闭 0.0%、变频器最低运行频率 20.0Hz）。
     */
    private Double outputMin;

    /**
     * 执行机构输出上限 (Output High Limit)
     * <p>
     * 控制输出的物理或工艺安全最大值（例如：阀门最大开度 100.0%、变频器额定上限 50.0Hz）。
     */
    private Double outputMax;

    /**
     * 调节冷却周期 (Cooldown Interval, 单位：毫秒)
     * <p>
     * 两次连续控制下发动作之间的最小时间间隔（节流限制）。
     * 保证现场设备有足够的机械物理响应时间，并防止高频下发指令堵塞总线（如 Modbus、PLC通信）。
     */
    private Long cooldownMs;

    /**
     * 控制作用方向 (Action Direction)
     * <ul>
     *   <li><b>正作用 (Direct Acting)</b>：测量值(PV)增加时，输出量(Output)需要增大（如制冷系统：水温越高，冷水阀开度越大）。</li>
     *   <li><b>反作用 (Reverse Acting)</b>：测量值(PV)增加时，输出量(Output)需要减小（如加热系统：水温越高，蒸汽阀开度越小）。</li>
     * </ul>
     */
    private ActionDirection direction;
}
