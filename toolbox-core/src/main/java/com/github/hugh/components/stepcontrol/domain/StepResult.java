package com.github.hugh.components.stepcontrol.domain;

import com.github.hugh.components.stepcontrol.enums.StepReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 控制动作决策结果
 * <p>
 * 封装控制回路单个周期的计算产物，包含是否触发动作、目标值、调节步长及决策原因。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepResult {

    /**
     * 是否需要向下游执行机构发送动作指令
     */
    private boolean needAction;

    /**
     * 本次计算后的目标输出值（CV - Control Variable）
     * <p>
     * 注：若 {@code needAction} 为 false，该字段返回的是当前未发生变化的维持输出，避免误设为 0
     */
    private double newOutput;

    /**
     * 本次调整的增量（Delta），当无需调节时增量为 0.0
     */
    private double delta;

    /**
     * 结构化动作原因
     */
    private StepReason reason;

    /**
     * 快捷跳过工厂方法（保持当前输出不变）
     *
     * @param currentOutput 当前维持的控制输出
     * @param reason        跳过的枚举原因（需 actionRequired == false）
     * @return 跳过动作结果
     */
    public static StepResult skip(double currentOutput, StepReason reason) {
        return new StepResult(false, currentOutput, 0.0, reason);
    }

    /**
     * 快捷执行工厂方法
     *
     * @param newOutput 本次调整后的目标输出
     * @param delta     本次调整的增量步长
     * @param reason    执行枚举原因
     * @return 执行动作结果
     */
    public static StepResult execute(double newOutput, double delta, StepReason reason) {
        return new StepResult(true, newOutput, delta, reason);
    }
}
