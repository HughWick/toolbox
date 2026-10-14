package com.github.hugh.components.stepcontrol.domain;

import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用闭环控制参数配置实体
 * <p>
 * 用于规范和约束控制回路（如PID、比例调节等）的核心控制参数，
 * 包含目标值设定、抗抖动死区、响应增益、输出限幅及频率保护机制。
 */
@Data
@Builder
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
     * 冷启动预置工作点 / 默认初始输出 (Cold-Start Working Point / Bias)
     * <p>
     * 作用：当回路首次接管某台设备且外部无法提供执行机构当前真实开度时，算法从此预置工作点开始闭环寻优。
     * 场景：
     * 1. 默认置为 null：工具类将自动安全回退至 outputMin（从安全下限开始缓慢爬坡试探）。
     * 2. 置为经验平衡点（如 50.0%）：适用于已知设备大致常态运行在半开状态的场景，大幅缩短达到设定值（SP）的爬坡时间。
     */
    private Double coldStartOutput;
    /**
     * 控制作用方向 (Action Direction)
     * <ul>
     *   <li><b>正作用 (Direct Acting)</b>：测量值(PV)增加时，输出量(Output)需要增大（如制冷系统：水温越高，冷水阀开度越大）。</li>
     *   <li><b>反作用 (Reverse Acting)</b>：测量值(PV)增加时，输出量(Output)需要减小（如加热系统：水温越高，蒸汽阀开度越小）。</li>
     * </ul>
     */
    private ActionDirection direction;

    /**
     * 极简工业工厂：只需指定目标值与方向，死区自动按目标的 2% 估算
     */
    public static StepConfig of(double targetSP, ActionDirection direction) {
        double autoDeadBand = Math.max(1.0, Math.abs(targetSP) * 0.02);
        return of(targetSP, autoDeadBand, direction);
    }

    /**
     * 业务推荐工厂：指定 目标值 + 允许误差 + 控制方向
     *
     * @param targetSP  期望维持的目标物理量（如 435.0 lux, 26.0 ℃）
     * @param deadBand  允许的误差死区（如 ±15.0 lux, ±0.5 ℃）
     * @param direction REVERSE（反作用：越暗越加力/加热）或 DIRECT（正作用：越热越制冷）
     */
    public static StepConfig of(double targetSP, double deadBand, ActionDirection direction) {
        return StepConfig.builder()
                .setPoint(targetSP)
                .deadBand(deadBand)
                .direction(direction)
                .outputMin(0.0)      // 默认输出下限 0%
                .outputMax(100.0)    // 默认输出上限 100%
                .maxStep(15.0)       // 默认单步安全上限 15%（防冲击）
                .cooldownMs(1000L)   // 默认保护冷却 1 秒
                // 默认采用安全下限 outputMin（从 0 开始爬坡最安全，避免一上来就超调炸机）
                .coldStartOutput(0.0)
                // 注意：gain 保持为 null，底层会自动启动自适应学习与黄金阻尼！
                .build();
    }
}
