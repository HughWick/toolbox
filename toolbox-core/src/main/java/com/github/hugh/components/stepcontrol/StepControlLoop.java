package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.domain.StepState;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import lombok.Getter;

import java.util.Objects;

/**
 * 步进式工业级闭环控制器（Step Controller）
 * <p>
 * <b>算法特性：</b>
 * <ul>
 *   <li><b>正/反作用支持：</b>支持制冷模式（DIRECT，PV 升高增大输出）与加热模式（REVERSE，PV 降低增大输出）。</li>
 *   <li><b>防抖节流（Cooldown）：</b>防止系统在大滞后或惯性响应期间高频重复动作。</li>
 *   <li><b>死区过滤（DeadBand）：</b>消除测量噪声引起的执行机构频繁微调震荡。</li>
 *   <li><b>步进速率限制（MaxStep）：</b>防止单次控制增量过大引发超调与系统冲击。</li>
 *   <li><b>输出抗饱和（Output Clamping 与 Anti-Windup）：</b>执行器上下限硬约束，饱和时静默拦截。</li>
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
     * 内部透明维护的多租户/多设备状态池（Key 为 deviceId 等唯一标识）
     * 避免业务层每次 new 对象导致冷却失效与增益记忆丢失
     * <p>
     * 特性：
     * 1. maximumSize(50000)：硬性容量防爆（上万设备安全可控，超出按 LRU/LFU 驱逐）
     * 2. expireAfterAccess(2 小时)：设备 2 小时不上报数据，自动释放增益记忆与状态，彻底杜绝僵尸设备 OOM
     */
    private static final com.github.benmanes.caffeine.cache.Cache<String, StepState> STATE_CACHE =
            com.github.benmanes.caffeine.cache.Caffeine.newBuilder()
                    .maximumSize(500_000)
                    .expireAfterAccess(java.time.Duration.ofHours(2))
                    .build();

    /**
     * 控制回路核心配置
     */
    private final StepConfig config;

    /**
     * 实例模式绑定的控制状态
     */
    private final StepState internalState;

    /**
     * 实例模式构造函数
     *
     * @param config        控制参数配置
     * @param initialOutput 执行机构初始物理输出（如 0.0 代表 0%）
     */
    public StepControlLoop(StepConfig config, double initialOutput) {
        this.config = Objects.requireNonNull(config, "StepConfig 不能为空");
        this.internalState = new StepState(initialOutput);
    }

    /**
     * 外部指定状态上下文构造（适用于分布式架构从 Redis/DB 加载快照）
     *
     * @param config 控制参数配置
     * @param state  已恢复的设备状态快照
     */
    public StepControlLoop(StepConfig config, StepState state) {
        this.config = Objects.requireNonNull(config, "StepConfig 不能为空");
        this.internalState = Objects.requireNonNull(state, "StepState 不能为空");
    }

    /**
     * 获取指定设备的内部控制运行状态（专供监控与可观测性使用）。
     *
     * @param deviceId 设备唯一标识符，不可为空
     * @return 对应设备的 {@link StepState} 内部状态实例；若设备尚未开始控制循环或状态已被清理，则返回 {@code null}
     */
    public static StepState getInternalStateForMonitor(String deviceId) {
        return STATE_CACHE.getIfPresent(deviceId);
    }

    /**
     * 极简业务入口（完全免传初始输出）
     * 自动从 config 中获取合理基准，开发者心智负担为 0
     *
     * @param deviceId  设备唯一标识符，不可为空
     * @param currentPV 当前传感器测量值 (PV)
     * @param config    控制参数配置
     */
    public static StepResult compute(String deviceId, double currentPV, StepConfig config) {
        // 兜底基准优先级：
        // 1. 如果 config 显式配置了 coldStartOutput 则使用
        // 2. 否则默认采用下限值 outputMin（如 0.0）
        double defaultBaseline = config.getColdStartOutput() != null
                ? config.getColdStartOutput()
                : config.getOutputMin();
        return compute(deviceId, currentPV, defaultBaseline, config, System.currentTimeMillis());
    }

    /**
     * 极简静态计算入口（使用系统当前时间）
     *
     * @param deviceId              设备唯一标识（用于状态隔离与增益记忆）
     * @param currentPV             当前传感器测量值 (PV)
     * @param currentOutputIfAbsent 若首次接入设备时的初始输出值
     * @param config                控制配置
     * @return 调节决策结果 {@link StepResult}
     */
    public static StepResult compute(String deviceId, double currentPV, double currentOutputIfAbsent, StepConfig config) {
        return compute(deviceId, currentPV, currentOutputIfAbsent, config, System.currentTimeMillis());
    }

    /**
     * 极简静态计算入口（支持时间戳注入）
     *
     * @param deviceId              设备唯一标识
     * @param currentPV             当前传感器测量值 (PV)
     * @param currentOutputIfAbsent 首次接入设备时的初始输出值
     * @param config                控制配置
     * @param nowMs                 当前时间戳（毫秒）
     * @return 调节决策结果 {@link StepResult}
     */
    public static StepResult compute(String deviceId, double currentPV, double currentOutputIfAbsent, StepConfig config, long nowMs) {
        // 自动拉取或安全初始化设备的状态上下文，业务无感持久化
        StepState state = STATE_CACHE.get(deviceId, k -> new StepState(currentOutputIfAbsent));
        return compute(state, config, currentPV, nowMs);
    }

    /**
     * 实例对象直接计算（使用系统当前时间）
     *
     * @param processVariable 当前测量到的过程变量 (PV)
     * @return 调节决策结果 {@link StepResult}
     */
    public StepResult compute(double processVariable) {
        return compute(processVariable, System.currentTimeMillis());
    }

    /**
     * 实例对象直接计算（支持时间戳注入）
     *
     * @param processVariable 当前测量到的过程变量 (PV)
     * @param nowMs           当前时间戳（毫秒）
     * @return 调节决策结果 {@link StepResult}
     */
    public StepResult compute(double processVariable, long nowMs) {
        return compute(this.internalState, this.config, processVariable, nowMs);
    }

    /**
     * 统一标准计算内核
     * <p>
     * 整合了【固定增益确定性运算】与【自适应在线辨识学习】两条分支，避免重复代码。
     *
     * @param state  设备运行时状态上下文
     * @param config 控制回路参数配置
     * @param pv     当前过程变量值 (PV)
     * @param nowMs  当前系统时间戳 (毫秒)
     * @return 控制执行决策结果
     */
    public static StepResult compute(StepState state, StepConfig config, double pv, long nowMs) {
        synchronized (state) {
            // 1. 节流判断（Cooldown）
            // 规避执行器响应滞后期间的高频连续动作，保证物理系统稳定收敛
            if (nowMs - state.getLastAdjustTimestamp() < config.getCooldownMs()) {
                return StepResult.skip(state.getCurrentOutput(), StepReason.COOLDOWN_ACTIVE);
            }
            // 2. 自适应增益学习（Auto-Tuning Online Identification）
            // 当未显式配置增益（或显式声明自适应）时，通过历史两次动作动态辨识物理对象的增益 K_plant
            learnPlantGainIfNecessary(state, config, pv);
            // 3. 计算控制误差 e (Error)
            // REVERSE (反作用，如加热/调光): 测量值低于设定值，需加大输出 (SP - PV)
            // DIRECT  (正作用，如制冷/泄压): 测量值高于设定值，需加大输出 (PV - SP)
            double error = (config.getDirection() == ActionDirection.REVERSE)
                    ? (config.getSetPoint() - pv)
                    : (pv - config.getSetPoint());
            // 4. 死区容差过滤（Deadband Filtering）
            // 误差落在容差范围内时判定为已达标，停止输出，消除震荡与机械磨损
            if (Math.abs(error) <= config.getDeadBand()) {
                return StepResult.skip(state.getCurrentOutput(), StepReason.WITHIN_DEAD_BAND);
            }
            // 5. 步进计算（结合固定增益或自适应黄金阻尼）
            double delta = calculateDelta(state, config, error);
            // 6. 最大步进限幅（Slew Rate Limiting）
            // 限制单次计算周期的最大变化量绝对值（|ΔOutput| <= maxStep）
            double maxStep = config.getMaxStep();
            delta = Math.max(-maxStep, Math.min(maxStep, delta));
            // 7. 目标输出极值约束与防饱和（Output Clamping & Anti-Windup）
            double nextOutput = state.getCurrentOutput() + delta;
            nextOutput = Math.max(config.getOutputMin(), Math.min(config.getOutputMax(), nextOutput));
            // 8. 物理极限拦截与真实生效量校验
            double actualDelta = nextOutput - state.getCurrentOutput();
            if (Double.compare(nextOutput, state.getCurrentOutput()) == 0) {
                return StepResult.skip(state.getCurrentOutput(), StepReason.OUTPUT_SATURATED);
            }
            // 9. 更新状态上下文并输出执行指令
            state.setLastRecordedPV(pv);
            state.setLastActionOutput(state.getCurrentOutput());
            state.setCurrentOutput(nextOutput);
            state.setLastAdjustTimestamp(nowMs);
            return StepResult.execute(nextOutput, actualDelta, StepReason.ADJUSTED_OK);
        }
    }

    /**
     * 在线辨识受控对象增益
     *
     * @param state     状态上下文，存储历史输出、测量基准以及在线增益辨识结果（更新 {@code estimatedPlantGain}）
     * @param config    控制参数配置，若显式配置了 {@code gain}，则跳过在线辨识
     * @param currentPV 当前实时过程测量值（Process Variable）
     */
    private static void learnPlantGainIfNecessary(StepState state, StepConfig config, double currentPV) {
        // 如果开发者显式配置了固定 Gain，代表希望采用外部固定增益（无需自学习）
        if (config.getGain() != null && config.getGain() > 0.0) {
            return;
        }
        // 仅当此前有明确的输出步进，且传感器读数产生了有效响应时计算斜率
        if (state.getLastRecordedPV() != null && state.getLastActionOutput() != null) {
            double deltaOutput = Math.abs(state.getCurrentOutput() - state.getLastActionOutput());
            double deltaPV = Math.abs(currentPV - state.getLastRecordedPV());
            // 过滤执行器机械间隙与测量噪声：步进 >= 1.0 且 PV 发生可观测扰动
            if (deltaOutput >= 1.0 && deltaPV > 0.001) {
                double observedGain = deltaPV / deltaOutput;
                if (state.getEstimatedPlantGain() == null) {
                    state.setEstimatedPlantGain(observedGain);
                } else {
                    // 指数加权移动平均滤波 (EMA): 70% 历史经验 + 30% 本次实测，抗噪声扰动
                    state.setEstimatedPlantGain(0.7 * state.getEstimatedPlantGain() + 0.3 * observedGain);
                }
            }
        }
    }

    /**
     * 统一计算本控制周期的步进调整量（Delta Step）。
     *
     * @param state  控制内部状态，提供当前已估计的对象增益 {@code estimatedPlantGain}
     * @param config 步进控制配置，提供最大步进限制 {@code maxStep} 和可选的固定 {@code gain}
     * @param error  当前闭环设定误差（{@code SP - PV}）
     * @return 本周期建议的控制步长增量 {@code delta}（带正负极性符号）
     */
    private static double calculateDelta(StepState state, StepConfig config, double error) {
        // 分支 A: 显式配置了 Gain（如原有单元测试、离线整定参数），直接走常规比例调节
        if (config.getGain() != null && config.getGain() > 0.0) {
            return error * config.getGain();
        }
        // 分支 B: 未配置 Gain，走自适应动态阻尼模式
        Double plantGain = state.getEstimatedPlantGain();
        if (plantGain == null || plantGain <= 1e-4) {
            // 【冷启动安全探测阶段】：尚未学习到环境增益
            // 放大初始探测步长（上限为 maxStep 的 50%，但不超过 15.0%），加速首轮破局
            double probeStep = Math.min(15.0, config.getMaxStep() * 0.5);
            return Math.signum(error) * probeStep;
        } else {
            // 【黄金闭环阶段】：利用真实辨识物理增益
            double fullStep = error / plantGain;
            // 动态阻尼（非线性增益）机制 (Dynamic Damping Scheduling)
            // 根据误差占比，动态决定是激进逼近还是保守防震荡
            double errorRatio = 0.0;
            if (Math.abs(config.getSetPoint()) > 1e-4) {
                errorRatio = Math.abs(error) / Math.abs(config.getSetPoint());
            } else {
                // 兜底：若设定值为 0，用绝对死区容差倍数衡量
                errorRatio = Math.abs(error) > (config.getDeadBand() * 4) ? 1.0 : 0.0;
            }
            double dampingFactor;
            if (errorRatio > 0.15) {
                // 误差极大 (偏离 > 15%)：全速激进逼近，阻尼 1.0 消除尾部拖延现象
                dampingFactor = 1.0;
            } else if (errorRatio > 0.05) {
                // 误差中等 (偏离 5%~15%)：平滑降速
                dampingFactor = 0.85;
            } else {
                // 接近死区：退回 0.7 保守阻尼，防止测量噪声导致越界超调
                dampingFactor = 0.7;
            }
            return fullStep * dampingFactor;
        }
    }

    /**
     * 获取当前输出值
     */
    public double getCurrentOutput() {
        return this.internalState.getCurrentOutput();
    }

    /**
     * 同步执行器实际反馈值（如步进电机对齐、人工手动调整后回填）
     *
     * @param actualOutput 执行器真实物理位置/开度
     */
    public void syncFeedback(double actualOutput) {
        this.internalState.setCurrentOutput(actualOutput);
    }

    /**
     * 辅助清理离线设备的状态上下文，避免内存溢出（建议配合设备下线事件）
     *
     * @param deviceId 设备唯一标识
     */
    public static void evictState(String deviceId) {
        STATE_CACHE.invalidate(deviceId);
    }
}