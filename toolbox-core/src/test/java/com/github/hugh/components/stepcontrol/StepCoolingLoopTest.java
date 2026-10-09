package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StepCoolingLoopTest {

    @Test
    @DisplayName("核心验证: 机房夏季散热降温闭环控制验证 (正作用 DIRECT 模式)")
    void testCoolingContinuousRegulation_DirectAction() {
        // =========================================================================
        // 1. 物理环境与受控对象模型构建 (机房制冷温控):
        // 假设满载服务器自然发热底温 = 36.0 ℃ (无制冷介入时的物理平衡温度)
        // 空调/散热风机满开(100%输出)时的最大降温能力 = 20.0 ℃ (即满负荷时可降至 16.0 ℃)
        // 目标设定值 SP = 24.0 ℃
        // =========================================================================
        final double ambientBaseTemp = 36.0;         // 无制冷时的环境基准温度 (℃)
        final double maxCoolingCapacity = 20.0;     // 制冷系统满载最大降温能力 (℃)
        final double targetSP = 24.0;               // 目标恒温设定值 (℃)

        // 物理可行性先验检查:
        // ① 自然底温不能低于设定值 (因为制冷系统无法提供正向升温)
        assertTrue(ambientBaseTemp >= targetSP,
                "自然底温低于目标设定值，纯制冷执行器无法制热，系统物理不可达！");
        // ② 极限降温深度必须能够覆盖目标 SP
        assertTrue((ambientBaseTemp - maxCoolingCapacity) <= targetSP,
                "制冷系统最大降温能力不足，即使100%输出也无法达到目标温度！");

        // =========================================================================
        // 2. 控制器参数配置 (正作用控制: 温度越高，开度需越大):
        // 目标 SP = 24.0 ℃
        // 死区 DeadBand = 0.5 ℃ (允许在 23.5 ~ 24.5 ℃ 之间稳定，避免执行器频繁动作)
        // 增益系数 Kp = 4.5 (%/℃) (每偏离 1.0℃ 期望步进调节 4.5% 功率)
        // 单次最大步长限制 MaxStep = 25.0% (防机械冲击与过冲)
        // =========================================================================
        StepConfig config = new StepConfig(
                targetSP,
                0.5,                    // 死区容差 ±0.5 ℃
                4.5,                    // 比例增益 Kp
                25.0,                   // 单次最大动作步长限制 25%
                0.0,                    // 最小输出 0% (关闭制冷)
                100.0,                  // 最大输出 100% (全速制冷)
                1000L,                  // 最小动作间隔 (1秒)
                ActionDirection.DIRECT  // 【正作用】：温度越高于SP，制冷输出越需要增大
        );

        // 初始开度 0% (初始机房温度 = 36.0 ℃，处于高温报警区)
        StepControlLoop loop = new StepControlLoop(config, 0.0);

        System.out.println("\n=========================================================================================================");
        System.out.printf("               [IoT 闭环控制] 高温机房散热至目标温度 %.1f ℃ 仿真演化日志 (初始底温: %.1f ℃)\n", targetSP, ambientBaseTemp);
        System.out.println("=========================================================================================================");
        System.out.printf("%-4s | %-9s | %-10s | %-8s | %-11s | %-12s | %-10s | %-18s\n",
                "轮次", "仿真时间", "当前温度(PV)", "目标(SP)", "控制误差(e)", "制冷开度(CV)", "步进(Δ)", "决策动作 / 状态");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        long simulatedTime = 10000L;
        int stableCount = 0;
        int maxRounds = 10;

        for (int round = 1; round <= maxRounds; round++) {
            // 物理环境模型计算当前实际机房温度:
            // 制冷输出越大，机房实际温度越低: PV = BaseTemp - (CV / 100.0) * MaxCoolingCapacity
            double currentTemp = ambientBaseTemp - (loop.getCurrentOutput() / 100.0) * maxCoolingCapacity;
            // 闭环控制器计算决策
            StepResult result = loop.compute(currentTemp, simulatedTime);
            String timeStr = String.format("+%02ds", (round - 1) * 2);
            String actionDesc = formatActionDescription(result);
            // 正作用控制中，偏差通常定义为: e = PV - SP (正偏差驱动正调节)
            double error = currentTemp - config.getSetPoint();
            System.out.printf("#%-5d | %-10s | %8.2f ℃   | %8.1f | %+10.2f | %10.2f %% | %+9.2f %% | %s\n",
                    round, timeStr, currentTemp, config.getSetPoint(),
                    error,
                    loop.getCurrentOutput(),
                    result.getDelta(),
                    actionDesc
            );

            // 稳态命中判定: 命中死区 或 触发输出饱和防积分/过冲状态
            if (result.getReason() == StepReason.WITHIN_DEAD_BAND
                    || result.getReason() == StepReason.OUTPUT_SATURATED) {
                stableCount++;
            }

            simulatedTime += 2000L; // 模拟每 2 秒一个调节周期
        }
        System.out.println("=========================================================================================================\n");

        // =========================================================================
        // 3. 算法收敛性与稳态断言:
        // =========================================================================
        double finalTemp = ambientBaseTemp - (loop.getCurrentOutput() / 100.0) * maxCoolingCapacity;

        // 断言一: 系统具备良好阻尼与稳定性，在 10 轮中有 5 轮以上维持在稳态死区内
        assertTrue(stableCount >= 5,
                String.format("系统未能保持稳态，稳态周期数不足: %d/5", stableCount));

        // 断言二: 最终控制结果必须落在死区允许范围内 (|PV - SP| <= DeadBand)
        assertTrue(Math.abs(finalTemp - targetSP) <= config.getDeadBand(),
                String.format("最终温度未落在死区范围内！最终温度: %.2f ℃, 目标: %.1f ℃, 死区: ±%.1f ℃",
                        finalTemp, targetSP, config.getDeadBand()));
    }


    @Test
    @DisplayName("空调外挂闭环调温: 通用工况自适应仿真与稳态验证")
    void testAirConditionerSetTempRegulation() {
        // =========================================================================
        // 1. 【业务配置区】（未来在此处随意替换任何数值，算法与仿真均自适应）
        // =========================================================================
        final double targetSP = 24.0;             // 目标期望室温 (℃)
        final double ambientBaseTemp = 45.0;      // 外部环境底温 (℃) -> 可任意换为 35.0, 45.0, 50.0 等
        final double deadBand = 0.5;              // 稳态死区 (±℃)
        final double kp = 0.8;                    // 比例增益 (无量纲)
        final double maxStep = 2.0;               // 单次最大安全调温步长 (℃)
        final double minAcSetTemp = 16.0;         // 空调物理支持的最低设定值 (℃)
        final double maxAcSetTemp = 30.0;         // 空调物理支持的最高设定值 (℃)
        final double initialAcSetTemp = 26.0;     // 初始下发空调设定值 (℃)

        // 空调额定最大降温温降 (℃): 真实机房选型必须大于 (环境底温 - 目标温度)
        // 此处设置具备 5℃ 工程冗余的制冷能力，确保物理上可达
        final double maxCoolingCapacity = Math.max(30.0, (ambientBaseTemp - targetSP) + 5.0);

        // =========================================================================
        // 2. 闭环控制器初始化 (REVERSE 反作用回路: 室温越热，下发的设定值越低)
        // =========================================================================
        StepConfig config = new StepConfig(
                targetSP,
                deadBand,
                kp,
                maxStep,
                minAcSetTemp,
                maxAcSetTemp,
                1000L,
                ActionDirection.REVERSE
        );
        StepControlLoop loop = new StepControlLoop(config, initialAcSetTemp);

        // =========================================================================
        // 3. 动态计算仿真周期 (确保无论温差多大，都有足够步数走完逼近段并观察稳态)
        // =========================================================================
        final int requiredStableRounds = 5; // 断言要求至少维持稳态的周期数
        // 估算逼近所需最大步数 = 初始最大可能温差 / 单步步长，外加富余缓冲
        int approachRounds = (int) Math.ceil(Math.abs(ambientBaseTemp - targetSP) / maxStep) + 3;
        int maxRounds = approachRounds + requiredStableRounds;

        System.out.println("\n=========================================================================================================");
        System.out.printf("   [IoT 闭环控制] 机房调温仿真 | 目标: %.1f ℃ | 底温: %.1f ℃ | 最大制冷能力: %.1f ℃\n",
                targetSP, ambientBaseTemp, maxCoolingCapacity);
        System.out.println("=========================================================================================================");
        System.out.printf("%-4s | %-7s | %-12s | %-8s | %-11s | %-12s | %-10s | %-18s\n",
                "轮次", "仿真时间", "当前温度(PV)", "目标(SP)", "控制误差(e)", "空调设定(CV)", "步进(Δ)", "决策动作 / 状态");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        long simulatedTime = 10000L;
        int stableCount = 0;
        double currentTemp = 0.0;

        // =========================================================================
        // 4. 闭环步进驱动与物理演化循环
        // =========================================================================
        for (int round = 1; round <= maxRounds; round++) {
            // [物理环境模型] 空调设定越低，制冷出力占比越大 (16℃ 对应 100% 出力，30℃ 对应 0% 出力)
            double coolingRatio = (maxAcSetTemp - loop.getCurrentOutput()) / (maxAcSetTemp - minAcSetTemp);
            currentTemp = ambientBaseTemp - coolingRatio * maxCoolingCapacity;

            // [控制器计算决策]
            StepResult result = loop.compute(currentTemp, simulatedTime);

            // 打印控制日志
            String timeStr = String.format("+%02ds", (round - 1) * 2);
            String actionDesc = formatActionDescription(result);
            double error = currentTemp - config.getSetPoint();

            System.out.printf("#%-5d | %-7s | %8.2f ℃   | %6.1f ℃ | %+10.2f | %8.2f ℃   | %+8.2f ℃  | %s\n",
                    round, timeStr, currentTemp, config.getSetPoint(),
                    error,
                    loop.getCurrentOutput(),
                    result.getDelta(),
                    actionDesc
            );

            // 稳态命中判定: 温度落入死区
            if (result.getReason() == StepReason.WITHIN_DEAD_BAND) {
                stableCount++;
            }

            simulatedTime += 2000L; // 模拟周期推进 2 秒
        }
        System.out.println("=========================================================================================================\n");

        // =========================================================================
        // 5. 工程断言 (自适应验证收敛性与控制品质)
        // =========================================================================
        // 断言一: 系统具备良好阻尼与稳定性，必须在死区内持续保持足够的周期数
        assertTrue(stableCount >= requiredStableRounds,
                String.format("系统未能保持稳态，稳态周期不足: %d/%d (当前底温 %.1f ℃，目标 %.1f ℃)",
                        stableCount, requiredStableRounds, ambientBaseTemp, targetSP));

        // 断言二: 最终温度必须精确锁定在死区范围之内 (|PV - SP| <= deadBand)
        assertTrue(Math.abs(currentTemp - targetSP) <= config.getDeadBand(),
                String.format("最终温度未落在死区范围内！当前室温: %.2f ℃, 目标: %.1f ℃, 死区: ±%.1f ℃",
                        currentTemp, targetSP, config.getDeadBand()));
    }

    /**
     * 辅助格式化决策动作描述 (适配控制循环返回的原因枚举)
     */
    private String formatActionDescription(StepResult result) {
        if (result == null) {
            return "[空响应]";
        }
        if (result.getReason() == StepReason.WITHIN_DEAD_BAND) {
            return "[维持] 当前温度已进入死区目标区间，保持当前设定";
        }
        if (result.getReason() == StepReason.OUTPUT_SATURATED) {
            return "[维持] 输出已达物理极限抗饱和状态，拦截无效指令";
        }
        return String.format("[调节] Δ=%+.2f℃ -> 下发设备更新设定值", result.getDelta());
    }
    @Test
    @DisplayName("空调外挂闭环调温: 模拟跌破20℃突发过冷冲击与动态噪声反复平衡")
    void testDynamicDisturbanceAndRecoveryLoop() {
        // =========================================================================
        // 1. 【工况与算法参数配置区】(可根据业务随时替换)
        // =========================================================================
        final double targetSP = 24.0;             // 目标设定点: 期望室温 24.0 ℃
        final double ambientBaseTemp = 45.0;      // 基础环境底温: 45.0 ℃
        final double deadBand = 0.5;              // 死区范围: ±0.5 ℃ (23.5 ~ 24.5 ℃)
        final double kp = 0.8;                    // 比例增益
        final double maxStep = 2.0;               // 单次最大步长: 2.0 ℃
        final double minAcSetTemp = 16.0;         // 空调最低设定: 16.0 ℃
        final double maxAcSetTemp = 30.0;         // 空调最高设定: 30.0 ℃
        final double initialAcSetTemp = 26.0;     // 初始空调设定: 26.0 ℃
        final double maxCoolingCapacity = 30.0;   // 空调最大降温能力: 30.0 ℃

        StepConfig config = new StepConfig(
                targetSP,
                deadBand,
                kp,
                maxStep,
                minAcSetTemp,
                maxAcSetTemp,
                1000L,
                ActionDirection.REVERSE // 反作用：太热降温(CV↓)，太冷升温(CV↑)
        );
        StepControlLoop loop = new StepControlLoop(config, initialAcSetTemp);

        System.out.println("\n=========================================================================================================");
        System.out.printf("   [IoT 闭环控制] 机房动态抗扰仿真 | 目标: %.1f ℃ | 底温: %.1f ℃ | 最大制冷能力: %.1f ℃\n",
                targetSP, ambientBaseTemp, maxCoolingCapacity);
        System.out.println("=========================================================================================================");
        System.out.printf("%-4s | %-7s | %-12s | %-8s | %-11s | %-12s | %-10s | %-18s\n",
                "轮次", "仿真时间", "当前温度(PV)", "目标(SP)", "控制误差(e)", "空调设定(CV)", "步进(Δ)", "决策动作 / 状态");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        // 固定种子，确保单测执行结果可重复、不偶发失败
        Random random = new Random(42);
        long simulatedTime = 10000L;
        int maxRounds = 14;

        double minRecordedTemp = Double.MAX_VALUE;
        boolean detectedHeatingAction = false;
        int inDeadBandCount = 0;
        double currentTemp = 0.0;

        for (int round = 1; round <= maxRounds; round++) {
            // 1. 空调基础热响应模型
            double coolingRatio = (maxAcSetTemp - loop.getCurrentOutput()) / (maxAcSetTemp - minAcSetTemp);
            double baseRoomTemp = ambientBaseTemp - coolingRatio * maxCoolingCapacity;

            // 2. 模拟环境动态扰动:
            // - 在第 5 轮注入突发强冷风扰动 (-5.2 ℃)，模拟外部冲击使室温跌破 20 ℃
            // - 第 6 轮扰动衰减为 -2.5 ℃，第 7 轮衰减为 -0.8 ℃，后续恢复
            double shockDisturbance = 0.0;
            if (round == 5) {
                shockDisturbance = -5.2; // 跌破 20 ℃
            } else if (round == 6) {
                shockDisturbance = -2.5;
            } else if (round == 7) {
                shockDisturbance = -0.8;
            }

            // 3. 模拟传感器与微气流的高斯随机浮动 (±0.3 ℃)
            double randomNoise = (random.nextDouble() - 0.5) * 0.6;

            // 合成当前实际传感器检测温度 (PV)
            currentTemp = baseRoomTemp + shockDisturbance + randomNoise;
            minRecordedTemp = Math.min(minRecordedTemp, currentTemp);

            // 控制器决策
            StepResult result = loop.compute(currentTemp, simulatedTime);
            String timeStr = String.format("+%02ds", (round - 1) * 2);
            String actionDesc = formatActionDescription(result);
            double error = currentTemp - config.getSetPoint();

            // 检测控制器是否触发过冷拉高 CV 的动作
            if (result.getDelta() > 0.0) {
                detectedHeatingAction = true;
            }

            if (result.getReason() == StepReason.WITHIN_DEAD_BAND) {
                inDeadBandCount++;
            }

            System.out.printf("#%-5d | %-7s | %8.2f ℃   | %6.1f ℃ | %+10.2f | %8.2f ℃   | %+8.2f ℃  | %s\n",
                    round, timeStr, currentTemp, config.getSetPoint(),
                    error,
                    loop.getCurrentOutput(),
                    result.getDelta(),
                    actionDesc
            );

            simulatedTime += 2000L;
        }
        System.out.println("=========================================================================================================\n");

        // =========================================================================
        // 4. 业务断言与抗扰动收敛性检验
        // =========================================================================
        // 断言一: 仿真期间确实成功模拟了跌破 20 ℃ 的突发过冷极端工况
        assertTrue(minRecordedTemp < 20.0,
                String.format("仿真未能触发跌破 20℃ 的工况，记录的最低温度为: %.2f ℃", minRecordedTemp));

        // 断言二: 控制器在遭遇过冷时，必须做出正向反拉动作（调高空调设定温度以降低制冷）
        assertTrue(detectedHeatingAction, "控制器在遭遇过冷扰动时未能做出正向调高升温动作！");

        // 断言三: 扰动退去后，系统能够迅速克服残留随机噪声，并在死区内保持稳定平衡
        assertTrue(Math.abs(currentTemp - targetSP) <= config.getDeadBand() + 0.3,
                String.format("系统未能恢复平衡！最终温度: %.2f ℃, 偏离目标过多", currentTemp));
        assertTrue(inDeadBandCount >= 4, "系统在死区内平衡的周期过少，说明抗扰性不足！");
    }
}
