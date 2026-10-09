package com.github.hugh.components.stepcontrol.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 控制回路动作判定原因枚举
 * <p>
 * 统一管理控制决策的状态码与业务语义，替代零散的硬编码魔法字符串。
 */
@Getter
@AllArgsConstructor
public enum StepReason {

    /**
     * 控制执行成功：计算得到有效的调整增量，需通知执行器调节
     */
    ADJUSTED_OK("控制调整成功，执行调节动作", true),

    /**
     * 节流跳过：两次调控间隔小于设定的冷却时间（Cooldown），防止大滞后系统震荡
     */
    COOLDOWN_ACTIVE("处于冷却时间内，防抖节流生效", false),

    /**
     * 死区跳过：过程变量误差绝对值落在死区（DeadBand）之内，视为已达稳态
     */
    WITHIN_DEAD_BAND("控制误差处于死区容差内，无需调节", false),

    /**
     * 饱和跳过：执行器输出已达物理极限（OutputMin 或 OutputMax），且无法继续向目标方向调整
     */
    OUTPUT_SATURATED("输出已达物理极限抗饱和状态，拦截无效指令", false);

    /**
     * 中文描述
     */
    private final String description;

    /**
     * 该状态下是否需要执行器发生动作
     */
    private final boolean actionRequired;
}
