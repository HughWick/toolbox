package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StepLightLoopTest {

    @Test
    @DisplayName("核心验证: 环境照度多工况闭环调光控制验证 (含不可控强光抑制)")
    void testLightSensorContinuousRegulation_Optimized() {
        // 1. 物理环境模型构建:
        // 假设未受控的自然底色环境光 = 150 lux (若外界光 >= SP，则超出可控物理域)
        final double ambientBaseLux = 150.0;
        final double maxBulbContribution = 3000;
        final double targetSP = 1435; // 目标设定值 SP 改为 850.0 lux
        // 物理可行性先验检查: 自然光不可大于目标值
        assertTrue(ambientBaseLux <= targetSP, "自然环境底光超出目标SP，灯具无法产生负向光强，系统物理不可达！");
        // 2. 控制器参数配置:
        // 目标 SP = 850.0 lux
        // 执行器灵敏度: 4850 lux / 100% = 48.5 lux/1%。
        // 推荐死区: 设置为 25.0 lux (允许在 825 ~ 875 lux 之间稳定，相当于约 ±0.5% 灯光开度容差)
        StepConfig config = new StepConfig(
                targetSP,
                12.5,             // 死区放宽至合理的物理容差 (±25 lux)
                0.045,            // 增益增量 Kp
                30.0,             // 单次最大步长限制 20%
                0.0,
                100.0,
                1000L,
                ActionDirection.REVERSE // 照度越高越需要调暗
        );
        // 初始开度 100% (初始总照度 = 150 + 4850 = 5000 lux)
        StepControlLoop loop = new StepControlLoop(config, 0);
        System.out.println("\n=========================================================================================================");
        System.out.printf("               [IoT 闭环控制] 强光调至目标照度 %.1f lux 仿真演化日志 (底光: %.1f lux)\n", targetSP, ambientBaseLux);
        System.out.println("=========================================================================================================");
        System.out.printf("%-4s | %-9s | %-10s | %-8s | %-11s | %-12s | %-10s | %-18s\n",
                "轮次", "仿真时间", "当前照度(PV)", "目标(SP)", "控制误差(e)", "输出亮度(CV)", "步进(Δ)", "决策动作 / 状态");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        long simulatedTime = 10000L;
        int stableCount = 0;
        int maxRounds = 10; // 扩展采样轮次以覆盖充分的稳态验证
        for (int round = 1; round <= maxRounds; round++) {
            // 物理环境模型计算当前实际照度
            double currentLux = ambientBaseLux + (loop.getCurrentOutput() / 100.0) * maxBulbContribution;
            // 闭环控制核心决策计算
            StepResult result = loop.compute(currentLux, simulatedTime);
            String timeStr = String.format("+%02ds", (round - 1) * 2);
            String actionDesc = formatActionDescription(result);
            System.out.printf("#%-5d | %-10s | %8.1f lux | %8.1f | %+10.1f | %10.2f %% | %+9.2f %% | %s\n",
                    round, timeStr, currentLux, config.getSetPoint(),
                    (config.getSetPoint() - currentLux),
                    loop.getCurrentOutput(),
                    result.getDelta(),
                    actionDesc
            );
            // 判定恒定状态: 命中死区 或 物理输出已达极限且误差已无法消除(抗饱和)
            if (result.getReason() == StepReason.WITHIN_DEAD_BAND) {
                stableCount++;
            } else if (result.getReason() == StepReason.OUTPUT_SATURATED) {
                stableCount++;
            }
            simulatedTime += 2000L; // 模拟每 2 秒一个周期
        }
        System.out.println("=========================================================================================================\n");
        // 3. 算法精准性与稳态断言:
        double finalLux = ambientBaseLux + (loop.getCurrentOutput() / 100.0) * maxBulbContribution;
        assertTrue(stableCount >= 5, String.format("系统未能在后半程持续保持稳定，稳态周期数: %d/5", stableCount));
        assertTrue(Math.abs(finalLux - targetSP) <= config.getDeadBand(),
                String.format("最终照度未落在死区范围内！最终照度: %.1f lux, 目标: %.1f lux", finalLux, targetSP));
    }

    private String formatActionDescription(StepResult result) {
        if (!result.isNeedAction()) {
            return "[维持] " + result.getReason().getDescription();
        }
        return String.format("[调节] Δ=%.2f%% -> 下发设备", result.getDelta());
    }
}
