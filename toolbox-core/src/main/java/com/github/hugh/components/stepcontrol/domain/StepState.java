package com.github.hugh.components.stepcontrol.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 控制回路实时运行时状态上下文（用于跨请求持久化与自适应演化）
 */
@Data
@NoArgsConstructor
public class StepState implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前控制变量输出值（CV - Control Variable），如当前阀门开度、设定亮度百分比
     */
    private volatile double currentOutput;

    /**
     * 上一次实际执行控制动作的时间戳（毫秒），用于冷却节流判断
     */
    private volatile long lastAdjustTimestamp = 0L;

    /**
     * 在线辨识的受控对象物理增益（|ΔPV / ΔOutput|）
     * 为 null 时表示系统处于冷启动阶段，尚未掌握受控物理特征
     */
    private volatile Double estimatedPlantGain = null;

    /**
     * 上一次下发控制动作瞬间所记录的过程测量值 (PV)
     */
    private volatile Double lastRecordedPV = null;

    /**
     * 上一次发生变化并下发到执行器的控制输出值
     */
    private volatile Double lastActionOutput = null;

    public StepState(double initialOutput) {
        this.currentOutput = initialOutput;
    }
}
