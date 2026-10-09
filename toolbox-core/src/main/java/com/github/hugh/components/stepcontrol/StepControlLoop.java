package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 通用闭环单回路控制器（基于增量比例调节）。
 * <p>
 * <b>算法特性：</b>
 * <ul>
 *   <li><b>正/反作用支持：</b>支持制冷模式（DIRECT，PV 升高增大输出）与加热模式（REVERSE，PV 降低增大输出）。</li>
 *   <li><b>防抖节流（Cooldown）：</b>防止系统在大滞后或惯性响应期间高频重复动作。</li>
 *   <li><b>死区过滤（DeadBand）：</b>消除测量噪声引起的执行机构频繁微调震荡。</li>
 *   <li><b>步进速率限制（MaxStep）：</b>防止单次控制增量过大引发超调与系统冲击。</li>
 *   <li><b>输出抗饱和（Output Clamping & Anti-Windup）：</b>执行器上下限硬约束，饱和时静默拦截。</li>
 * </ul>
 *
 * <b>典型适用场景：</b>
 * <ul>
 *   <li><b>IoT / 工业控制：</b>恒温箱温控、管道恒压控制、液位平衡调节、风机转速调控。</li>
 *   <li><b>软件系统自适应：</b>根据 CPU/RT 动态调节限流阈值（自适应限流）、动态调整线程池队列长度。</li>
 * </ul>
 */
@Getter
public class StepControlLoop {

    /**
     * 控制回路核心配置（设定值、死区、增益、步进、上下限、作用方向等）
     */
    private final StepConfig config;

    /**
     * 当前控制变量输出值（CV - Control Variable），使用 volatile 保证多线程可见性
     */
    private volatile double currentOutput;

    /**
     * 上一次实际执行控制动作的时间戳（毫秒），用于冷却时间节流判断
     */
    private final AtomicLong lastAdjustTimestamp = new AtomicLong(0L);

    /**
     * 构造函数
     *
     * @param config        控制回路参数配置对象
     * @param initialOutput 初始输出值（如阀门初始开度、初始限流 QPS 阈值）
     */
    public StepControlLoop(StepConfig config, double initialOutput) {
        this.config = config;
        this.currentOutput = initialOutput;
    }

    /**
     * 计算控制动作（使用系统当前时间作为计算基准）
     *
     * @param processVariable 当前测量到的过程变量（PV - Process Variable，如当前温度、实际 CPU 利用率）
     * @return 控制决策结果 {@link StepResult}
     */
    public StepResult compute(double processVariable) {
        return compute(processVariable, System.currentTimeMillis());
    }

    /**
     * 通用标准计算逻辑（增量式比例计算与多级过滤）
     *
     * @param processVariable 当前测量到的过程变量（PV - Process Variable）
     * @param nowMs           当前时间戳（毫秒），支持外部注入用于回放测试或确定性调度
     * @return 控制决策结果，包含是否执行、跳过原因、目标控制量及调整增量
     */
    public StepResult compute(double processVariable, long nowMs) {
        // 1. 节流判断（Cooldown）
        // 规避执行器惯性滞后响应期间的高频动作，保证系统平稳收敛
        if (nowMs - lastAdjustTimestamp.get() < config.getCooldownMs()) {
            return StepResult.skip(this.currentOutput, StepReason.COOLDOWN_ACTIVE);
        }
        // 2. 根据系统作用方向计算控制误差 e (Error)
        // REVERSE: 反作用（加热/加水），当前值越低于设定值，越需要加大输出 (SP - PV)
        // DIRECT : 正作用（制冷/泄压），当前值越高于设定值，越需要加大输出 (PV - SP)
        double error;
        if (config.getDirection() == ActionDirection.REVERSE) {
            error = config.getSetPoint() - processVariable;
        } else {
            error = processVariable - config.getSetPoint();
        }
        // 3. 容差死区过滤（DeadBand Filtering）
        // 误差绝对值在死区以内时视为已达到稳态，不作调节，避免机械磨损与微幅震荡
        if (Math.abs(error) <= config.getDeadBand()) {
            return StepResult.skip(this.currentOutput, StepReason.WITHIN_DEAD_BAND);
        }
        // 4. 比例步进与单次最大调整限幅（Slew Rate Limiting）
        // 步长 delta = error * Kp，限制在 [-maxStep, +maxStep] 之间，防止大幅超调
        double delta = error * config.getGain();
        double maxStep = config.getMaxStep();
        delta = Math.max(-maxStep, Math.min(maxStep, delta));
        // 5. 目标输出极值约束（Clamping / Anti-Windup）
        // 限制绝对输出值在执行器的物理极值 [OutputMin, OutputMax] 之间
        double nextOutput = this.currentOutput + delta;
        nextOutput = Math.max(config.getOutputMin(), Math.min(config.getOutputMax(), nextOutput));
        // 6. 物理极限拦截（Saturated Output Skip）与真实生效增量计算
        // 计算经物理上下限截断后，实际真实生效的步进量 actualDelta
        double actualDelta = nextOutput - this.currentOutput;
        // 若当前执行器已达最大/最小极限且输出没有发生任何变化，无需向下游发送控制报文
        if (Double.compare(nextOutput, this.currentOutput) == 0) {
            return StepResult.skip(this.currentOutput, StepReason.OUTPUT_SATURATED);
        }
        // 7. 更新本地控制状态并返回动作指令
        this.currentOutput = nextOutput;
        this.lastAdjustTimestamp.set(nowMs);
        return StepResult.execute(nextOutput, actualDelta, StepReason.ADJUSTED_OK);
    }

    /**
     * 同步执行器实际反馈值。
     * <p>
     * <b>适用场景：</b>
     * <ul>
     *   <li>执行机构上报实际物理位置（如步进电机丢步校准、阀门实际开度反馈）。</li>
     *   <li>人工介入模式（如人工手动调节了阀门/参数）后，将本地状态与实际物理量拉齐。</li>
     * </ul>
     *
     * @param actualOutput 执行器上报的真实物理输出值
     */
    public void syncFeedback(double actualOutput) {
        this.currentOutput = actualOutput;
    }
}
